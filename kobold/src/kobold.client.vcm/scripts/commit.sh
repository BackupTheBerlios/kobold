#!/bin/sh
#PARAMETER:

#$1 working directory: /tmp
#-d CVS_root_directory
#$2 username: rendgeor
#$3 userpassword: *******

#$4 cvs repository server: cvs.berlios.de
#$5 cvs repository path: /cvsroot/kobold
#$6 cvs log-message: "bloop"
#$7 file/s: "bla.c bla.h" (blank->commit all)

cd $1

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2:$3@$4:$5 commit -m $6 $7 

