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
 * $Id: RoleTreeViewPart.java,v 1.18 2004/08/23 13:00:42 vanto Exp $
 *
 */
package kobold.client.plam.view;

import java.util.ArrayList;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider;
import kobold.client.plam.controller.roletree.RoleTreeLabelProvider;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.productline.Productline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

/**
 * RoleTree
 * 
 * Provides an eclipse tree view containing all product lines, their products 
 * and assets as well as every component and variant in a tree structure.
 * 
 * @author Tammo van Lessen
 */
public class RoleTreeViewPart extends ViewPart implements ISelectionChangedListener,
														  IMenuListener
{
	private static final Log logger = LogFactory.getLog(RoleTreeViewPart.class);
	
	static final String TAG_SELECTION= "selection"; //$NON-NLS-1$
	static final String TAG_EXPANDED= "expanded"; //$NON-NLS-1$
	static final String TAG_ELEMENT= "element"; //$NON-NLS-1$
	static final String TAG_TREEELEMENT= "treeelement"; //$NON-NLS-1$
	static final String TAG_ARCHELEMENT= "architecture"; //$NON-NLS-1$
	static final String TAG_PLID = "plid"; //$NON-NLS-1$
	static final String TAG_PATH= "path"; //$NON-NLS-1$
	private IMemento memento;
	
	private TreeViewer viewer;
	private Shell shell = new Shell();
	private DrillDownAdapter drillDownAdapter;
	private Action doubleClickAction;
	
	private RoleTreeActionGroup actionGroup;
	private RoleTreeContentProvider contentProvider;
	
    /**
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setUseHashlookup(true);
		
		contentProvider = new RoleTreeContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new RoleTreeLabelProvider());
		viewer.addSelectionChangedListener(this);

		//viewer.setSorter(new NameSorter());
		viewer.setInput(ResourcesPlugin.getWorkspace());
		
		drillDownAdapter = new DrillDownAdapter(viewer);
		actionGroup = new RoleTreeActionGroup(this);

	    IActionBars bars = getViewSite().getActionBars();
		actionGroup.fillActionBars(bars);
		drillDownAdapter.addNavigationActions(bars.getMenuManager());

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				actionGroup.handleDoubleClick(event);
			}
		});

		if (memento != null) {
			restoreExpansionState(memento);
			restoreSelectionState(memento);
			logger.debug("restore memento");
		}
		memento = null;

		hookContextMenu();
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus() {
		viewer.getControl().setFocus();
    }

    public TreeViewer getViewer() 
    {
        return viewer;
    }
    
    private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection =
			(IStructuredSelection) event.getSelection();

		IProject p = null;
		Object selected = selection.getFirstElement();
		if (selected instanceof IProject) {
		    p = (IProject)selected;
		} else if (selected instanceof AbstractAsset) {
		    p = ((AbstractAsset)selected).getRoot().getKoboldProject().getProject();
		} else if (selected instanceof RoleTreeContentProvider.TreeContainer) {
		    p = ((RoleTreeContentProvider.TreeContainer)selected).getPL()
		    	.getRoot().getKoboldProject().getProject();
		} else if (selected instanceof RoleTreeContentProvider.ArchitectureItem) {
		    p = ((RoleTreeContentProvider.ArchitectureItem)selected).getAsset()
	    		.getRoot().getKoboldProject().getProject();
		}
		
		if (actionGroup != null) {
		    actionGroup.handleSelectionChanged(event);
		}
		
		if (p != null && p != KoboldPLAMPlugin.getCurrentProject()) {
			try {
                KoboldPLAMPlugin.getDefault().setCurrentProject((IProject)p);
    			//viewer.collapseAll();
    			//viewer.expandToLevel(p, AbstractTreeViewer.ALL_LEVELS);
            } catch (CoreException e) {}
		}
	}

    /**
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager manager)
    {
		actionGroup.setContext(new ActionContext(viewer.getSelection()));
		actionGroup.fillContextMenu(manager);
		actionGroup.setContext(null);
    }
    
    public void dispose()
    {
        actionGroup.dispose();
        super.dispose();
    }
    
    public void init(IViewSite site, IMemento memento) throws PartInitException
    {
        super.init(site, memento);
        this.memento = memento;
    }
    
    public void saveState(IMemento mem)
    {
		// keep old state if viewer has not been initialized.
        if (viewer == null) {
			if (memento != null)
				mem.putMemento(memento);
			return;
		}
        logger.debug("store memento");
        saveExpansionState(mem);
        saveSelectionState(mem);
    }
    
    protected void saveSelectionState(IMemento memento) {
		Object elements[] = ((IStructuredSelection) viewer.getSelection()).toArray();
		if (elements.length > 0) {
			IMemento m = memento.createChild(TAG_SELECTION);
			for (int i = 0; i < elements.length; i++) {
				Object o = elements[i];
				if (o instanceof AbstractAsset) {
					IMemento elementMem = m.createChild(TAG_ELEMENT);
				    elementMem.putString(TAG_PATH, ((AbstractAsset) elements[i]).getId());
				} else if (o instanceof RoleTreeContentProvider.TreeContainer) {
					IMemento elementMem = m.createChild(TAG_TREEELEMENT);
				    elementMem.putString(TAG_PATH, ((RoleTreeContentProvider.TreeContainer) elements[i]).getId());
				    elementMem.putString(TAG_PLID, ((RoleTreeContentProvider.TreeContainer) elements[i]).getPL().getId());
				} else if (o instanceof RoleTreeContentProvider.ArchitectureItem) {
					IMemento elementMem = m.createChild(TAG_ARCHELEMENT);
				}
			}
		}
	}

	protected void saveExpansionState(IMemento memento) {
		Object expandedElements[]= viewer.getVisibleExpandedElements();
		if (expandedElements.length > 0) {
			IMemento m = memento.createChild(TAG_EXPANDED);
			for (int i = 0; i < expandedElements.length; i++) {

				// we can only persist AbstractAssets and TreeContainer for now
				Object o = expandedElements[i];
				if (o instanceof AbstractAsset) {
					IMemento elementMem = m.createChild(TAG_ELEMENT);
				    elementMem.putString(TAG_PATH, ((AbstractAsset) expandedElements[i]).getId());
				} else if (o instanceof RoleTreeContentProvider.TreeContainer) {
					IMemento elementMem = m.createChild(TAG_TREEELEMENT);
				    elementMem.putString(TAG_PATH, ((RoleTreeContentProvider.TreeContainer) expandedElements[i]).getId());
				    elementMem.putString(TAG_PLID, ((RoleTreeContentProvider.TreeContainer) expandedElements[i]).getPL().getId());
				}
			}
		}
	}
	
	protected void restoreSelectionState(IMemento memento) {
		IMemento childMem;
		childMem= memento.getChild(TAG_SELECTION);
		if (childMem != null) {
			ArrayList list= new ArrayList();
			IMemento[] elementMem= childMem.getChildren(TAG_ELEMENT);
			for (int i= 0; i < elementMem.length; i++) {
			    logger.debug("Select: " + elementMem[i].getString(TAG_PATH));
			    IProject[] projects = (IProject[])contentProvider.getElements(ResourcesPlugin.getWorkspace());
			    for (int k = 0; k < projects.length; k++) {
			        try {
                        KoboldProject kpn = (KoboldProject)projects[k].getNature(KoboldProject.NATURE_ID);
                        AbstractAsset asset = kpn.getProductline().getAssetById(elementMem[i].getString(TAG_PATH));
                        logger.debug("Select asset: " + asset);
                        if (asset != null) {
                            list.add(asset);
                        }
                    } catch (CoreException e) {
                        logger.debug("Strange error", e);
                    }
			    }
			}
			viewer.setSelection(new StructuredSelection(list));
		}
	}

	protected void restoreExpansionState(IMemento memento) {
		IMemento m = memento.getChild(TAG_EXPANDED);
		if (m != null) {
			ArrayList elements = new ArrayList();
			IMemento[] elementMem = m.getChildren(TAG_ELEMENT);
			for (int i = 0; i < elementMem.length; i++) {
			    logger.debug("Expand: " + elementMem[i].getString(TAG_PATH));
			    IProject[] projects = (IProject[])contentProvider.getElements(ResourcesPlugin.getWorkspace());
			    for (int k = 0; k < projects.length; k++) {
			        try {
                        KoboldProject kpn = (KoboldProject)projects[k].getNature(KoboldProject.NATURE_ID);
                        AbstractAsset asset = kpn.getProductline().getAssetById(elementMem[i].getString(TAG_PATH));
                        logger.debug("Expand asset: " + asset);
                        if (asset != null) {
                            elements.add(asset);
                        }
                    } catch (CoreException e) {
                        logger.debug("Strange error", e);
                    }
			    }
			}

			elementMem = m.getChildren(TAG_TREEELEMENT);
			for (int i = 0; i < elementMem.length; i++) {
			    logger.debug("Expand: " + elementMem[i].getString(TAG_PATH));
			    IProject[] projects = (IProject[])contentProvider.getElements(ResourcesPlugin.getWorkspace());
			    for (int k = 0; k < projects.length; k++) {
			        try {
                        KoboldProject kpn = (KoboldProject)projects[k].getNature(KoboldProject.NATURE_ID);
                        Productline pl = kpn.getProductline();
        			    if (pl.getId().equals(elementMem[i].getString(TAG_PLID))) {
        			        RoleTreeContentProvider.TreeContainer tc = contentProvider.new TreeContainer(elementMem[i].getString(TAG_PATH), pl);
        			        logger.debug("Expand treecontainer: " + tc);
        			        if (tc != null) {
        			            elements.add(tc);
        			        }
        			    }
                    } catch (CoreException e) {
                        logger.debug("Strange error", e);
                    }
			    }			    
			}

			viewer.setExpandedElements(elements.toArray());
		}
	}
}
