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
 * $Id: SpecificComponent.java,v 1.5 2004/06/24 11:25:10 rendgeor Exp $
 *
 */

package kobold.common.model.product;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.common.model.AbstractAsset;
import kobold.common.model.Release;


/**
 * Represents a product specific component.
 */
public class SpecificComponent extends AbstractAsset {

	private List releases;
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#component";
	
	/**
	 * Basic constructor.
	 * @param componentName the component name.
	 */
	public SpecificComponent (String componentName) {
		super(componentName);
		releases = new ArrayList();
	}
	
	/**
	 * DOM constructor.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	public SpecificComponent (Element element) {
		deserialize(element);
	}
	
	/**
	 * Adds a new release.
	 *
	 * @param release contains the new release.
	 */
	public void addRelease (Release release) {
		releases.add(release);
	}

	
	/**
	 * Serializes the component.
	 * @see kobold.common.data.plam.ComponentSpecific#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element componentElement = DocumentHelper.createElement("specific-component");
		componentElement.addAttribute("name", getName());

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
		setName(element.attributeValue("name"));
	}

	/**
	 * @see kobold.common.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.COMPONENT;
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getAttributes()
	 */
	public Map getAttributes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getChildren()
	 */
	public List getChildren() {
		// TODO add releases;
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLType()
	 */
	public String getGXLType() {
		return GXL_TYPE;
	}		
}

