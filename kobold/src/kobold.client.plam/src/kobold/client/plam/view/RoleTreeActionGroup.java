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
 * $Id: RoleTreeActionGroup.java,v 1.19 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.view;

import org.apache.log4j.Logger;

import java.util.Iterator;

import kobold.client.plam.KoboldConstants;
import kobold.client.plam.action.ConfigureAssetAction;
import kobold.client.plam.action.RefreshFileDescriptorsAction;
import kobold.client.plam.action.SuggestFileAction;
import kobold.client.plam.controller.roletree.OpenFileAction;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider.ArchitectureItem;
import kobold.client.plam.editor.ArchitectureEditorInput;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.product.ProductComponent;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.DeleteResourceAction;
import org.eclipse.ui.actions.NewWizardMenu;
import org.eclipse.ui.part.DrillDownAdapter;


/**
 * Provides all actions used in the RoleTree.
 * @author Tammo
 */
public class RoleTreeActionGroup extends ActionGroup
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(RoleTreeActionGroup.class);

    public static final String GROUP_NEW = "roletree.group.new"; //$NON-NLS-1$
    
    private IWorkbenchWindow window;
    private RoleTreeViewPart part;
    private DrillDownAdapter drillDownAdapter;
    private RefreshFileDescriptorsAction refreshFDAction;
    private ConfigureAssetAction configureAssetAction;
    private DeleteResourceAction deleteAction;
    private SuggestFileAction suggestFileAction;
    private OpenFileAction openFileAction;
   
    
    public RoleTreeActionGroup(RoleTreeViewPart part) 
    {
		this.part = part;
        this.window = part.getSite().getWorkbenchWindow();
		
		refreshFDAction = new RefreshFileDescriptorsAction(part.getSite().getShell());
		configureAssetAction = new ConfigureAssetAction(part.getSite().getShell());
		
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		deleteAction = new DeleteResourceAction(part.getSite().getShell());
		deleteAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		deleteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		
		suggestFileAction = new SuggestFileAction(part.getSite().getShell());
		
		openFileAction = new OpenFileAction(part.getSite().getPage());
    }
    
    
    public void dispose()
    {
        super.dispose();
    }
    
    public void fillActionBars(IActionBars actionBars)
    {
        super.fillActionBars(actionBars);
        actionBars.getToolBarManager().add(refreshFDAction);
    }
    
    public void fillContextMenu(IMenuManager manager)
    {
		manager.add(new Separator(GROUP_NEW));
        super.fillContextMenu(manager);
		manager.add(new Separator());
		
		ISelection selection= getContext().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel= (IStructuredSelection) selection;
			if (sel.size() <= 1) {
				IMenuManager newMenu= new MenuManager("New..."); //$NON-NLS-1$
				manager.appendToGroup(GROUP_NEW, newMenu);
				new NewWizardMenu(newMenu, window, false);
			}
		}		

        Iterator it = ((StructuredSelection)getContext().getSelection()).iterator();
        int i = 0, a = 0, p = 0;
        while (it.hasNext()) {
            Object sel = it.next();
            i++;
            if (sel instanceof FileDescriptor) {
                a++;
            }
            if (sel instanceof IProject) {
                p++;
            }
        }
        if (a == i || p == i) {
            manager.add(deleteAction);    
        }
		
		manager.add(new Separator());
		manager.add(refreshFDAction);
		manager.add(new Separator());
		manager.add(configureAssetAction);

		IStructuredSelection sel= (IStructuredSelection) selection;
		if (sel.size() <= 1 && sel.getFirstElement() instanceof ProductComponent) {
		    manager.add(suggestFileAction);
		}
		
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions"));

    }
    
    public ActionContext getContext()
    {
        return super.getContext();
    }
    
    public void setContext(ActionContext context)
    {
        super.setContext(context);
    }
    
    public void updateActionBars()
    {
        super.updateActionBars();
    }

    public void handleSelectionChanged(SelectionChangedEvent event)
    {
        refreshFDAction.handleSelectionChanged(event);
        configureAssetAction.handleSelectionChanged(event);
        manageDeleteAction(event);
        deleteAction.selectionChanged(((IStructuredSelection)event.getSelection()));
        suggestFileAction.handleSelectionChanged(event);
        openFileAction.selectionChanged(event);
    }
    
    private void manageDeleteAction(SelectionChangedEvent event)
    {
    }
    
    public void handleDoubleClick(DoubleClickEvent event)
    {
		TreeViewer viewer = (TreeViewer)part.getViewer();
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
				logger.error("handleDoubleClick(DoubleClickEvent)", e);
		    }
		} else if (obj instanceof AbstractAsset && configureAssetAction.isEnabled()
		         && !(obj instanceof FileDescriptor)) { 
		    configureAssetAction.run();
    	} 
		else if(obj instanceof FileDescriptor && !((FileDescriptor)obj).isDirectory()) {			
				openFileAction.run();				
		} else {
		    viewer.setExpandedState(obj, !viewer.getExpandedState(obj));
		}
		
    }

}
