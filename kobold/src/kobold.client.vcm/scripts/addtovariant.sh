#!/bin/sh
#
echo addtovariant $1 $2 $3 $4

echo cd "$1"
cd "$1" || exit 1

