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
 *MiG05.08.2004
 */
package kobold.client.plam.editor.dialog;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.useractions.Useractions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class ChangePasswordDialog extends TitleAreaDialog{
    private Label labelPassword;
    private Label labelConfPass;
    
    private Text textPassword;
    private Text textConfPass;
	
    public ChangePasswordDialog(Shell parentShell)
    {
        super(parentShell);
    }
    
    protected Control createDialogArea(Composite parent)
    {
        setTitle("Change Password");
        setMessage("Changes your password");
        getShell().setText("Change Password");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createContent(composite);
        return composite;
    }
    
    private void createContent(Composite parent){
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, false);
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
		
        try{
            Composite control = new Composite(parent, SWT.NONE);
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 2;
            control.setLayout(gridLayout);
            
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
                        
            //password
            labelPassword = new Label(control,SWT.NONE);
            labelPassword.setText("Please insert your new password:");
            textPassword = new Text(control, SWT.BORDER);
            textPassword.setEchoChar('*');
            
            //confirmpassword
            labelConfPass = new Label(control,SWT.NONE);
            labelConfPass.setText("Please confirm your new password:");
            textConfPass = new Text(control, SWT.BORDER);
            textConfPass.setEchoChar('*');
            
            //just for testing purposes
		    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
		    Label tmpLabel = new Label(control,SWT.NONE);
		    tmpLabel.setText(tmpProj.getPassword());
		    

            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
		
    	
    }
    
    protected void okPressed()
    {
        Useractions acts = new Useractions();
        if (textPassword.getText().equals(textConfPass.getText())){
		    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
        	acts.changePassword(tmpProj.getPassword(), textPassword.getText());
        	this.close();
            super.okPressed();
        }
        else {
        	Color color = new Color(null,255,0,0);
        	this.labelPassword.setForeground(color);
        	this.labelConfPass.setForeground(color);
        	this.textPassword.setText("");
        	this.textConfPass.setText("");
        }

    }
    
    protected void cancelPressed(){
    	super.cancelPressed();
    }



}
