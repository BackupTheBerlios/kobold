/*
 * Created on 21.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.useractions;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.dialogs.Dialog;
/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UIUpdateUserFullName extends Dialog{
	
	
	private Label labelUserName;
	private Label labelRealName;
	private Label labelPassword;
	private Label labelConfPass;
	
	private Text textUserName;
	private Text textRealName;
	private Text textPassword;
	private Text textConfPass;
	
	public UIUpdateUserFullName (Shell parentShell){
		super(parentShell);
	}
	
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());	
		try{
		Composite control = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		control.setLayout(gridLayout);
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);


		//User Name
		labelUserName = new Label(control,SWT.NONE);
		labelUserName.setText("Please insert the username of the user:");
		textUserName = new Text(control, SWT.BORDER);		
		textUserName.setLayoutData(data);

		//RealName
		labelRealName = new Label(control,SWT.NONE);
		labelRealName.setText("Please insert the new real name of the user:");
		textRealName = new Text(control, SWT.BORDER);		
		
		//password
		labelPassword = new Label(control,SWT.NONE);
		labelPassword.setText("Please insert the password of the user:");
		textPassword = new Text(control, SWT.BORDER);
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return area;
	}

	public void okPressed(){
		Useractions acts = new Useractions();
		acts.updateFullName(textUserName.getText(),textRealName.getText(),textPassword.getText());
	}

	public void cancelPressed(){
		this.close();
	}	
	
	
	
}
