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
 * $Id: UpdateUserDataDialog.java,v 1.9 2004/09/22 14:28:50 neco Exp $
 */
package kobold.client.plam.editor.dialog;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.UserManager;
import kobold.common.data.User;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UpdateUserDataDialog extends TitleAreaDialog{
	
    private Label labelRealName;
    private Label labelPassword;
    private Label labelOldPassword;
    private Label labelNewPassword;
    private Label labelConfPassword;
    
    private Text textRealName;
    private Text textPassword;
    private Text textOldPassword;
    private Text textNewPassword;
    private Text textConfPassword;
    
    private boolean isEditFullName;
	private String userName;
	private User user;
	
    /**
     * @param parentShell
     * @param isEditFullName if <code>true</code> this dialog is used to edit
     *        the users fullname, otherwise it is used to change the password.
     */
    public UpdateUserDataDialog(Shell parentShell, boolean isEditFullName)
    {
        super(parentShell);
        this.isEditFullName = isEditFullName;
        this.userName = KoboldPLAMPlugin.getCurrentKoboldProject().getUserName();
        this.user = (User)KoboldPLAMPlugin.getCurrentKoboldProject().getUserPool().get(userName);
    }
    
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Update " + userName + "'s user data");
        setTitle("Update " + userName + "'s user data");
        if(isEditFullName){
        	setMessage("Change your full name in this dialog.");
        }
        else{
        	setMessage("Change your password in this dialog.");
        }
        
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
		
        if (isEditFullName) {
            //RealName
            labelRealName = new Label(panel,SWT.NONE);
            labelRealName.setText("Full name:");
            textRealName = new Text(panel, SWT.BORDER);		
            textRealName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            textRealName.setText(user.getFullname());
            
            textRealName.setFocus();
                
            //password
            labelPassword = new Label(panel,SWT.NONE);
            labelPassword.setText("Password:");
            textPassword = new Text(panel, SWT.BORDER);
            textPassword.setEchoChar('*');
            textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }
        else {
            // old password
            labelOldPassword = new Label(panel,SWT.NONE);
            labelOldPassword.setText("Old password:");
            textOldPassword = new Text(panel, SWT.BORDER);		
            textOldPassword.setEchoChar('*');
            textOldPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            
            textOldPassword.setFocus();
                
            //password
            labelNewPassword = new Label(panel,SWT.NONE);
            labelNewPassword.setText("Password:");
            textNewPassword = new Text(panel, SWT.BORDER);
            textNewPassword.setEchoChar('*');
            textNewPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            //confirm password
            labelConfPassword = new Label(panel,SWT.NONE);
            labelConfPassword.setText("Confirm password:");
            textConfPassword = new Text(panel, SWT.BORDER);
            textConfPassword.setEchoChar('*');
            textConfPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        }
    
    }
   
    protected void okPressed()
    {
    
	    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
        UserManager acts = new UserManager();
       
        if (isEditFullName) {
        	if (textPassword.getText().equals(tmpProj.getPassword())){
        	    acts.updateFullName(tmpProj.getUserName(), textRealName.getText(), textPassword.getText());
        	}
        	else {
        		MessageDialog.openError(getShell(), "Kobold Error", 
        				"Can't update full name, because the entered password don't match.      "
						+ "Please try again.");
        	}
        }
        else {
            if (textOldPassword.getText().equals(tmpProj.getPassword())){
        		acts.changePassword(textOldPassword.getText(), textNewPassword.getText());
            }
            else {
            	MessageDialog.openError(getShell(), "Kobold Error", 
    			"Can't update password, because the entered old password don't match. " 
       			+ "Please try again.");
            }
            String t = textNewPassword.getText();
            String t2 = textConfPassword.getText();
            if (textNewPassword.getText().equals(textConfPassword.getText()))
            {
            	MessageDialog.openInformation(getShell(),"Kobold Information",
            			"Password updated successfully!");
            }
            else
            {
            	MessageDialog.openError(getShell(), "Kobold Error", 
            			"Can't update password, because the entered new password doesn't match the confirm password. " 
               			+ "Please try again.");
            }
        }
        
        super.okPressed();
    }
    
    protected void cancelPressed(){
    	super.cancelPressed();
    }
 
}
