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
 * $Id: RoleTreeContentProvider.java,v 1.17 2004/06/28 22:35:25 vanto Exp $
 *
 */
package kobold.client.plam.controller.roletree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import kobold.client.plam.KoboldProjectNature;
import kobold.common.model.AbstractRootAsset;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Productline;
import kobold.common.model.productline.Variant;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * RoleTreeContentProvider
 *
 * Provides the mapping from the architecture model to the tree structure
 * displayed by the role tree.
 * @author Tammo van Lessen
 */
public class RoleTreeContentProvider implements IStructuredContentProvider, 
					ITreeContentProvider, IResourceChangeListener, PropertyChangeListener 
{
	private static Object[] EMPTY_ARRAY = new Object[0];
	private IWorkspace input;
	protected TreeViewer viewer;
	private List listenRootAssets = new LinkedList();
	
	
    private IProject[] getKoboldProjects() 
    {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		Set kobolds = new HashSet();
		for (int i = 0; i < projects.length; i++) {
			IProject p = projects[i];
			boolean isKoboldProject = false;
			try {
				isKoboldProject = p.hasNature(KoboldProjectNature.NATURE_ID);
				KoboldProjectNature kpn = (KoboldProjectNature)p.getNature(KoboldProjectNature.NATURE_ID);

				if (isKoboldProject) {
					kobolds.add(p);
					
					// listen to model changes.
					Productline pl = kpn.getPLAMProject().getProductline();
					if (pl != null && !listenRootAssets.contains(pl)) {
					    listenRootAssets.add(pl);
					    pl.addModelChangeListener(this);
					}
				}

			} catch (CoreException e) {
				// p ist not open or doesnt exist
			} 
			
		}
		
		IProject[] result = new IProject[kobolds.size()];
		kobolds.toArray(result);

		return result;
    }
    
    /**
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object parent) 
    {
		if (parent.equals(ResourcesPlugin.getWorkspace())) {
			return getKoboldProjects();
		}
		return getChildren(parent);
    }

    public void dispose() {
		if (input != null) {
			input.removeResourceChangeListener(this);
			input = null;
		}
		
		// remove model change listener from productlines.
		Iterator it = listenRootAssets.iterator();
		while (it.hasNext()) {
		    ((Productline)it.next()).removeModelChangeListener(this);
		}
}

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (this.input != null) {
			this.input.removeResourceChangeListener(this);
		}

		this.input = (IWorkspace) newInput;

		if (this.input != null) {
			this.input.addResourceChangeListener(this, 
					IResourceChangeEvent.POST_CHANGE);
		}

		this.viewer = (TreeViewer) viewer;
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
		
    	if (parentElement instanceof IProject) {
    	 	try {
                KoboldProjectNature nature = (KoboldProjectNature)((IProject)parentElement).getNature(KoboldProjectNature.NATURE_ID);
                return new Productline[] {nature.getPLAMProject().getProductline()};
            } catch (CoreException e) {
                e.printStackTrace();
            }
    	    return new Object[0];
    	    
    	} else if(parentElement instanceof Productline) {
    	    Productline pl = (Productline)parentElement;
    	    return new Object[] {new ArchitectureItem(pl),
    	            			 new TreeContainer("users", pl), 
    	            			 new TreeContainer("assets", pl),
    	            			 new TreeContainer("products", pl)};
    	 	
    	} else if ((parentElement instanceof TreeContainer) 
    	        && ((TreeContainer)parentElement).id.equals("assets")) {
    	    return ((TreeContainer)parentElement).productline.getComponents().toArray();
    	
    	} else if (parentElement instanceof Component) {
    	    return ((Component)parentElement).getVariants().toArray();
    	
    	} else if (parentElement instanceof Variant) {
    	    List list = new ArrayList();
    	    list.addAll(((Variant)parentElement).getComponents());
    	    list.addAll(((Variant)parentElement).getReleases());
    	    return list.toArray();

    	} else if ((parentElement instanceof TreeContainer) 
    	        && ((TreeContainer)parentElement).id.equals("users")) {

    	    return new Object[0];
    	}
    	     
        return new Object[0];
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
    /*
     * if (element instanceof TreeObject) {
		  return ((TreeObject)element).getParent();
		  }
	*/	  
        return null;
    }

    /**
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
    	
    	return getChildren(element).length > 0;

    }

	public void resourceChanged(IResourceChangeEvent event) {
		viewer.getControl().getDisplay().syncExec(new Runnable() {		
			public void run() {		
				viewer.refresh();
			}
		});
	}

	public class TreeContainer
	{
	    private Productline productline;
	    private String id;
	    
	    TreeContainer(String id, Productline pl) {
	        this.productline = pl;
	        this.id = id;
	    }
	    
	    public String getName()
	    {
	        if (id.equals("users"))
	            return "Users";
	        else if (id.equals("assets"))
	            return "Core-Assets";
	        if (id.equals("products"))
	            return "Products";
	        else return "unkown id";
	    }
	}
	
	public class ArchitectureItem 
	{
	    private AbstractRootAsset asset;

        ArchitectureItem(AbstractRootAsset asset)
	    {
	        this.asset = asset;
	    }
        
        public AbstractRootAsset getAsset()
        {
            return asset;
        }
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
	    viewer.getControl().getDisplay().syncExec(new Runnable() {		
			public void run() {		
				viewer.refresh();
				// FIXME: restore expanded nodes.
				//viewer.expandAll();
			}
		});
	}
}
