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
 * $Id: ProductlineTest.java,v 1.8 2004/08/05 18:04:21 martinplies Exp $
 *
 */
package kobold.client.plam.model;
import java.sql.Date;

import junit.framework.TestCase;
import kobold.client.plam.MetaInformation;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductRelease;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.common.io.RepositoryDescriptor;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;



/**
 * @author garbeam
 *
 * TestCase for ProductLine.
 */
public class ProductlineTest extends TestCase {

	/**
	 * Constructor for ProductLineTest.
	 * @param arg0
	 */
	public ProductlineTest(String arg0) {
		super(arg0);
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());

	}
	//TODO
	public void testSerialize() {
		
		String path = "/tmp";
		
		//windows
		//String path = "c:\temp";
		
		//---------------------------------------------

		Productline pl = new Productline();
		pl.setName("PLtest23");

		/////--------Product----------------
		//add a product
		Product productA = new Product (pl);
		productA.setName("me");
		
		RepositoryDescriptor descriptor = new RepositoryDescriptor ();
		productA.setRepositoryDescriptor(descriptor);
		
		productA.addProductRelease(new ProductRelease(new Date(10,10,04)));
		
		Product productB = new Product (pl);
		productB.setName("xp");
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--add component (specific)
		SpecificComponent componentC = new SpecificComponent ("compC");
		productA.addComponent(componentC);
		
		//--add Release
		Release releaseA = new Release ();
		releaseA.setName("versA");
		
		Release releaseB = new Release ();
		releaseB.setName("versB");

		componentC.addRelease(releaseA);
		componentC.addRelease(releaseB);

		//--add FD
/*		FileRevision fd1 = new FileRevision ();
		fd1.setFilename("/tmp/");
		releaseA.addFileRevision(fd1);
		
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
		*/
		/////--------Component (CoreAsset)--------------------------------------------------
		//add a component
		Component coreAssetA = new Component("coreAssetA");
		Component coreAssetB = new Component("coreAssetB");
		
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
		FileDescriptor fd2 = new FileDescriptor ();
		fd2.setFilename("bla");
		
		//ReleaseC.addFileRevision(fd2);

		//ReleaseB has no FD!
		
		//\\--add component to a variant
		Component componentD = new Component("compC");
		variantA.addComponent(componentD);
		
		//getParents and Roots
		//Release releaseGet = (Release) fd2.getParent();
		//Productline plGet = (Productline) fd2.getRoot ();
		
	
	
		//------------------------------
		//serialize the whole product-line (all included)
 
		//.productmetainfo , ...

		ModelStorage.storeModel(pl);
		
		//pl.serializeProductline("/tmp", 0);


 		//serialize the products
		//productA.serializeProduct("/tmp");
		//productB.serializeProduct("/tmp");
		
		//serialize the coreAssets
		//coreAssetA.serializeComponent("/tmp");
		//coreAssetB.serializeComponent("/tmp");
		/**/

		
		//do all
		//
		//pl.serializeProductline(true);
		//pl.serialize("/tmp", true);

		
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

//		System.out.println ("-----------NOW DESERIALIZING---------");
//		Productline p2 = new Productline("windows");
//		p2.deserialize(path);
//		
//		//--one product test
//		Product me = p2.getProduct("me");
//		assertTrue (me.getName().equals("me"));
//		//TODO
//		//warum funktionier das nicht:
//		//assertTrue (me.getName() == "me");
//
//		
//		//SpecificComponent compC;
//		//compC = (SpecificComponent)me.getComponent("compC", compC);
//		
//		//--one component test
//		Component compA = p2.getComponent ("coreAssetA");
//		
//		//product = manager.getProduct("windows");
//		//assertTrue (product.getName() == "windows");
//		//...
//		//productline = manager.getProductLine("office");
//		//assertTrue (productline.getName() == "office");
	

	}
}

