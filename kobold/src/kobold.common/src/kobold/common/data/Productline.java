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
 * $Id: Productline.java,v 1.23 2004/09/23 13:43:17 vanto Exp $
 *
 */
package kobold.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;


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
    
    // change this to false to test server side id mapping
    private static final boolean stickToNameMapping = true;
	
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
	 * Adds a new product to this productline. Please note that each registered
     * product needs to have its own (unique) name. Adding of a prodct with a
     * name that has already been registered will be refused. 
     * 
	 * @param product the product to add.
     * @return true, if the passed productline could be added successfully, 
     *         false otherwise
	 */
	public boolean addProduct(Product product) {
        // will be removed when change to id mapping can be made risklessly
        if (stickToNameMapping) {
            return addProductNameMapping(product);
        }
        
        // 1.) refuse adding if product's name is already registered
        if (getProductByName(product.getName()) != null) {
            return false;
        }
        
        // 2.) set parent and add 
        product.setParent(this);
		products.put(product.getId(), product);
		return true;
	}
	
    // will be removed when change to id mapping can be made risklessly
    private boolean addProductNameMapping(Product product) {
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
	 * Gets a product by its id.
     *  
	 * @param productId the product's id.
     * @return the product with the specified id or null if no product with
     *         that id exists
	 */
	public Product getProduct(String productId) {
	    return (Product)products.get(productId);
	}
	
    /**
     * Gets a product by its name
     * 
     * @param productName name of the product to get
     * @return Product with the specified name or null if no product with that
     *         name exists
     */
    public Product getProductByName(String productName) {
        for(Iterator it = products.values().iterator(); it.hasNext();) {
            Product p = (Product) it.next();
            if (p.getName().equals(productName)) {
                return p;
            }
        }
        
        return null; //no registered product with that name
    }
    
	/**
	 * Gets a coreasset by its id.
	 * @param coreAssetId the coreasset's id.
     * @return the coreasset with the specified id or null if no coreasset with
     *         that id exists
	 */
	public Component getCoreAsset(String coreAssetId) {
	    return (Component)products.get(coreAssetId);
	}
    
    /**
     * Gets a coreasset by its name.
     *  
     * @param coreAssetName name of the coreasset to get.
     * @return the coreasset with the specified name or null if no coreasset 
     *         with that name exists
     */
    public Component getCoreAssetByName(String coreAssetName) {
        for(Iterator it = coreassets.values().iterator(); it.hasNext();) {
            Component c = (Component) it.next();
            if (c.getName().equals(coreAssetName)) {
                return c;
            }
        }
        
        return null; //no registered core asset with that name
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
	 * Removes a product by its id.
	 * @param productId the product's id.
     * @return the removed Product if a product with the passed id existed, null 
     *         otherwise
	 */
	public Product removeProduct(String productId) {
		Product ret = (Product) products.remove(productId);
        
        if (ret != null){
        	ret.setParent(null);
        }
        
        return ret;
	}
    
    /**
     * Removes a product by its name.
     * 
     * @param productName name of the product to remove
     * @return the removed Product object, or null if no product with the 
     *         specified name exists
     */
    public Product removeProductByName(String productName) {
        String id = getProductIdByName(productName);
        return id == null ? null : removeProduct(id);
    }
    
    /**
     * @param productName name of the product whose id should be returned
     * @return id of the product with the specified name or null if no such
     *         product exists
     */
    public String getProductIdByName(String productName) {
        Product p = getProductByName(productName);
        return p == null ? null : p.getId();
    }

	/**
	 * Adds new core asset to this productline. Please note that each registered
     * coreasset needs to have its own (unique) name. Adding of a ca with a
     * name that has already been registered will be refused. 
     * 
	 * @param coreasset the coreasset to add.
     * @return true if the passed coreasset could be registered successfully,
     *         false otherwise
	 */
	public boolean addCoreAsset(Component coreasset) {
        // will be removed when change to id mapping can be made risklessly
        if (stickToNameMapping) {
            return addCoreAssetNameMapping(coreasset);
        }
        
        // 1.) refuse adding if coreasset's name is already registered
        if (getCoreAssetByName(coreasset.getName()) != null) {
            return false;
        }
        
        // 2.) set parent and add
        coreasset.setParent(this);
        coreassets.put(coreasset.getId(), coreasset);
        return true;
	}
    
    // will be removed when change to id mapping can be made risklessly
    private boolean addCoreAssetNameMapping(Component coreasset) {
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
	 * Removes a coreasset by its id.
	 * @param coreasset the coreassset to remove.
     * @return the removed coreasset if a core asset with the specified id 
     *         existed, null otherwise 
	 */
	public Component removeCoreAsset(String coreAssetId) {
		Component ret = (Component) coreassets.remove(coreAssetId);
        
        if (ret != null){
        	ret.setParent(null);
        }
        
        return ret;
	}
    
    /**
     * Removes a core asset by its name.
     * 
     * @param coreAssetName name of the core asset to remove
     * @return the removed core asset or null if no core asset with the 
     *         specified name exists
     */
    public Component removeCoreAssetByName(String coreAssetName) {
        String id = getCoreAssetIdByName(coreAssetName);
        return id == null ? null : removeCoreAsset(id);
    }

    /**
     * @param coreAssetName name of the core asset whose id should be returned
     * @return id of the core asset with the specified name or null if no such
     *         product exists
     */
    public String getCoreAssetIdByName(String coreAssetName) {
        Component c = getCoreAssetByName(coreAssetName);
        return c == null ? null : c.getId();
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
        // will be removed when change to id mapping can be made risklessly
        if (stickToNameMapping) {
            deserializeNameMapping(element);
            return;
        }

		super.deserialize(element);
	    Element coreassetElements = element.element("coreassets");
		for (Iterator iterator = coreassetElements.elementIterator(Asset.COMPONENT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			addCoreAsset(new Component(this, elem));
		}
		
		Element productElements = element.element("products");
		for (Iterator iterator = productElements.elementIterator(Asset.PRODUCT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			addProduct(new Product(this, elem));
		}
	}
    
    // will be removed when change to id mapping can be made risklessly
    private void deserializeNameMapping(Element element) {
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