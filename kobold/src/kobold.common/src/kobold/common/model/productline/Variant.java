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
 * $Id: Variant.java,v 1.2 2004/06/22 00:57:41 vanto Exp $
 *
 */

package kobold.common.model.productline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.common.model.AbstractAsset;
import kobold.common.model.Release;

import org.dom4j.Element;
/**
 * @author garbeam
 */
public class Variant extends AbstractAsset {

	private List components = new ArrayList();
	private List releases = new ArrayList();;
	
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Variant (String productName) {
		super(productName);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Variant(Element element) {
	    deserialize(element);
	}
	

	/**
	 * Serializes the component.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = super.serialize();
				
		//now all components
		Element componentsEl = element.addElement("components");
		for (Iterator it = components.iterator(); it.hasNext();)	{
				Component component = (Component)it.next();
				componentsEl.add(component.serialize());
		}

		//now all releases
		Element releasesEl = element.addElement("releases");
		for (Iterator it = releases.iterator(); it.hasNext();)	{
				Release release = (Release)it.next();
				releasesEl.add(release.serialize());
		}

		return element;
	}
	


	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.element("components").elementIterator("asset");
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addComponent(new Component(varEl));
		}
		
		it = element.element("variants").elementIterator("asset");
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addRelease(new Release(varEl));
		}

		
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
	public void addComponent(Component component) {
		components.add(component);
		component.setParent(this);
	}

	
	/**
	 * Adds a new version.
	 *
	 * @param version contains the new version
	 */
	public void addRelease(Release release) {
		releases.add(release);
		release.setParent(this);
	}

    
}
