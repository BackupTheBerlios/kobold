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
 * $Id: NewProjectWizardServerPage.java,v 1.3 2004/05/15 16:18:16 vanto Exp $
 *
 */
package kobold.client.plam.wizard;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page to get the kobold server address 
 * 
 * @author Tammo
 */
public class NewProjectWizardServerPage extends WizardPage {

	private Text serverUrlField;
	private Text usernameField;
	private Text passwordField;
	
	private Listener nameModifyListener = new Listener() {
		public void handleEvent(Event e) {
			boolean valid = validatePage();
			setPageComplete(valid);
		}
	};
	
	
	/**
	 * Constructor for NewProjectSetWizardPage.
	 * @param pageName
	 */
	public NewProjectWizardServerPage(String pageName) {
		super(pageName);
		setPageComplete(false);
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
		// project server group
		Composite projectGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new server label
		Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText(Messages.getString("NewProjectWizardServerPage.KoboldServerUrl")); //$NON-NLS-1$
		projectLabel.setFont(parent.getFont());

		// new server entry field
		serverUrlField = new Text(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		serverUrlField.setLayoutData(data);
		serverUrlField.setFont(parent.getFont());

		serverUrlField.addListener(SWT.Modify, nameModifyListener);
		
		// new server label
		Label usernameLabel = new Label(projectGroup, SWT.NONE);
		usernameLabel.setText(Messages.getString("NewProjectWizardServerPage.Username")); //$NON-NLS-1$
		usernameLabel.setFont(parent.getFont());

		// new server entry field
		usernameField = new Text(projectGroup, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		usernameField.setLayoutData(data);
		usernameField.setFont(parent.getFont());
		
		usernameField.addListener(SWT.Modify, nameModifyListener);

		// new server label
		Label passwordLabel = new Label(projectGroup, SWT.NONE);
		passwordLabel.setText(Messages.getString("NewProjectWizardServerPage.Password")); //$NON-NLS-1$
		passwordLabel.setFont(parent.getFont());

		// new server entry field
		passwordField = new Text(projectGroup, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		passwordField.setLayoutData(data);
		passwordField.setFont(parent.getFont());
		
		passwordField.addListener(SWT.Modify, nameModifyListener);
	}
	
	private boolean validatePage()
	{
		//if (!serverUrlField.getText().matches("^(http|https)://[a-zA-Z.]+(:\\d+)?(/\\S*)*/*")) { //$NON-NLS-1$
		//	setErrorMessage(Messages.getString("NewProjectWizardServerPage.InvalidUrlMsg")); //$NON-NLS-1$
		//	return false;
		//}
		try {
			new URL(serverUrlField.getText());
		} catch (MalformedURLException e) {
			// malformed url -> page not valid
			return false;
		}

		if (usernameField.getText().length() == 0) {
			setErrorMessage(Messages.getString("NewProjectWizardServerPage.NoUsernameMsg")); //$NON-NLS-1$
			return false;
		}

		if (passwordField.getText().length() == 0) {
			setErrorMessage(Messages.getString("NewProjectWizardServerPage.NoPasswordMsg")); //$NON-NLS-1$
			return false;
		}
		
		setErrorMessage(null);
		setMessage(null);

		return true;
	}

	/**
	 * Returns the server url
	 */	
	public URL getServerURL() {	
		try {
			return new URL(serverUrlField.getText());
		} catch (MalformedURLException e) {
			// should not happen because url-string gets always validated in validatePage() 
		}
		return null;
	}

	/**
	 * Returns the server url
	 */	
	public String getUsername() {	
		return usernameField.getText();
	}

	/**
	 * Returns the server url
	 */	
	public String getPassword() {	
		return passwordField.getText();
	}

}
