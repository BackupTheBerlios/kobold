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
 * $Id: WorkflowDialog.java,v 1.10 2004/05/18 14:27:44 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;

/**
 * @author pliesmn
 */

import java.util.ArrayList;
import java.util.List;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * @author scheglov_ke
 */
public class WorkflowDialog extends Dialog {
	String description;
	AbstractKoboldMessage msg;
	private List viewItems = new ArrayList();
	private ContainerViewItem containerItem;
	private boolean send = false;
	final WorkflowDialog thisWorkflowDialog = this;
	//List checkButton

	
	public WorkflowDialog(Shell parentShell, AbstractKoboldMessage msg)
	{
	    super(parentShell);	
		this.msg = msg;		
	}
	
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());
		

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
		
		
		Text mt = new Text(area,SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		mt.setText(msg.getMessageText());
	
		if (msg instanceof WorkflowMessage) {
			WorkflowItem[] items = ((WorkflowMessage)msg).getWorkflowControls();
			WorkflowItem wi = new WorkflowItem("","",WorkflowItem.CONTAINER);
			wi.addChildren(items);
		    containerItem = new ContainerViewItem(wi);
		    containerItem.createViewControl(area);
		}  
	   	
  	return area;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		/*Composite buttons = new Composite(parent, SWT.NONE);		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttons.setLayout(gridLayout);
		*/
		
/*		Button button = new Button(parent,SWT.PUSH);
		button.setText("send");	
      	button.addSelectionListener(new SelectionAdapter() {
		   public void widgetSelected(SelectionEvent e) {
		 	  thisWorkflowDialog.buttonSendPressed();
		    }
	     });
      	
      	Button buttonClose = new Button(parent,SWT.PUSH);
      	buttonClose.setText("Close");	
      	buttonClose.addSelectionListener(new SelectionAdapter() {
		   public void widgetSelected(SelectionEvent e) {
		 	  thisWorkflowDialog.buttonClosePressed();
		    }
	     });	
	     */
        createButton(parent, IDialogConstants.OK_ID, "Send", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
		
	}
	
	
	public void okPressed(){
		System.out.println("send pressed");
		this.containerItem.applyValues(this);
		System.out.println( ((WorkflowMessage ) this.msg).getWorkflowData() );
	    this.close();
	}
	
	public void cancelPressed(){
		System.out.println("close pressed");
		this.close(); 
	}
	 
	protected void setAnswer(String key, String value) {
		if (msg instanceof WorkflowMessage) {
		  WorkflowMessage wm = (WorkflowMessage) msg;
		  if (wm.getWorkflowData() == null) {
		  	wm.se
		  }
		  ((WorkflowMessage)msg).putWorkflowData(key,value);
	    }		
	}

	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Dialog");
	}	

	/**
	 * @return Returns the send.
	 */
	public boolean isSend() {		
		return  send;
	}
}


