@echo off
setlocal
title Rebuild Kongling Apple IAP Local Mock
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\admin\AppData\Local\Temp\temurin-jdk11\jdk-11.0.32.1+1"
set "MAVEN=Z:\KLlocal\tools\maven\apache-maven-3.8.8\bin\mvn.cmd"
set "MAVEN_REPO=%~dp0..\.m2"

if not exist "%JAVA_HOME%\bin\java.exe" (
  echo [ERROR] Java 11 was not found.
  pause
  exit /b 1
)
if not exist "%MAVEN%" (
  echo [ERROR] Maven was not found.
  pause
  exit /b 1
)

call "%MAVEN%" "-Dmaven.repo.local=%MAVEN_REPO%" clean package
if errorlevel 1 (
  echo.
  echo [ERROR] Build failed.
  pause
  exit /b 1
)
echo.
echo Build completed successfully.
pause
