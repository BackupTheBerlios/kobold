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
 * $Id: Variant.java,v 1.3 2004/06/22 11:29:15 vanto Exp $
 *
 */

package kobold.common.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.common.model.AbstractAsset;
import kobold.common.model.Release;

import org.dom4j.Element;

/**
 * @author garbeam
 * @author vanto
 */
public class Variant extends AbstractAsset {

	private List components = new ArrayList();
	private List releases = new ArrayList();;
	
	/**
	 * Basic constructor.
	 * @param productName
	 * @param productLineName
	 */
	public Variant (String productName) {
		super(productName);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Variant(Element element) {
	    deserialize(element);
	}
	

	/**
	 * @see kobold.common.data.ISerializable#serialize()
	 */
	public Element serialize() {
		Element element = super.serialize();
				
		//now all components
		Element componentsEl = element.addElement("components");
		for (Iterator it = components.iterator(); it.hasNext();)	{
				Component component = (Component)it.next();
				componentsEl.add(component.serialize());
		}

		//now all releases
		Element releasesEl = element.addElement("releases");
		for (Iterator it = releases.iterator(); it.hasNext();)	{
				Release release = (Release)it.next();
				releasesEl.add(release.serialize());
		}

		return element;
	}
	

	/**
	 * @see kobold.common.data.ISerializable#deserialize(Element)
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		
		Iterator it = element.element("components").elementIterator(AbstractAsset.COMPONENT);
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addComponent(new Component(varEl));
		}
		
		it = element.element("releases").elementIterator(AbstractAsset.RELEASE);
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addRelease(new Release(varEl));
		}

		
	}


	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return AbstractAsset.VARIANT;
	}

	/**
	 * Adds a component and sets its parent to this.
	 *
	 * @param component contains the new component
	 */
	public void addComponent(Component component) {
		components.add(component);
		component.setParent(this);
	}

	/**
	 * Removes a component and sets its parent to null.
	 * 
	 * @param comp
	 */
	public void removeComponent(Component comp)
	{
	    components.remove(comp);
	    comp.setParent(null);
	}
	
	/**
	 * Returns a unmodifiable list of components.
	 * 
	 * @return
	 */
	public List getComponents()
	{
	    return Collections.unmodifiableList(components);
	}

	
	/**
	 * Adds a version and sets its parent to this
	 *
	 * @param version contains the new version
	 */
	public void addRelease(Release release) {
		releases.add(release);
		release.setParent(this);
	}

	/**
	 * Removes a release and sets its parent to null.
	 * 
	 * @param release
	 */
	public void removeRelease(Release release)
	{
	    releases.remove(release);
	    release.setParent(null);
	}
	/**
	 * Returns a unmodifiable list of releases.
	 * 
	 * @return
	 */
	public List getReleases()
	{
	    return Collections.unmodifiableList(releases);
	}

    
}
