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
 * $Id: UIChangePassword.java,v 1.5 2004/08/04 12:55:53 martinplies Exp $
 *
 */
package kobold.client.plam.useractions;


import kobold.client.plam.wizard.Messages;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UIChangePassword extends TitleAreaDialog	{
    
    private Label labelOldPassword;
    private Label labelPassword;
    private Label labelConfPass;
    
    private Text textOldPassword;
    private Text textPassword;
    private Text textConfPass;
    private Button okButton;
    
    /**
     * @param item
     */
    public UIChangePassword(Shell parentShell) {
        super(parentShell);
        // TODO Auto-generated constructor stub
    }
    
    
    
    public Control createDialogArea(Composite parent) {
        this.setTitle("Password Change");
        Composite control = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        control.setLayout(gridLayout);
        
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        control.setLayoutData(data);
        
        //control.setBackground(ColorConstants.white);
        
        //the old password
        labelOldPassword = new Label(control,SWT.NONE);
        labelOldPassword.setText("Current password:");
        textOldPassword = new Text(control, SWT.BORDER);
        textOldPassword.setToolTipText("Please insert your current password.");
        data = new GridData(GridData.FILL_HORIZONTAL);
        textOldPassword.setLayoutData(data);
        
        //password
        labelPassword = new Label(control,SWT.NONE);
        labelPassword.setText("New password:");
        textPassword = new Text(control, SWT.BORDER);	
        textPassword.setToolTipText("Please insert here your new password.");
        textPassword.addModifyListener( new ModifyListener(){
            public void modifyText(ModifyEvent e) {
               UIChangePassword.this.changePassword();
            }            
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        textPassword.setLayoutData(data);
        
        //confirmpassword
        labelConfPass = new Label(control,SWT.NONE);
        labelConfPass.setText("New password:");
        textConfPass = new Text(control, SWT.BORDER);
        textConfPass.setToolTipText("Please confirm here your password.");
        textConfPass.addModifyListener( new ModifyListener(){
            public void modifyText(ModifyEvent e) {
               UIChangePassword.this.changePassword();
            }            
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        textConfPass.setLayoutData(data);
        
        return control;
    }
    
    /**
     * 
     */
    protected void changePassword() {
        if ( textPassword.getText().equals(textConfPass.getText())){
            this.setMessage("Passwords are not equal!", IMessageProvider.ERROR);
            this.okButton.setEnabled(false);
        } else {
            this.setMessage(null, IMessageProvider.NONE);
            this.okButton.setEnabled(true);
        }
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, 
                IDialogConstants.CANCEL_LABEL, false); //$NON-NLS-1$
    }



    public void okPressed(){
        Useractions acts = new Useractions();
        
        acts.changePassword(textPassword.getText(),textConfPass.getText());
        
    }
    
    public void cancelPressed(){
        this.close();
    }
    
    
    
    
    
}
