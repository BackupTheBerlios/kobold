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
 * $Id: AbstractRootAsset.java,v 1.8 2004/08/02 09:23:04 vanto Exp $
 *
 */
package kobold.client.plam.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kobold.client.plam.PLAMProject;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.common.io.RepositoryDescriptor;


/**
 * This is the abstract root for Productline.
 * 
 * @author Tammo
 */
public abstract class AbstractRootAsset extends AbstractMaintainedAsset
{
    private PLAMProject project;
    private Map userPool = new HashMap();
    protected Map releasePool = new HashMap();
    protected transient PropertyChangeSupport changeListeners = new PropertyChangeSupport(this); 
    protected transient List vcmListeners = new LinkedList();
    private List metaNodes = new ArrayList();
    private EdgeContainer edgeContainer = new EdgeContainer(this);
    
    // TODO: set the repository descriptor on instantiation!
	private RepositoryDescriptor repositoryDescriptor = null;
    
    /**
     * Default constructor. 
     */
    public AbstractRootAsset()
    {
        super();
    }

    /**
     * Name constructor. 
     */
    public AbstractRootAsset(String name)
    {
        super(name);
    }

    
    /**
     * @param project The project to set.
     */
    public void setProject(PLAMProject project)
    {
        this.project = project;
    }
    
    
    /**
     * @return Returns the project.
     */
    public PLAMProject getProject()
    {
        return project;
    }
    
    /**
     * @return Returns the userPool.
     */
    public Map getUserPool()
    {
        return userPool;
    }

    public void addModelChangeListener(PropertyChangeListener l)
	{
		changeListeners.addPropertyChangeListener(l);
	}

	public void removeModelChangeListener(PropertyChangeListener l)
	{
		changeListeners.removePropertyChangeListener(l);
	}
	
	public void fireModelChange() 
	{
	    changeListeners.firePropertyChange("refresh", false, true);
	}
	
    public void addVCMActionListener(IVCMActionListener l)
	{
		vcmListeners.add(l);
	}

	public void removeVCMActionListener(IVCMActionListener l)
	{
		vcmListeners.remove(l);
	}

	public void refreshResources(IFileDescriptorContainer fdCont)
	{
	    for (int i = 0; i < vcmListeners.size(); i++) {
	        ((IVCMActionListener)vcmListeners.get(i)).refreshFiledescriptors(fdCont);
	    }
	}
	
    /**
     * @return Returns the edgeConatainer.
     */
    public EdgeContainer getEdgeContainer()
    {
        return edgeContainer;
    }

    public void addMetaNode(MetaNode mn)
    {
        metaNodes.add(mn);
        mn.setParent(this);
        fireStructureChange(ID_META, mn);
    }
    
    public void removeMetaNode(MetaNode mn)
    {
        mn.setParent(null);
        metaNodes.remove(mn);
        fireStructureChange(ID_META, mn);
    }
    
    public List getMetaNodes()
    {
        return Collections.unmodifiableList(metaNodes);
    }

	/**
	 * @return
	 */
	public RepositoryDescriptor getRepositoryDescriptor() {
		return repositoryDescriptor;
	}

	/**
	 * @param descriptor
	 */
	public void setRepositoryDescriptor(RepositoryDescriptor descriptor) {
		repositoryDescriptor = descriptor;
	}
    

}
