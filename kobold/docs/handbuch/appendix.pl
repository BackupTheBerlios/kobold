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

open(FILE, "../../src/kobold.common/src/kobold/common/data/AbstractKoboldMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/Asset.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/Component.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/IdManager.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/KoboldMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/Product.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/Productline.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/RPCSpy.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/User.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/UserContext.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/WorkflowItem.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

open(FILE, "../../src/kobold.common/src/kobold/common/data/WorkflowMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);

print "\\end{verbatim}}";
