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
 * $Id: RoleTreeViewPart.java,v 1.16 2004/08/04 17:52:58 vanto Exp $
 *
 */
package kobold.client.plam.view;

import kobold.client.plam.KoboldConstants;
import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider;
import kobold.client.plam.controller.roletree.RoleTreeLabelProvider;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider.ArchitectureItem;
import kobold.client.plam.editor.ArchitectureEditorInput;
import kobold.client.plam.editor.dialog.EditUserManager;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IFileDescriptorContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
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
public class RoleTreeViewPart extends ViewPart implements ISelectionChangedListener
{
	private static final Log logger = LogFactory.getLog(RoleTreeViewPart.class);
	
	static final String TAG_SELECTION= "selection"; //$NON-NLS-1$
	static final String TAG_EXPANDED= "expanded"; //$NON-NLS-1$
	static final String TAG_ELEMENT= "element"; //$NON-NLS-1$
	static final String TAG_PATH= "path"; //$NON-NLS-1$
	
	private TreeViewer viewer;
	private Shell shell = new Shell();
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	
    /**
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		
		viewer.setContentProvider(new RoleTreeContentProvider());
		viewer.setLabelProvider(new RoleTreeLabelProvider());
		viewer.addSelectionChangedListener(this);

		//viewer.setSorter(new NameSorter());
		viewer.setInput(ResourcesPlugin.getWorkspace());
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus() {
		viewer.getControl().setFocus();
    }

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RoleTreeViewPart.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions"));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				EditUserManager eum = new EditUserManager(shell);
				eum.open();
			}
		};
		action1.setText("Create New User");
		action1.setToolTipText("Creates a New User for the PL");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				//FIXME
				if (obj instanceof IFileDescriptorContainer) {
				    ((IFileDescriptorContainer)obj).getRoot().getKoboldProject().refreshResources((IFileDescriptorContainer)obj);
				}
			}

		};
		action2.setText("Refresh");
		action2.setToolTipText("Refresh Resources");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJS_TASK_TSK));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				if (obj instanceof ArchitectureItem) {
				    try {
				        final IWorkbenchWindow activeWorkbenchWindow =
				            PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				            IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
				            
				            ArchitectureItem ai = (ArchitectureItem)obj; 
				            final ArchitectureEditorInput editorInput =
				                new ArchitectureEditorInput(ai.getAsset());
				            page.openEditor(editorInput, KoboldConstants.ID_ARCH_EDITOR, true);
				    } catch (PartInitException e) {
				        e.printStackTrace();
				    }
				} else
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
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
		
		if (p != null && p != KoboldPLAMPlugin.getCurrentProject()) {
			try {
                KoboldPLAMPlugin.getDefault().setCurrentProject((IProject)p);
    			viewer.collapseAll();
    			viewer.expandToLevel(p, AbstractTreeViewer.ALL_LEVELS);
            } catch (CoreException e) {}
		}
	}
}
