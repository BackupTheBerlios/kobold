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
 * $Id: ProductlineManager.java,v 1.13 2004/08/06 06:02:32 neccaino Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import kobold.common.data.Component;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.User;
import kobold.common.io.RepositoryDescriptor;

/**
 * This singelton class stores all the productlines for a Kobold server. New
 * productlines can be added (as Productline-object) and removed. Note that
 * every productline needs a unique name. Adding of new productlines will be
 * refused if their name has already been registered with another productline.
 * This class is serializable.
 * 
 * @see kobold.common.data.Productline    
 */
public class ProductlineManager {

	private HashMap productlines;
	private String productStore = "products.xml";
	
	static private ProductlineManager instance;
	 
	static public ProductlineManager getInstance() {
		 if (instance == null ) {
		 	 instance = new ProductlineManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
     * TODO: delete call of dummy prods before delivery
	 */
	private ProductlineManager() {
		productlines = new HashMap();
		this.productStore = 
			System.getProperty("kobold.server.storePath") + 
			System.getProperty("kobold.server.productStore");
		deserialize();
		// DEBUG
		dummyProds();
	}
	
	/**
	 * Adds a productline. Note that this will be refused if there has already 
     * been added a productline with the same name as the one to add.
	 *
	 * @param productline the productline to add
     * @return true, if the new productline has been added successfully, false,
     *         if the productline could not be added (e.g. because there already
     *         existes a productline with the same name as the new one).
	 */
	public boolean addProductline(Productline productLine) {
        Object o = productlines.put(productLine.getId(), productLine);
        
        if (o != null){
            // Obviously a productline with the name specified in 'productLine
            // already existed, so undo the change and signal error
            productlines.put(productLine.getId(), o);
            serialize();
            return true;
        }
        else{
            return false;
        }
	}

	/**
	 * Gets a productline by its name.
	 *
	 * @param id the id of the productLine.
     * @return Productline with the passed name, null if it doesn't exist
	 */
	public Productline getProductline(String id) {
		return (Productline) productlines.get(id);
	}

	/**
	 * Removes the specified productline.
	 * 
	 * @param id the id of the productline.
     * @return <code>true</code> if the product existed and has been removed
     *         successfully, <code>false</code> otherwise.
	 */
	public boolean removeProductline(String id) {
		Object obj = productlines.remove(id);
		serialize();
		return obj != null;
	}
    
    /**
     * The following method returns the id of the productline specified by
     * its name.
     * 
     * @param nameOfProductline name of the productline whose id is to be
     *        returned
     * @return String containing the passed productline's id or <code>null</code>
     *         if not productlone with the passed name exists
     */
    public String getProductlineIdByName(String nameOfProductline){
        Vector v = getProductlineNames();
        Iterator it = v.iterator();
        String id = null;
        
        while(it.hasNext()){
            // store id of next pl
            String tmp = (String)it.next();
            
            // compare next pl's name 
            if (((String)it.next()).equals(nameOfProductline)){
                // tmp contains the desired pl's id
                id = tmp;
                break;
            }
        }
        
        return id;
    }

	/**
	 * Serializes all products and productlines to the file specified
     * by productStore.
     * 
     * @see serialize(String)
	 */
	public void serialize() {
		serialize(this.productStore);
	}
		
	/**
	 * Serializes all products and productlines to the file specified
	 * by path.
	 * 
	 * @param path the path to serialize.
	 */
	public void serialize(String path) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold");

		Element productlines = root.addElement("productlines");

		for (Iterator it = this.productlines.values().iterator(); it.hasNext();) {
			Productline productline = (Productline) it.next();
			productlines.add(productline.serialize());
		}

		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.server.controller.ProductAdmin");
			log.error(e);
		}

	}

	/**
	 * Deserializes all products and productlines from
	 * the productStore.
	 */
	public void deserialize() {
		deserialize(productStore);
	}
	
	/**
	 * Deserializes all products and productlines from the specified file.
	 * 
	 * @param path - file where to read from.
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
			
			Element root = document.getRootElement();
			for (Iterator iterator = root.element("productlines").elementIterator("productline");
			     iterator.hasNext(); )
		    {
			    Element element = (Element) iterator.next();
				Productline productline = new Productline(element);
				productlines.put(productline.getId(), productline);
			}
		} catch (DocumentException e) {
			Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			log.error(e);
		}
	}

	/**
	 * Returns a list of two-dimensional objects which contain the productline id
	 * as first component and the productline name as second component.
     * 
     * @return Vector of String objects with all productlines' names
	 */
	public Vector getProductlineNames() {
	    
	    Vector result = new Vector();
	    
	    for (Iterator iterator = productlines.values().iterator(); iterator.hasNext(); )
	    {
	        Productline productline = (Productline) iterator.next();
	        result.add(productline.getId());
	        result.add(productline.getName());
	    }
	    
	    return result;
	}
	
	// DEBUG, TODO: delete before delivery
	public void dummyProds() {
		
	    Productline pl = new Productline("kobold2", "kobold2",
		         			   new RepositoryDescriptor(
				               RepositoryDescriptor.CVS_REPOSITORY, "ssh",
						       "cvs.berlios.de", "/cvsroot/kobold/", "kobold2"));
	    
	    kobold.server.data.User vanto = UserManager.getInstance().getUser("vanto");
		pl.addMaintainer(new User(vanto.getUserName(), vanto.getFullName()));
		
		Product product = new Product(pl, "hallo", "hallo", new RepositoryDescriptor(
				               RepositoryDescriptor.CVS_REPOSITORY, "ssh",
						       "cvs.berlios.de", "/cvsroot/kobold/", "kobold2/hallo"));
		
		kobold.server.data.User garbeam = UserManager.getInstance().getUser("garbeam");
		product.addMaintainer(new User(garbeam.getUserName(), garbeam.getFullName()));
		
		pl.addProduct(product);
		
		addProductline(pl);

		Component comp = new Component(pl, "lala", "lala",  new RepositoryDescriptor(
	               RepositoryDescriptor.CVS_REPOSITORY, "ssh",
			       "cvs.berlios.de", "/cvsroot/kobold/", "kobold2/hallo"));
		
		addProductline(new Productline("kobold3", "kobold3",
				       new RepositoryDescriptor(
		                RepositoryDescriptor.CVS_REPOSITORY,
		 			    "pserver", "cvs.berlios.de",
		 			    "/cvsroot/kobold/", "kobold3")));
		addProductline(new Productline("kobold4", "kobold4",
					   new RepositoryDescriptor(
					   	RepositoryDescriptor.CVS_REPOSITORY,
						"ssh", "cvs.berlios.de",
					   	"/cvsroot/kobold/", "kobold4")));
	}
}