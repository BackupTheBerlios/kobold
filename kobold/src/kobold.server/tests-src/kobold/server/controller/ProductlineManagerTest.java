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
 * $Id: ProductlineManagerTest.java,v 1.6 2004/09/23 13:43:14 vanto Exp $
 *
 */

package kobold.server.controller;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.io.RepositoryDescriptor;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

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

	    Document document = DocumentHelper.createDocument();
	    Element elem = productline.serialize();
	    document.setRootElement(elem);	
	    OutputFormat outformat = OutputFormat.createPrettyPrint();
	    try {
            XMLWriter writer = new XMLWriter(System.out, outformat);
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
		manager.addProductline(productline);

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
		
		pl = null;
		names = manager.getProductlineNames();
	    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
	        String id = (String) iterator.next();
	        String name = (String)iterator.next();
	        if ("kobold2".equals(name)) {
	            pl = manager.getProductline(id);
	            break;
	        }
	    }
	    
        assertTrue(pl != null);
	    
	    document = DocumentHelper.createDocument();
	    elem = pl.serialize();
	    document.setRootElement(elem);
	      
	    outformat = OutputFormat.createPrettyPrint();
	    try {
            XMLWriter writer = new XMLWriter(System.out, outformat);
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	    Product hallo = pl.getProduct("hallo");
	    assertTrue(hallo != null);
	    System.out.println("product: " + hallo.getName());
	    List maintainer = hallo.getMaintainers();
	}
}
