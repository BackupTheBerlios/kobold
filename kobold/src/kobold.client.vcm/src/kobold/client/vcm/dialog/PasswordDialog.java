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
 * grosseml 31.08.2004
 */
package kobold.client.vcm.dialog;

import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.preferences.VCMPreferencePage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PasswordDialog extends Dialog{
	
    private Label labelUsername;
    private Label labelPassword;
    
    private Text textUsername;
    private Text textPassword;

    /**
     * @param parentShell
     */
    public PasswordDialog(Shell parentShell)
    {
        super(parentShell);
    }
    
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Enter VCM Password");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createContent(composite);
        return composite;
    }
    
    private void createContent(Composite parent){
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
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

    	GridData gd = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.FILL_HORIZONTAL);
		gd.widthHint = 350;
		panel.setLayoutData(gd);
		
        //password
        labelPassword = new Label(panel,SWT.NONE);
        labelPassword.setText("Password: ");
        textPassword = new Text(panel, SWT.BORDER);
        textPassword.setEchoChar('*');
        textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		textPassword.setFocus();
    }
   
    protected void okPressed()
    {
 		KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(
 		        VCMPreferencePage.KOBOLD_VCM_PWD_STR, textPassword.getText());
    	super.okPressed();
    }
    
    protected void cancelPressed(){
    	super.cancelPressed();
    }
}
