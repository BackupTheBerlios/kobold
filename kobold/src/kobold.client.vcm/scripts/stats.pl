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

#for listing all files
use File::Find;

#the global hash map
my %data;


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
     


        #if the actual line ($_) represent a file
        if ($type ne "D") {
            #empty lines
            if (length($path) > 0) {
                #pretty printing "binary"
                $tag =~ s/-kb/binary/g;

                #!print "$root/$path\t$rev\t$date\t$tag\n";
                #store all in the hash map instead
                $data {"$root/$path"} = "$rev\t$date\t$tag";
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
                push (@temp, "$root/$path");
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

###MAIN###

if ($#ARGV == 0)
{
    #or the current directory?
    #my $currDir = cwd;#`pwd`; 
    #chomp $currDir;

    #the path to start to parse
    my $currDir = $ARGV[0];

    my $root = shift; # shifted at next argument in @_

########ALL physical files and directories
    sub show{
        #print ("$File::Find::name \n");
        
        #store all in the hash map instead
        $data {"$File::Find::name"} = "";
    }
    find (\&show,$currDir);


##########

    #print "beginning with: $currDir\n";
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
