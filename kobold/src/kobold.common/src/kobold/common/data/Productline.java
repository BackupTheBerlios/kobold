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
 * $Id: Productline.java,v 1.21 2004/08/26 10:57:26 neccaino Exp $
 *
 */
package kobold.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import kobold.common.io.RepositoryDescriptor;


/**
 * This class represents a server side productline. Productlines consist of
 * several products, they can be assigned Users (Productline engineers) who
 * have the right to modify it and they encapsulate the necessary information 
 * about the repository where the actual products are stored.
 * 
 * They are created (and removed) by the corresponding administration methods 
 * and stored directly on the Kobold server. 
 * 
 * @see kobold.server.controller.SecureKoboldWebServer
 * @see kobold.common.data.Product
 * @see kobold.common.io.RepositoryDescriptor 
 */
public class Productline extends Asset {

	private Map coreassets = new HashMap();
	private Map products = new HashMap();
	
	/**
	 * Base constructor for productlines.
	 * @param name the name of this productline.
	 * @param resource the file or directory name.
	 * @param repositoryDescriptor containing the necessary information about
	 *        this productlines repository
	 */
	public Productline(String name, String resource, RepositoryDescriptor repositoryDescriptor) {
		super(null, Asset.PRODUCT_LINE, name, resource, repositoryDescriptor);
	}

	/**
	 * DOM constructor for server-side productlines.
	 * @param element the DOM element representing this productline.
	 */
	public Productline(Element element) {
		super(null);
		deserialize(element);
	}

	/**
     * TODO: change to id mapping
	 * Adds a new product to this productline. Please note that each registered
     * product needs to have its own (unique) name. Adding of a prodct with a
     * name that has already been registered will be refused. 
     * 
	 * @param product the product to add.
     * @return true, if the passed productline could be added successfully, 
     *         false otherwise
	 */
	public boolean addProduct(Product product) {
		Object o = products.put(product.getName(), product);
		
		if (o != null){
            // a product with the same name as the one to add already exists
            // => undo the change and signal error
            products.put(product.getName(), o);
            return false;
		}
        else{
        	product.setParent(this);
            return true;
        }
	}
	
	/**
     * TODO: change to id mapping
	 * Gets a product by its name. 
	 * @param productName the product name.
     * @return the product with the specified name or null if no product with
     *         that name exists
	 */
	public Product getProduct(String productName) {
	    return (Product)products.get(productName);
	}
	
	/**
     * TODO: change to id mapping
	 * Gets a coreasset by its name.
	 * @param coreAssetName the coreasset.
     * @return the coreasset with the specified name or null if no coreasset with
     *         that name exists
	 */
	public Component getCoreAsset(String coreAssetName) {
	    return (Component)products.get(coreAssetName);
	}
    
    /**
     * Gets a coreasset by its name.
     * 
     * NOTE: not yet implemented
     *  
     * @param coreAssetName name of the coreasset to get.
     * @return the coreasset with the specified name or null if no coreasset 
     *         with that name exists
     */
    public Component getCoreAssetByName(String coreAssetName) {
        //TODO: implement
        return null;
    }
	
	/**
     * Gets all products.
     * @return List containing every registered product
	 */
	public List getProducts() {
	    return new ArrayList(products.values());
	}
	
	/**
	 * Gets all coreassets.
     * @return List containing every registered core asset
	 */
	public List getCoreAssets() {
	    return new ArrayList(coreassets.values());
	}
	
	/**
     * TODO: change to id mapping
	 * Removes the passed product from this productline if it is registered.
	 * @param product the product to remove.
     * @return the removed Product if it was part of this productline or null 
     *         if not
	 */
	public Product removeProduct(Product product) {
		Product ret = (Product) products.remove(product.getName());
        
        if (ret != null){
        	ret.setParent(null);
        }
        
        return ret;
	}
    
    /**
     * Removes a product by its name.
     * 
     * NOTE: not yet implemented
     * 
     * @param productName name of the product to remove
     * @return the removed Product object, or null if no product with the 
     *         specified name exists
     */
    public Product removeProductByName(String productName) {
        return null; //TODO: implement
    }

	/**
     * TODO: change to id mapping
	 * Adds new core asset to this productline. Please note that each registered
     * coreasset needs to have its own (unique) name. Adding of a ca with a
     * name that has already been registered will be refused. 
     * 
	 * @param coreasset the coreasset to add.
     * @return true if the passed coreasset could be registered successfully,
     *         false otherwise
	 */
	public boolean addCoreAsset(Component coreasset) {
        Object o = coreassets.put(coreasset.getName(), coreasset);
        
        if (o != null){
            coreassets.put(coreasset.getName(), o);
            return false;
        }
        else{
        	coreasset.setParent(this);
            return true;
        }
	}
	
	/**
     * TODO: change to id mapping
	 * Removes a coreasset from this productline, if it is registered.
	 * @param coreasset the coreassset to remove.
     * @return the removed coreasset if it existed as part of this productline 
     *         or null if not
	 */
	public Component removeCoreAsset(Component coreasset) {
		Component ret = (Component) coreassets.remove(coreasset.getName());
        
        if (ret != null){
        	coreasset.setParent(null);
        }
        
        return ret;
	}
    
    /**
     * Removes a core asset by its name.
     * 
     * NOTE: not yet implemented
     * 
     * @param coreAssetName name of the core asset to remove
     * @return the removed core asset or null if no core asset with the 
     *         specified name exists
     */
    public Component removeCoreAssetByName(String coreAssetName) {
        return null; //TODO: implement
    }

	/**
	 * Serializes this productline.
	 */
	public Element serialize() {
		Element element = super.serialize();

		Element coreassetElements = element.addElement("coreassets");
		for (Iterator iterator = coreassets.values().iterator(); iterator.hasNext(); ) {
			Component component = (Component) iterator.next();
			coreassetElements.add(component.serialize());
		}
		
		Element productElements = element.addElement("products");
		for (Iterator iterator = products.values().iterator(); iterator.hasNext(); ) {
			Product product = (Product) iterator.next();
			productElements.add(product.serialize());
		}

		return element;
	}

	/**
	 * Deserializes this productline. It's asserted that super deserialization
	 * is already finished.
	 * @param element the DOM element representing this productline.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
	    Element coreassetElements = element.element("coreassets");
		for (Iterator iterator = coreassetElements.elementIterator(Asset.COMPONENT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			Component component = new Component(this, elem);
			coreassets.put(component.getName(), component);
		}
		
		Element productElements = element.element("products");
		for (Iterator iterator = productElements.elementIterator(Asset.PRODUCT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			Product product = new Product(this, elem);
			products.put(product.getName(), product);
		}
	}
}