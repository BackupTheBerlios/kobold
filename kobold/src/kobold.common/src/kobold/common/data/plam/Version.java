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
 * $Id: Version.java,v 1.8 2004/06/17 13:30:31 rendgeor Exp $
 *
 */

package kobold.common.data.plam;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

//import java.util.HashMap;

//import java.util.Iterator;
/**
 * @author garbeam
 */
public class Version extends AbstractAsset {

	private FileDescriptor fileDescriptor;
	/**
	 * Basic constructor.
	 * @param versionName
	 */
	public Version (String versionName) {
		setName (versionName);
		
		//fileDescriptor = new FileDescriptor ("fd_unnamed");

	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Version (Element element) {

		//fileDescriptor = new FileDescriptor ("fd_unnamed");
		
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
		Element versionElement = DocumentHelper.createElement("version");
		versionElement.addText(getName());

		if (fileDescriptor != null)
		{
			//now all fd'S
			//Element fdElement = versionElement.addElement ("fds");
		
			//serialize the fd
			versionElement.add (fileDescriptor.serialize ());
		}
		return versionElement;

	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
		Element product = element.element("product");
		setName (element.getText ());
		//this.productLineName = element.elementText("productline");
	}

	/**
	 * @return name of the dependent productline.

	public String getDependsName() {
		return productLineName;
	}
	 */


	/**
	 * @see kobold.common.data.AbstractProduct#getType()
	 */
	public String getType() {
		return AbstractAsset.VERSION;
	}

	/**
	 * Adds a new fileDescriptor.
	 *
	 * @param fileDescriptor contains the new fileDescriptor
	 */
	public void addFileDescriptor(FileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
		//set parent
		fileDescriptor.setParent(this);

	}
    
}
