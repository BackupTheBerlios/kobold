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
 * $Id: RoleTreeContentProvider.java,v 1.7 2004/05/16 13:35:25 vanto Exp $
 *
 */
package kobold.client.plam.controller.roletree;

import java.util.HashSet;
import java.util.Set;

import kobold.client.plam.KoboldProjectNature;
import kobold.common.data.RoleP;
import kobold.common.data.RolePE;
import kobold.common.data.RolePLE;
import kobold.common.data.User;

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
					ITreeContentProvider, IResourceChangeListener 
{
	private static Object[] EMPTY_ARRAY = new Object[0];
	private IWorkspace input;
	protected TreeViewer viewer;
	
	
	
    private IProject[] getKoboldProjects() 
    {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		Set kobolds = new HashSet();
		for (int i = 0; i < projects.length; i++) {
			IProject p = projects[i];
			boolean isKoboldProject = false;
			try {
				isKoboldProject = p.hasNature(KoboldProjectNature.NATURE_ID);
			} catch (CoreException e) {
				// p ist not open or doesnt exist
			} 
			
			if (isKoboldProject) {
				kobolds.add(p);
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
    		User testUser = new User();
    		testUser.setUserName("vanto");
    		testUser.setRealName("Tammo van Lessen");
    		testUser.addRole(new RolePLE("test"));
    		return new User[] {testUser};
    	} else if(parentElement instanceof User) {
    		User aUser = (User)parentElement;
    		return aUser.getRoles().toArray();
    	}
    	else if(parentElement instanceof RoleP) {
    		RoleP programmer = (RoleP)parentElement;
    		return new Object[] {programmer.getProductName()};
    	}
    	else if(parentElement instanceof RolePE) {
    		RolePE productEngineer = (RolePE)parentElement;
    		return new Object[] {productEngineer.getProductName()};
    	}
    	else if(parentElement instanceof RolePLE) {
    		RolePLE productLineEngineer = (RolePLE)parentElement;
    		return new Object[] {productLineEngineer.getProductlineName()};
    	}
        return null;
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
				System.out.println("x");
				viewer.refresh();
			}
		});
	}


	/*public class ItemContainer {
		private Object treeParent;
		private Object item;
		private IProject project;
		
		public ItemContainer(Object treeParent, Object item)
		{
			this.treeParent = treeParent; 
			this.item = item;
		}
		
		public Object getParent()
		{
			return treeParent;
		}

		public Object getItem() {
			return item;	
		}
		
		public Object[] getChildren()
		{
			if (treeParent instanceof IProject) {
				return new String[] {"A", "B" };
			} else if(treeParent instanceof User) {
				User aUser = (User)treeParent;
				return aUser.getRoles().toArray();
			}
			else if(treeParent instanceof RoleP) {
				RoleP programmer = (RoleP)treeParent;
				return programmer.getProducts().toArray();
			}
			else if(treeParent instanceof RolePE) {
				RolePE productEngineer = (RolePE)treeParent;
				return productEngineer.getProducts().toArray();
			}
			else if(treeParent instanceof RolePLE) {
				RolePLE productLineEngineer = (RolePLE)treeParent;
				return productLineEngineer.getProductLines().toArray();
			}
			return null;
		}
		
		public boolean hasChildren()
		{
			return (getChildren().length != 0); 
		}
		
		public IProject getProject()
		{
			if (project == null && treeParent != null && treeParent instanceof ItemContainer) {
				project = ((ItemContainer)treeParent).getProject();
			}
			
			return project;		
		}
	}*/
}
