/*
 * Created on 10.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.workflow;

/**
 * @author meiner1
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.ListIterator;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import kobold.common.data.*;
/**
 * @author scheglov_ke
 */
public class WorkflowDialog extends Dialog {
	WorkflowItemGroup workflowItemGroup;
	String description;
	String subject;
	Composite parent1;
	//List checkButton

	
	public WorkflowDialog(Shell parentShell, WorkflowItemGroup  workflowItemGroup, String subject, String description) {		
		super(parentShell);
		this.parent1 = parentShell;
		this.workflowItemGroup = workflowItemGroup;
		this.subject = subject;
		this.description = description;	
			
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());
		Label lbl = new Label(area,SWT.NONE);
		lbl.setText(subject);
		Button b = new Button(parent1,SWT.PUSH);
		b.setText("dsgtdfgtfh");
		//addItem(area,workflowItemGroup);
		
		// DESIGNER: Add controls before this line.
		return area;
		
	}
	/**
	 * @param area
	 * @param workflowItemGroup2
	 */
	private void addItem(Composite area, WorkflowItemGroup workflowItemGroup) {		
		Composite comp = new Composite(area,SWT.NONE);
		for (ListIterator ite = workflowItemGroup.getWorkflowItems().listIterator();
		     ite.hasNext();){
			Object object = ite.next();
			if (object instanceof WorkflowItemGroup){
			    addItem(comp,(WorkflowItemGroup) object);
			} else if(object instanceof WorkflowItem){
			   addItem(comp,(WorkflowItem) object);
			}					
		}
	}
	
	private void addItem(Composite area, WorkflowItem workflowItem) {
	   int style;
		if (workflowItem.getType() == (WorkflowItem.RADIO))
		  style = SWT.RADIO;
		else style = SWT.CHECK;			
		Button b = new Button(area, style);
			
					
	}
	

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Dialog");
	}
	
	
	
	
/*	public boolean getCBValue(){
		cb.g		
	}
 */
}


