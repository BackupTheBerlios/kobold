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
 * $Id: ProductComponent.java,v 1.1 2004/06/25 12:58:28 rendgeor Exp $
 *
 */
package kobold.common.model.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import kobold.common.model.AbstractAsset;

/**
 * @author rendgeor
 */
public abstract class ProductComponent extends AbstractAsset {

	private List prodComps = new ArrayList();
	
	public ProductComponent() {
		super("unnamed");
	}

	public ProductComponent(String name) {
		super(name);
	}
	
	public void addProductComponent(ProductComponent comp)
	{
		prodComps.add(comp);
		comp.setParent(this);
	}
	
	public void removeProductComponent(ProductComponent comp)
	{
		prodComps.remove(comp);
	}
	
	public List getProductComponents()
	{
		return Collections.unmodifiableList(prodComps);
	}
	
	
	/**
	 * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.elementIterator("product-components");
		while (it.hasNext()) {
			Element el = (Element)it.next();
			addProductComponent(createProductComponent(el));
		}
	}
	
	/**
	 * @see kobold.common.data.ISerializable#serialize()
	 */
	public Element serialize() {
		Element element = super.serialize();
		Element childrenEl = element.addElement("product-components");
		
		Iterator it = prodComps.iterator();
		while (it.hasNext()) {
			ProductComponent comp = (ProductComponent) it.next();
			childrenEl.add(comp.serialize());
		}
		
		return element;
	}
	
	public static ProductComponent createProductComponent(Element element)
	{
		if (element.getName().equals(AbstractAsset.RELATED_COMPONENT)) {
			return new RelatedComponent(element);
		} else if (element.getName().equals(AbstractAsset.SPECIFIC_COMPONENT)) {
			return new SpecificComponent(element);
		}
		throw new IllegalArgumentException("Unkown product component type");
	}
}
