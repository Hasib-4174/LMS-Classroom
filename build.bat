@echo off
echo === LMS-Classroom Build & Run Script (Windows) ===

REM -----------------------------
REM Project Paths
REM -----------------------------
set PROJECT_ROOT=%cd%
set SRC_DIR=%PROJECT_ROOT%\src
set BIN_DIR=%PROJECT_ROOT%\bin
set MAIN_CLASS=app.App

REM -----------------------------
REM Prepare directories
REM -----------------------------
echo [INFO] Creating bin directory...
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

REM -----------------------------
REM Build Java sources
REM -----------------------------
echo [BUILD] Compiling Java files...

REM Create sources.txt listing all .java files
dir /S /B "%SRC_DIR%\*.java" > sources.txt

javac -d "%BIN_DIR%" @sources.txt
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    del sources.txt
    exit /b %errorlevel%
)

del sources.txt
echo [SUCCESS] Compilation complete.

REM -----------------------------
REM Copy resources
REM -----------------------------
if exist "%SRC_DIR%\resources" (
    echo [INFO] Copying resources...
    xcopy "%SRC_DIR%\resources" "%BIN_DIR%\resources" /E /I /Y >nul
    echo [SUCCESS] Resources copied.
) else (
    echo [INFO] No resources folder found. Skipping.
)

REM -----------------------------
REM Run Project
REM -----------------------------
echo [RUN] Starting application...
java -cp "%BIN_DIR%" %MAIN_CLASS%
set EXIT_CODE=%errorlevel%

echo.
echo === Program exited with code: %EXIT_CODE% ===
exit /b %EXIT_CODE%
