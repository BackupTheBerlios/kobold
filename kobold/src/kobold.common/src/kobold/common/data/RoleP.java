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
 * $Id: RoleP.java,v 1.6 2004/05/17 09:17:11 garbeam Exp $
 *
 */
package kobold.common.data;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * this class stores the data necessary to link a user to a product
 * as a programmer
 * 
 * @author Armin Cont
 */
public class RoleP extends Role {

	private String productName;

	/**
	 * Basic constructor.
	 */
	public RoleP(String productName) {
		this.productName = productName;	
	}

	/**
	 * @param element
	 */
	public RoleP(Element element) {
		deserialize(element);
	}

	/**
	 * @param element
	 */
	public void deserialize(Element element) {
		productName = element.elementText("product");
	}

	/**
	 * Serializes this role.
	 * @see kobold.common.data.Role#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element role = DocumentHelper.createElement("role").addText("P");
		role.addElement("product").addText(productName);
		return role;
	}

	/**
	 * @return product of this role. 
	 */
	public String getProductName() {
		return productName;
	}
	
}