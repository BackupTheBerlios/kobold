#!/bin/sh
#PARAMETER:
#
#$1 working directory: /tmp

cd $1

#
#-d CVS_root_directory
#$2 username: rendgeor
#$3 userpassword: *******

#local or other!
#$4 cvs repository server: cvs.berlios.de
#$5 cvs repository path: /cvsroot/kobold --> /cvsroot/kobold/CVSROOT must exist!
if [ $4 = "local" ] ; then

    cvs -z3 -d $5 import -m $6 $7 $8 $9

fi

if [ $4 != "local" ] ; then

#$5 cvs repository path: /cvsroot/kobold
#$6 cvs message : "initial import"
#$7 cvs module name: blasser
#$8 cvs user: rendgeor
#$9 cvs release tag: R0

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2:$3@$4:$5 import -m $6 $7 $8 $9

fi
