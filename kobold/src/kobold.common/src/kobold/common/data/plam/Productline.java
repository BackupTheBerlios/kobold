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
 * $Id: Productline.java,v 1.3 2004/06/03 13:44:17 rendgeor Exp $
 *
 */
package kobold.common.data.plam;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author rendgeor
 */
public class Productline {

	//name of the PL
	private String name;
	
	//the products and core-assets
	private HashMap products;
	private HashMap coreAssets;
	
	//the repository-path
	String repositoryPath;
	
	
	private String productlineStore = "productline.xml";
	
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
	 * Adds a new product.
	 *
	 * @param product String containing the new productname
	 */
	public void addProduct(Product product) {
		products.put(product.getName(), product);
	}

	/**
	 * Removes a product
	 * 
	 * @param product The product to remove.
	 */
	public void removeProduct (Product product){
		products.remove(product);
	}
		
	
	/**
	 * Adds a new coreAsset.
	 *
	 * @param coreAsset String containing the new coreAssetname
	 */
	public void addCoreAsset(CoreAsset coreAsset) {
		coreAssets.put(coreAsset.getName(), coreAsset);
	}

	/**
	 * Removes a coreAsset
	 * 
	 * @param product The coreAsset to remove.
	 */
	public void removeProduct (CoreAsset coreAsset){
		coreAssets.remove(coreAsset);
	}
	
	
	/**
	 * Serializes the file specified
	 * productlineStore.
	 */
	public void serialize() {
		serialize(this.productlineStore);
	}
	
	/**
	 * Serializes the productline.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public void serialize(String path) {

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold-Productline");
		
		//serialize all products and coreAssets
		
		//now all products
		Element productsElement = root.addElement("products");

		for (Iterator it = this.products.values().iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			productsElement.add(product.serialize());
		}

		//now all coreAssets
		Element coreAssetsElement = root.addElement("coreAssets");		
		
		for (Iterator it = this.coreAssets.values().iterator(); it.hasNext();) {
			CoreAsset product = (CoreAsset) it.next();
			coreAssetsElement.add(product.serialize());
		}

		
		//write it to an xml-file
		 XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.server.controller.ProductAdmin");
			log.error(e);
		}	
	
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
			document = reader.read(path);
		} catch (DocumentException e) {
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}

		//all products				
		List listP = document.selectNodes( "/products/product" );
		for (Iterator iter = listP.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			Product product = new Product(element);
			products.put(product.getName(), product);
		}

		//all coreAssets				
		List listCA = document.selectNodes( "/coreAssets/coreAsset" );
		for (Iterator iter = listP.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			CoreAsset product = new CoreAsset(element);
			coreAssets.put(product.getName(), product);
		}

	
	}

	/**
	 * Deserializes all products and coreAssets from
	 * the productlineStore.
	 */
	public void deserialize() {
		deserialize(productlineStore);
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