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
 * $Id: SpecificComponent.java,v 1.12 2004/09/23 13:43:20 vanto Exp $
 *
 */

package kobold.client.plam.model.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;

import org.dom4j.Element;


/**
 * Represents a product specific component.
 */
public class SpecificComponent extends ProductComponent 
{


	private List releases = new ArrayList();
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#component";
	
	/**
	 * Basic constructor.
	 * @param componentName the component name.
	 */
	public SpecificComponent (String name) 
	{
		super(name);
	}
	
	/**
	 * DOM constructor.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	protected SpecificComponent(AbstractAsset parent, Element element) {
		super();
		setParent(parent);
	    deserialize(element);
	}
	
	/**
	 * Adds a new release.
	 *
	 * @param release contains the new release.
	 */
	public void addRelease (Release release) {
		releases.add(release);
		setParent(this);
		addToPool(release
		        );
	}

	
	/**
	 * Serializes the component.
	 * @see kobold.common.data.plam.ComponentSpecific#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element componentElement = super.serialize();

		Element releasesElement = componentElement.addElement("releases");
		for (Iterator it = releases.iterator(); it.hasNext(); ) {
			Release release = (Release) it.next();
			releasesElement.add(release.serialize());
		}
		return componentElement;
	}

	/**
	 * Deserializes this component.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		Iterator it = element.element("releases").elementIterator(AbstractAsset.RELEASE);
		while (it.hasNext()) {
			Element relEl = (Element)it.next();
			addRelease(new Release(this, relEl));
		}
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.SPECIFIC_COMPONENT;
	}
   

    /* (non-Javadoc)
     * @see kobold.common.model.IGXLExport#getGXLType()
     */
    public String getGXLType()
    {     
        return GXL_TYPE;
    }

    
}

