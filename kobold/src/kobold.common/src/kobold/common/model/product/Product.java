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
 * $Id: Product.java,v 1.8 2004/06/24 03:35:01 rendgeor Exp $
 *
 */

package kobold.common.model.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;

import kobold.common.data.Productline;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.model.AbstractAsset;

/**
 * This class represents a product.  
 */
public class Product extends AbstractAsset {

	// containers
	private List productReleases;
	private List specificComponents;
	private List relatedComponents;
	private RepositoryDescriptor repositoryDescriptor = null;
	private String localPath;
		
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Product(String productName) {
		super(productName);
		
		productReleases = new ArrayList();
		specificComponents = new ArrayList();
		relatedComponents = new ArrayList();
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Product(Element element, AbstractAsset parent, String path) {
		setName(element.attributeValue("name"));
		setParent(parent);
		deserialize(path);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		
		Element productElement = super.serialize();

		Element prodRelElement = productElement.addElement("releases");
		for (Iterator it = productReleases.iterator(); it.hasNext(); ) {
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

	public void serializeProduct (String path)
	{
		//creates a document
		Document document = DocumentHelper.createDocument();
		
		//get the abstractAsset information
		Element root = document.addElement("productmetainfo");
		
		//add the serialized element
		root.add (serialize ());
		
		//write it to an xml-file
			 XMLWriter writer;
			try {
				writer = new XMLWriter(new FileWriter(path+ File.separatorChar + ((AbstractAsset)getParent()).getName() 
													+ File.separatorChar + "PRODUCTS" + File.separatorChar + getName() 
													+ File.separatorChar + ".productmetainfo.xml"));
				writer.write(document);
				writer.close();
			} catch (IOException e) {
				Log log = LogFactory.getLog("kobold....");
				log.error(e);
			}	
	}


	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element realElement = element.element (AbstractAsset.PRODUCT);
		super.deserialize(realElement);
		//setName(element.attributeValue("name"));
		// TODO
	}
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path+
			 File.separatorChar + ((AbstractAsset)getParent()) .getName()
			+ File.separatorChar + "PRODUCTS" + File.separatorChar + getName() 
			+ File.separatorChar + ".productmetainfo.xml");
		} catch (DocumentException e) {
			System.out.print(path+ File.separatorChar + ((AbstractAsset)getParent()).getName()
			+ File.separatorChar + "PRODUCTS" + File.separatorChar + getName() 
			+ File.separatorChar + ".productmetainfo.xml" +  " read error");
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}

		//give the result to the deserializer
		deserialize(document.getRootElement());
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

	/**
	 * @return
	 */
	public List getProductReleases() {
		return productReleases;
	}

	/**
	 * @return
	 */
	public List getRelatedComponents() {
		return relatedComponents;
	}

	/**
	 * @return
	 */
	public RepositoryDescriptor getRepositoryDescriptor() {
		return repositoryDescriptor;
	}

	/**
	 * @return
	 */
	public List getSpecificComponents() {
		return specificComponents;
	}

	/**
	 * @param descriptor
	 */
	public void setRepositoryDescriptor(RepositoryDescriptor descriptor) {
		repositoryDescriptor = descriptor;
	}

    /**
     * @return Returns the localPath.
     */
    public String getLocalPath()
    {
        return localPath;
    }
    
    /**
     * @param localRepository The localRepository to set.
     */
    public void setLocalPath(String localPath)
    {
        this.localPath = localPath;
    }
}
