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
 * $Id: FileDescriptorTest.java,v 1.3 2004/08/24 11:03:26 rendgeor Exp $
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
			"/src\tD*\n"
			+"/src/kobold\tD*\n"
			+"/src/kobold/client\tD*\n"
			+"/src/kobold/client/plam\tD*\n"
			+"/src/kobold/client/plam/model\tD*\n"
			+"/src/kobold/client/plam/model/IComponentContainer.java\t*\n"
			;
		//update FD(s)
		StatusUpdater statUp = new StatusUpdater ();
		
		FileDescriptor fd = new FileDescriptor();
		fd.setFilename("$$$root");
		fd.setDirectory(true);
		statUp.parseInputString(fd, dump);
	
		//prettyPrintFD(fd);
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
