@echo off
cd %1
cvs -z3 -d :pserver:%2@%3:%4 import -m %5 %6 %7 %8