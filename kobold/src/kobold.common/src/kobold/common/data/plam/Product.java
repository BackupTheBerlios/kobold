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
 * $Id: Product.java,v 1.7 2004/06/17 13:30:31 rendgeor Exp $
 *
 */

package kobold.common.data.plam;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;

import java.util.Iterator;
/**
 * @author garbeam
 */
public class Product extends AbstractAsset {

	//the components
	private HashMap components;	
	
	//the repository-path
	String repositoryPath;
	
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Product (String productName) {
		super(productName);
		
		components = new HashMap();
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
	public Element serialize() {
		Element productElement = DocumentHelper.createElement("product");
		productElement.addText(getName());

		
		//now all components
		if (this.components.values().iterator().hasNext())
		{
			Element componentElement = productElement.addElement ("components");
		
			//serialize each component
			
			for (Iterator it = this.components.values().iterator(); it.hasNext();)
			{
				ComponentSpecific component = (ComponentSpecific) it.next ();
				componentElement.add (component.serialize ());
			}
		}
		
		if (getRepositoryPath() != null)
		{
			Element repositoryPathElement = productElement.addElement ("repositoryPath");
			repositoryPathElement.addText (getRepositoryPath());
		}

		
		return productElement;
	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		setName(element.getText());
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
		return AbstractAsset.PRODUCT;
	}

  
	/**
	 * Adds a new component.
	 *
	 * @param component contains the new component
	 */
	public void addComponent(ComponentSpecific component) {
		components.put(component.getName(), component);
		//set parent
		component.setParent(this);

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
