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
 * $Id: AddToProductDialog.java,v 1.5 2004/11/09 12:08:32 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Variant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.actions.WorkspaceAction;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPlugin;


/**
 * Dialog for adding a variant to products
 * @author schneipk
 */
public class AddToProductDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(EditMaintainerDialog.class);
 
    private Release currentRelease;
    private CheckboxTableViewer allProductViewer; 
    private List productPool;
    private Table allProducts;
    
    /**
     * @param parentShell
     */
    public AddToProductDialog(Shell parentShell, Release variant)
    {
        super(parentShell);
        this.currentRelease = variant;
        productPool = currentRelease.getRoot().getProductline().getProducts();
//	    userPool = asset.getRoot().getKoboldProject().getUserPool();
    }

    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Add to Product");
        setTitle("Add Variant to Product");
        setMessage("Adds selected Variant to given Products");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createProductLists(composite);
        return composite;
    }
    
    private void createProductLists(Composite parent) {
        
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setFont(parent.getFont());

		Label label = new Label(panel, SWT.NONE);
		label.setText("Available Products");
		
		allProducts = new Table(panel, SWT.CHECK | SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		allProducts.setLinesVisible(false);
		TableColumn colProductNames = new TableColumn(allProducts, SWT.NONE);
		colProductNames.setText("Product Names");
		TableLayout tableLayout = new TableLayout();
		allProducts.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(100));
		
	    colProductNames.pack();
		
		allProductViewer = new CheckboxTableViewer(allProducts);
		//allUserViewer.getTable().getLayoutData().
		allProductViewer.setLabelProvider(new LabelProvider() {
		    private Image image;
		    public String getText(Object element) {
                Product product = (Product)element;     
                return product.getName();
            }
            
            public Image getImage(Object element) {
                if (image == null) {
        			image = KoboldPLAMPlugin.getImageDescriptor("icons/product.gif").createImage();
        		}
        		return image;
            }
        });
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
		        				   | GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		allProducts.setLayoutData(gd);
	    
		if (productPool != null) {
		    allProductViewer.add(productPool.toArray());
	    }
//	    allProductViewer.setCheckedElements(currentVariant.toArray());
    }
 
    protected void okPressed()
    {
//        asset.getMaintainers().clear();
//        Folder tmpFolder =  Folder.create(true,true, new NullProgressMonitor());
        IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
        
        Object[] checkedElems = allProductViewer.getCheckedElements();
        for (int i = 0; i < checkedElems.length; i++) {
            RelatedComponent tmpRelatedComponent = new RelatedComponent((Variant)currentRelease.getParent(),currentRelease);
            l.addToProduct((Product)checkedElems[i],currentRelease);
//            ModelStorage.serializeProduct(tmpRelatedComponent.getRoot().getProductline(),null);
            ((Product)checkedElems[i]).addRelatedComponent(tmpRelatedComponent);
            ModelStorage.serializeProduct(((Product)tmpRelatedComponent.getRoot()).getProductline(),new NullProgressMonitor());
//            asset.addMaintainer((User)checkedElems[i]);
        }
        super.okPressed();
    }
    
}
