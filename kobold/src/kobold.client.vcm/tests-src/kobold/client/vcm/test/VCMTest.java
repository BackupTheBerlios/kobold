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
 * $Id: VCMTest.java,v 1.7 2004/09/01 09:22:33 garbeam Exp $
 *
 */
package kobold.client.vcm.test;


import junit.framework.TestCase;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.controller.StatusUpdater;

/**
 * @author rendgeor
 *
 * TestCase for VCM actions.
 */
public class VCMTest extends TestCase {

	/**
	 * Constructor for ProductLineTest.
	 * @param arg0
	 */
	public VCMTest(String arg0) {
		
		super(arg0);
	}
	
	public void testBla () 
	{
		
		//start the VCMPlugin...
		//TODO: no KoboldVCMPlugin exist!? -->return null
		//KoboldVCMPlugin.getDefault();
		
		
		//create KoboldAction object
		//KoboldAction action= new KoboldAction ();
		
		//TODO: NullPointerException!
		//System.out.println ("Local Path: " + action.getLocalPath());
	}
	
	public void testStatusUpdater ()
	{
		//---Test the Status Updater ----//
		
		//create an IFileDescriptorContainer object
		Variant var = new Variant ("testVar");
		
		
		//update FD(s)
		StatusUpdater statUp = new StatusUpdater ();

		String[] command = {"perl", "/home/rendgeor/workspace/kobold.client.vcm/scripts/"+ 
				"stats.pl", "/home/rendgeor/workspace/kobold.client.vcm/"};
		
		//statUp.processConnection(command, var);

		
		//assertTrue (productline.getName() == "office");
	

	}

		
}
