#!/usr/bin/perl
#
# See ../LICENSE.txt for license information.
#

# specified output format
# <type>\t<path>\t<revision>\t<date>\t<tag>

# TODO: check OS --> current directory solved!
#       confusing warnings perl -w ?

#Datentypen:
# $ für Skalar,
# @ für Arrays,
# % für Hash,
# & für Funktion.

use strict;
use Cwd;

#for date parsing
use Date::Parse;

#for listing all files
use File::Find;

#the global hash map
my %data;
#the path to start to parse
my $currDir = $ARGV[0];


sub read_entries {

    my $root = shift; # shifted at next argument in @_

    my $filename = "$root/CVS/Entries";
    my @temp;



    #empty directories with no included data aren't
    #checked out! --> don't display a message
    open(FILE, "< $filename" );
    #    or die "Can't open $filename : $!";

    #for all lines (filenames)
    while(<FILE>) {
        #chomp;
        
        #split each line by "/"
        my ($type, $path, $rev ,$date, $tag) = split( '\/' );
        
        #print "KOMPLETT: $type, $path, $rev ,$date, $tag\n";
     
        #convert date from: Thu Jul  1 09:36:12 2004
        #             to: year,month,date,hour,minutes,seconds

        #returns a unix time value, but what'S that??
        #my $time = str2time($date);
        #$date = $time;

        #or instead: (works)
        my ($ss,$mm,$hh,$day,$month,$year,$zone) = strptime($date);
        
        #year minus 1900
        #month: 0..11

        #fixes, only for displaying
        #$year = $year + 1900;
        #$month = $month + 1;

        $date = "$year,$month,$day,$hh,$mm,$ss";
        #print "HALLO: $ss,$mm,$hh,$day,$month,$year\n";

        #if the actual line ($_) represent 
        #a file
        if ($type ne "D") {
            #empty lines
            if (length($path) > 0) {
                #pretty printing "binary"
                $tag =~ s/-kb/binary/g;

                #!print "$root/$path\t$rev\t$date\t$tag\n";
                #store all in the hash map instead


                #absolut path:
                #    $data {"$root/$path"} = "$rev\t$date\t";
                    $data {"$path"} = "$rev\t$date\t$tag";
            }
        }
        
        #for all directories, call recursive

        #store all directories because when opening another file the old
        #isn't accessable!!!
        elsif ($type eq "D" ) 
        {
            #handling for error-lines only consist of "D"
            if (length($path) > 0)
            {
                #absloute path:
                #push (@temp, "$root/$path");

                #local path:
                push (@temp, "$path");
                #doesn't work recursive: read_entries("$root/$path");
            }
       }
            
    }
    close (FILE);

    #now do all subdirectories
    #print "ALLE: @temp\n";
    foreach my $array_element (@temp)
    {
        #print the directory name
        #directories: no need to store here!
        #!print "$array_element\n";


        #process all directories stored in @temp
        read_entries ($array_element);
    }

}

sub read_phys {
        ########ALL physical files and directories
        sub show{
            #print ("$File::Find::name \n");
            
            #to avoid that the currDir is printed too!
            if ("$File::Find::name" ne $currDir)
            {

                my $newFile = "$File::Find::name";

                my $currDirLength = length ($currDir);


                #erase the currDir
                #$newFile =~ s/\$currDir/g;

                #or:
                #cut off the prefix
                #$teilstring = substr(STRING,STARTPOS[,LAENGE])
                $newFile = substr ($newFile, $currDirLength);

                #directory?
                if ( -d "$File::Find::name" ) {

                    #store all in the hash map instead
                    $data {"$newFile"} = "D*";
                }
                elsif ( -T "$File::Fine::name" ) {
                    $data {"$newFile"} = "T*";
                }
                #file?
                else {
                    $data {"$newFile"} = "*";
                }
            }
        }
        find ((\&show),$currDir);
}

###MAIN###
##########

#or the current directory?
#my $currDir = cwd;#`pwd`; 
#chomp $currDir;

if ($#ARGV == 0)
{
    #set the directory to work into
    #print "HALLO, working in: $currDir";
    chdir "$currDir" or die "Can't cd to $currDir: $!\n";

    my $root = shift; # shifted at next argument in @_
    #print "beginning with: $currDir\n";
    read_phys;
    read_entries($currDir);


    #now print all entries of the hash map
    foreach my $key ( keys %data){
        print $key."\t".$data{$key}."\n"
    }
}

else
{
    print "give me argument: path to parse!\n";
}
