@echo off
set "MVN_BIN=C:\Users\Lenovo\tools\apache-maven-3.9.16\bin\mvn.cmd"
if exist "%MVN_BIN%" (
    call "%MVN_BIN%" %*
) else (
    where mvn >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        mvn %*
    ) else (
        echo [ERROR] Maven not found.
        exit /b 1
    )
)
