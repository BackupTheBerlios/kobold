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
 * $Id: Product.java,v 1.5 2004/05/15 14:45:08 garbeam Exp $
 *
 */

package kobold.common.data;

import org.dom4j.Element;

/**
 * @author garbeam
 */
public class Product {

	private String productLineName;
	private String productName;

	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Product (String productName, String productLineName) {
		this.productLineName = productLineName;
		this.productName = productName;
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Product (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public void serialize(Element root) {
		Element product = root.addElement("product").addText(this.productName);
		root.addElement("depends").addText(this.productLineName);
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	private void deserialize(Element element) {
		Element product = element.element("product");
		this.productName = element.getText();
		this.productLineName = element.elementText("depends");
	}

	/**
	 * @return name of this product.
	 */
	public String getName() {
		return productName;
	}

	/**
	 * @return name of the dependent productline.
	 */
	public String getDependsName() {
		return productLineName;
	}

}
