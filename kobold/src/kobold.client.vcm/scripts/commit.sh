#!/bin/sh
#PARAMETER:
#$1 working directory: /tmp

cd $1

#-d CVS_root_directory
#$1 username: rendgeor
#$2 cvs repository server: cvs.berlios.de
#$3 cvs repository path: /cvsroot/kobold
#$4 file/s: "bla.c bla.h" (blank->commit all)
#$5 cvs message: "bloop"

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2@$3:$4 commit $5 #-m $5

