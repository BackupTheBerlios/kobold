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
 * $Id: Productline.java,v 1.10 2004/06/02 15:56:11 rendgeor Exp $
 *
 */
package kobold.common.data;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;

import kobold.common.data.Product;
/**
 * @author rendgeor
 */
public class Productline implements IAsset {

	//name of the PL
	private String name;
	
	//the products and core-assets
	private HashMap products;
	private HashMap coreAssets;
	
	//create a instance
	static private Productline instance;

	static public Productline getInstance() {
		 if (instance == null ) {
		 	 instance = new Productline("PL");
		 }
		 return instance;
	}	
	
	/**Basic constructor of this singleton.
	 */
	public Productline(String name) {
		this.name = name;
		
	}

	/**
	 */
	public Productline(Element element) {
		deserialize(element);
	}

	
	/**
	 * Adds a new product.
	 *
	 * @param product String containing the new productname
	 */
	public void addProduct(Product product) {
		products.put(product.getName(), product);
	}

	/**
	 * Adds a new coreAsset.
	 *
	 * @param coreAsset String containing the new coreAssetname
	 */
	public void addCoreAsset(Product coreAsset) {
		products.put(coreAsset.getName(), coreAsset);
	}
	
	/**
	 * Serializes the productline.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		return DocumentHelper.createElement("productline").addText(this.name);
	}

	/**
	 * @param productName
	 */
	public void deserialize(Element element) {
		//this.name = element.elementText("productline");
	    this.name = element.getTextTrim();
	}

	/**
	 * @see kobold.common.data.IAsset#getType()
	 */
	public String getType() {
		return IAsset.PRODUCT_LINE;
	}

    /**
     * @see kobold.common.data.IAsset#getName()
     */
    public String getName()
    {
        return name;
    }

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}