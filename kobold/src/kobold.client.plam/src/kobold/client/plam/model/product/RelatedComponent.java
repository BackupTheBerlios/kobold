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
 * $Id: RelatedComponent.java,v 1.16 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.model.product;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.productline.Variant;

import org.dom4j.Element;

/**
 * Represents a related component. Related components are
 * components of products which are related to a product
 * lines component.
 */

public class RelatedComponent extends ProductComponent 
                              implements IGXLExport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(RelatedComponent.class);

	private Variant  relatedVariant;
	private Release relatedRelease;
	
	/**
	 * Basic constructor.
	 * @param variant the related variant.
	 * @param release the release of the corresponding component in the productline.
	 **/
	public RelatedComponent (Variant variant,
							 Release release) {
		super(variant.getParent().getName());
		relatedVariant = variant;
		this.relatedRelease = release;		
	}
	
	
	
	/**
	 * Serializes the component.
	 * @see kobold.client.plam.model.AbstractAsset#serialize()
	 */
	public Element serialize() {
		Element element = super.serialize();
		element.addElement("ref").addAttribute("release-id", relatedRelease.getId());
		
		return element;
	}

	/**
	 * Deserializes this component.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		String releaseId = element.element("ref").attributeValue("release-id");
	    relatedRelease = (Release)getRoot().getProductline().getAssetById(releaseId);
		this.relatedVariant = (Variant)relatedRelease.getParent();
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.RELATED_COMPONENT;
	}

	/**
	 * DOM constructor.
     * @param parent
	 * @param element
	 */
	protected RelatedComponent (AbstractAsset parent, Element element) {
		super();
		setParent(parent);
		deserialize(element);
	}	

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getGXLChildren()
	 */
	public List getGXLChildren() {
	    List children = new ArrayList();
	    children.addAll(getProductComponents());
		return children;
	}


	/**
	 * @see 
	 * kobold.client.plam.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return relatedVariant.getGXLType();
	}

	public Variant getRelatedVariant()
	{
	    return relatedVariant;
	}

	public Release getRelatedRelease()
	{
	    return relatedRelease;
	}



    /**
     *  Update Release of this ReleatedComponent.
     */
    public void updateRelease() {
       Release head = relatedVariant.getHead();
       if (head != null && head != this.getRelatedRelease()) {
            this.relatedRelease = head;
            KoboldProject proj = KoboldPLAMPlugin.getCurrentKoboldProject();
            proj.updateRelease(this, head); 
       }
    }
    

}
