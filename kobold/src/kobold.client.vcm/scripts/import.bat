@echo off
cd /d %1


if %3 == local	goto LOCAL	endif

if %2 == CVS goto CVS endif
goto ERROR

:CVS
cvs -z3 -d :pserver:%4:%5@%6:%7 import -m %9 %8 start %4
goto END

:LOCAL
cvs -z3 -d %5 import -m %6 %7 %8 %9
goto END

:ERROR
echo This VCM Type is not supported by this script

:END


