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
 * $Id: ComponentSpecific.java,v 1.5 2004/06/17 12:23:02 rendgeor Exp $
 *
 */

package kobold.common.data.plam;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;

import java.util.HashMap;
import java.util.Iterator;
/**
 * @author garbeam
 */
public class ComponentSpecific extends AbstractComponent {

	//the parent
	private Product parent;

	//the versions
	private HashMap versions;
	/**
	 * Basic constructor.
	 * @param componentName
	 * @param productLineName
	 */
	public ComponentSpecific (String componentName) {
		super(componentName);
		
		//p-spec type
		versions = new HashMap ();
		
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public ComponentSpecific (Element element) {
		super (element);
	}
	



	/**
	 * Adds a new version.
	 *
	 * @param version contains the new vversion
	 */
	public void addVersion(Version version) {
		versions.put(version.getName(), version);
		version.setParent(this);
	}

	
	/**
	 * Serializes the component.
	 * @see kobold.common.data.plam.ComponentSpecific#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element componentElement = DocumentHelper.createElement("component");
		componentElement.addText(getName());

		//now all versions
		Element versionElement = componentElement.addElement ("versions");
		
		//serialize each component
		for (Iterator it = this.versions.values().iterator(); it.hasNext();)
		{
			Version version = (Version) it.next ();
			versionElement.add (version.serialize ());
		}
	
		return componentElement;
	}

	/**
	 * Deserializes this component.
	 * @param componentName
	 */
	public void deserialize(Element element) {
		setName(element.getText());
	}
	
	/**
	 * @param parent The parent to set.
	 */

	/*public void setParent (Product parentProduct)
	{
		parent = parentProduct;
	}*/
	
}

