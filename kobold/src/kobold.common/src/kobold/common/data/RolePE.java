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
 * $Id: RolePE.java,v 1.5 2004/05/12 10:32:27 neco Exp $
 *
 */
package kobold.common.data;

import java.util.List;

import org.dom4j.Element;

/**
 * this class stores the data necessary to link a user to a product
 * as product engineer
 * 
 * @author Armin Cont
 */
public class RolePE extends Role {

	private List products;

	/**
	 * Basic constructor.
	 */
	public RolePE() {
		
	}
	
	/**
	 * @param element
	 */
	public RolePE(Element element) {
		deserialize(element);
	}
	
	/**
	 * @param element
	 */
	private void deserialize(Element element) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Serializes this role.
	 * @see kobold.common.data.Role#serialize(org.dom4j.Element)
	 */
	public void serialize(Element roles) {
		Element role = roles.addElement("role").addText("PE");
	}
	
	/**
	 * @return
	 */
	public List getProducts() {
		return products;
	}
	
}
