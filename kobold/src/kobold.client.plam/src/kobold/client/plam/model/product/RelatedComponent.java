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
 * $Id: RelatedComponent.java,v 1.8 2004/08/06 10:50:58 vanto Exp $
 *
 */
package kobold.client.plam.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.productline.Variant;

import org.dom4j.Element;

/**
 * Represents a related component. Related components are
 * components of products which are related to a product
 * lines component.
 */

public class RelatedComponent extends ProductComponent 
                              implements IGXLExport {

	private Variant  relatedVariant;
	private Release plCompRelease;
	
	/**
	 * Basic constructor.
	 * @param variant the related variant.
	 * @param plCompRelease the release of the corresponding component in the productline.
	 **/
	public RelatedComponent (Variant variant,
							 Release plCompRelease) {
		super(variant.getParent().getName());
		relatedVariant = variant;
		this.plCompRelease = plCompRelease;
	}
	
	
	
	/**
	 * Serializes the component.
	 * @see kobold.client.plam.model.AbstractAsset#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = super.serialize();

		element.addElement("ref").addAttribute("ref-id", plCompRelease.getId());
		
		return element;
	}

	/**
	 * Deserializes this component.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		String variantId = element.element("ref").attributeValue("ref-id");
	    this.relatedVariant = (Variant) this.getRoot().getProductline().getAsset(variantId);
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.RELATED_COMPONENT;
	}

	/**
	 * DOM constructor.
	 * @param productName
	 */
	protected RelatedComponent (Element element) {
		super();
		deserialize(element);
	}
	
	/**
	 * @see kobold.common.model.AbstractAsset#getGXLAttributes()
	 */
	public Map getGXLAttributes() {
		return relatedVariant.getGXLAttributes();
	}

	/**
	 * @see kobold.common.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
	    List children = new ArrayList();
	    children.addAll(getProductComponents());
		return children;
	}

	/**
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return relatedVariant.getGXLType();
	}


}
