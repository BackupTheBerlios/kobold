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
 * $Id: Productline.java,v 1.7 2004/06/23 13:34:58 vanto Exp $
 *
 */
package kobold.common.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.common.model.AbstractAsset;
import kobold.common.model.IComponentContainer;
import kobold.common.model.product.Product;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	 */
	public Element serialize() {
		Element root = super.serialize();
		
		//serialize all products and coreAssets
		Element productsEl = root.addElement("products");
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			
			// FIXME: Store only a file reference to the product here. 
			// FIXME: Save the whole project in its own directory.
			// FIXME: productsEl.add(product.serialize());
		}
		
		Element coreAssetsEl = root.addElement("coreassets");		
		for (Iterator it = coreAssets.iterator(); it.hasNext();) {
			Component product = (Component) it.next();
			// FIXME: Store only a file reference to the product here. 
			// FIXME: Save the whole coreasset in its own directory.
			// FIXME: coreAssetsEl.add(product.serialize());
		}
		
		if (repositoryPath != null) {
			root.addAttribute("repositoryPath", repositoryPath);
		}
		
		return root;
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
			document = reader.read(path);
		} catch (DocumentException e) {
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}

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