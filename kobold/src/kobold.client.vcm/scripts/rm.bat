@echo off
cd %1

if %4 = "local"					goto LOCAL	endif

cvs.exe -z3 -d :pserver:%2:%3@%4:%5 rm %6

:LOCAL
cvs -z3 -d %5 rm %6
