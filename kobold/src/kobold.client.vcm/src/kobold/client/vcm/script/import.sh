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
#$5 cvs message : "initial import"
#$6 cvs module name: blasser
#$7 cvs user: rendgeor
#$8 cvs release tag: R0

#LOGIN wird vorrausgestzt-->erzeugt .cvspass
#cvs -d :pserver:anonymous@cvs.berlios.de:/cvsroot/kobold login 
cvs -z3 -d :pserver:$2@$3:$4 import -m $5 $6 $7 $8

