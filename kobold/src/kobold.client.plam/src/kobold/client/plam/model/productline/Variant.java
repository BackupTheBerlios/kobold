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
 * $Id: Variant.java,v 1.10 2004/07/25 21:26:34 garbeam Exp $
 *
 */

package kobold.client.plam.model.productline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IGXLExport;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.Release;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * @author garbeam
 * @author vanto
 */
public class Variant extends AbstractAsset 
					 implements IGXLExport, IComponentContainer,
					 			IReleaseContainer, IFileDescriptorContainer {

	private List components = new ArrayList();
	private List releases = new ArrayList();
	private List filedescs = new ArrayList();
	
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#variant";;
	
	/**
	 * Basic constructor.
	 * @param productName
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
	 * @return
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
	 * @param version contains the new version
	 */
	public void addRelease(Release release, int index) {
		if (index >= 0) {
			releases.add(index, release);
		} else {
			releases.add(release);
		}

		release.setParent(this);
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
	 * @return
	 */
	public List getReleases()
	{
	    return Collections.unmodifiableList(releases);
	}

    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#addFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void addFileDescriptor(FileDescriptor fd)
    {
        filedescs.add(fd);
        fd.setParentAsset(this);
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
		System.out.println ("fd "+name + " not found!");
		return null;
	}



	public List getGXLChildren() {
		List children = new ArrayList();
		children.addAll(components);
		if (releases.size() > 0) {
		  List files = (List) releases.get(releases.size()-1);
		  children.addAll(files);
		}
		return children;
	}
	
	public Map getGXLAttributes() {
		return null;
	}
	
	public String getGXLType() {
		return GXL_TYPE;
	}

	/**
	 * Returns path structure of this variant.
	 * Used by getLocalPath and getRemoteRepository methods.
	 * Note that local path and remote repository path structures
	 * have to be correlant.
	 */
	private String myPath() {
		AbstractRootAsset root = getRoot();
		AbstractAsset asset = this;
		String path = "";
		while (asset != root) {
			path = asset.getName() + "/" + path;
			asset = asset.getParent();
	    }
		return path;
	}
	
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	public IResource getLocalPath() {
		
		AbstractRootAsset root = getRoot();
		IProject project = root.getProject().getIProject();
		return project.getFolder(root.getProject().getPath().toString() + myPath());
	}
  
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getRemoteRepository()
	 */
	public RepositoryDescriptor getRemoteRepository() {
		Productline productline = (Productline)getRoot();
		RepositoryDescriptor repositoryDescriptor =
			new RepositoryDescriptor(productline.getRepositoryDescriptor().serialize());
		repositoryDescriptor.setPath(repositoryDescriptor.getPath() + myPath());
		return repositoryDescriptor;
	}
}
