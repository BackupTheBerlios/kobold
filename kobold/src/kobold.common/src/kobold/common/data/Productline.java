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
 * $Id: Productline.java,v 1.14 2004/07/14 15:55:46 garbeam Exp $
 *
 */
package kobold.common.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import kobold.common.io.RepositoryDescriptor;


/**
 * Represents a server side productline. Used for client-server interchange.
 */
public class Productline extends Asset {

	private Map coreassets = new HashMap();
	private Map products = new HashMap();
	
	/**
	 * Base constructor for productlines.
	 * @param name the name of this productline.
	 * @param repositoryDescriptor the repository descriptor.
	 */
	public Productline(String name, RepositoryDescriptor repositoryDescriptor) {
		super(null, Asset.PRODUCT_LINE, name, repositoryDescriptor);
	}

	/**
	 * DOM constructor for server-side productlines.
	 * @param element the DOM element representing this productline.
	 */
	public Productline(Element element) {
		super(null, element);
		deserialize(element);
	}

	/**
	 * Adds new product to this productline.
	 * @param product the product to add.
	 */
	public void addProduct(Product product) {
		products.put(product.getName(), product);
		product.setParent(this);
	}
	
	/**
	 * Gets a product by its name.
	 * @param productName the product name.
	 */
	public Product getProduct(String productName) {
	    return (Product)products.get(productName);
	}
	
	/**
	 * Removes existing product from this productline.
	 * @param product the product to remove.
	 */
	public void removeProduct(Product product) {
		products.remove(product.getName());
		product.setParent(null);
	}

	/**
	 * Adds new core asset to this productline.
	 * @param coreasset the coreasset to add.
	 */
	public void addCoreAsset(Component coreasset) {
		coreassets.put(coreasset.getName(), coreasset);
		coreasset.setParent(this);
	}
	
	/**
	 * Removes existing coreasset from this productline.
	 * @param coreasset the coreassset to remove.
	 */
	public void removeCoreAsset(Component coreasset) {
		coreassets.remove(coreasset.getName());
		coreasset.setParent(null);
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
		Element coreassetElements = element.element("coreassets");
		for (Iterator iterator = coreassetElements.elementIterator(Asset.COMPONENT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			Component component = new Component(this, elem);
			coreassets.put(component.getName(), component);
		}
		
		Element productElements = element.element("products");
		for (Iterator iterator = coreassetElements.elementIterator(Asset.PRODUCT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			Product product = new Product(this, elem);
			products.put(product.getName(), product);
		}
	}
}