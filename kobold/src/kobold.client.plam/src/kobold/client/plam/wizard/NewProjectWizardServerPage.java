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
 * $Id: NewProjectWizardServerPage.java,v 1.1 2004/05/13 14:39:18 vanto Exp $
 *
 */
package kobold.client.plam.wizard;

import java.net.URI;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Wizard page to get the kobold server adress 
 * 
 * @author Tammo
 */
public class NewProjectWizardServerPage extends WizardPage {

	/**
	 * Constructor for NewProjectSetWizardPage.
	 * @param pageName
	 */
	public NewProjectWizardServerPage(String pageName) {
		super(pageName);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));		
				
		createServerChooser(composite);
		
		setControl(composite);
	}

	/**
	 * Creates the area for selecting the projects
	 */		
	public void createServerChooser(Composite parent) {
		Label selectLabel = new Label(parent, SWT.LEFT);
		selectLabel.setText("Enter Kobold Server address");
		
		// TODO add widget				
	}
	
	/**
	 * Returns the selected projects
	 */	
	public URI getKoboldServerURI() {	
		return null;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizardPage#isPageComplete()
	 */
	public boolean isPageComplete() {
		return super.isPageComplete();
	}
}
