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
 * $Id: Variant.java,v 1.4 2004/06/16 16:59:17 rendgeor Exp $
 *
 */

package kobold.common.data.plam;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author garbeam
 */
public class Variant extends AbstractAsset {

	private HashMap components;
	private HashMap versions;
	
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Variant (String productName) {
		super(productName);
		
		components = new HashMap ();
		versions  = new HashMap ();
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Variant (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element product = DocumentHelper.createElement("product");
		product.addText(getName());
		return product;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element product = element.element("product");
		setName (element.getText());
		//this.productLineName = element.elementText("productline");
	}

	/**
	 * @return name of the dependent productline.

	public String getDependsName() {
		return productLineName;
	}
	 */


	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return AbstractAsset.VARIANT;
	}

	/**
	 * Adds a new component.
	 *
	 * @param component contains the new component
	 */
	public void addComponent(ComponentRelated component) {
		components.put(component.getName(), component);
	}

	
	/**
	 * Adds a new version.
	 *
	 * @param version contains the new version
	 */
	public void addVariant(Version version) {
		versions.put(version.getName(), version);
	}

    
}
