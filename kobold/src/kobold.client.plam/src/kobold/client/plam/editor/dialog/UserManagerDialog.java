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
 * $Id: UserManagerDialog.java,v 1.7 2004/08/25 00:35:16 neco Exp $
 */
 
package kobold.client.plam.editor.dialog;

import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.UserManager;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.common.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;


/**
 * Provides a Dialog for importing a new certificate.
 * 
 * @author garbeam
 */
public class UserManagerDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(UserManagerDialog.class);
 
    private Label labelUserName;
    private Label labelRealName;
    private Label labelPassword;
    private Label labelConfPass;
    
    private Text textUserName;
    private Text textRealName;
    private Text textPassword;
    private Text textConfPass;
    
    private AbstractMaintainedAsset asset;
    private TableViewer viewer;
    private Table user;
    private Map userPool;
    
    /**
     * @param parentShell
     */
    public UserManagerDialog(Shell parentShell)
    {
        super(parentShell);
        KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
        if (kp!=null){
        	
        	userPool = kp.getUserPool();	
        }
    
    }

    public Control createDialogArea(Composite parent)
    {
        getShell().setText("User Management");
        setTitle("User Management");
        setMessage("You can create new users in this dialog.");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createContent(composite);
        createUserLists(composite);
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
        textRealName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
            
        labelPassword = new Label(panel,SWT.NONE);
        labelPassword.setText("Password:");
        textPassword = new Text(panel, SWT.BORDER);
        textPassword.setEchoChar('*');
        textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
            
        //confirmpassword
        labelConfPass = new Label(panel,SWT.NONE);
        labelConfPass.setText("Confirm password:");
        textConfPass = new Text(panel, SWT.BORDER);
        textConfPass.setEchoChar('*');
        textConfPass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                					 GridData.GRAB_HORIZONTAL));
    }
    
    private void createUserLists(final Composite parent) {
        
		final Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setFont(parent.getFont());
		
		user = new Table(parent, SWT.BORDER | SWT.LEAD | SWT.WRAP 
				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		user.setLinesVisible(false);
        TableColumn colUserNames = new TableColumn(user, SWT.NONE);
        colUserNames.setText("User");
        TableLayout tableLayout = new TableLayout();
        user.setLayout(tableLayout);
        tableLayout.addColumnData(new ColumnWeightData(100));
        colUserNames.pack();

        viewer = new TableViewer(user);
        viewer.setLabelProvider(new LabelProvider() {
            private Image image;
            public String getText(Object element) {
            User user = (User)element;     
            return user.getUsername() + " (" + user.getFullname() + ")";
            }
            
            public Image getImage(Object element) {
            if (image == null) {
            	image = KoboldPLAMPlugin.getImageDescriptor("icons/user.gif").createImage();
            }
            return image;
            }
            });
        GridData gd = new GridData(GridData.GRAB_HORIZONTAL
        				   | GridData.FILL_HORIZONTAL);
        gd.heightHint = 100;
        user.setLayoutData(gd);
		refreshUserList();	    
		
		Button addUser = new Button(panel, SWT.NONE);
		addUser.setText("&Add user");
		addUser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String user = textUserName.getText();
				String real = textRealName.getText();
				UserManager acts = new UserManager();
			    if (real.length() == 0 || user.length() == 0 || 
			    		textPassword.getText().length() == 0) {
			    	MessageDialog.openError(getShell(), "Kobold Error", 
			    	"User name, full name and password should not be empty.");
			    }
                
                else if (textPassword.getText().equals(textConfPass.getText())){
                	acts.createUser(textRealName.getText(),textUserName.getText(), 
                		textPassword.getText(), textConfPass.getText());
                	
                	textRealName.setText("");
                	textUserName.setText("");
                	textPassword.setText("");
                	textConfPass.setText("");
                	refreshUserList();
                }
                else {
                    MessageDialog.openError(getShell(), "Kobold Error", 
                    "Can't add user, because the entered passwords don't match.");
                }
                
			}
		});
		
		textUserName.setFocus();

    }
    
    private void refreshUserList(){
        viewer.getTable().removeAll();
        viewer.add(userPool.values().toArray());
    }
 
    protected void okPressed()
    {
    	this.close();
    }
    
}
