#!/usr/bin/perl
#
# See ../LICENSE.txt for license information.
#

# generates appendix for usermanual

use strict;

print "{\\small \\begin{verbatim}";
open(FILE, "../../src/kobold.common/src/kobold/common/controller/IKoboldServer.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\\end{verbatim}}";
