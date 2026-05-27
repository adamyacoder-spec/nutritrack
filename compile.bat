@echo off
echo Compiling NutriTrack...
cd src
javac --module-path "..\lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp ".;..\lib\mysql-connector-j-8.4.0.jar" Main.java model\*.java view\*.java controller\*.java dao\*.java util\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Compilation successful!
cd ..
pause
