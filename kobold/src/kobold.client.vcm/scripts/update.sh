#!/bin/sh
#PARAMETER:
#
#$1 working directory: /tmp

cd $1

#
#-d CVS_root_directory
#$2 username: rendgeor
#$3 userpassword: *******

#$4 cvs repository server: cvs.berlios.de
#$5 cvs repository path: /cvsroot/kobold

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2:$3@$4:$5 up -dPA

