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

cd /d %1

if %2 == CVS goto CVS endif
goto END

:CVS
if %3 == local goto LOCAL endif

if %9 == ""
    cvs -z3 -d :%3:%4:%5@%6:%7 co -d . %8
else
    cvs -z3 -d :%3:%4:%5@%6:%7 co -d . -r %9 %8
endif

goto END

:LOCAL
if %9 == ""
    cvs -z3 -d %7 co -d . %8
else
    cvs -z3 -d %7 co -d . -r %9 %8
endif

:END