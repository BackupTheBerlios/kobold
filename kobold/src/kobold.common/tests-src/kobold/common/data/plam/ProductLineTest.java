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
 * $Id: ProductLineTest.java,v 1.10 2004/06/23 15:00:39 rendgeor Exp $
 *
 */
package kobold.common.data.plam;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import junit.framework.TestCase;
import kobold.common.model.FileDescriptor;
import kobold.common.model.Release;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Productline;
import kobold.common.model.productline.Variant;



/**
 * @author garbeam
 *
 * TestCase for ProductLine.
 */
public class ProductLineTest extends TestCase {

	/**
	 * Constructor for ProductLineTest.
	 * @param arg0
	 */
	public ProductLineTest(String arg0) {
		
		super(arg0);
	}
	
	public void testSerialize() {
		
		
		Productline pl = new Productline("windows");
		pl.setRepositoryPath("bla.blubber.com");
		
		/////--------Product----------------
		//add a product
		/*Product productA = new Product ("me");
		productA.setRepositoryPath("cvs.berlios.de");
		Product productB = new Product ("xp");
		productA.setRepositoryPath("svn.berlios.de");
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--add component
		ComponentSpecific componentC = new ComponentSpecific ("compC");
		productA.addComponent(componentC);
		
		//--add Release
		Release releaseA = new Release ("versA");
		Release releaseB = new Release ("versB");

		componentC.addRelease(releaseA);
		componentC.addRelease(releaseB);

		//--add FD
		FileDescriptor fd1 = new FileDescriptor ("fd1");
		fd1.setPath("/tmp/");
		releaseA.addFileDescriptor(fd1);
		
		//--add a second FD
		FileDescriptor fd1_1 = new FileDescriptor ("fd1_1");
		FileDescriptor fd1_2 = new FileDescriptor ("fd1_2");
		fd1_2.setLastAuthor("garbeam");
		fd1_2.setLastChanged(new Date());
		fd1_2.setRevision("1.2.3");
		fd1_2.setPath("hallo.dat");
		
		fd1.addFileDescriptor(fd1_1);
		fd1.addFileDescriptor(fd1_2);
		
		*/

		//ReleaseB has no FD!
		
		/////--------CoreAsset--------------------------------------------------
		//add a component
		Component coreAssetA = new Component("coreAssetA");
		coreAssetA.setRepositoryPath("cvs.berlios.de");
		Component coreAssetB = new Component("coreAssetB");
		coreAssetB.setRepositoryPath("svn.berlios.de");
		
		pl.addComponent(coreAssetA);
		pl.addComponent(coreAssetB);

		//--add variant--\\
		Variant variantA = new Variant ("varA");
		Variant variantB = new Variant ("varB");
		
		coreAssetA.addVariant(variantA);
		coreAssetA.addVariant(variantB);

		//--add Release		
		Release ReleaseC = new Release ("versC");
		Release ReleaseD = new Release ("versD");

		variantA.addRelease(ReleaseC);
		variantA.addRelease(ReleaseD);

		//--add FD
		FileDescriptor fd2 = new FileDescriptor ("fd2");
		
		ReleaseC.addFileDescriptor(fd2);

		//ReleaseB has no FD!
		
		//\\--add component to a variant
		Component componentD = new Component("compC");
		variantA.addComponent(componentD);
		
		//getParents and Roots
		Release releaseGet = (Release) fd2.getParent();
		//Productline plGet = (Productline) fd2.getRoot ();
		
		//------------------------------
		//serialize the whole product-line (all included)
		// .productlinemetainfo 
		Element ser = pl.serialize();
		try {
			XMLWriter w = new XMLWriter(System.out, OutputFormat.createPrettyPrint());
			w.write(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ser = coreAssetA.serialize();
		try {
			XMLWriter w = new XMLWriter(System.out, OutputFormat.createPrettyPrint());
			w.write(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//.productmetainfo 
		//pl.serialize("test-productmetainfo.xml", 1);
		//.coreassetmetainfo 
		//pl.serialize("test-coreassetmetainfo.xml", 2);
		
		//pl.removeProduct();
		//...	

		//pl.deserialize("test-product.xml");
		
		//product = manager.getProduct("windows");
		//assertTrue (product.getName() == "windows");
		//...
		//productline = manager.getProductLine("office");
		//assertTrue (productline.getName() == "office");
	
	}
}
