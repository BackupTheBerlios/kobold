/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: FileDescriptorTest.java,v 1.1 2004/08/06 11:07:19 garbeam Exp $
 *
 */
package kobold.client.vcm.test;

import java.util.Iterator;

import junit.framework.TestCase;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.vcm.controller.StatusUpdater;

/**
 * @author rendgeor
 *
 * TestCase for VCM actions.
 */
public class FileDescriptorTest extends TestCase {

	/**
	 * Constructor for ProductLineTest.
	 * @param arg0
	 */
	public FileDescriptorTest(String arg0) {
		
		super(arg0);
	}
	

	
	public void testStatusUpdater ()
	{
	    String dump = 
	            "./src/CVS	*\n" + 
	            "./config/CVS/Root	*\n" + 
	            "./autom4te-2.53.cache	D*\n" + 
	            "./src	D*\n" + 
	            "./config/CVS/Repository	*\n" + 
	            "./doc/portsman.man	1.4	1043939574\n" + 
	            "./CVS	D*\n" + 
	            "./Makefile.am	1.3	1043939574\n" + 
	            "./src/fns.h	1.22	1064905457\n" + 
	            "./CVS/Root	*\n" + 
	            "./stats.txt	*\n" + 
	            "./src/CVS/Entries	*\n" + 
	            "./doc/CVS/Repository	*\n" + 
	            "./src/tree.c	1.4	1043939574\n" + 
	            "./NEWS	1.2	1043939574\n" + 
	            "./src/parse.c	1.21	1064905457\n" + 
	            "./doc/CVS/Root	*\n" + 
	            "./config.log	*\n" + 
	            "./CVS/Entries	*\n" + 
	            "./doc	D*\n" + 
	            "./src/CVS/Repository	*\n" + 
	            "./src/res.h	1.8	1064905457\n" + 
	            "./autogen-rh72.sh	*\n" + 
	            "./src/includes.h	1.6	1064905457\n" + 
	            "./README	1.3	1064905457\n" + 
	            "./autom4te-2.53.cache/output.0	*\n" + 
	            "./Makefile	*\n" + 
	            "./doc/Makefile.in	*\n" + 
	            "./CONTRIB	1.4	1043939574\n" + 
	            "./doc/Makefile.am	1.6	1043939574\n" + 
	            "./src/window.c	1.12	1043939574\n" + 
	            "./src/types.h	1.17	1064905458\n" + 
	            "./src/list.c	1.7	1043939574\n" + 
	            "./config.status	*\n" + 
	            "./aclocal.m4	*\n" + 
	            "./autom4te-2.53.cache/traces.0	*\n" + 
	            "./FAQ	1.1.1.1	1043078869\n" + 
	            "./config/CVS	*\n" + 
	            "./doc/Makefile	*\n" + 
	            "./src/compare.c	1.3	1043939574\n" + 
	            "./src/system.c	1.21	1064905457\n" + 
	            "./Makefile.in	*\n" + 
	            "./configure.ac	1.5	1043939574\n" + 
	            "./CVS/Repository	*\n" + 
	            "./config/CVS/Entries	*\n" + 
	            "./config/portsmanrc.sample	1.6	1043939574\n" + 
	            "./config/Makefile.in	*\n" + 
	            "./TODO	1.4	1064905457\n" + 
	            "./COPYING	1.2	1043939574\n" + 
	            "./COPYRIGHT	1.2	1043939574\n" + 
	            "./ChangeLog	1.3	1064905457\n" + 
	            "./doc/CVS/Entries	*\n" + 
	            "./src/consts.h	1.17	1064905457\n" + 
	            "./src/main.c	1.23	1043939574\n" + 
	            "./config/Makefile	*\n" + 
	            "./doc/portsmanrc.man	1.5	1043939574\n" + 
	            "./src/tree.h	1.5	1064905457\n" + 
	            "./autom4te-2.53.cache/requests	*\n" + 
	            "./install-sh	*\n" + 
	            "./src/manage.c	1.24	1064905457\n" + 
	            "./config	D*\n" + 
	            "./CVS/Entries.Log	*\n" + 
	            "./src/Makefile.in	*\n" + 
	            "./mkinstalldirs	*\n" + 
	            "./config/Makefile.am	1.3	1064905457\n" + 
	            "./src/Makefile.am	1.3	1043939574\n" + 
	            "./AUTHORS	1.1.1.1	1043078869\n" + 
	            "./BUGS	1.2	1043413812\n" + 
	            "./configure	*\n" + 
	            "./autogen.sh	1.1.1.1	1043078869\n" + 
	            "./doc/CVS	*\n" + 
	            "./INSTALL	1.1.1.1	1043078869\n" + 
	            "./src/globals.h	1.10	1064905457\n" + 
	            "./src/Makefile	*\n" + 
	            "./src/io.c	1.2	1043939574\n" + 
	            "./missing	*\n" + 
	            "./src/CVS/Root	*\n" + 
	            "./src/list.h	1.7	1064905457\n" + 
	            "./LICENSE	1.2	1043939574\n" + 
	            "./src/browse.c	1.34	1043939574";

		//update FD(s)
		StatusUpdater statUp = new StatusUpdater ();
		
		FileDescriptor fd = new FileDescriptor();
		fd.setFilename("$$$root");
		fd.setDirectory(true);
		statUp.parseInputString(fd, dump);
	
		prettyPrintFD(fd);
	}
	
	private void prettyPrintFD(FileDescriptor fd) {
	    System.out.println("fd: " + fd.getFilename() + "\t" + fd.getRevision() +
	                       ((fd.getLastChange() != null) ? "\t" + fd.getLastChange().toString() : ""));
	    for (Iterator iterator = fd.getFileDescriptors().iterator();
	         iterator.hasNext(); )
	    {
	        prettyPrintFD((FileDescriptor)iterator.next());
	    }
	}
		
}
