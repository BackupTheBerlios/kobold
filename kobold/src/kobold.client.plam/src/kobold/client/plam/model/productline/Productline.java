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
 * $Id: Productline.java,v 1.25 2004/08/23 13:00:42 vanto Exp $
 *
 */
package kobold.client.plam.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.product.Product;
import kobold.common.data.User;

import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;

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
	private Map idPool = new HashMap(); 
	
    public Productline() 
	{
	    super();
	    addToPool(this);
	}

    public Productline(String name) 
	{
	    super(name);
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
		addToPool(product);
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
		addToPool(coreAsset);

		;
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
	 * returns a Serialized productline.
	 */
	public Element serialize() {
		

		//get the AbstractAsset info
		Element root = super.serialize();
		
		//serialize all products and coreAssets
		Element productsEl = root.addElement("products");

		//now all products
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product product = (Product) it.next();
			//product.serializeProduct();

			// Store only a file reference to the product here. 
			Element pEl = productsEl.addElement("product");
			pEl.addAttribute("refid", product.getId());
			
			
				//TODO ms.serializeProduct(product);

		}
		
		//now all components
		Element componentsEl = root.addElement("components");
		for (Iterator it = coreAssets.iterator(); it.hasNext();)	{
				Component component = (Component)it.next();
				componentsEl.add(component.serialize());
				
				//create component dirs
				//TODO createComponentDirectory(component);

				//serializeAll?
				//TODO	ms.serializeComponent(component);


		}
		
		// serialize EdgeContainer
		//Element edgeContElem = root.addElement("edgeconatainer");
		//edgeContElem.add(this.getEdgeContainer().serialize());
		root.add(getEdgeContainer().serialize());
		
//		if (repositoryPath != null) {
//			root.addAttribute("repositoryPath", repositoryPath);
//		}
		
		return root;
	}
	

	
//	private void createProductDirectory (Product product)
//	{
//		//create directory for every product			
//		File newDir = new File (getLocalPath().toOSString()/*+ File.separatorChar +getName() */+ File.separator 
//										+ "PRODUCTS" + File.separator + product.getName());
//
//
//		if (newDir.mkdir()==true)
//		{
//			System.out.println("Project-PL-Products-Dirs Directory was created");
//		}
//		else
//		System.out.println("Project-PL-Products-Dirs Directory already existed");
//	}

	
//	private void createComponentDirectory (Component component)
//	{
//	
//		//create directory for every component			
//		File newDir = new File (getLocalPath().toOSString()/*+ File.separatorChar +getName() */+ File.separator 
//										+ "CAS" + File.separator + component.getName());
//
//		if (newDir.mkdir()==true)
//		{
//			System.out.println("Project-PL-CAS-Dirs Directory was created");
//		}
//
//		else
//		System.out.println("Project-PL-CAS-Dirs Directory already existed");
//	
//	}	
	public void deserialize(Element element) {
	    super.deserialize(element);
	    //repositoryPath = element.attributeValue("repositoryPath");
	    
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
		    addComponent(new Component(this, compEl));
		}
		
		// edges must deserialize at last. Otherwise the other nodes are missing in the ie pool. 
		Element edges = element.element("edges");
		this.getEdgeContainer().deserialize(edges);		
		
	}
	
	/**
	 * @see kobold.common.data.IAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.PRODUCT_LINE;
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
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	public IPath getLocalPath() {
	    return ModelStorage.getPathForAsset(this);		
	}
	
	public kobold.common.data.Productline getServerRepresentation()
	{
	    kobold.common.data.Productline spl = new kobold.common.data.Productline(getName(), 
	        	getResource(), getRepositoryDescriptor());
	    
	    spl.setId(getId());
	    Iterator it = getMaintainers().iterator();
	    while (it.hasNext()) {
	        User u = (User)it.next();
	        spl.addMaintainer(u);
	    }
	    
	    it = getComponents().iterator();
	    while (it.hasNext()) {
	        Component c = (Component)it.next();
	        spl.addCoreAsset(c.getServerRepresentation(spl));
	    }
	    return spl;
	}
	
	/**
	 * @param id
	 * @return Returns asset with this id or null.
	 */
	public AbstractAsset getAssetById(String id){
	    return (AbstractAsset) idPool.get(id);
	}
	
	/**
	 * Add asset to idpool.
	 * @param asset
	 */
	public void addAssetToPool(AbstractAsset asset) {
	    synchronized (idPool) {
	        idPool.put(asset.getId(), asset);
	    }
	}
	
	/**
	 * Delete asset with this id from the id pool.
	 * @param id 
	 */
	public void removeAsset(String id) {
	    synchronized (idPool) {
	        idPool.remove(id);
	    }
	}

    /* (non-Javadoc)
     * @see kobold.client.plam.model.AbstractRootAsset#getProductline()
     */
    public Productline getProductline() {        
        return this;
    }
	
	
}