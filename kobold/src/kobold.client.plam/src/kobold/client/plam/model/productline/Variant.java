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
 * $Id: Variant.java,v 1.31 2004/11/05 10:32:32 grosseml Exp $
 *
 */

package kobold.client.plam.model.productline;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;

/**
 * @author garbeam
 * @author vanto
 */
public class Variant extends AbstractAsset 
					 implements IGXLExport, IComponentContainer,
					 			IReleaseContainer, IFileDescriptorContainer,
					 			IAdaptable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Variant.class);

	private List components = new ArrayList();
	private List releases = new ArrayList();
	private List filedescs = new ArrayList();
	
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#variant";;
	
	public Variant()
	{
	    super();
	}
	
	public Variant(String name)
	{
	    super(name);
	}

	/**
	 * DOM constructor.
	 */
	public Variant(AbstractAsset parent, Element element) {
	    super();
	    setParent(parent);
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
		    addComponent(new Component(this, varEl));
		}
		
		it = element.element("releases").elementIterator(AbstractAsset.RELEASE);
		while (it.hasNext()) {
		    Element varEl = (Element)it.next();
		    addRelease(new Release(this, varEl));
		}

		
	}


	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.VARIANT;
	}

	public void addComponent(Component comp)
	{		
	    addComponent(comp, -1);
	}
	
	/**
	 * Adds a component and sets its parent to this.
	 *
	 * @param component contains the new component
	 */
	public void addComponent(Component component, int index) {
		if (index >= 0) {
			components.add(index, component);
		} else {
			components.add(component);
		}

		component.setParent(this);
		addToPool(component);
		fireStructureChange(AbstractAsset.ID_CHILDREN, component);
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
		fireStructureChange(AbstractAsset.ID_CHILDREN, comp);
	}
	
	/**
	 * Returns a unmodifiable list of components.
	 * 
	 */
	public List getComponents()
	{
	    return Collections.unmodifiableList(components);
	}
	
	public void addRelease(Release release)
	{
	    addRelease(release, -1);
	}
	
	/**
	 * Adds a version and sets its parent to this
	 *
	 * @param release contains the new version
	 */
	public void addRelease(Release release, int index) {
		if (index >= 0) {
			releases.add(index, release);
		} else {
			releases.add(release);
		}

		release.setParent(this);
		addToPool(release);
		fireStructureChange(AbstractAsset.ID_CHILDREN, release);
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
		fireStructureChange(AbstractAsset.ID_CHILDREN, release);
	}
	
	/**
	 * Returns a unmodifiable list of releases.
	 * 
	 */
	public List getReleases()
	{
	    return Collections.unmodifiableList(releases);
	}

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#addFileDescriptor(kobold.client.plam.model.FileDescriptor)
     */
    public void addFileDescriptor(FileDescriptor fd)
    {
        filedescs.add(fd);
        fd.setParentAsset(this);
        fireStructureChange(AbstractAsset.ID_FILE_DESCRIPTORS, fd);
    }


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#removeFileDescriptor(kobold.client.plam.model.FileDescriptor)
     */
    public void removeFileDescriptor(FileDescriptor fd)
    {
        filedescs.remove(fd);
        fd.setParentAsset(null);
        fireStructureChange(AbstractAsset.ID_FILE_DESCRIPTORS, fd);
    }


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#getFileDescriptors()
     */
    public List getFileDescriptors()
    {
        return Collections.unmodifiableList(filedescs);
    }
    
    /**
     * returns the fd with the name
     */
    public FileDescriptor getFileDescriptor (String name)
	{
		for (Iterator it = filedescs.iterator(); it.hasNext();) {
			FileDescriptor fd = (FileDescriptor) it.next();
			if (fd.getFilename().equals(name)) {
				return fd;
			}
		}	
		if (logger.isDebugEnabled()) {
			logger.debug("getFileDescriptor(String) - fd " + name
					+ " not found!");
		}
		return null;
	}



	public List getGXLChildren() {
		List children = new ArrayList();
		children.addAll(components);
		children.addAll(releases);
		return children;
	}
	
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
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getRemoteRepository()
	 */
	public RepositoryDescriptor getRemoteRepository() {
	    return ModelStorage.getRepositoryDescriptorForAsset(this);
	}

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#clear()
     */
    public void clear() {
        filedescs.clear();
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
        if (adapter == IResource.class) {
            return ModelStorage.getFolderForAsset(this);
        }
        return null;
    }
    
	public List getChildren()
	{
	    List l = new ArrayList();
	    l.addAll(getComponents());
	    l.addAll(getReleases());
	    return l;
	}

    /**
     * Returns the latest release
     */
    public Release getHead() {
       return (Release) releases.get(releases.size() -1);
    }

}
