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
 * $Id: ProductLineTest.java,v 1.5 2004/06/16 16:59:17 rendgeor Exp $
 *
 */
package kobold.common.data.plam;
import junit.framework.TestCase;



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
		
		//------------------------
		//add a product
		Product productA = new Product ("me");
		Product productB = new Product ("xp");
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--
		ComponentRelated componentA = new ComponentRelated ("compA");
		ComponentRelated componentB = new ComponentRelated ("compB");
		
		ComponentSpecific componentC = new ComponentSpecific ("compC");
		
		productA.addComponent(componentC);
		//productA.addComponent(componentB);
		
		//--
		Variant variantA = new Variant ("varA");
		Variant variantB = new Variant ("varB");
		
		componentA.addVariant(variantA);
		componentA.addVariant(variantB);
		
		//--
		Version versionA = new Version ("versA");
		Version versionB = new Version ("versB");


		componentC.addVersion(versionA);
		componentC.addVersion(versionB);

	
		//--
		FileDescriptor fd1 = new FileDescriptor ("fd1");
		versionA.addFileDescriptor(fd1);
		
		//------------------------------
		//add a coreAsset
		CoreAsset coreAssetA = new CoreAsset ("caBla");
		CoreAsset coreAssetB = new CoreAsset ("caBlubber");
		
		pl.addCoreAsset (coreAssetA);
		pl.addCoreAsset (coreAssetB);

		//serialize the whole product-line (all included)
		// .productlinemetainfo 
		pl.serialize("test-productlinemetainfo.xml", 0);
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
