/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, Bettina Druckenmueller,
 * Anselm Garbe, Michael Grosse, Tammo van Lessen, Martin Plies, Oliver Rendgen,
 * Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * $Id: ProductLineChooserWizardPage.java,v 1.8 2004/07/02 12:33:58 vanto Exp $
 *  
 */
package kobold.client.plam.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.common.data.RolePLE;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Wizard Page for choosing a product line out of the server list.
 * 
 * @author Tammo
 */
public class ProductLineChooserWizardPage extends WizardPage implements ISelectionChangedListener {

    public static final String PAGE_ID
	= "KOBOLD_WIZARD_ROLES"; 
    
    private Combo combo;
    //private TableViewer viewer;
	//private RoleContentProvider provider;
	
	/*private String[] titles = {"Rolle", "Produktlinie", "Produkt"};
	private ColumnLayoutData columnLayouts[] =	{
			new ColumnWeightData(75),
			new ColumnWeightData(150),
			new ColumnWeightData(150)};*/
    
	
	/**
	 */
	protected ProductLineChooserWizardPage() {
		super(PAGE_ID);
		setPageComplete(false);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		/*Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		setControl(composite);
		

		Table table = new Table (composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);	
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);

		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText (titles [i]);	
			
			tableLayout.addColumnData(columnLayouts[i]);
		}	

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	

		provider = new RoleContentProvider();
		
		viewer = new TableViewer(table);
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(new RoleLabelProvider());

		viewer.addSelectionChangedListener(this);*/
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));		

		setControl(composite);
		
		Composite projectGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// new server label
		Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText("Product Line");
		projectLabel.setFont(parent.getFont());

		combo = new Combo (projectGroup, SWT.READ_ONLY);
		
		//combo.setItems (new String [] {pline, "A-1", "B-1", "C-1"});
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		combo.setLayoutData(data);
		combo.setFont(parent.getFont());

		combo.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setPageComplete(validatePage());
			}
		});

	}
	
	private boolean validatePage()
	{
		if (combo.getSelectionIndex() == 0) {
			setErrorMessage("You have to select a product line.");
			return false;
		}

		setErrorMessage(null);
		setMessage(null);

		return true;
	}
	
	void setRoles(List roles)
	{
	    List pls = new ArrayList();
	    pls.add("");
	    Iterator it = roles.iterator();
	    while (it.hasNext()) {
	        Object role = it.next();
	        if (role instanceof RolePLE) {
	            pls.add(((RolePLE)role).getProductlineName());
	        }
	    }
	    combo.setItems((String[])pls.toArray(new String[0]));
	}
	
	public String getProductLineName()
	{
		return combo.getText();
	}

	
	/*private class RoleContentProvider implements IStructuredContentProvider {

        private List roles;
        
        public Object[] getElements(Object inputElement)
        {
            if (roles == null) {
                return null;
            }
            return roles.toArray();
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            this.roles = (List)newInput;
        }
	}*/
	
    /**
     * @author Tammo
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Generation - Code and Comments
     */
    /*public class RoleLabelProvider extends LabelProvider implements ITableLabelProvider
    {

        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }

        public String getColumnText(Object element, int columnIndex)
        {
            if (element == null) {
                return null;
            }
            
            if (element instanceof RoleP) {
                RoleP p = (RoleP) element;
                switch (columnIndex) {
                    case 0: return "P";
                    case 1: return "-";
                    case 2: return p.getProductName();
                }
            }
            
            if (element instanceof RolePE) {
                RolePE p = (RolePE) element;
                switch (columnIndex) {
                    case 0: return "PE";
                    case 1: return "-";
                    case 2: return p.getProductName();
                }
            }

            if (element instanceof RolePLE) {
                RolePLE p = (RolePLE) element;
                switch (columnIndex) {
                    case 0: return "PLE";
                    case 1: return p.getProductlineName();
                    case 2: return "-";
                }
            }
            
            return element.toString();
        }
        
    }*/

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        setPageComplete(event.getSelection() != null);
    }
}
