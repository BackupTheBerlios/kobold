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
 * $Id: ProductLineTest.java,v 1.7 2004/06/17 13:30:31 rendgeor Exp $
 *
 */
package kobold.common.data.plam;
import junit.framework.TestCase;
import kobold.common.exceptions.BogusProductlineException;



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
		Product productA = new Product ("me");
		productA.setRepositoryPath("cvs.berlios.de");
		Product productB = new Product ("xp");
		productA.setRepositoryPath("svn.berlios.de");
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--add component
		ComponentSpecific componentC = new ComponentSpecific ("compC");
		productA.addComponent(componentC);
		
		//--add version
		Version versionA = new Version ("versA");
		Version versionB = new Version ("versB");

		componentC.addVersion(versionA);
		componentC.addVersion(versionB);

		//--add FD
		FileDescriptor fd1 = new FileDescriptor ("fd1");
		fd1.setPath("/tmp/");
		versionA.addFileDescriptor(fd1);
		
		//--add a second FD
		FileDescriptor fd1_1 = new FileDescriptor ("fd1_1");
		FileDescriptor fd1_2 = new FileDescriptor ("fd1_2");
		fd1_2.setLastAuthor("garbeam");
		fd1_2.setLastChangeDate("11.12.2323");
		fd1_2.setVersion("1.2.3");
		fd1_2.setPath("hallo.dat");
		
		fd1.addFileDescriptor(fd1_1);
		fd1.addFileDescriptor(fd1_2);
		
		

		//versionB has no FD!
		
		/////--------CoreAsset--------------------------------------------------
		//add a component
		CoreAsset coreAssetA = new CoreAsset ("coreAssetA");
		coreAssetA.setRepositoryPath("cvs.berlios.de");
		CoreAsset coreAssetB = new CoreAsset ("coreAssetB");
		coreAssetB.setRepositoryPath("svn.berlios.de");
		
		pl.addCoreAsset(coreAssetA);
		pl.addCoreAsset(coreAssetB);

		//--add component
		ComponentRelated componentA = new ComponentRelated ("compA");
		ComponentRelated componentB = new ComponentRelated ("compB");

		coreAssetA.addComponent(componentA);
		coreAssetA.addComponent(componentB);
		
		//--add variant--\\
		Variant variantA = new Variant ("varA");
		Variant variantB = new Variant ("varB");
		
		componentA.addVariant(variantA);
		componentA.addVariant(variantB);

		//--add version		
		Version versionC = new Version ("versC");
		Version versionD = new Version ("versD");

		variantA.addVersion(versionC);
		variantA.addVersion(versionD);

		//--add FD
		FileDescriptor fd2 = new FileDescriptor ("fd2");
		
		versionC.addFileDescriptor(fd2);

		//versionB has no FD!
		
		//\\--add component to a variant
		ComponentRelated componentD = new ComponentRelated ("compC");
		variantA.addComponent(componentD);
		
		//getParents and Roots
		Version versionGet = (Version) fd2.getParent();
		try
		{
		Productline plGet = (Productline) fd2.getRoot ();
		}
		catch (BogusProductlineException e)
		{
		System.out.print("ERROR_WHILE_GET_ROOT");	
		}
		
		//------------------------------
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
