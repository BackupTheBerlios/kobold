#!/usr/bin/perl -w
#
# See ../LICENSE.txt for license information.
#

# specified output format
# <type>\t<path>\t<revision>\t<date>\t<flags>

# TODO: check OS

use strict;

sub read_entries {
    my $root = shift; # shifted at next argument in @_
    my $filename = "$root/CVS/Entries";

    open(FILE, "< $filename" )
        or die "Can't open $filename : $!";

    my ($type, $path, $rev ,$date, $tag);
    $type = $path = $rev = $date = $tag = "";
    while(<FILE>) {
        chomp;
        if (!($_ eq "D")) {
            my ($type, $path, $rev ,$date, $tag) = split( '\/' );
            $_ = $tag;
            $tag =~ s/-kb/binary/g;
            if ($type eq "D") {
                read_entries("$root/$path");
            }
            print "$root/$path\t$rev\t$date\t$tag\n";
        }
    }
}

my $currDir = `pwd`;
chomp $currDir;

#print "beginning with: $currDir\n";
read_entries($currDir);
