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
 * $Id: Component.java,v 1.2 2004/06/22 00:57:41 vanto Exp $
 *
 */

package kobold.common.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.common.model.AbstractAsset;

import org.dom4j.Element;
/**
 * @author garbeam
 */
public class Component extends AbstractAsset {

	//the variants
	private List variants = new ArrayList();
	
	//the repository-path
	String repositoryPath;
	
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Component(String productName) {
		super(productName);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Component(Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the component.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element element = super.serialize();
				
		//now all variants
		Element variantsEl = element.addElement("variants");
		for (Iterator it = variants.iterator(); it.hasNext();)	{
				Variant variant = (Variant)it.next();
				variantsEl.add(variant.serialize ());
		}

		element.addAttribute("repositoryPath", repositoryPath);
		
		return element;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.element("variants").elementIterator("asset");
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addVariant(new Variant(varEl));
		}
		
		repositoryPath = element.attributeValue("repositoryPath");
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
		return AbstractAsset.COMPONENT;
	}


	/**
	 * Adds a new component.
	 *
	 * @param component contains the new component
	 */
	public void addVariant(Variant variant) 
	{
		variants.add(variant);
		variant.setParent(this);
	}
    
	public void removeVariant(Variant variant) 
	{
	    variants.remove(variant);
	    variant.setParent(null);
	}
	
	public List getVariants()
	{
	    return Collections.unmodifiableList(variants);
	}
	
	/**
	 * @return Returns the repositoryPath.
	 */
	public String getRepositoryPath() {
		return repositoryPath;
	}
	
	/**
	 * @param repositoryPath The repositoryPath to set.
	 */
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}
}
