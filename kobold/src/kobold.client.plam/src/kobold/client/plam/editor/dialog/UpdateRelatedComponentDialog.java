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
 * $Id: UpdateRelatedComponentDialog.java,v 1.2 2004/10/21 21:32:41 martinplies Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IProductComponentContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Variant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
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
import org.eclipse.swt.widgets.TableItem;


/**
 * @author pliesmn
 *
 */
public class UpdateRelatedComponentDialog extends TitleAreaDialog {
    public static final Log logger = LogFactory.getLog(EditMaintainerDialog.class);
    private final Image image = KoboldPLAMPlugin.getImageDescriptor("icons/relcomp.gif").createImage();
       private IProductComponentContainer productCompCont;
       private CheckboxTableViewer tableViewer; 
       private List RelCompPool;
       private Table table;
       private HashMap messages = new HashMap();
    /**
     * @param parentShell
     */
    public UpdateRelatedComponentDialog(IProductComponentContainer ppc, Shell parentShell) {
        super(parentShell);
        productCompCont = ppc; 
        this.RelCompPool = createRelCompList();
       
    }
    
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Update");
        setTitle("Update Releases");
        setMessage("Update all Releases of this "+ productCompCont.getName());
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createRelCompTable(composite);        
        return composite;
    }

    /**
     * @param parent
     */
    private void createRelCompTable(Composite parent) {
        
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
		label.setText("All Related Components");
		
		table = new Table(panel, SWT.CHECK | SWT.BORDER 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData (GridData.FILL_BOTH));
		
		TableColumn colRelCompsNames = new TableColumn(table, SWT.LEFT);
		colRelCompsNames.setText("Related Components");	
		TableColumn colRelCompsPath = new TableColumn(table, SWT.NONE);
		colRelCompsPath.setText("Path");
		TableColumn colRelCompsMessage = new TableColumn(table, SWT.NONE);
		colRelCompsMessage.setText("Message");
		
		
	
		colRelCompsNames.pack();
		colRelCompsPath.pack();
		colRelCompsMessage.pack();	
		colRelCompsPath.setWidth(200);		
		
		
		
		
		tableViewer = new CheckboxTableViewer(table);
	
		tableViewer.setLabelProvider(new ViewLabelProvider(this.productCompCont));
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
		        				   | GridData.FILL_HORIZONTAL);
		//gd.heightHint = 200;
		//table.setLayoutData(gd);		
		if (RelCompPool != null) {
		    Object[] array = RelCompPool.toArray();
		    tableViewer.add(array);
		    tableViewer.setCheckedElements(array);
	    }
        
		//allRelCompViewer.refresh();
		this.table.redraw();
    }
    
    /*
     * Return a List with all RelatedComponents of productCompCont.
     * 
     */
    private List createRelCompList(){
        ArrayList relComps = new ArrayList();
        if (this.productCompCont instanceof RelatedComponent){
            relComps.add(this.productCompCont);
        }
        LinkedList productComponnents = new LinkedList();        
        productComponnents.addAll(this.productCompCont.getProductComponents());
        while (productComponnents.size() > 0) {
           ProductComponent pc = (ProductComponent) productComponnents.removeFirst();
           productComponnents.addAll(0, pc.getProductComponents());
           if (pc instanceof RelatedComponent){
               relComps.add(pc);
           }
        }
        return relComps;
    }
    
    protected void createButtonsForButtonBar(Composite parent) 
    {
    		createButton(parent, IDialogConstants.PROCEED_ID, "&Update", true);  
    		createButton(parent, IDialogConstants.CLOSE_ID, "&Close", false);
    }
    
    public void buttonPressed(int button){
        switch (button){
          case IDialogConstants.PROCEED_ID: updatePressed(); break;
          case IDialogConstants.CLOSE_ID: close(); break;   
        }
    }
    
    protected void updatePressed()
    {
        Object[] checkedElems = tableViewer.getCheckedElements();
        for (int i = 0; i < checkedElems.length; i++) {
            RelatedComponent rc = (RelatedComponent) checkedElems[i];
            rc.updateRelease();
            messages.put(rc,"updated");
            tableViewer.refresh(rc);
        }
        ModelStorage.serializeProduct(this.productCompCont.getRoot()
                .getProductline(), new NullProgressMonitor());
        
        //tableViewer.refresh();
        this.table.redraw();
        
    }
    
    private String getPath(AbstractAsset asset) {
        if (asset == null) {
            return ".";
        } else if (asset == this.productCompCont) {
            return asset.getName();
        } else {
            return getPath(asset.getParent()) + "/" + asset.getName()  ;
        }
    }
    
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider{
       
        IProductComponentContainer root;
        
        public ViewLabelProvider(IProductComponentContainer root){
            super();
            this.root = root;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0){
                return image;
            }
            return null;
        }
               

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {  
          String s;
            if (messages.containsKey(element)) {
                s = (String) messages.get(element);
            } else {
                s = "";
            }
            switch (columnIndex){
              case 0 : return ((RelatedComponent) element).getName();
              case 1 : return getPath((RelatedComponent) element);
              case 2 : return s ;             
          }
          return "";
        }
        
            
        
    }
  
    
    
}
