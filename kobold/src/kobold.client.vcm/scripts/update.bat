@echo off
cd %1
cvs.exe -z3 -d :pserver:%2@%3:%4 up -dPA

