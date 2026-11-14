@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo       INTERVIEWHUB - DATABASE INITIALIZATION UTILITY     
echo ========================================================
echo.

set /p MYSQL_PASS="Enter your local MySQL root password (press Enter if blank): "
set "MYSQL_CMD=mysql"

where mysql >nul 2>nul
if %ERRORLEVEL% neq 0 (
    if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
        set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    ) else (
        echo [ERROR] mysql.exe not found. Please ensure MySQL Server 8.0 is installed.
        pause
        exit /b 1
    )
)

echo.
echo [1/3] Executing schema.sql to create database and tables...
if "%MYSQL_PASS%"=="" (
    "%MYSQL_CMD%" -u root < database\schema.sql
) else (
    "%MYSQL_CMD%" -u root -p%MYSQL_PASS% < database\schema.sql
)

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Failed to execute schema.sql. Please verify your root password.
    pause
    exit /b 1
)

echo.
echo [2/3] Executing sample_data.sql to seed initial data...
if "%MYSQL_PASS%"=="" (
    "%MYSQL_CMD%" -u root < database\sample_data.sql
) else (
    "%MYSQL_CMD%" -u root -p%MYSQL_PASS% < database\sample_data.sql
)

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Failed to execute sample_data.sql.
    pause
    exit /b 1
)

echo.
echo [3/3] Generating src\main\resources\database.properties...
(
    echo # Auto-generated InterviewHub Database Configuration
    echo db.url=jdbc:mysql://localhost:3306/interviewhub_db?useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC
    echo db.user=root
    echo db.password=%MYSQL_PASS%
) > src\main\resources\database.properties

echo.
echo ========================================================
echo [SUCCESS] Database interviewhub_db initialized successfully!
echo [SUCCESS] Generated src\main\resources\database.properties
echo ========================================================
echo.
echo You can now run the application with:
echo    .\mvnw.cmd exec:java
echo.
pause
