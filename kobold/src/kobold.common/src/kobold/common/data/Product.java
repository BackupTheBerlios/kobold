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
 * $Id: Product.java,v 1.14 2004/08/26 10:57:45 neccaino Exp $
 *
 */

package kobold.common.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.common.io.RepositoryDescriptor;
import kobold.common.data.Productline;

import org.dom4j.Element;

/**
 * Represents a server side product. Used for client-server interchange.
 */
public class Product extends Asset {

	private List components = new ArrayList(); //TODO: change to id mapping
	
	/**
	 * Basic constructor.
	 * @param productline the parent productline.
	 * @param name the name of this product.
	 * @param resource the file or directory name
	 * @param repositoryDescriptor the repository descriptor of this product.
	 */
	public Product (Productline productline, String name, String resource,
	               RepositoryDescriptor repositoryDescriptor) {
		super(productline, Asset.PRODUCT, name, resource, repositoryDescriptor);
	}
	
	/**
	 * DOM constructor.
	 * @param productline the parent productline.
	 * @param the DOM element representing this asset.
	 */
	public Product (Productline productline, Element element) {
	    super(productline);
		deserialize(element);
	}
	
	/**
	 * Returns all components of this product.
     * @return List containing all registered components
	 */
	public List getComponents() {
		return components;
	}
	
	/**
     * TODO: change to id mapping
	 * Adds new component to this product.
	 * @param component the component.
	 */
	public void addComponent(Component component) {
		components.add(component);
	}

	/**
     * TODO: change to id mapping
	 * Removes an existing component from this product.
	 * @param component the component.
	 */
	public void removeComponent(Component component) {
		components.remove(component);
	}

	/**
	 * Serializes this product.
	 */
	public Element serialize() {
		Element element = super.serialize();
		
		Element compElements = element.addElement("components");
		for (Iterator iterator = components.iterator(); iterator.hasNext(); ) {
			Component component = (Component) iterator.next();
			compElements.add(component.serialize());
		}
		
		return element;
	}
	
	/**
	 * Deserializes this product. It's asserted that super deserialization
	 * is already finished.
	 * @param element the DOM element representing this product.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
	    Element compElements = element.element("components");
		
		for (Iterator iterator = compElements.elementIterator(Asset.COMPONENT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			components.add(new Component(this, elem));
		}
	}
}
