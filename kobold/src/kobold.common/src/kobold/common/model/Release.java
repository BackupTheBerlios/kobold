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
 * $Id: Release.java,v 1.6 2004/06/25 17:25:34 martinplies Exp $
 *
 */

package kobold.common.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * TODO: Kopfkommentar
 * @author garbeam
 */
public class Release extends AbstractAsset {

    private List fileDescriptors = new ArrayList();

	/**
	 * Basic constructor.
	 * @param versionName
	 */
	public Release (String releaseName) {
	    super(releaseName);
	}
	
	/**
	 * DOM constructor.
	 * @param productName
	 */
	public Release (Element element) {
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public Element serialize() {
	    Element element = super.serialize();

	    Element fdsEl = element.addElement("filedescriptors");
	    
	    for (Iterator it = fileDescriptors.iterator(); it.hasNext();) {
            FileDescriptor fd = (FileDescriptor) it.next();
            fdsEl.add(fd.serialize());
        } 

		return element;

	}

	/**
	 * Deserializes this product.
	 * @param productName
	 */
	public void deserialize(Element element) {
	    Iterator it = element.element("filedescriptors").elementIterator(AbstractAsset.FILE_DESCRIPTOR);
		while (it.hasNext()) {
		    Element fdEl = (Element)it.next();
		    addFileDescriptor(new FileDescriptor(fdEl));
		}
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
	public String getType() 
	{
		return AbstractAsset.RELEASE;
	}

	/**
	 * Adds a new fileDescriptor.
	 *
	 * @param fileDescriptor contains the new fileDescriptor
	 */
	public void addFileDescriptor(FileDescriptor fileDescriptor) 
	{
		fileDescriptors.add(fileDescriptor);
		//set parent
		fileDescriptor.setParent(this);
	}
	
	public void removeFileDescriptor(FileDescriptor fd) 
	{
	    fileDescriptors.remove(fd);
	    fd.setParent(null);
	}
	
	public List getFileDescriptors() 
	{
	    return Collections.unmodifiableList(fileDescriptors);
	}

	/* (non-Javadoc)
	 * @see kobold.common.model.AbstractAsset#getGXLAttributes()
	 */


}
