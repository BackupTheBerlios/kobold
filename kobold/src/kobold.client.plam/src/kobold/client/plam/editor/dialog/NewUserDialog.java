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
 * */
package kobold.client.plam.editor.dialog;

import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.useractions.Useractions;
import kobold.common.data.User;

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
public class NewUserDialog extends TitleAreaDialog{
	private Map userPool;
	
    private Label labelUserName;
    private Label labelRealName;
    private Label labelPassword;
    private Label labelConfPass;
    
    private Text textUserName;
    private Text textRealName;
    private Text textPassword;
    private Text textConfPass;
	
	
    public NewUserDialog(Shell parentShell)
    {
        super(parentShell);
        KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
        if (kp!=null){
        	
        	userPool = kp.getUserPool();	
        }
    
    }
    
    protected Control createDialogArea(Composite parent)
    {
        setTitle("New User");
        setMessage("Enter the values of the new user you want to create.");
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
		
        labelUserName = new Label(panel,SWT.NONE);
        labelUserName.setText("User name:");
        textUserName = new Text(panel, SWT.BORDER);		
        textUserName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
            
        labelRealName = new Label(panel,SWT.NONE);
        labelRealName.setText("Full name:");
        textRealName = new Text(panel, SWT.BORDER);		
        textUserName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
            
        labelPassword = new Label(panel,SWT.NONE);
        labelPassword.setText("Password:");
        textPassword = new Text(panel, SWT.BORDER);
        textPassword.setEchoChar('*');
        textUserName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
            
        //confirmpassword
        labelConfPass = new Label(panel,SWT.NONE);
        labelConfPass.setText("Confirm password:");
        textConfPass = new Text(panel, SWT.BORDER);
        textConfPass.setEchoChar('*');
        textUserName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
}
    
    protected void okPressed()
    {
        Useractions acts = new Useractions();
        if (textPassword.getText().equals(textConfPass.getText())){
        	acts.createUser(textRealName.getText(),textUserName.getText(), textPassword.getText(), textConfPass.getText());
        	//this.close();
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
