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
 * $Id: Component.java,v 1.1 2004/07/05 15:59:32 garbeam Exp $
 *
 */

package kobold.common.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;

/**
* Represents a server-side component. If the parent is a productline this class
* represents a server-side coreasset. Used for client-server interchange. 
 */
public class Component extends Asset {

	private List components = new ArrayList();
	
	/**
	 * Basic constructor.
	 * @param parent the parent asset, e.g. a product or productline.
	 * @param name the name of this component.
	 * @param repositoryDescriptor the repository descriptor of this
	 * 		  component.
	 */
	public Component(Asset parent, String name, RepositoryDescriptor repositoryDescriptor) {
		super(parent, Asset.COMPONENT, name, repositoryDescriptor);
	}
	
	/**
	 * DOM constructor.
	 * @param parent the parent of this component.
	 * @param element the DOM element representing this component.
	 */
	public Component (Asset parent, Element element) {
		super (parent, element);
		deserialize(element);
	}
	
	/**
	 * Adds new component to this product.
	 * @param component the component.
	 */
	public void addComponent(Component component) {
		components.add(component);
	}

	/**
	 * Removes an existing component from this product.
	 * @param component the component.
	 */
	public void removeComponent(Component component) {
		components.remove(component);
	}
	
	/**
	 * Serializes this component.
	 */
	public Element serialize() {
		Element element = super.serialize();

		Element compElements = element.addElement("components");
		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			Component component = (Component) iterator.next();
			compElements.add(component.serialize());
		}
		
		return element;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element compElements = element.element("components");
		
		for (Iterator iterator = compElements.elementIterator(Asset.COMPONENT);
			 iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			components.add(new Component(this, elem));
		}
	}
}
