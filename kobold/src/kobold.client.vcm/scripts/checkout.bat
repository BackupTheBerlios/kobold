
cd /d %1
Set test=%1
if %4 == local					goto LOCAL	endif
if %2 == CVS goto CVS endif
goto ERROR

:CVS
SHIFT
cd ..
set test2= %CD%
set test2==%test%-%test2%
echo %test%%CD%
rem set dir=
echo cvs.exe -z3 -d:pserver:%3:%4@%5:%6 co -d %test% %7 %8
goto END

:LOCAL
cvs -z3 -d %5 co %7 %6
goto END

:ERROR
echo This VCM Type is not supported by this script
:END
