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
 * $Id: Component.java,v 1.2 2004/06/09 11:47:47 rendgeor Exp $
 *
 */

package kobold.common.data.plam;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author garbeam
 */
public class Component implements IAsset {

	private String componentName;

	//the variants and versions
	private HashMap variants;
	private HashMap versions;
	/**
	 * Basic constructor.
	 * @param componentName
	 * @param productLineName
	 */
	public Component (String componentName, boolean relatedType) {
		super();
		this.componentName = componentName;
		
		//related type
		if (relatedType)
		{
			variants = new HashMap ();
		}
		//p-spec type
		else
		{
			versions = new HashMap ();
		}
		
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Component (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element product = DocumentHelper.createElement("product");
		product.addText(this.componentName);
		//product.addElement("productline").addText(this.productLineName);
		return product;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element product = element.element("product");
		this.componentName = element.getText();
		//this.productLineName = element.elementText("productline");
	}


	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return IAsset.COMPONENT;
	}

    /**
     * @see kobold.common.data.IAsset#getName()
     */
    public String getName()
    {
        return componentName;
    }
    

	/**
	 * Adds a new variant.
	 *
	 * @param variant contains the new variant
	 */
	public void addVariant(Variant variant) {
		variants.put(variant.getName(), variant);
	}

	/**
	 * Adds a new version.
	 *
	 * @param version contains the new vversion
	 */
	public void addVersion(Version version) {
		versions.put(version.getName(), version);
	}

	
}
