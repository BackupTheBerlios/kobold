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
 * $Id: WorkflowDialog.java,v 1.18 2004/08/24 15:49:30 garbeam Exp $
 *
 */
package kobold.client.plam.workflow;

/**
 * @author pliesmn
 */

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.SecureKoboldClient;
import kobold.client.plam.controller.ServerHelper;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * @author scheglov_ke
 */
public class WorkflowDialog extends TitleAreaDialog {
	String description;
	AbstractKoboldMessage msg;
	private List viewItems = new ArrayList();
	private ContainerViewItem containerItem;
    private Text comment;
	
	public WorkflowDialog(Shell parentShell, AbstractKoboldMessage msg)
	{
	    super(parentShell);	
		this.msg = msg;
	}
	
	protected Control createDialogArea(Composite parent) {
		if (msg instanceof WorkflowMessage) {
		    getShell().setText("Workflow Message");
    	    setTitle("Workflow Message");
    	    setMessage("This is a workflow message. You can respond to this message" +
    	               " to let the workflow execute the next steps.");
		}
		else {
		    getShell().setText("Message");
		    setTitle("Message");
		    setMessage("This is a message for you.");
		}
		
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout());		

		Composite header = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		header.setLayout(gridLayout);
		
		new Label(header, SWT.NONE).setText("Sender:");
		
		if (msg.getSender() != null) {
    		new Label(header, SWT.NONE).setText(msg.getSender());
		}
		else {
    		new Label(header, SWT.NONE).setText("Kobold System");
		}
		
		new Label(header, SWT.NONE).setText("Receiver:");
		new Label(header, SWT.NONE).setText(msg.getReceiver());
		new Label(header, SWT.NONE).setText("Subject:");
		new Label(header, SWT.NONE).setText(msg.getSubject());
		
		// add Mesagetext
		Text mt = new Text(area,SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		mt.setText(msg.getMessageText());
	
		if (msg instanceof WorkflowMessage) {
		    // add workflowcontrol items
			WorkflowItem[] items = ((WorkflowMessage)msg).getWorkflowControls();
			WorkflowItem wi = new WorkflowItem("","",WorkflowItem.CONTAINER);
			wi.addChildren(items);
		    containerItem = new ContainerViewItem(wi);		 
		    containerItem.createViewControl(area);
		   
		   // add comment item 	     
		   comment = new Text(area, SWT.WRAP |SWT.MULTI |SWT.V_SCROLL|SWT.VERTICAL);
		   GridData gd = new GridData();
           gd.heightHint = 150;
           gd.widthHint = 350;           
           comment.setLayoutData(gd);  			 
    	}  	   	
      	return area;
	}

//	protected void createButtonsForButtonBar(Composite parent) {
//        createButton(parent, IDialogConstants.OK_ID, "Send", true);
//		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);		
//	}
	
	public void okPressed(){
		if (msg instanceof WorkflowMessage) {
		   WorkflowMessage wm = (WorkflowMessage) msg;
		   this.containerItem.applyValues(this);		
		   System.out.println(wm.getWorkflowData());
		   
		   wm.setComment(comment.getText());
		   try {
    	   	UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
		   	wm.setSender(ctx.getUserName());
		   	SecureKoboldClient.getInstance().sendMessage(ctx, wm);
		   } catch (Exception e) {
			// FIXME: Bullshit!
		    //wm.setSender("unknown");
			//SecureKoboldClient.getInstance().sendMessage(null, wm);
		   }
		}
	    this.close();
	}
	
	public void cancelPressed(){
		this.close(); 
	}
	 
	protected void setAnswer(String key, String value) {
		if (msg instanceof WorkflowMessage) {
		  WorkflowMessage wm = (WorkflowMessage) msg;
		  wm.putWorkflowData(key,value);
	    }		
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Dialog");
	}	

}