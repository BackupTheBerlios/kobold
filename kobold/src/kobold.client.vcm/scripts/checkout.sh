#!/bin/sh
#PARAMETER:
#
#$1 working directory: /tmp

cd $1

#
#-d CVS_root_directory
#$2 username: rendgeor
#$3 cvs repository server: cvs.berlios.de
#$4 cvs repository path: /cvsroot/kobold
#$5 module: kobold

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2@$3:$4 co $5

