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
 * $Id: Component.java,v 1.15 2004/08/23 01:28:00 martinplies Exp $
 *
 */

package kobold.client.plam.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.common.data.Productline;
import kobold.common.data.User;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;

/**
 * Represents a component of a productline.
 */
public class Component extends AbstractMaintainedAsset 
	                   implements IGXLExport, IVariantContainer {

	public static final String GXL_TYPE = "http://kobold.berlios.de/types#component";
	//the variants
	private List variants = new ArrayList();
	
	/**
	 * Basic constructor.
	 */
	public Component() {
		super();
	}
	
	public Component(String name) {
		super(name);
	}

	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Component(Element element) {
		super();
	    deserialize(element);
	}
	
	/**
	 * @see kobold.common.data.ISerializable#serialize()
	 */
	public Element serialize() {
		Element element = super.serialize();
				
		//now all variants
		Element variantsEl = element.addElement("variants");
		for (Iterator it = variants.iterator(); it.hasNext();)	{
				Variant variant = (Variant)it.next();
				variantsEl.add(variant.serialize ());
		}

		return element;
	}

	


	/**
	 * @see kobold.common.data.ISerializable#deserialize(Element)
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.element("variants").elementIterator(AbstractAsset.VARIANT);
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addVariant(new Variant(varEl));
		}
	}

	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return AbstractAsset.COMPONENT;
	}


	public void addVariant(Variant var){
		addVariant(var, -1);
	}

	/**
	 * Adds a variant and sets its parent to this.
	 *
	 * @param variant new variant
	 */
	public void addVariant(Variant variant, int index) 
	{
		if (index >= 0) {
			variants.add(index, variant);
		} else {
			variants.add(variant);
		}

		variant.setParent(this);
		addToPool(variant);
		fireStructureChange(AbstractAsset.ID_CHILDREN, variant);
	}
    
	/**
	 * Removes a variant and sets its parent to null.
	 * 
	 * @param variant
	 */
	public void removeVariant(Variant variant) 
	{
	    variants.remove(variant);
	    variant.setParent(null);
	    fireStructureChange(AbstractAsset.ID_CHILDREN, variant);
	}
	
	/**
	 * Returns a unmodifiable list of variants.
	 * 
	 * @return
	 */
	public List getVariants()
	{
	    return Collections.unmodifiableList(variants);
	}
	
	public List getGXLChildren() {
		return variants;
	}	

	/**
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return GXL_TYPE;
	}
	
	public IPath getLocalPath() {
	    return ModelStorage.getPathForAsset(this);
	}
	
	public RepositoryDescriptor getRemoteRepository() {
	    return ModelStorage.getRepositoryDescriptorForAsset(this);
	}
	
	public kobold.common.data.Component getServerRepresentation(Productline spl)
	{
	    kobold.common.data.Component sc = new kobold.common.data.Component(spl, getName(), 
	        getResource(), getRemoteRepository());
	    sc.setId(getId());
	    Iterator it = getMaintainers().iterator();
	    while (it.hasNext()) {
	        User u = (User)it.next();
	        spl.addMaintainer(u);
	    }
	    return sc;
	}

}
