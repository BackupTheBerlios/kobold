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
 * $Id: ProductLineTest.java,v 1.17 2004/06/24 11:58:18 rendgeor Exp $
 *
 */
package kobold.common.data.plam;


import java.util.Date;

import junit.framework.TestCase;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.model.FileDescriptor;
import kobold.common.model.Release;
import kobold.common.model.product.Product;
import kobold.common.model.product.ProductRelease;
import kobold.common.model.product.SpecificComponent;
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
		
		String path = "/tmp";
		
		//windows
		//String path = "c:\temp";
		
		//---------------------------------------------

		Productline pl = new Productline("windows");
		/**/
		pl.setRepositoryPath("bla.blubber.com");
		
		/////--------Product----------------
		//add a product
		Product productA = new Product ("me");
		
		RepositoryDescriptor descriptor = new RepositoryDescriptor ();
		productA.setRepositoryDescriptor(descriptor);
		
		productA.addProductRelease(new ProductRelease(new Date(11,2,2003)));
		
		Product productB = new Product ("xp");
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--add component (specific)
		SpecificComponent componentC = new SpecificComponent ("compC");
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
		
		//Note: ReleaseB has no FD!
		
		/////--------Component (CoreAsset)--------------------------------------------------
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
		//Release releaseGet = (Release) fd2.getParent();
		//Productline plGet = (Productline) fd2.getRoot ();
		
	

		//------------------------------
		//serialize the whole product-line (all included)
 
		//.productmetainfo 

		//pl.serializeProductline("/tmp", 0);


 		//serialize the products
		//productA.serializeProduct("/tmp");
		//productB.serializeProduct("/tmp");
		
		//serialize the coreAssets
		//coreAssetA.serializeComponent("/tmp");
		//coreAssetB.serializeComponent("/tmp");
		/**/

		//do all
		pl.serializeAll(path);

		
		/*Element ser = pl.serialize();
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
		}*/


	//----------------------------DESRERAILIZING------------------------------------------------		

		System.out.println ("-----------NOW DESERIALIZING---------");
		Productline p2 = new Productline("windows");
		p2.deserialize(path);
		
		
		//product = manager.getProduct("windows");
		//assertTrue (product.getName() == "windows");
		//...
		//productline = manager.getProductLine("office");
		//assertTrue (productline.getName() == "office");
	
	}
}
