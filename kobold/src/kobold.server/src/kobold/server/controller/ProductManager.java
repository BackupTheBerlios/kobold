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
 * $Id: ProductManager.java,v 1.8 2004/06/24 09:58:57 grosseml Exp $
 *
 */
package kobold.server.controller;

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

import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.io.RepositoryDescriptor;
/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs). It's a
 * singleton class.
 *
 * @author garbeam
 */
public class ProductManager {

	private HashMap products;
	private HashMap productLines;
	private String productStore = "products.xml";
	
	
	static private ProductManager instance;
	 
	static public ProductManager getInstance() {
		 if (instance == null ) {
		 	 instance = new ProductManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
	 * @param path
	 */
	private ProductManager() {
		products = new HashMap();
		productLines = new HashMap();
		this.productStore = 
			System.getProperty("kobold.server.storePath") + 
			System.getProperty("kobold.server.productStore");
		// DEBUG
		dummyProds();
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
	 * Adds a new productline.
	 *
	 * @param productLine String containing the new productLine name
	 */
	public void addProductLine(Productline productLine) {
		productLines.put(productLine.getName(), productLine);
	}

	/**
	 * Changes the stored information for the user specified in info.
	 *
	 * @param productname the name of the product.
	 */
	public Product getProduct(String productName) {
		return  (Product)products.get(productName);
	}

	/**
	 * Changes the stored information for the user specified in info.
	 *
	 * @param productLineName the name of the productLine.
	 */
	public Productline getProductLine(String productLineName) {
		return (Productline) productLines.get(productLineName);
	}

	/**
	 * Removes the specified product.
	 */
	public void removeProduct(Product product) {
		products.remove(product);
	}

	/**
	 * Removes the specified productLine.
	 */
	public void removeProductLine(Productline productLine) {
		productLines.remove(productLine);
	}

	/**
	 * Serializes all users with its roles to the file specified
	 * productStore.
	 */
	public void serialize() {
		serialize(this.productStore);
	}
		
	/**
	 * Serializes all products and productlines to the file specified
	 * by path.
	 * 
	 * @param path the path to serialize.
	 */
	public void serialize(String path) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold");

		//now all products
		Element products = root.addElement("products");

		for (Iterator it = this.products.values().iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			products.add(product.serialize());
		}
		//now all productlines
		Element productLines = root.addElement("productlines");

		for (Iterator it = this.productLines.values().iterator(); it.hasNext();) {
			Productline productLine = (Productline) it.next();
			products.add(productLine.serialize());
		}

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
	 * Deserializes all products and productlines from
	 * the productStore.
	 */
	public void deserialize() {
		deserialize(productStore);
	}
	
	/**
	 * Deserializes all products and productlines from the specified file.
	 * 
	 * @param path - file where to read from.
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
		} catch (DocumentException e) {
			Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			log.error(e);
		}

		List listPL = document.selectNodes( "/products/productlines" );
		for (Iterator iter = listPL.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			Productline productLine = new Productline(element);
			productLines.put(productLine.getName(), productLine);
		}
				
		List listP = document.selectNodes( "/products/product" );
		for (Iterator iter = listP.iterator(); iter.hasNext(); ) {
			Element element = (Element) iter.next();
			Product product = new Product(element);
			products.put(product.getName(), product);
		}
	}
	
	// DEBUG
	public void dummyProds() {
		
		addProductLine(new Productline("kobold2",
					   new RepositoryDescriptor(
						"cvs", "cvs.berlios.de",
						"/cvsroot/kobold/kobold2")));
		addProductLine(new Productline("kobold3",
				       new RepositoryDescriptor(
		 			   "cvs", "cvs.berlios.de",
		 			   "/cvsroot/kobold/kobold3")));
		addProductLine(new Productline("kobold4",
					   new RepositoryDescriptor(
					   "cvs", "cvs.berlios.de",
					   "/cvsroot/kobold/kobold4")));
		
		// TODO: repository descriptot for products
		addProduct(new Product("kobold server", "kobold2",
				new RepositoryDescriptor("cvs",
				"cvs.berlios.de", "/cvsroot/kobold/kobold2")));
		addProduct(new Product("kobold client", "kobold3",
				new RepositoryDescriptor("cvs",
			   	"cvs.berlios.de", "/cvsroot/kobold/kobold2")));
		addProduct(new Product("kobold vcm", "kobold4",
				new RepositoryDescriptor("cvs",
			   	"cvs.berlios.de", "/cvsroot/kobold/kobold2")));
	}
}