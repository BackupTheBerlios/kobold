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
 * $Id: WorkflowDialog.java,v 1.6 2004/05/17 00:56:40 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;

/**
 * @author pliesmn
 */

import java.util.ArrayList;
import java.util.*;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.common.data.KoboldMessage;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
/**
 * @author scheglov_ke
 */
public class WorkflowDialog extends Dialog {
	String description;
	KoboldMessage msg;
	private List viewItems = new ArrayList();
	private ContainerViewItem containerItem;
	//List checkButton

	
	public WorkflowDialog(Shell parentShell, KoboldMessage msg)
	{
	    super(parentShell);	
		this.msg = msg;		
	}
	
	/*public WorkflowDialog(Shell parentShell, WorkflowItemGroup  workflowItemGroup, String subject, String description) {		
		super(parentShell);
		this.parent1 = parentShell;
		this.workflowItemGroup = workflowItemGroup;
		this.subject = subject;
		this.description = description;	
			
	}*/
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());
		Label lbl = new Label(area,SWT.NONE);
		lbl.setText(subject);

		Composite header = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		header.setLayout(gridLayout);
		
		new Label(header, SWT.NONE).setText("Sender:");
		new Label(header, SWT.NONE).setText(msg.getSender());
		new Label(header, SWT.NONE).setText("Receiver:");
		new Label(header, SWT.NONE).setText(msg.getReceiver());
		new Label(header, SWT.NONE).setText("Subject:");
		new Label(header, SWT.NONE).setText(msg.getSubject());
		
		if (msg instanceof WorkflowMessage) {
			WorkflowItem[] items = ((WorkflowMessage)msg).getWorkflowControls();
			WorkflowItem wi = new WorkflowItem("","",WorkflowItem.CONTAINER);
			wi.addChildren(items);
		    containerItem = new ContainerViewItem(wi);
		}
		
		
		
		
		Composite buttons = new Composite(parent, SWT.NONE);		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttons.setLayout(gridLayout);
		
		Button ok = new Button(buttons, SWT.NONE);
		ok.setText("Send");

		Button close = new Button(buttons, SWT.NONE);
		close.setText("Close");	
		return area;
	}
	
	/**
	 * @param area
	 * @param workflowItemGroup2
	 */
	/*private void addItem(Composite area, WorkflowItemGroup workflowItemGroup) {		
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
	}*/
/*	
	private void addItem(Composite area, WorkflowItem workflowItem) {
	   int style;
		if (workflowItem.getType() == (WorkflowItem.RADIO))
		  style = SWT.RADIO;
		else style = SWT.CHECK;			
		Button b = new Button(area, style);							
	}
	
*/
	protected void createButtonsForButtonBar(Composite parent) {		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
	}
	
	public void okPressed() {
		
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Dialog");
	}
	
	
	
	

}


