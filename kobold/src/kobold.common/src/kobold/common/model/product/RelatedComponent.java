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
 * $Id: RelatedComponent.java,v 1.6 2004/06/25 12:58:28 rendgeor Exp $
 *
 */

package kobold.common.model.product;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;

import kobold.common.model.AbstractAsset;
import kobold.common.model.Release;
import kobold.common.model.productline.Component;

/**
 * Represents a related component. Related components are
 * components of products which are related to a product
 * lines component.
 */
public class RelatedComponent extends ProductComponent {

	private Component relatedComponent;
	private Release plCompRelease;
	private Release pCompRelease;
	
	/**
	 * Basic constructor.
	 * @param component the related component.
	 * @param plCompRelease the release of the corresponding component in the productline.
	 * @param pCompRelease the product-instance of the productline-component release. 
	 */
	public RelatedComponent (Component component,
							 Release plCompRelease,
							 Release pCompRelease) {
		super(component.getName());
		relatedComponent = component;
		this.plCompRelease = plCompRelease;
		this.pCompRelease = pCompRelease;
	}
	
	/**
	 * Serializes the component.
	 * @see kobold.common.model.AbstractAsset#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = super.serialize();

		element.addElement("ref").addAttribute("ref-id", plCompRelease.getId());
		element.addElement("local").add(pCompRelease.serialize());
		
		return element;
	}

	/**
	 * Deserializes this component.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		// FIXME: deserialize ref! via instance pool
		pCompRelease = new Release(element.element("local").element(AbstractAsset.RELEASE));
	}

	/**
	 * @see kobold.common.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.RELATED_COMPONENT;
	}

	/**
	 * DOM constructor.
	 * @param productName
	 */
	public RelatedComponent (Element element) {
		super();
		deserialize(element);
	}
	
	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLAttributes()
	 */
	public Map getGXLAttributes() {
		return relatedComponent.getGXLAttributes();
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
		return relatedComponent.getGXLChildren();
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return relatedComponent.getGXLType();
	}

	
}
