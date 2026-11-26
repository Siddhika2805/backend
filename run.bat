@echo off
setlocal enabledelayedexpansion
title InterviewHub - Multi-Interviewer Interview Management System

cd /d "%~dp0"

echo =================================================
echo   Starting InterviewHub Console Application...
echo =================================================

:: Add Maven and Java paths if available
if exist "C:\Users\Lenovo\tools\apache-maven-3.9.16\bin" (
    set "PATH=%PATH%;C:\Users\Lenovo\tools\apache-maven-3.9.16\bin"
)

:: Option 1: Run standalone fat JAR if present
if exist "InterviewHub.jar" (
    java -jar "InterviewHub.jar" %*
    goto :end
)

:: Option 2: Run with classes and lib/ connector
if exist "target\classes\com\interviewhub\Main.class" (
    java -cp "target\classes;lib\*;target\*" com.interviewhub.Main %*
    goto :end
)

:: Option 3: Compile and run with Maven Wrapper
if exist "%~dp0mvnw.cmd" (
    call "%~dp0mvnw.cmd" exec:java
    goto :end
)

:: Option 4: System Maven
mvn exec:java

:end
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Application exited with code %ERRORLEVEL%.
    pause
)
