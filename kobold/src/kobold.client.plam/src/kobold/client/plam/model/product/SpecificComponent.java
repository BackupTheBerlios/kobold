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
 * $Id: SpecificComponent.java,v 1.3 2004/07/08 15:00:52 rendgeor Exp $
 *
 */

package kobold.client.plam.model.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.Release;

import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;


/**
 * Represents a product specific component.
 */
public class SpecificComponent extends ProductComponent 
							   implements IFileDescriptorContainer {


	private List releases = new ArrayList();
	private List filedescs = new ArrayList();
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#component";
	
	/**
	 * Basic constructor.
	 * @param componentName the component name.
	 */
	public SpecificComponent (String componentName) {
		super(componentName);
	}
	
	/**
	 * DOM constructor.
	 * @param element the DOM element representing this
	 * 		  object.
	 */
	protected SpecificComponent (Element element) {
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
			addRelease(new Release(relEl));
		}
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.SPECIFIC_COMPONENT;
	}

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#addFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void addFileDescriptor(FileDescriptor fd)
    {
        filedescs.add(fd);
        fd.setParentAsset(fd);
    }

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#removeFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void removeFileDescriptor(FileDescriptor fd)
    {
        filedescs.remove(fd);
        fd.setParentAsset(null);
    }

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#getFileDescriptors()
     */
    public List getFileDescriptors()
    {
        return Collections.unmodifiableList(filedescs);
    }


	
	/** (non-Javadoc)
     * @see kobold.client.plam.model.IGXLExport#getGXLAttributes()
     */
    public Map getGXLAttributes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see kobold.common.model.IGXLExport#getGXLChildren()
     */
    public List getGXLChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see kobold.common.model.IGXLExport#getGXLType()
     */
    public String getGXLType()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	public IPath getLocalPath() {
		//FIXME: calc localpath
		return null;
	}
	
	//TODO
	public boolean clear ()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see kobold.client.plam.model.IFileDescriptorContainer#setLocalPath(java.lang.String)
	 */
	public IPath setLocalPath(String localPath) {
		// TODO Auto-generated method stub
		return null;
	}

}

