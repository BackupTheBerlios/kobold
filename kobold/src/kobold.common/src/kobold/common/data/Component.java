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
 * $Id: Component.java,v 1.6 2004/11/05 10:50:56 grosseml Exp $
 *
 */

package kobold.common.data;

import org.apache.log4j.Logger;

import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;

/**
* Represents a server-side component. If the parent is a productline this class
* represents a server-side coreasset. Used for client-server interchange. 
 */
public class Component extends Asset {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Component.class);

	/**
	 * Basic constructor.
	 * @param parent the parent asset, e.g. a product or productline.
	 * @param name the name of this component.
	 * @param resource the resource
	 * @param repositoryDescriptor the repository descriptor of this
	 * 		  component.
	 */
	public Component(Asset parent, String name, String resource,
	                 RepositoryDescriptor repositoryDescriptor)
	{
		super(parent, Asset.COMPONENT, name, resource, repositoryDescriptor);
	}
	
	/**
	 * DOM constructor.
	 * @param parent the parent of this component.
	 * @param element the DOM element representing this component.
	 */
	public Component (Asset parent, Element element) {
		super(parent);
		super.deserialize(element);
	}
	
	/**
	 * Serializes this component.
	 */
	public Element serialize() {
		Element element = super.serialize();
		return element;
	}
}
