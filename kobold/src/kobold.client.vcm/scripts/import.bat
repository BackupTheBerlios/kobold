@echo off
cd %1
cvs -z3 -d :pserver:%2:%3@%4:%45import -m %6 %7 %8 %9