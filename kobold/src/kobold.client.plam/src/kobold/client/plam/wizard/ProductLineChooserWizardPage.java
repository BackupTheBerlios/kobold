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
 * $Id: ProductLineChooserWizardPage.java,v 1.5 2004/05/18 11:22:48 vanto Exp $
 *  
 */
package kobold.client.plam.wizard;

import java.net.URL;

import kobold.client.plam.controller.SecureKoboldClient;
import kobold.common.data.Productline;
import kobold.common.data.UserContext;

import org.apache.commons.logging.LogFactory;
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
public class ProductLineChooserWizardPage extends WizardPage {

	private Combo combo;
	
	/**
	 */
	protected ProductLineChooserWizardPage(String pageName) {
		super(pageName);
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

		NewProjectWizardServerPage serverInfoPage =
			(NewProjectWizardServerPage) getWizard().getPage(NewProjectWizardServerPage.PAGE_ID);
	
		String pline = "unknown";		
		SecureKoboldClient client;
		try {
			System.out.println("got URL: " + serverInfoPage.getServerURL());
			//client = new SecureKoboldClient(serverInfoPage.getServerURL());
			client = new SecureKoboldClient(new URL("https://localhost:23232/RPC2"));
			UserContext uc = client.login("garbeam", "garbeam");
			System.out.println(uc);
			//client.login(serverInfoPage.getUsername(),
			//											serverInfoPage.getPassword());
			Productline productline = client.getProductline(uc, "kobold2");
			pline = productline.getName();
			System.err.println(pline);
			client.logout(uc);
		} catch (Exception exception) {
			LogFactory.getLog(getClass().getName()).info(exception);
			exception.printStackTrace();
		}
		
		// new server label
		Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText("Product Line");
		projectLabel.setFont(parent.getFont());

		combo = new Combo (projectGroup, SWT.READ_ONLY);
		
		combo.setItems (new String [] {pline, "A-1", "B-1", "C-1"});
		
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
	
	public String getProductLineName()
	{
		return combo.getText();
	}

}
