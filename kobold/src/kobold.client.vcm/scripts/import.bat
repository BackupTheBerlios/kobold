@echo off
cd %1

if %4 = "local"					goto LOCAL	endif

cvs -z3 -d :pserver:%2:%3@%4:%5 import -m %6 %7 %8 %9
goto END

:LOCAL
cvs -z3 -d %5 import -m %6 %7 %8 %9

:END
