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
public class UIChangeRole extends Dialog	{
	//private Text textWidget;
	private Label labelUserName;
	private Label labelNewRole;
	
	private Text textUserName;
	private Text textNewRole;

/**
 * @param item
 */
public UIChangeRole(Shell parentShell) {
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
	labelUserName.setText("Please insert the username of the user you wish to change:");
	textUserName = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textUserName.setLayoutData(data);

	//RealName
	labelNewRole = new Label(control,SWT.NONE);
	labelNewRole.setText("Please insert the new role (P / PE) name of the user:");
	textNewRole = new Text(control, SWT.NONE);		
	data = new GridData(GridData.FILL_HORIZONTAL);
	textNewRole.setLayoutData(data);
	

	return control;
}

public void okPressed(){
	Useractions acts = new Useractions();
	
	acts.removeRole(textUserName.getText(),"Produkt1");
	acts.addRole(textUserName.getText(),"Produkt1", textNewRole.getText());
}

public void cancelPressed(){
	this.close();
}

}

