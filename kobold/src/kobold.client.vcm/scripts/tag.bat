@echo off
cd /d %1

if %3 == local					goto LOCAL	endif
if %2 == CVS goto CVS endif
goto ERROR

:CVS
echo cvs.exe -z3 -d :%3:%4:%5@%6:%7 tag %9 %8
goto END

:LOCAL
cvs -z3 -d %7 tag %9 %8

:END
