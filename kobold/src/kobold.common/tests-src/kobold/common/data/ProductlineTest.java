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
 * $Id: ProductlineTest.java,v 1.2 2004/07/07 15:40:32 garbeam Exp $
 *
 */
package kobold.common.data;

import junit.framework.TestCase;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.io.XMLWriter;

/**
 * @author Tammo
 *
 */
public class ProductlineTest extends TestCase {

	/**
	 * Constructor for KoboldMessageTest.
	 */
	public ProductlineTest() 
	{
		super("Kobold Message Test");
	}

	public void testSerialize() 
	{
		Productline productline =
			new Productline("koboldline",
				new RepositoryDescriptor("cvs", "pserver", "cvs.berlios.de",
						                 "/cvsroot/kobold", "kobold"));

		productline.addMaintainer(new User("garbeam", "Anselm"));
		
		Component component = new Component(productline, "common",
				new RepositoryDescriptor("cvs", "pserver", "cvs.berlios.de",
										 "/cvsroot/kobold", "kobold/common"));
		component.addComponent(new Component(productline, "data",
				new RepositoryDescriptor("cvs", "pserver", "cvs.berlios.de",
										 "/cvsroot/kobold", "kobold/common/data")));
		productline.addCoreAsset(component);
		
		Product product = new Product(productline, "wmi",
				new RepositoryDescriptor("cvs", "pserver", "cvs.berlios.de",
						 				 "/cvsroot/wmi", "wmi"));
						 				 
		Component pComp = new Component(product, "tools", 
				new RepositoryDescriptor("cvs", "pserver", "cvs.berlios.de",
										 "/cvsroot/wmi", "wmi/tools"));
		
		product.addComponent(pComp);
		
		try {
			XMLWriter w = new XMLWriter(System.out);
			w.write(productline.serialize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
