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
 * $Id: ProductlineManagerTest.java,v 1.4 2004/08/03 15:07:18 garbeam Exp $
 *
 */

package kobold.server.controller;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;

import junit.framework.TestCase;

import kobold.common.data.Productline;
import kobold.common.io.RepositoryDescriptor;
import kobold.server.controller.ProductlineManager;

/**
 * @author garbeam
 *
 * TestCase for UserManager.
 */
public class ProductlineManagerTest extends TestCase {

	/**
	 * Constructor for UserManagerTest.
	 * @param arg0
	 */
	public ProductlineManagerTest(String arg0) {
		
		super(arg0);
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());
        System.setProperty("kobold.server.storePath", "/tmp/");
        System.setProperty("kobold.server.productStore", "test-product.xml");
	}
	
	public void testSerialize() {
		
		Productline productline = new Productline("zucker", "zucker",
				new RepositoryDescriptor(
						RepositoryDescriptor.CVS_REPOSITORY,
						"pserver", "zucker.org", "/root/zucker", "zucker"));
		
		ProductlineManager manager = ProductlineManager.getInstance();
		
		manager.addProductline(productline);
	
	}
	
	public void testDeserialize() {
	    ProductlineManager manager = ProductlineManager.getInstance();
	    Productline pl = null;
		
	    Vector names = manager.getProductlineNames();
	    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
	        String id = (String) iterator.next();
	        String name = (String)iterator.next();
	        if ("zucker".equals(name)) {
	            pl = manager.getProductline(id);
	            break;
	        }
	    }
	    
	    RepositoryDescriptor repositoryDescriptor = pl.getRepositoryDescriptor();
	    
	    System.out.println("host: " + repositoryDescriptor.getHost());
		assertTrue(repositoryDescriptor.getHost().equals("zucker.org"));
		
		manager.removeProductline("zucker");
	}
}
