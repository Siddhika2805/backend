@echo off
setlocal
set "MVN_EXEC=mvn"
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    mvn %*
) else if exist "%USERPROFILE%\tools\apache-maven-3.9.16\bin\mvn.cmd" (
    call "%USERPROFILE%\tools\apache-maven-3.9.16\bin\mvn.cmd" %*
) else (
    echo [ERROR] Maven not found in PATH or at %USERPROFILE%\tools\apache-maven-3.9.16\bin\mvn.cmd
    exit /b 1
)
