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
 * $Id: FileDescriptorTest.java,v 1.7 2004/08/24 14:51:05 garbeam Exp $
 *
 */
package kobold.client.vcm.test;

import java.util.Iterator;

import org.eclipse.core.runtime.IPath;

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
			//+"/src/kobold\tD*\n"
			+"/src/kobold/client\tD*\n"
			+"/src/kobold/client/plam\tD*\n"
			+"/src/kobold/client/plam/model\tD*\n"
			+"/src/kobold/client/plam/model/IComponentContainer.java\t*\n"
			+"/src/kobold/client2/plam/model/IComponentContainer2.java\t*\n"
			+"/src/kobold/client/vcm/popup/action/DisconnectAction.java\t1.2\t104,5,13,16,33,39\n"
			;
		//update FD(s)
		StatusUpdater statUp = new StatusUpdater ();
		
		FileDescriptor fd = new FileDescriptor();
		fd.setFilename("$$$root");
		fd.setDirectory(true);
		statUp.parseInputString(fd, dump);
	
		prettyPrintFD(fd, "");
	}
	
	/**
	 * prints the root node and all children
	 * @param fd
	 */
	private void prettyPrintFD(FileDescriptor fd, String prefix) {
	    String newPrefix = prefix + IPath.SEPARATOR +   fd.getFilename();
	    System.out.println("fd: "+ newPrefix + "\t" + fd.getRevision() +
	                       ((fd.getLastChange() != null) ? "\t" + fd.getLastChange().toString() : ""));

	    //get all children
	    for (Iterator iterator = fd.getFileDescriptors().iterator();
	         iterator.hasNext(); )
	    {
	    	FileDescriptor fdActual = (FileDescriptor)iterator.next();
	    	
	        prettyPrintFD(fdActual, newPrefix);

	    }
	}
		
}
