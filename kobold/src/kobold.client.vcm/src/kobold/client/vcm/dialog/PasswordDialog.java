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

import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.UserManager;
import kobold.client.plam.editor.dialog.EditMaintainerDialog;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.preferences.VCMPreferencePage;
import kobold.common.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Text;

public class PasswordDialog extends TitleAreaDialog{
	
    private Label labelUsername;
    private Label labelPassword;

    
    private Text textUsername;
    private Text textPassword;

    
	private String userName;
	private User user;
	
    /**
     * @param parentShell
     * @param isEditFullName if <code>true</code> this dialog is used to edit
     *        the users fullname, otherwise it is used to change the password.
     */
    public PasswordDialog(Shell parentShell)
    {
        super(parentShell);
        this.userName = KoboldPLAMPlugin.getCurrentKoboldProject().getUserName();
        this.user = (User)KoboldPLAMPlugin.getCurrentKoboldProject().getUserPool().get(userName);
    }
    
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Authentification");
        setTitle("Insert Username and Password");
        setMessage("Please insert your username and password in this dialog.");
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

		labelUsername = new Label(panel, SWT.NONE);
		labelUsername.setText("Username: ");
		textUsername = new Text(panel, SWT.BORDER);
		textUsername.setText(KoboldVCMPlugin.getDefault().getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_USER_STR));
		//textUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        //password
        labelPassword = new Label(panel,SWT.NONE);
        labelPassword.setText("Password: ");
        textPassword = new Text(panel, SWT.BORDER);
        textPassword.setEchoChar('*');
        textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if(textUsername.getText().equals(""))
		{
			textUsername.setFocus();
		} 
		else
		{
			textPassword.setFocus();
		}
    }
   
    protected void okPressed()
    {
    
	    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
       

      	if (textPassword.getText().equals(tmpProj.getPassword())){
      		KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_USER_STR, textUsername.getText());
      		KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_PWD_STR, textPassword.getText());
      	}
      	else
      	{
        		MessageDialog.openError(getShell(), "Kobold Error", 
        				"Password or Username incorrect. "
						+ "Please try again.");
       	}

        
        super.okPressed();
    }
    
    protected void cancelPressed(){
 		KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_PWD_STR, "");   	
    	super.cancelPressed();
    }
 
}
