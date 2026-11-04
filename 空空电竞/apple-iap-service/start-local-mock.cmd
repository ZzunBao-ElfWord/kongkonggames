@echo off
setlocal
title Kongling Apple IAP Local Mock
cd /d "%~dp0"
set "JAVA_EXE=C:\Users\admin\AppData\Local\Temp\temurin-jdk11\jdk-11.0.32.1+1\bin\java.exe"
set "APP_JAR=%~dp0target\apple-iap-service-0.1.0-SNAPSHOT.jar"

if not exist "%JAVA_EXE%" (
  echo [ERROR] Java 11 was not found:
  echo %JAVA_EXE%
  echo Please install Java 11 or update JAVA_EXE in this file.
  pause
  exit /b 1
)

if not exist "%APP_JAR%" (
  echo [ERROR] The application package was not found:
  echo %APP_JAR%
  echo Run rebuild-local-mock.cmd first.
  pause
  exit /b 1
)

echo Starting Apple IAP local mock service...
echo Keep this window open while testing.
echo After startup, open: http://127.0.0.1:8091
echo.
"%JAVA_EXE%" -jar "%APP_JAR%" --spring.profiles.active=local-mock
set "EXIT_CODE=%ERRORLEVEL%"
echo.
echo The service stopped with exit code %EXIT_CODE%.
pause
exit /b %EXIT_CODE%
