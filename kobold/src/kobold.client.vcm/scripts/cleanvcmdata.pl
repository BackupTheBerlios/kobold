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
use File::Spec::Functions;

###MAIN###
##########


#if ($#ARGV == 1)
if ($#ARGV == 0)
{
    #set the directory to work into
    #print "HALLO, working in: $currDir";

    #the path to start to parse
    #my $currDir = $ARGV[0];
    my $currDir = shift;


    #dir-pattern to delete
    #my $dir = shift; # shifted at next argument in @_
    my $dir = "CVS";

    print "Delete dirs called: $dir\n";

    #find all dirs
    find( \&wanted, $currDir);

    sub wanted {
        return unless -d $_;
        #return if $_ eq curdir();
     
        print "$File::Find::name\n";

        #call delete dir with: "@_"
        deleteDir ($File::Find::name);
     }

    #delete all files in one dir
    
    sub deleteDir {
        #acces to parameters: $_[0],$_[1],$_[2],...

        #don't change the dir --> File:Find will not process any longer!
        #print "Changing to: $_[0]\n";
        #chdir "$_[0]" or die "Can't cd to $currDir: $!\n";

            
        if (opendir(DIR, "$_[0]/$dir")) {
            print "HALLO: $_[0]/$dir\n";

            my @files=readdir(DIR);
            closedir(DIR);
            foreach(@files){
                print "Delete file $_[0]/$dir/$_\n";
                unlink("$_[0]/$dir/$_");
            }
        }

        #now delete the empty dir
        rmdir("$_[0]/$dir");
    }

}

else
{
    print "give me argument: absolut-path to start with deletion!\n";# dir-pattern!\n";
}
