/*
 * Created on 17.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.workflow;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.ServerHelper;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;


/**
 * @author bettina
 *
 * This dialog helps to determine the recipient of the core group suggestion.
 * This can either be a PE or a PLE. If the user decides to send his suggestion
 * to a PE, the PE can forward the suggestion to his PLE if he supports the suggestion.
 */
public class CoreGroupDialog extends Dialog{

	private Button pe;
	private Button ple;
	private Shell shell;
	
	public CoreGroupDialog(Shell parentShell)
	{
	    super(parentShell);
	    shell = parentShell;
	}

	
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());		

		Composite header = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		header.setLayout(gridLayout);
		
		// add Mesagetext
		Text mt = new Text(area,SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		mt.setText("Please choose the kind of recipient of your suggestion: ");
	
	    
		//add radiobuttons
		pe = new Button(area, 1 << 4);
		pe.setText("Send to a PE");
		ple = new Button(area, 1 << 4);
		ple.setText("Send to a PLE");
		
   	   	
  	  	return area;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);		
	}
	
	public void okPressed(){
		
		if (pe.getSelection()){
			WorkflowMessage msg = new WorkflowMessage("Core Group Suggestion");
			try {
				UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
			    					
				msg.setSender(ctx.getUserName());	
				msg.setStep(1);
				msg.setReceiver("PE");
				msg.putWorkflowData("P", ctx.getUserName());
			
				msg.setSubject("Core Group Suggestion");
				msg.setMessageText("Enter the data of the file you want to suggest:");
				WorkflowItem recipient = new WorkflowItem ("recipient", "Recipient: ", WorkflowItem.TEXT);
				WorkflowItem file = new WorkflowItem("file", "File: ", WorkflowItem.TEXT);
				WorkflowItem component = new WorkflowItem ("component", "Component: ", WorkflowItem.TEXT);
				msg.addWorkflowControl(recipient);
				msg.addWorkflowControl(file);
				msg.addWorkflowControl(component);
				WorkflowDialog wfDialog = new WorkflowDialog(shell, msg);
				wfDialog.open();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (ple.getSelection()){
			WorkflowMessage msg = new WorkflowMessage("Core Group Suggestion");
			try {
				UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
			    					
				msg.setSender(ctx.getUserName());	
				msg.setStep(2);
				msg.setReceiver("PLE");
				msg.putWorkflowData("PE", ctx.getUserName());
				msg.putWorkflowData("P", "-");
				msg.putWorkflowData("decision", "true");
			
				msg.setSubject("Core Group Suggestion");
				msg.setMessageText("Enter the data of the file you want to suggest:");
				WorkflowItem recipient = new WorkflowItem ("recipient", "Recipient: ", WorkflowItem.TEXT);
				WorkflowItem file = new WorkflowItem("file", "File: ", WorkflowItem.TEXT);
				WorkflowItem component = new WorkflowItem ("component", "Component: ", WorkflowItem.TEXT);
				msg.addWorkflowControl(recipient);
				msg.addWorkflowControl(file);
				msg.addWorkflowControl(component);
				WorkflowDialog wfDialog = new WorkflowDialog(shell, msg);
				wfDialog.open();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    this.close();
	}
	
	public void cancelPressed(){
		System.out.println("close pressed");
		this.close(); 
	}
	

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select the Recipient");
	}
	
	
}
