# InterviewHub PowerShell Runner
$ErrorActionPreference = "Stop"

Set-Location -Path $PSScriptRoot

Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "  Starting InterviewHub Console Application...   " -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan

# Ensure Maven is accessible in current session
$mavenBin = "C:\Users\Lenovo\tools\apache-maven-3.9.16\bin"
if (Test-Path $mavenBin) {
    if ($env:Path -notlike "*$mavenBin*") {
        $env:Path = "$env:Path;$mavenBin"
    }
}

# 1. Run standalone fat JAR if available
if (Test-Path "$PSScriptRoot\InterviewHub.jar") {
    java -jar "$PSScriptRoot\InterviewHub.jar" $args
    exit $LASTEXITCODE
}

# 2. Run from compiled classes with lib dependencies
if (Test-Path "$PSScriptRoot\target\classes\com\interviewhub\Main.class") {
    java -cp "$PSScriptRoot\target\classes;$PSScriptRoot\lib\*;$PSScriptRoot\target\*" com.interviewhub.Main $args
    exit $LASTEXITCODE
}

# 3. Fallback to Maven Wrapper
if (Test-Path "$PSScriptRoot\mvnw.cmd") {
    & "$PSScriptRoot\mvnw.cmd" exec:java
    exit $LASTEXITCODE
}

# 4. Fallback to System Maven
mvn exec:java
