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
 * $Id: ProductLineChooserWizardPage.java,v 1.10 2004/08/01 11:53:11 garbeam Exp $
 *  
 */
package kobold.client.plam.wizard;

import java.util.List;

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

    public static final String PAGE_ID = "KOBOLD_WIZARD_ROLES"; 
    private Combo combo;
	
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
	
	void setProductlines(List pls)
	{
	    combo.setItems((String[])pls.toArray(new String[0]));
	}
	
	public String getProductLineName()
	{
		return combo.getText();
	}

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event)
    {
        setPageComplete(event.getSelection() != null);
    }
}
