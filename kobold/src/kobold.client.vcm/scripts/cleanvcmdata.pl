#!/usr/bin/perl
#
# See ../LICENSE.txt for license information.
#

# Implements a perl script "cleanvcmdata.pl" to clean up all
# VCM-specific data (CVS-dirs and files included)

#Datentypen:
# $ für Skalar,
# @ für Arrays,
# % für Hash,
# & für Funktion.

use strict;
use Cwd;

#for listing all files
use File::Find;

#the path to start to parse
my $currDir = $ARGV[0];

###MAIN###
##########

if ($#ARGV == 1)
{
    #set the directory to work into
    #print "HALLO, working in: $currDir";
    chdir "$currDir" or die "Can't cd to $currDir: $!\n";

    my $dir = shift; # shifted at next argument in @_
    print "Delete dir $dir\n";

    #delete all files in one dir
    
    opendir(DIR,"$dir");
    my @files=readdir(DIR);
    closedir(DIR);
    foreach(@files){
        print "Delete file $dir/$_\n";
        unlink("$dir/$_");
    }

    #now delete the empty dir
    rmdir("$dir");


}

else
{
    print "give me argument: dir to start with deletion!\n";
}
