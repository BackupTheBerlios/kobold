@echo off
cd %1

if %4 = "local"					goto LOCAL	endif

cvs.exe -z3 -d :pserver:%2:%3@%4:%5 add %6
goto END

:LOCAL
cvs -z3 -d %5 add %6

:END
