#!usr/bin/perl
#
# See ../LICENSE.txt for license information.
#

# generates appendix for usermanual

use strict;
print "\\chapter{Source Code}\n";

print "\\section{IKoboldServer}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/controller/IKoboldServer.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";

print "\\section{IKoboldServerAdministration}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/controller/IKoboldServerAdministration.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";

print "\\section{AbstractKoboldMessage}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/AbstractKoboldMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";

print "\\section{Asset}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/Asset.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";



print "\\section{Component}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/Component.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";




print "\\section{IdManager}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/IdManager.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";




print "\\section{KoboldMessage}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/KoboldMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";






print "\\section{Product}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/Product.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";





print "\\section{Productline}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/Productline.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";




print "\\section{RPCSpy}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/RPCSpy.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";






print "\\section{User}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/User.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";





print "\\section{UserContext}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/UserContext.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";





print "\\section{WorkflowItem}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/WorkflowItem.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";
print "\\end{verbatim}\n";



print "\\section{WorkflowMessage}\n";
print "\\small \\begin{verbatim}\n";
open(FILE, "../../src/kobold.common/src/kobold/common/data/WorkflowMessage.java" );
while(<FILE>) {
    print $_;
}
close (FILE);
print "\n";


print "\\end{verbatim}";
