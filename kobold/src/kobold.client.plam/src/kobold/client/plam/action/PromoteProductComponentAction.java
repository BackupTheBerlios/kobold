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
 * $Id: PromoteProductComponentAction.java,v 1.1 2004/10/12 00:01:11 vanto Exp $
 *
 */
package kobold.client.plam.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.roletree.RoleTreeLabelProvider;
import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.model.IProductComponentContainer;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;


/**
 * @author Tammo
 */
public class PromoteProductComponentAction implements IActionDelegate
{

    private ProductComponent selection;
    
    public void run(IAction action)
    {
        Shell shell = Display.getDefault().getActiveShell();
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, 
            new RoleTreeLabelProvider(), new ContainerContentProvider());
        dialog.setInput(selection.getRoot());
        dialog.setAllowMultiple(false);
        dialog.setTitle("Promote Productcomponent to Productline");
        dialog.setMessage("Choose the target component. The new core asset will be created as a variant of this component.");
        dialog.setBlockOnOpen(true);
        dialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection)
            {
        		String pluginId = KoboldPLAMPlugin.getDefault().getBundle().getSymbolicName();
    			
    			if (selection.length != 1) {
    				return new Status(IStatus.ERROR, pluginId, IStatus.ERROR, "You have to select exactly one item.", null);
    			}
                
    			if (!(selection[0] instanceof IVariantContainer)) {
    			    return new Status(IStatus.ERROR, pluginId, IStatus.ERROR, "You have to select a component.", null);
    			}
                
    			return new Status(IStatus.OK, pluginId, IStatus.OK, "", null);
            }
        });
        
        if (dialog.open() == Dialog.OK) {
            IVariantContainer vc = (IVariantContainer)dialog.getFirstResult();
            
            Variant v = new Variant(selection.getName() + "(Global)");
            AssetConfigurationDialog dlg = new AssetConfigurationDialog(shell, v);
            
            vc.addVariant(v);
            
            if (dlg.open() == Dialog.CANCEL) {
                vc.removeVariant(v);
            } else {
                Release r = new Release("initial");
                v.addRelease(r);
                // FIXME: move files from selection to v and import. tag it with r.
                RelatedComponent rc = new RelatedComponent(v, r);
                ((Product)selection.getRoot()).addProductComponent(rc);
                
                // remove edges
                List edges = new ArrayList(selection.getRoot().getEdgeContainer().getEdges(selection));
                Iterator it = edges.iterator();
                while (it.hasNext()) {
                    selection.getRoot().getEdgeContainer().removeEdge((Edge)it.next());
                }
                
                ((IProductComponentContainer)selection.getParent()).removeProductComponent(selection);
                
                // FIXME: recreate edges from productline?
                
            }
            
        }
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        Object o = ((IStructuredSelection)selection).getFirstElement();
        if (o instanceof ProductComponent) {
            this.selection = (ProductComponent)o;
            action.setEnabled(true);
        } else {
            this.selection = null;
            action.setEnabled(false);
        }
    }
    
    
    private class ContainerContentProvider implements ITreeContentProvider
    {
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement)
        {
            if (parentElement instanceof Productline) {
                return ((Productline)parentElement).getComponents().toArray();
            }
     
            if (parentElement instanceof Component) {
                return ((Component)parentElement).getVariants().toArray();
            }

            if (parentElement instanceof Variant) {
                return ((Variant)parentElement).getComponents().toArray();
            }

            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object element)
        {
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object element)
        {
            Object[] o = getChildren(element);
            return (o == null) ? false : o.length > 0;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement)
        {
            if (inputElement instanceof Product) {
                return new Object[] {((Product)inputElement).getProductline()};
            }
            return getChildren(inputElement);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose()
        {
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
        }
        
    }

}
