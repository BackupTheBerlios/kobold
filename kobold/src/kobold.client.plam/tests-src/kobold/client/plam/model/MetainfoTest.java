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
 * $Id: MetainfoTest.java,v 1.4 2004/08/05 19:13:15 neco Exp $
 *
 */
package kobold.client.plam.model;
import java.sql.Date;

import junit.framework.TestCase;
import kobold.client.plam.MetaInformation;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductRelease;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.common.io.RepositoryDescriptor;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;
import org.eclipse.core.runtime.Path;



/**
 * @author garbeam
 *
 * TestCase for ProductLine.
 */
public class MetainfoTest extends TestCase {

	/**
	 * Constructor for ProductLineTest.
	 * @param arg0
	 */
	public MetainfoTest(String arg0) {
		super(arg0);
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());

	}
	
	public void testMetaInfo() {
		
	
		Productline pl = new Productline();
		pl.setName("PLtest23");
		pl.setDescription("jfdhvjhjjhf fdjhohfv jhlohgjgjh," +
				"gfufuzzhzhhhhhhhhhhhhhhhhhh" + 
				"jfdhvjhjjhf fdjhohfv jhlohgjgjh," +
				"gfufuzzhzhhhhhhhhhhhhhhhhhh");
	

		/////--------Product----------------
		//add a product
		Product productA = new Product ();
		productA.setName("me");
		
		RepositoryDescriptor descriptor = new RepositoryDescriptor ();
		productA.setRepositoryDescriptor(descriptor);
		
		productA.addProductRelease(new ProductRelease(new Date(10,10,04)));
		
		Product productB = new Product ();
		productB.setName("xp");
		
		
		pl.addProduct(productA);
		pl.addProduct(productB);

		//--add component (specific)
		SpecificComponent componentC = new SpecificComponent ("compC");
		productA.addComponent(componentC);
		
		//RelatedComponent relComp = new RelatedComponent();
		//relComp.setName("related_A");
		//productB.addComponent(relComp);
		
		//--add Release
		Release releaseA = new Release ();
		releaseA.setName("versA");
		
		Release releaseB = new Release ();
		releaseB.setName("versB");

		componentC.addRelease(releaseA);
		componentC.addRelease(releaseB);


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
		
	
		MetaInformation metaInformation = new MetaInformation(new Path("Metainfo.pdf"));
		metaInformation.getMetaData(pl);

	}
}

