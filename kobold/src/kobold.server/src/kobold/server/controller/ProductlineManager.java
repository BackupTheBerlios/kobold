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
 * $Id: ProductlineManager.java,v 1.24 2004/11/05 10:50:56 grosseml Exp $
 *
 */
package kobold.server.controller;

import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.User;
import kobold.common.io.RepositoryDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ProductlineManager.class);

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
	public boolean addProductline(Productline productline) {
        Productline pl = getProductlineByName(productline.getName());
        
        if (pl != null){
            // Obviously a productline with the name specified in 'productLine
            // already existed, so stop and signal error
            return false;
        }
        else{
            productlines.put(productline.getId(), productline);
            serialize();
            return true;
        }
	}

	/**
	 * Gets a productline by its id.
	 *
	 * @param id the id of the productLine.
     * @return Productline with the passed name, null if it doesn't exist
	 */
	public Productline getProductline(String id) {
		return (Productline) productlines.get(id);
	}

    /**
     * Returns the productline with the passed name.
     * 
     * @param nameOfProductline String containing the name of the productline to get
     * @return the productline with the passed name or null if it doesen't exist
     */
    public Productline getProductlineByName(String nameOfProductline){
    	Iterator it = productlines.values().iterator();
        
        while(it.hasNext()){
        	Productline tmp = (Productline) it.next();
            if (tmp.getName().equals(nameOfProductline)){
                return tmp;
            }
        }
        
        return null;
    }
    
	/**
	 * Removes the specified productline.
	 * 
	 * @param id the id of the productline to remove
     * @return <code>true</code> if the productline existed and could be removed
     *         successfully, <code>false</code> otherwise.
	 */
	public boolean removeProductline(String id) {
		Object obj = productlines.remove(id);
		serialize();
		return obj != null;
	}
    
    /**
     * Removes the productline with the specified name.
     * 
     * @param nameOfProductline String containing the name of the productline to remove
     * @return <code>true</code> if the productline existed and could be removed
     *         successfully, <code>false</code> otherwise.
     */
    public boolean removeProductlineByName(String nameOfProductline) {
        // 1.) stop if the productline doesn't exist
        Productline pl = getProductlineByName(nameOfProductline);
        
        if (pl == null){
            return false;
        }
        
       // 2.) Remove the productline
       productlines.remove(pl.getId());
       serialize();
       return true;
    }

    /**
     * @return Iterator over a Collection containing all registered productline
     *         objects.
     */
    public Iterator getProductlineIterator(){
         return productlines.values().iterator(); 
    }
    
    /**
	 * Serializes all products and productlines to the file specified
     * by productStore.
     * 
     * @see ProductlineManager#serialize(String) serialize(String)
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
     * Returns a vector of String objects containing all the productlines' ids
     * and names. Name and id of one productline are stored in two seperate
     * strings. The String object containing the id of a specific productline 
     * is followed directly by a string object containing that productline's
     * name on the returned vector. The first on the vector contains a 
     * productline's id.
     * 
     * Note that an empty vector will be returned if no productline is 
     * registered on the server. 
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
    
    /**
     * @return Vector containing all assets registered on the server (that is 
     * all productlines, products, core assets and components).
     */
    public Vector getAllRegisteredAssets(){
        Vector ret = new Vector();
        
        // add all productlines
        Iterator productlineIterator = productlines.values().iterator();
        while(productlineIterator.hasNext()){
            Productline pl = (Productline) productlineIterator.next();
            ret.add(pl);
            
            // add all products of pl
            Iterator productIterator = pl.getProducts().iterator();
            while(productIterator.hasNext()){
                Product p = (Product) productIterator.next();
                ret.add(p);
                
                // add all components of p
                Iterator componentIterator = p.getComponents().iterator();
                while(componentIterator.hasNext()){
                    ret.add(componentIterator.next());
                }
            }
            
            // add all coreassets
            Iterator coreassetIterator = pl.getCoreAssets().iterator();
            while(coreassetIterator.hasNext()){
                ret.add(coreassetIterator.next());
            }
        }
        
        return ret;
    }
	
	// DEBUG, TODO: delete before delivery
	public void dummyProds() {
		Productline pl = new Productline("werkbold", "werkbold",
		        new RepositoryDescriptor(RepositoryDescriptor.CVS_REPOSITORY, "pserver",
		                "cvs.berlios.de", "/cvsroot/kobold/", "werkbold"));
		kobold.server.data.User vanto = UserManager.getInstance().getUser("vanto");
		pl.addMaintainer(new User(vanto.getUserName(), vanto.getFullName()));
	    
		addProductline(pl);
	}
}