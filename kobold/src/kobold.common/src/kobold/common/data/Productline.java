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
 * $Id: Productline.java,v 1.12 2004/06/24 00:38:52 garbeam Exp $
 *
 */
package kobold.common.data;

import kobold.common.io.RepositoryDescriptor;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author garbeam
 */
public class Productline implements IAsset {

	private String name;
	private RepositoryDescriptor repositoryDescriptor;
	
	/**
	 */
	public Productline(String name,
					   RepositoryDescriptor repDescr)
	{
		this.name = name;
		this.repositoryDescriptor = repDescr;
	}

	/**
	 */
	public Productline(Element element) {
		deserialize(element);
	}

	/**
	 * Serializes the productline.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = DocumentHelper.createElement("productline");
		element.addAttribute("name", name);
		element.add(repositoryDescriptor.serialize());
		
		return element;
	}

	/**
	 * @param productName
	 */
	public void deserialize(Element element) {
		this.name = element.attributeValue("name");
		this.repositoryDescriptor =
			new RepositoryDescriptor(element.element("repository-descriptor"));
	}

	/**
	 * @see kobold.common.data.IAsset#getType()
	 */
	public String getType() {
		return IAsset.PRODUCT_LINE;
	}

    /**
     * @see kobold.common.data.IAsset#getName()
     */
    public String getName()
    {
        return name;
    }

}