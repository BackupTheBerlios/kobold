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
 * $Id: Product.java,v 1.27 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.model.product;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.IProductComponentContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.productline.Productline;
import kobold.common.data.User;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IPath;

/**
 * This class represents a product.  
 */
public class Product extends AbstractRootAsset
                     implements IGXLExport, IProductComponentContainer{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Product.class);

	// containers
	private List productReleases = new ArrayList();
	private List specificComponents = new ArrayList();
	private List relatedComponents = new ArrayList();

	private static final String GXL_TYPE = "http://kobold.berlios.de/types#Product";
    private Productline productline;
		
	public Product(Productline productline)
	{	    
	    super();
	    this.productline = productline;
	}
		

	public Product(String name, Productline productline)
	{	   
	    super(name);
	    this.productline = productline;
	}
	
	public Product(Productline productline, Element element)
	{
	    this(productline);
	    deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 */
	public Element serialize() {
		
		Element productElement = super.serialize();

		Element prodRelElement = productElement.addElement("releases");
		for (Iterator it = productReleases.iterator(); it.hasNext(); ) {
			if (logger.isDebugEnabled()) {
				logger.debug("serialize() - release serialized");
			}	
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

		// serialize EdgeContainer
		productElement.add(getEdgeContainer().serialize());

		return productElement;
	}

	

	/**
	 * Deserializes this product.
	 * @param element
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		//setName(element.attributeValue("name"));
	    
		Iterator it = element.element("releases").elementIterator(AbstractAsset.PRODUCT_RELEASE);
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    addProductRelease(new ProductRelease(this, pEl));
		}
		it = element.element("specific-components").elementIterator(AbstractAsset.SPECIFIC_COMPONENT);
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
			addProductComponent(new SpecificComponent(this, pEl));
		}

		it = element.element("related-components").elementIterator(AbstractAsset.RELATED_COMPONENT);
		while (it.hasNext()) {
		    Element pEl = (Element)it.next();
		    /* load and create the product by finding its local path and 
		     		  deserializing it from there.
		    */
		    addProductComponent(new RelatedComponent(this, pEl));	
		}

		// edges must deserialize at last. Otherwise the other nodes are missing in the ie pool. 
		Element edges = element.element("edges");
		this.getEdgeContainer().deserialize(edges);

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
			logger.error("deserializeProduct()" + getLocalPath().toOSString()
					+ File.separatorChar
					+ ((AbstractAsset) getParent()).getName()
					+ File.separatorChar + "PRODUCTS" + File.separatorChar
					+ getName() + File.separatorChar
					+ ".productmetainfo.xml read error", e);
			//Log log = LogFactory.getLog("kobold.server.controller.ProductManager");
			//log.error(e);
		}
	}
	
	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.PRODUCT;
	}
  
	/**
	 * Adds a new component.
	 *
	 * @param component contains the new component
	 */
	public void addProductComponent(ProductComponent component) {
	    addProductComponent(component, -1);
	}
	
    public void addProductComponent(ProductComponent comp, int index) {
        if (comp instanceof SpecificComponent) {
            addSpecificComponent((SpecificComponent)comp, index);
		}
		else if (comp instanceof RelatedComponent) {
		    addRelatedComponent((RelatedComponent)comp, index);
		}
    }

	
	public void addProductRelease(ProductRelease productRelease)
	{
		productReleases.add(productRelease);
		productRelease.setParent(this);
		addToPool(productRelease);
	}
	
	public List getProductReleases() {
		return Collections.unmodifiableList(productReleases);
	}

	public List getRelatedComponents() {
		return Collections.unmodifiableList(relatedComponents);
	}
	
	public List getSpecificComponents() {
		return Collections.unmodifiableList(specificComponents);
	}   

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
		ArrayList children = new ArrayList();
		children.addAll(specificComponents);
		return children;
		// TODO add Release
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getGXLType()
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

    /**
     * @see kobold.client.plam.model.AbstractRootAsset#getProductline()
     */
    public Productline getProductline() {        
        return productline;
    }

	public kobold.common.data.Product getServerRepresentation(kobold.common.data.Productline spl)
	{
	    kobold.common.data.Product sp = new kobold.common.data.Product (spl, getName(), getResource(),
	               getRepositoryDescriptor());
	    sp.setId(getId());
	    Iterator it = getMaintainers().iterator();
	    while (it.hasNext()) {
	        User u = (User)it.next();
	        sp.addMaintainer(u);
	    }
	    
	    it = getRelatedComponents().iterator();
	    while (it.hasNext()) {
	        ProductComponent pc = (ProductComponent)it.next();
	        sp.addComponent(pc.getServerRepresentation(spl));
	    }
	    return sp;

	}

	public List getChildren()
	{
	    List l = new ArrayList();
	    l.addAll(getProductReleases());
	    l.addAll(getSpecificComponents());
	    l.addAll(getRelatedComponents());
	    return l;
	}
 

    /**
     * @see kobold.client.plam.model.IProductComponentContainer#removeProductComponent(kobold.client.plam.model.product.ProductComponent)
     */
    public void removeProductComponent(ProductComponent comp) {
        if(comp instanceof SpecificComponent){
            this.specificComponents.remove(comp);
        } else {
            this.relatedComponents.remove(comp);
        }
        comp.setParent(null);
		fireStructureChange(AbstractAsset.ID_CHILDREN, comp);
        
    }


    /* (non-Javadoc)
     * @see kobold.client.plam.model.IProductComponentContainer#getProductComponents()
     */
    public List getProductComponents() {
        List list = new ArrayList();
        list.addAll(this.relatedComponents);
        list.addAll(this.specificComponents);
        return list;
    }


    public void addSpecificComponent(SpecificComponent comp){
        addSpecificComponent(comp, -1);
    }
    
    public void addSpecificComponent(SpecificComponent comp, int index){
        if (index >= 0){
            specificComponents.add(index, (SpecificComponent)comp);
        } else {
            specificComponents.add((SpecificComponent)comp);
        }        
        comp.setParent(this);
        addToPool(comp);
        fireStructureChange(AbstractAsset.ID_CHILDREN, comp);
    }
    
    public void addRelatedComponent(RelatedComponent comp){
        addRelatedComponent(comp, -1);
    }
    public void addRelatedComponent(RelatedComponent comp, int index){
        if (index >= 0){
           relatedComponents.add(index, (RelatedComponent)comp);
        } else {
            relatedComponents.add((RelatedComponent)comp); 
        }        
		comp.setParent(this);
		addToPool(comp);
		fireStructureChange(AbstractAsset.ID_CHILDREN, comp);
    }

}
