rem this batch file's purpose is to help users start the
rem Kobold server with the right parameters
rem please see the manual for more information
@echo off
cls
java -Dkobold.server.configFile=/home/rendgeor/workspace/server.properties -jar kobold.server_fat.jar 23232
