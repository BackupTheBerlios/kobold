@echo off
rem %1 working directory
rem %2 repo type
rem %3 protocoal type
rem %4 username
rem %5 password
rem %6 host
rem %7 root
rem %8 module
rem %9 userdef

echo cd /d %1

if %2 == CVS goto CVS
goto END

:CVS
if %3 == local goto LOCAL

if "%9" == "" GOTO EMPTY1
echo    cvs -z3 -d :%3:%4:%5@%6:%7 up -dP -r %9

goto END

:EMPTY1
echo    cvs -z3 -d :%3:%4:%5@%6:%7 up -dP
goto END

:LOCAL
if "%9" == "" goto EMPTY2
echo    cvs -z3 -d %7 up -dP -r %9
goto END

:EMPTY2
echo    cvs -z3 -d %7 up -dP
goto END

:END
