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
 * $Id: ProductRelease.java,v 1.2 2004/06/21 22:35:35 garbeam Exp $
 *
 */

package kobold.common.model.product;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kobold.common.data.ISerializable;
import kobold.common.model.Release;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * This class represents a product release. 
 */
public class ProductRelease implements ISerializable {

	// container
	private List releases;
	private Date creationDate;
	
	/**
	 * Basic constructor.
	 * @param creationDate the origin date when this release
	 * 		  has been created.	
	 */
	public ProductRelease (Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public ProductRelease (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element productRelease = DocumentHelper.createElement("product-release");
		productRelease.addAttribute("created", creationDate.toLocaleString());
		
		Element releasesElement = productRelease.addElement("releases");
		for (Iterator it = releases.iterator(); it.hasNext(); ) {
			Release release = (Release) it.next();
			releasesElement.add(release.serialize());
		}
		
		return productRelease;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		creationDate = new Date(element.attributeValue("created"));
	}

	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return
	 */
	public List getReleases() {
		return releases;
	}

}
