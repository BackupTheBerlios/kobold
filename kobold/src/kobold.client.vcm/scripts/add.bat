@echo off
rem %1 working directory
rem %2 repo type
rem %3 protocol type
rem %4 username
rem %5 password
rem %6 host
rem %7 root
rem %8 module
rem %9 userdef

cd /d %1

IF %2==CVS GOTO CVS

goto END

:CVS
if %3==local goto LOCAL endif

cvs -z3 -d :%3:%4:%5@%6:%7 add %8

goto END

:LOCAL
cvs -z3 -d %7 add %8

:END
