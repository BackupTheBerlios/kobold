/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * $Id: FileDescriptor.java,v 1.21 2004/09/01 02:58:22 vanto Exp $
 *
 */
package kobold.client.plam.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.edges.INode;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.productline.Variant;
import kobold.common.data.User;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Base class for locating working copies.
 * This descriptor is used for files and for directories.
 * It may provide access to special meta information.
 *
 * @author garbeam
 */
public class FileDescriptor implements IFileDescriptorContainer, 
							INode, IGXLExport, IAdaptable {

    private List children = new ArrayList();
    private IFileDescriptorContainer parentAsset;
    private FileDescriptor parent;
    private String filename;
    private String revision = "";
    private User author;
    private Date lastChange;
    private boolean isBinary = false;
    private boolean isDirectory = false;
    
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    public FileDescriptor() {
    }

    
    /**
     * @param author The author to set.
     */
    public void setAuthor(User author)
    {
        this.author = author;
    }
    
    /**
     * @return Returns the author.
     */
    public User getAuthor()
    {
        return author;
    }
    
    
    /**
     * @param isDirectory The isDirectory to set.
     */
    public void setDirectory(boolean isDirectory)
    {
        this.isDirectory = isDirectory;
    }
    
    /**
     * @return Returns the isDirectory.
     */
    public boolean isDirectory()
    {
        return isDirectory;
    }
    
    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    
    /**
     * @return Returns the filename.
     */
    public String getFilename()
    {
        return filename;
    }
    
    /**
     * @param parent The parent to set.
     */
    public void setParent(FileDescriptor parent)
    {
        this.parent = parent;
    }
    
    /**
     * @return Returns the parent.
     */
    public FileDescriptor getParent()
    {
        return parent;
    }
    
    /**
     * Sets the parent of asset of this file descriptor. This must be a
     * Variant or a SpecificComponent.
     * 
     * @param parentAsset The parentAsset to set.
     */
    public void setParentAsset(IFileDescriptorContainer parentAsset)
    {
        if (!((parentAsset instanceof Variant)
               || (parentAsset instanceof ProductComponent))) {
            
            throw new IllegalArgumentException("parent must be an instance of Variant or ProductComponent");
        }
        this.parentAsset = parentAsset;

        // set parent asset for all children too;
        Iterator it = children.iterator();
        while (it.hasNext()) {
            ((FileDescriptor)it.next()).setParentAsset(parentAsset);
        }
    }
    
    /**
     * @return Returns the parentAsset.
     */
    public IFileDescriptorContainer getParentAsset()
    {
        return parentAsset;
    }
    
    /**
     * @param revision The revision to set.
     */
    public void setRevision(String revision)
    {
        this.revision = revision;
    }
    
    /**
     * @return Returns the revision.
     */
    public String getRevision()
    {
        return revision;
    }


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#addFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void addFileDescriptor(FileDescriptor fd)
    {
        children.add(fd);
        fd.setParent(this);
        if (parentAsset != null) {
            fd.setParentAsset(parentAsset);
        }
    	System.out.println ("fd "+fd.filename + " added!");
    }


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#removeFileDescriptor(kobold.common.io.FileDescriptor)
     */
    public void removeFileDescriptor(FileDescriptor fd)
    {
        children.remove(fd);
        fd.setParent(null);
        fd.setParentAsset(null);
    }


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#getFileDescriptors()
     */
    public List getFileDescriptors()
    {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * returns the fd with the name
     */
    public FileDescriptor getFileDescriptor (String name)
	{
		for (Iterator it = children.iterator(); it.hasNext();) {
			FileDescriptor fd = (FileDescriptor) it.next();
			if (fd.getFilename().equals(name)) {
				return fd;
			}
		}	
		System.out.println ("fd "+name + " not found!");
		return null;
	}

    
	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getLocalPath()
	 */
	public IPath getLocalPath() {
	    if (parent != null){
	       return new Path(getParent().getLocalPath().toString() + filename);
	    } else {
	        return new Path( this.parentAsset.getLocalPath().toString() + filename);
	    }
	}

	/**
	 * @return Returns the isBinary.
	 */
	public boolean isBinary() {
		return isBinary;
	}
	/**
	 * @param isBinary The isBinary to set.
	 */
	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}
	/**
	 * @return Returns the lastChange.
	 */
	public Date getLastChange() {
		return lastChange;
	}
	/**
	 * @param lastChange The lastChange to set.
	 */
	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

    /**
     * @see kobold.client.plam.model.edges.INode#getRoot()
     */
    public AbstractRootAsset getRoot() {
        return this.getParentAsset().getRoot();
    }

	/**
	 * @see kobold.client.plam.model.IFileDescriptorContainer#getRemoteRepository()
	 */
	public RepositoryDescriptor getRemoteRepository() {
		RepositoryDescriptor repositoryDescriptor =
			new RepositoryDescriptor(getParent().getRemoteRepository().serialize());
		repositoryDescriptor.setPath(repositoryDescriptor.getPath() + filename);
		return repositoryDescriptor;
	}


    /**
     * @see kobold.client.plam.model.IFileDescriptorContainer#clear()
     */
    public void clear() {
        children.clear();
    }


    /* (non-Javadoc)
     * @see kobold.client.plam.model.IGXLExport#getGXLChildren()
     */
    public List getGXLChildren() {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see kobold.client.plam.model.IGXLExport#getGXLType()
     */
    public String getGXLType() {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see kobold.client.plam.model.IGXLExport#getMaintainers()
     */
    public List getMaintainers() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
        if (adapter == IResource.class) {
            return ModelStorage.getResourceForFD(this);
        }
        return null;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}

	protected final void fireStructureChange(String prop, Object child){
		listeners.firePropertyChange(prop, null, child);

		AbstractRootAsset root = getRoot();
		if (root != null) {
		    root.fireModelChange();
		    if (root.getParent() instanceof AbstractRootAsset) {
		        ((AbstractRootAsset)root.getParent()).fireModelChange();
		    }
		}

	}

}
