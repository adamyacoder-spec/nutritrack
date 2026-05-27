@echo off
echo Starting NutriTrack JavaFX Dashboard...
cd src
java --module-path "..\lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp ".;..\lib\mysql-connector-j-8.4.0.jar" view.Dashboard
cd ..
