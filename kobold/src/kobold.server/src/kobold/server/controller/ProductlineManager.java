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
 * $Id: ProductlineManager.java,v 1.6 2004/07/21 17:07:36 garbeam Exp $
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

import kobold.common.data.Productline;
import kobold.common.io.RepositoryDescriptor;

/**
 * This singelton class stores all the productlines for a Kobold server. New
 * productlines can be added (as Productline-object) and removed. Note that
 * every productline needs a unique name. Adding of new productlines will be
 * refused if their name has already been registered with another productline.
 * This class is serializable.
 * 
 * @see Productline    
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
        Object o = productlines.put(productLine.getName(), productLine);
        
        if (o != null){
            // Obviously a productline with the name specified in 'productLine
            // already existed, so undo the change and signal error
            productlines.put(productLine.getName(), o);
            return false;
        }
        else{
            return true;
        }
	}

	/**
	 * Gets a productline by its name.
	 *
	 * @param productlineName the name of the productLine.
     * @return Productline with the passed name, null if it doesn't exist
	 */
	public Productline getProductline(String productlineName) {
		return (Productline) productlines.get(productlineName);
	}

	/**
	 * Removes the specified productline.
	 * 
	 * @param productline String containing the name the productline to remove.
     * @return the productline that has been removed or null if there dosen't
     *         exist a productline with the passed name
	 */
	public Productline removeProductline(String productlineName) {
		return (Productline) productlines.remove(productlineName);
	}

	/**
	 * Serializes all products and productlines to the file specified
     * by productStore.
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

		Element products = root.addElement("productlines");

		for (Iterator it = this.productlines.values().iterator(); it.hasNext();) {
			Productline productLine = (Productline) it.next();
			products.add(productLine.serialize());
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
				productlines.put(productline.getName(), productline);
			}
		} catch (DocumentException e) {
			Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			log.error(e);
		}
	}

	/**
	 * Returns a list of Strings which contain all the productline's names.
     * 
     * @return List of String-Objects with all productline's names
	 */
	public Vector getProductlineNames() {
	    Vector result = new Vector();
	    
	    for (Iterator iterator = productlines.values().iterator(); iterator.hasNext(); )
	    {
	        Productline productline = (Productline) iterator.next();
	        result.add(productline.getName());
	    }
	    
	    return result;
	}
	
	// DEBUG, TODO: delete before delivery
	public void dummyProds() {
		
	    addProductline(new Productline("kobold2",
					   new RepositoryDescriptor(
						RepositoryDescriptor.CVS_REPOSITORY, "ssh",
						"cvs.berlios.de", "/cvsroot/kobold/", "kobold2")));
		addProductline(new Productline("kobold3",
				       new RepositoryDescriptor(
				       		RepositoryDescriptor.CVS_REPOSITORY,
		 			   "pserver", "cvs.berlios.de",
		 			   "/cvsroot/kobold/", "kobold3")));
		addProductline(new Productline("kobold4",
					   new RepositoryDescriptor(
					   		RepositoryDescriptor.CVS_REPOSITORY,
							"ssh", "cvs.berlios.de",
					   		"/cvsroot/kobold/", "kobold4")));
	}
}