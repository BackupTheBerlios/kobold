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
 * $Id: CertificateWizardPage.java,v 1.2 2004/08/02 14:41:18 garbeam Exp $
 */
package kobold.client.plam.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Provides the server communication certificate configuration.
 */ 
public class CertificateWizardPage extends WizardPage {

    public static final String PAGE_ID = "KOBOLD_WIZARD_SERVER_CERTIFICATES"; 
    private Combo combo = null;
    private Button importButton = null;
    
    /**
     * Constructor for NewProjectSetWizardPage.
     * @param pageName
     */
    public CertificateWizardPage() {
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
				
		createCertificateChooser(composite);
		
		setControl(composite);
    }

	/**
	 * Creates the area for selecting the certificate
	 */
	public void createCertificateChooser(final Composite parent) {
	    
		// project server group
		Composite certGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		certGroup.setLayout(layout);
		certGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new certificates label
		Label certLabel = new Label(certGroup, SWT.NONE);
		certLabel.setText("Certicates");
		certLabel.setFont(parent.getFont());

		// combo box for certificates
		combo = new Combo (certGroup, SWT.READ_ONLY);
		
		combo.setItems (new String [] {"Werkbold1-Certificate"});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		combo.setLayoutData(data);
		combo.setFont(parent.getFont());

		combo.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setPageComplete(validatePage());
			}
		});
		
		importButton = new Button(certGroup, SWT.NONE);
		importButton.setText("&Import certificate...");
		importButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    NewCertificateDialog dlg = new NewCertificateDialog(parent.getShell());
			    dlg.open();
		    	// TODO: pop up import certificate dialog
			}
		});
	}
	
	private boolean validatePage()
	{
		if (combo.getSelectionIndex() == 0) {
			setErrorMessage("You have to select a certificate.");
			return false;
		}

		setErrorMessage(null);
		setMessage(null);

		return true;
	}


}
