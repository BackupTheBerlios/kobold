/*
 * Created on 24.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.useractions;


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

/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UIChangePassword extends Dialog	{

	private Label labelOldPassword;
	private Label labelPassword;
	private Label labelConfPass;
	
	private Text textOldPassword;
	private Text textPassword;
	private Text textConfPass;

/**
 * @param item
 */
public UIChangePassword(Shell parentShell) {
	super(parentShell);
	// TODO Auto-generated constructor stub
}



public Composite createDialogArea(Composite parent) {

	Composite control = new Composite(parent, SWT.NONE);
	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 2;
	control.setLayout(gridLayout);
	
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	control.setLayoutData(data);

	//control.setBackground(ColorConstants.white);
	
	//the old password
	labelOldPassword = new Label(control,SWT.NONE);
	labelOldPassword.setText("Please insert your current password:");
	textOldPassword = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textOldPassword.setLayoutData(data);
	
	//password
	labelPassword = new Label(control,SWT.NONE);
	labelPassword.setText("Please insert your new password:");
	textPassword = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textPassword.setLayoutData(data);

	//confirmpassword
	labelConfPass = new Label(control,SWT.NONE);
	labelConfPass.setText("Please confirm your password:");
	textConfPass = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textConfPass.setLayoutData(data);

	return control;
}

public void okPressed(){
	Useractions acts = new Useractions();
	
	acts.changePassword(textPassword.getText(),textConfPass.getText());
	
}

public void cancelPressed(){
	this.close();
}





}
