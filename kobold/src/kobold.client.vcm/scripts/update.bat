@echo off
cd /d %1

if %3 == local					goto LOCAL	endif
if %2 == CVS goto CVS endif
goto ERROR

:CVS
cvs.exe -z3 -d :pserver:%4:%5@%6:%7 up -dPA %8
goto END

:LOCAL
cvs -z3 -d %5 up -dPA %6
goto END

:ERROR
echo This VCM Type is not supported by this script
:END
