@echo off
set BASEDIR=C:\data
set LOGPATH=%BASEDIR%\log
set MAINCLASS=info.shenc.aliyunddnsrefresher.AliyunDdnsRefresherApplication
java -server -cp "%BASEDIR%/conf;%BASEDIR%\lib\*" -DlogPath=%LOGPATH% %MAINCLASS% >> %LOGPATH%\consoleOut.log