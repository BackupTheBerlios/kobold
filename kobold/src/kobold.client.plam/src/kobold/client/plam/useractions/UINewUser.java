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
public class UINewUser extends Dialog	{
	//private Text textWidget;
	private Label labelUserName;
	private Label labelRealName;
	private Label labelPassword;
	private Label labelConfPass;
	
	private Text textUserName;
	private Text textRealName;
	private Text textPassword;
	private Text textConfPass;

/**
 * @param item
 */
public UINewUser(Shell parentShell) {
	super(parentShell);
	// TODO Auto-generated constructor stub
}



public Composite createViewControl(Composite parent) {

	Composite control = new Composite(parent, SWT.NONE);
	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 2;
	control.setLayout(gridLayout);
	
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	control.setLayoutData(data);

	//control.setBackground(ColorConstants.white);
	
	//User Name
	labelUserName = new Label(control,SWT.NONE);
	labelUserName.setText("Please insert the username of the new user:");
	textUserName = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textUserName.setLayoutData(data);

	//RealName
	labelRealName = new Label(control,SWT.NONE);
	labelRealName.setText("Please insert the real name of the new user:");
	textRealName = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textRealName.setLayoutData(data);
	
	//password
	labelPassword = new Label(control,SWT.NONE);
	labelPassword.setText("Please insert the initial password of the new user:");
	textPassword = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textPassword.setLayoutData(data);

	//confirmpassword
	labelConfPass = new Label(control,SWT.NONE);
	labelConfPass.setText("Please retype the initial password of the new user:");
	textConfPass = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textConfPass.setLayoutData(data);

	return control;
}

public void okPressed(){
	Useractions acts = new Useractions();
	
	acts.createUser(textRealName.getText(),textUserName.getText(), textPassword.getText(), textConfPass.getText());
}

public void cancelPressed(){
	this.close();
}

}
