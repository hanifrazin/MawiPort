@echo off
REM GPS Cerdas: %~dp0 adalah path folder tempat script ini berada
java -Djava.awt.headless=true -jar "%~dp0target\mawiport-1.0-SNAPSHOT.jar" %*