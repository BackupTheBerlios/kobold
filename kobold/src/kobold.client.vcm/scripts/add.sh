#!/bin/sh
#
# $1 working directory
# $2 repo type
# $3 protocoal type
# $4 username
# $5 password
# $6 host
# $7 root
# $8 module
#

if [ $2 != "CVS" ] ; then
    exit;
fi

cd $1

if [ $2 = "local" ] ; then

    cvs -z3 -d $7 add $8

else

    cvs -z3 -d :$3:$4:$5@$6:$7 add $8

fi
