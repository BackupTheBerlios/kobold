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
 * $Id: Product.java,v 1.9 2004/08/01 12:54:35 rendgeor Exp $
 *
 */
package kobold.client.plam.model.product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.ModelStorage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.IPath;

/**
 * This class represents a product.  
 */
public class Product extends AbstractRootAsset
                     implements IGXLExport{

	// containers
	private List productReleases = new ArrayList();
	private List specificComponents = new ArrayList();
	private List relatedComponents = new ArrayList();

	private static final String GXL_TYPE = "http://kobold.berlios.de/types#Product";
		
	/**
	 * Basic constructor.
	 * @param productName
	 */
	public Product(String productName) {
		super(productName);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Product(Element element, AbstractAsset parent) {
		super(element.attributeValue("name"));
		setParent(parent);
		deserializeProduct();
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		
		Element productElement = super.serialize();

		Element prodRelElement = productElement.addElement("releases");
		for (Iterator it = productReleases.iterator(); it.hasNext(); ) {
			System.out.println ("release serialized");	
			ProductRelease prodRelease = (ProductRelease) it.next();
			prodRelElement.add(prodRelease.serialize());

		}

		Element specCompElement = productElement.addElement("specific-components");
		for (Iterator it = specificComponents.iterator(); it.hasNext(); ) {
			SpecificComponent specificComponent = (SpecificComponent) it.next();
			specCompElement.add(specificComponent.serialize());
		}

		Element relCompElement = productElement.addElement("related-components");
		for (Iterator it = relatedComponents.iterator(); it.hasNext(); ) {
			RelatedComponent relatedComponent = (RelatedComponent) it.next();
			relCompElement.add(relatedComponent.serialize());
		}
	
		return productElement;
	}

	

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		//setName(element.attributeValue("name"));

		
		System.out.println ("start deserializing!");
	    
		Iterator it = element.elementIterator("releases");
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
		    addProductRelease(new ProductRelease (pEl));
		}
		it = element.elementIterator("specific-components");
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
			addComponent(new SpecificComponent (pEl));
		}

		it = element.elementIterator("releated-component");
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
		    addComponent(new RelatedComponent (pEl));	
		}


	}
	
	private void deserializeProduct() {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(getLocalPath().toOSString()+
			 File.separatorChar + ((AbstractAsset)getParent()) .getName()
			+ File.separatorChar + "PRODUCTS" + File.separatorChar + getName() 
			+ File.separatorChar + ".productmetainfo.xml");
			
			//give the result to the deserializer
			deserialize(document.getRootElement().element(AbstractAsset.PRODUCT));
		} catch (DocumentException e) {
			System.err.print(getLocalPath().toOSString()+ File.separatorChar + ((AbstractAsset)getParent()).getName()
			+ File.separatorChar + "PRODUCTS" + File.separatorChar + getName() 
			+ File.separatorChar + ".productmetainfo.xml" +  " read error");
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}
	}
	

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
	public void addComponent(AbstractAsset component) {
		if (component instanceof SpecificComponent) {
			specificComponents.add((SpecificComponent)component);
		}
		else if (component instanceof RelatedComponent) {
			relatedComponents.add((RelatedComponent)component);
		}
		//component.setParent(this);
	}

	
	public void addProductRelease(ProductRelease productRelease)
	{
		productReleases.add(productRelease);
		productRelease.setParent(this);
	}
	
	/**
	 * @return
	 */
	public List getProductReleases() {
		return Collections.unmodifiableList(productReleases);
	}

	/**
	 * @return
	 */
	public List getRelatedComponents() {
		return Collections.unmodifiableList(relatedComponents);
	}

	/**
	 * @return
	 */
	public List getSpecificComponents() {
		return Collections.unmodifiableList(specificComponents);
	}

    
	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLAttributes()
	 */
	public Map getGXLAttributes() {
		HashMap attributes = new HashMap();
		return attributes;
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
		ArrayList children = new ArrayList();
		children.addAll(specificComponents);
		return children;
		// TODO add Release
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return GXL_TYPE;
	}
	
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	
	public IPath getLocalPath() {
	    return ModelStorage.getPathForAsset(this);		
	}
	
}
