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
 * $Id: Productline.java,v 1.8 2004/06/24 00:12:51 rendgeor Exp $
 *
 */
package kobold.common.model.productline;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.File;

import kobold.common.model.AbstractAsset;
import kobold.common.model.IComponentContainer;
import kobold.common.model.product.Product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author rendgeor
 */
public class Productline extends AbstractAsset
						 implements IComponentContainer {

	//the products and core-assets
	private List products = new ArrayList();
	private List coreAssets = new ArrayList();
	
	//the repository-path
	String repositoryPath;
	
	public Productline(String name) {
		super(name);
		setParent(null);
	}


	/**
	 * Adds a product and sets its parent to this.
	 *
	 */
	public void addProduct(Product product) {
		products.add(product);
		product.setParent(this);
		fireStructureChange(AbstractAsset.ID_CHILDREN, product);
	}

	/**
	 * Removes a product and sets its parent to null.
	 * 
	 * @param product The product to remove.
	 */
	public void removeProduct(Product product){
		products.remove(product);
		product.setParent(null);
		fireStructureChange(AbstractAsset.ID_CHILDREN, product);
	}
		
	/**
	 * Returns an unmodifiable list of products.
	 * 
	 */
	public List getProducts()
	{
	    return Collections.unmodifiableList(products);
	}
	
	public void addComponent(Component coreAsset)
	{
	    addComponent(coreAsset, -1);
	}
	
	/**
	 * Adds a CoreAsset (Component) and sets its parent to this.
	 *
	 */
	public void addComponent(Component coreAsset, int index) {
		if (index >= 0)
			coreAssets.add(index, coreAsset);
		else
			coreAssets.add(coreAsset);

		coreAsset.setParent(this);
		fireStructureChange(AbstractAsset.ID_CHILDREN, coreAsset);
	}

	/**
	 * Removes a CoreAsset and sets its parent to null.
	 * 
	 * @param product The coreAsset to remove.
	 */
	public void removeComponent(Component coreAsset){
		coreAssets.remove(coreAsset);
		coreAsset.setParent(null);
		fireStructureChange(AbstractAsset.ID_CHILDREN, coreAsset);
	}
	
	public List getComponents()
	{
	    return Collections.unmodifiableList(coreAssets);
	}
	
	/**
	 * Serializes the productline.
	 * @see kobold.common.data.plam.Product#serialize(org.dom4j.Element)
	 * @param path : path-prefix to save the serialized files
	 * @param level: 0=serialize only the .productlinemetainfo, 1=additionally all product and coreAsset metaInfos
	 */
	private Element serialize(String path, int level) {
		
		//get the AbstractAsset info
		Element root = super.serialize();
		
		//create directory for the PL
		File newDir = new File(path+ File.separatorChar + getName());
		newDir.mkdir();
		newDir = new File(path+ File.separatorChar + getName() + File.separatorChar + "PL");
		
		if (newDir.mkdir()==true)
		System.out.println("Directory was created");
		else
		System.out.println("Directory already existed");
		 
		//serialize all products and coreAssets
		Element productsEl = root.addElement("products");
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();

			//create directory for every product			
			newDir = new File(path+ File.separatorChar +getName() + File.separator 
											+ "PRODUCTS");
			newDir.mkdir();
			newDir = new File(path+ File.separatorChar +getName() + File.separator 
											+ "PRODUCTS" + File.separator + product.getName());


			if (newDir.mkdir()==true)
			{
				System.out.println("Directory was created");
				if (level == 1)
				product.serializeProduct(path);
			}
			else
			System.out.println("Directory already existed");
			
			// Store only a file reference to the product here. 
			productsEl.addElement(product.getName());

		}
		
		Element coreAssetsEl = root.addElement("coreassets");		
		for (Iterator it = coreAssets.iterator(); it.hasNext();) {
			Component component = (Component) it.next();
			
			//create directory for every product			
			newDir = new File(path+ File.separatorChar +getName() + File.separator 
											+ "CAS");
			newDir.mkdir();
			newDir = new File(path+ File.separatorChar +getName() + File.separator 
											+ "CAS" + File.separator + component.getName());

			if (newDir.mkdir()==true)
			{
				System.out.println("Directory was created");
				if (level == 1)
				component.serializeComponent(path);
			}

			else
			System.out.println("Directory already existed");

			
			
			// Store only a file reference to the product here.
			coreAssetsEl.addElement(component.getName()); 
		}
		
		if (repositoryPath != null) {
			root.addAttribute("repositoryPath", repositoryPath);
		}
		
		return root;
	}
	
	public void serializeProductline (String path, int level)
	{
		//creates a document
		Document document = DocumentHelper.createDocument();
		
		//get the abstractAsset information
		Element root = document.addElement("productlinemetainfo");
		
		//add the serialized element
		root.add (serialize (path, level));
		
		//write it to an xml-file
			 XMLWriter writer;
			try {
				writer = new XMLWriter(new FileWriter(path+ File.separatorChar + getName() 
													+ File.separatorChar + "PL" + File.separatorChar + ".productlinemetainfo.xml"));
				writer.write(document);
				writer.close();
			} catch (IOException e) {
				Log log = LogFactory.getLog("kobold....");
				log.error(e);
			}	
	}
	
	public void serializeAll (String path)
	{
		serializeProductline(path, 1);
		
	}

	
	public void deserialize(Element element) {
	    super.deserialize(element);
	    repositoryPath = element.attributeValue("repositoryPath");
	    
		Iterator it = element.element("products").elementIterator(AbstractAsset.PRODUCT);
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* FIXME: load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
		    // FIXME: addProduct(AbstractAsset.createProduct(localPath));
		}
		
		// FIXME: Same here with coreassets.
	}
	
	/**
	 * Deserializes all products and coreAssets from the specified file.
	 * 
	 * @param path - file where to read from.
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path+ File.separatorChar + getName() 
			+ File.separatorChar + "PL" + File.separatorChar + ".productlinemetainfo.xml");
		} catch (DocumentException e) {
			System.out.print( ".productlinemetainfo read error");
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}

		//give the result to the deserializer
		deserialize(document.getRootElement());
	}

	/**
	 * @see kobold.common.data.IAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.PRODUCT_LINE;
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
		firePropertyChange(AbstractAsset.ID_DATA, null, repositoryPath);
	}
}