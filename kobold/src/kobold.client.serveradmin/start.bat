rem this batch file's purpose is to help users start the
rem Kobold server administartion client with the right parameters
rem please see the manual for more information
@echo off
cls
java -Dkobold.sat.configFile=.\src\sat.properties -jar kobold.client.serveradmin_fat.jar 23232
