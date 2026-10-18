param(
    [string]$DatabaseName = 'sis',
    [string]$DatabaseUser = 'sis_user',
    [int]$Port = 5432
)

$ErrorActionPreference = 'Stop'
$savedEnvironment = @{}
foreach ($variableName in @('DB_URL', 'DB_USERNAME', 'DB_PASSWORD', 'SPRING_PROFILES_ACTIVE', 'JAVA_HOME')) {
    $savedEnvironment[$variableName] = [Environment]::GetEnvironmentVariable($variableName, 'Process')
}

Push-Location $PSScriptRoot
try {
    if (-not $env:JAVA_HOME) {
        $savedPreference = $ErrorActionPreference
        try {
            # Java writes these settings to stderr, including on a successful run.
            $ErrorActionPreference = 'Continue'
            $javaSettings = & java -XshowSettings:properties -version 2>&1
            $javaExitCode = $LASTEXITCODE
        }
        finally {
            $ErrorActionPreference = $savedPreference
        }
        $javaHomeLine = $javaSettings | Where-Object { "$_" -match '^\s*java\.home\s*=' } | Select-Object -First 1
        if ($javaExitCode -ne 0 -or -not $javaHomeLine) {
            throw 'Java was not found. Configure JAVA_HOME with a JDK 21 or newer.'
        }
        $env:JAVA_HOME = ("$javaHomeLine" -split '=', 2)[1].Trim()
    }

    $env:SPRING_PROFILES_ACTIVE = 'dev'
    $env:DB_URL = "jdbc:postgresql://localhost:$Port/$([uri]::EscapeDataString($DatabaseName))"
    $env:DB_USERNAME = $DatabaseUser
    if (-not $env:DB_PASSWORD) {
        $securePassword = Read-Host "PostgreSQL password for $DatabaseUser" -AsSecureString
        $env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $securePassword).Password
    }

    & (Join-Path $PSScriptRoot 'mvnw.cmd') spring-boot:run
    if ($LASTEXITCODE -ne 0) {
        throw "Application exited with code $LASTEXITCODE. Check the error above."
    }
}
finally {
    foreach ($variableName in $savedEnvironment.Keys) {
        [Environment]::SetEnvironmentVariable($variableName, $savedEnvironment[$variableName], 'Process')
    }
    Pop-Location
}
