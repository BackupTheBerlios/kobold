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
 * $Id: ProductManagerTest.java,v 1.1 2004/05/12 18:07:20 rendgeor Exp $
 *
 */

import junit.framework.TestCase;

import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.server.controller.ProductManager;




/**
 * @author garbeam
 *
 * TestCase for UserManager.
 */
public class ProductManagerTest extends TestCase {

	/**
	 * Constructor for UserManagerTest.
	 * @param arg0
	 */
	public ProductManagerTest(String arg0) {
		
		super(arg0);
	}
	
	public void testSerialize() {
		
		Product product = new Product();
		product.setProductName("windoof");
		
		Productline productline = new Productline();
		productline.setProductLineName("office");
		
		ProductManager manager = ProductManager.getInstance();
		
		manager.addProduct(product);
		manager.addProductLine(productline);
		manager.serialize("test.xml");
		
		manager.removeProduct(product);
		manager.removeProductLine(productline);
		manager.deserialize("test.xml");
		
		product = manager.getProduct("windoof");
		assertTrue (product.getProductName() == "windoof");
		//...
		productline = manager.getProductLine("office");
		assertTrue (productline.getProductLineName() == "office");
		
	}
}
