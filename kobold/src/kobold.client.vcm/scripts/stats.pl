#!/usr/bin/perl
#
# See ../LICENSE.txt for license information.
#

# specified output format
# <type>\t<path>\t<revision>\t<date>\t<tag>

# TODO: check OS --> current directory solved!
#       confusing warnings perl -w ?

use strict;
use Cwd;

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
        my ($type, $path, $rev ,$date, $tag) = split( '\/' );
        
        #print "KOMPLETT: $type, $path, $rev ,$date, $tag\n";


        #if the actual line ($_) represent a file
        if ($type ne "D") {
            #pretty printing "binary"
            $tag =~ s/-kb/binary/g;

            print "$root/$path\t$rev\t$date\t$tag\n";
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
                #doesn't work: read_entries("$root/$path");
            }
       }
            
    }
    close (FILE);

    #now do all subdirectories
    #print "ALLE: @temp\n";
    foreach my $array_element (@temp)
    {
        #print the directory name
        print "$array_element\n";
        read_entries ($array_element);
    }

}

###MAIN###

if ($#ARGV == 0)
{
    #or the current directory?
    #my $currDir = cwd;#`pwd`; #chomp $currDir;
    my $currDir = $ARGV[0];

    #print "beginning with: $currDir\n";
    read_entries($currDir);
}
else
{
    print "give me argument path!\n";
}
