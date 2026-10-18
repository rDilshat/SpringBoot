# Student API — SIS2

REST API для создания, просмотра, изменения и удаления студентов. Java 21+, Spring Boot 4.1.1, PostgreSQL, Spring Data JPA и Flyway. Docker не требуется.

## Запуск на локальном PostgreSQL

В pgAdmin должна существовать база `sis`, её владельцем должен быть пользователь `sis_user` с правом входа. PostgreSQL должен принимать подключения на `localhost:5432`.

Из PowerShell в папке проекта:

```powershell
.\run-local.ps1
```

Скрипт запросит пароль `sis_user` скрытым вводом и запустит приложение. Пароль не записывается в файлы. Если Windows блокирует выполнение скриптов, можно разрешить выполнение только для этого запуска:

```powershell
powershell -ExecutionPolicy Bypass -File .\run-local.ps1
```

Если база называется иначе или используется другой порт:

```powershell
.\run-local.ps1 -DatabaseName my_database -Port 5433
```

При первом запуске Flyway выполнит `db/migration/V1__create_students.sql`, создаст таблицу `students` и историю миграций. Hibernate только проверяет схему (`ddl-auto=validate`). После применения миграции её не редактируют: изменения схемы добавляют новой миграцией.

Для запуска непосредственно из IntelliJ добавьте переменные окружения в конфигурацию приложения:

```text
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5432/sis
DB_USERNAME=sis_user
DB_PASSWORD=<пароль пользователя sis_user>
```

Профиль `dev` используется по умолчанию. В нём URL и имя пользователя имеют указанные выше значения по умолчанию. Пароль обязателен. Профиль `prod` требует все три переменные `DB_*`. Настройки `validate` и `open-in-view=false` общие для обоих профилей.

Если Flyway сообщает `permission denied for schema public`, проверьте в pgAdmin, что **Owner базы — sis_user**. Не нужно выдавать приложению права суперпользователя.

## API

Базовый адрес: `http://localhost:8080/api/v1/students`.

| Метод | Путь | Результат |
|---|---|---|
| POST | `/api/v1/students` | Создание, `201` и `Location` |
| GET | `/api/v1/students` | Список студентов, отсортированный по ID |
| GET | `/api/v1/students/{studentId}` | Один студент |
| PUT | `/api/v1/students/{studentId}` | Изменение имени и фамилии без смены ID |
| DELETE | `/api/v1/students/{studentId}` | Удаление, `204` |

Пример POST:

```json
{"studentId":"S-1001","firstName":"Dilshat","lastName":"Amanov"}
```

Проверочные запросы находятся в `http/students.http`. Swagger UI: `http://localhost:8080/swagger-ui/index.html`.

Пустые и слишком длинные поля дают `400`, отсутствующий студент — `404`, занятый ID — `409`. Повторный POST не перезаписывает существующего студента, в том числе при одновременном создании одного ID.

## Слои и мапперы

```text
com.example.sis1.student
  web                 контроллер, API DTO, StudentApiMapper, обработчик HTTP-ошибок
  app                 StudentService, команды создания и изменения
  domain              Student, правила, исключения, интерфейс StudentRepository
  infra.persistence   JPA entity, Spring Data repository, адаптер, StudentEntityMapper
```

Контроллер вызывает сервис. Сервис организует транзакцию и работает с доменным интерфейсом репозитория. Домен не импортирует Spring, JPA или API DTO. `Student` защищает обязательность и длину полей и сохраняет идентичность при переименовании. JPA entity существует только внутри persistence-слоя.

`StudentApiMapper` преобразует входные DTO в команды приложения и доменных студентов в ответы API. `StudentEntityMapper` преобразует domain ↔ entity. Мапперы написаны вручную и собраны на границах слоёв.

## Тестирование без Docker

Набор содержит 12 тестов: 2 доменных, 5 сервисных, 4 MockMvc и 1 интеграционный. Обычные 11 тестов не требуют БД или пароля:

```powershell
.\mvnw.cmd test
```

- `StudentTest`: доменные ограничения и сохранение ID.
- `StudentServiceTest`: Mockito, без Spring-контекста и БД.
- `StudentControllerTest`: `@WebMvcTest` и MockMvc, сервис подменён.

Для полной проверки создайте через pgAdmin **отдельную базу `sis_test` с владельцем `sis_user`**. Таблицы вручную не нужны. В PowerShell задайте параметры тестовой базы:

```powershell
$env:TEST_DB_URL = 'jdbc:postgresql://localhost:5432/sis_test'
$env:TEST_DB_USERNAME = 'sis_user'
$testPassword = Read-Host 'PostgreSQL test password' -AsSecureString
$env:TEST_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $testPassword).Password
.\mvnw.cmd verify
Remove-Item Env:TEST_DB_PASSWORD
```

Для Maven должен быть настроен `JAVA_HOME` на JDK 21 или новее. Скрипт `run-local.ps1` определяет его автоматически, если переменная не задана.

`verify` дополнительно запускает один `StudentPersistenceIT` через Maven Failsafe: настоящий PostgreSQL, миграция Flyway, `validate`, сохранение и чтение после очистки persistence context. Тест откатывает свои изменения. Параметры `TEST_DB_*` отделены от `DB_*`, чтобы проверять репозиторий на тестовой базе. Интеграционный тест не пропускается при недоступной базе: `verify` завершится ошибкой.

Минимум для проверки вручную: создать студента, перезапустить приложение, получить его через GET. Данные должны сохраниться.
