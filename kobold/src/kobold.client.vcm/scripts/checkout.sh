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
# $9 userdef
#
echo co $1 $2 $3 $4 $5 $6 $7 $8 $9

if [ $2 != "CVS" ] ; then
    exit;
fi

echo cd "$1"
cd "$1"

if [ $3 == "local" ] ; then

echo    cvs -z3 -d $7 co $8
    cvs -z3 -d $7 co $8

else

echo    cvs -z3 -d :$3:$4:$5@$6:$7 co $8

    cvs -z3 -d :$3:$4:$5@$6:$7 co $8

fi
