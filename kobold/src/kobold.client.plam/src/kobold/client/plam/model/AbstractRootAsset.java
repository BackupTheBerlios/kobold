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
 * $Id: AbstractRootAsset.java,v 1.20 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.model;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.productline.Productline;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.Element;


/**
 * This is the abstract root for Productline.
 * 
 * @author Tammo
 */
public abstract class AbstractRootAsset extends AbstractMaintainedAsset
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AbstractRootAsset.class);

    private KoboldProject project;
    protected Map releasePool = new HashMap();
    protected transient PropertyChangeSupport changeListeners = new PropertyChangeSupport(this); 

    private List metaNodes = new ArrayList();
    private EdgeContainer edgeContainer = new EdgeContainer(this);
    
	private RepositoryDescriptor repositoryDescriptor = null;
    
    /**
     * Default constructor. 
     */
    public AbstractRootAsset()
    {
        super();
    }

    public AbstractRootAsset(String name)
    {
        super(name);
    }

    /**
     * @param project The project to set.
     */
    public void setProject(KoboldProject project)
    {
        this.project = project;
    }
    
    
    /**
     * @return Returns the project.
     */
    public KoboldProject getKoboldProject()
    {
        return project;
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
        this.addToPool(mn);
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
    
	public RepositoryDescriptor getRepositoryDescriptor() {
		return repositoryDescriptor;
	}

	/**
	 * @param descriptor
	 */
	public void setRepositoryDescriptor(RepositoryDescriptor descriptor) {
		repositoryDescriptor = descriptor;
	}
	
	public abstract Productline getProductline();
    

    public void deserialize(Element element)
    {
	    super.deserialize(element);
	    
		Iterator it = element.element("meta-nodes").elementIterator(AbstractAsset.META_NODE);
		while (it.hasNext()) {
		    Element mEl = (Element)it.next();
		    addMetaNode(new MetaNode(mEl));
		}
		
		
    }
    
    public Element serialize()
    {
		Element root = super.serialize();
		Element metaEl = root.addElement("meta-nodes");
		for (Iterator it = metaNodes.iterator(); it.hasNext();) {
			MetaNode node = (MetaNode) it.next();
			metaEl.add(node.serialize());
		}
		
		//root.add(edgeContainer.serialize());
		// must not desirialize here. Other Assets must desirialize first. 
		
		return root;
    }
    
}
