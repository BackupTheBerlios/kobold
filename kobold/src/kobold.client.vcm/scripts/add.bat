@echo off
cd /d %1

if %3 == local					goto LOCAL	endif
if %2 == CVS goto CVS endif
goto ERROR

:CVS
echo cvs.exe -z3 -d :pserver:%4:%5@%6:%7 add %8
goto END

:LOCAL
cvs -z3 -d %5 add %6

:END
