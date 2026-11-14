# InterviewHub - PowerShell Database Setup Script
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "     INTERVIEWHUB - DATABASE INITIALIZATION UTILITY     " -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

$password = Read-Host -Prompt "Enter your local MySQL root password (leave empty if none)"
$mysqlPath = "mysql"
if (!(Get-Command mysql -ErrorAction SilentlyContinue)) {
    if (Test-Path "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe") {
        $mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    } else {
        Write-Host "[ERROR] mysql.exe not found in PATH or standard Program Files location." -ForegroundColor Red
        return
    }
}

Write-Host "`n[1/3] Executing database\schema.sql..." -ForegroundColor Yellow
$pArg = if ($password -eq "") { "" } else { "-p$password" }
cmd.exe /c "`"$mysqlPath`" -u root $pArg < database\schema.sql"

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Failed to run schema.sql. Please verify your root password." -ForegroundColor Red
    return
}

Write-Host "[2/3] Executing database\sample_data.sql..." -ForegroundColor Yellow
cmd.exe /c "`"$mysqlPath`" -u root $pArg < database\sample_data.sql"

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Failed to run sample_data.sql." -ForegroundColor Red
    return
}

Write-Host "[3/3] Writing src\main\resources\database.properties..." -ForegroundColor Yellow
$props = @"
# Auto-generated InterviewHub Database Configuration
db.url=jdbc:mysql://localhost:3306/interviewhub_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=$password
"@
Set-Content -Path "src\main\resources\database.properties" -Value $props -Encoding UTF8

Write-Host "`n========================================================" -ForegroundColor Green
Write-Host "[SUCCESS] Database 'interviewhub_db' ready with seed data!" -ForegroundColor Green
Write-Host "[SUCCESS] Run application with: .\mvnw.cmd exec:java" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
