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
 * $Id: Productline.java,v 1.7 2004/07/26 16:27:52 martinplies Exp $
 *
 */
package kobold.client.plam.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.product.Product;

import org.dom4j.Element;

/**
 * @author rendgeor
 */
public class Productline extends AbstractRootAsset
                         implements IComponentContainer,
                         			IGXLExport{
    
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#productline";

	//the products and core-assets
	private List products = new ArrayList();
	private List coreAssets = new ArrayList();
	
	//the repository-path
	private String repositoryPath;
	private String localPath;

    private EdgeContainer edgeConatainer;
	
	public Productline() 
	{
	    super();
	}
	
	public Productline(Element element) {
		super();
		deserialize(element);
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
		if (index >= 0) {
			coreAssets.add(index, coreAsset);
		} else {
			coreAssets.add(coreAsset);
		}

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
	 */
	public Element serialize() {
		
		//get the AbstractAsset info
		Element root = super.serialize();
		
		//serialize all products and coreAssets
		Element productsEl = root.addElement("products");
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			//product.serializeProduct(path);

			// Store only a file reference to the product here. 
			Element pEl = productsEl.addElement("product");
			pEl.addAttribute("refid", product.getId());

		}
		
		//now all components
		Element componentsEl = root.addElement("components");
		for (Iterator it = coreAssets.iterator(); it.hasNext();)	{
				Component component = (Component)it.next();
				componentsEl.add(component.serialize());
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
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
		   
			//TODO addProduct(new Product (pEl, this,  path));
		}
		
		it = element.element("components").elementIterator(AbstractAsset.COMPONENT);
		while (it.hasNext()) {
		    Element compEl = (Element)it.next();
		    addComponent(new Component(compEl));
		}
		
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


	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLAttributes()
	 */
	public Map getGXLAttributes() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
		return coreAssets;
	}


	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return GXL_TYPE;
	}
	
	/* return the Product
	 * @param name the name of the product
	 */
	public Product getProduct (String productName)
	{
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			if (product.getName().equals(productName)) {
				return product;
			}
		}	
		System.out.println ("product "+productName + " not found!");
		return null;
	}

	/* return the CoreAsset
	 * @param name the name of the coreAsset
	 */
	public Component getComponent (String coreAssetName)
	{
		for (Iterator it = coreAssets.iterator(); it.hasNext();) {
			Component ca = (Component) it.next();
			if (ca.getName().equals(coreAssetName)) {
				return ca;
			}
		}	
		return null;
	}

	
    /**
     * @return Returns the edgeConatainer.
     */
    public EdgeContainer getEdgeConatainer() {
        return edgeConatainer;
    }

}