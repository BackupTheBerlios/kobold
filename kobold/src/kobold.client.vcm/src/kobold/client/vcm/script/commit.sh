#!/bin/sh
#PARAMETER:
#-d CVS_root_directory
#
#$1 username: rendgeor
#$2 cvs repository server: cvs.berlios.de
#$3 cvs repository path: /cvsroot/kobold
#$4 file/s: "bla.c bla.h" (blank->commit all)
#$5 cvs message: "added bloop"

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$1@$2:$3 commit -m$4

