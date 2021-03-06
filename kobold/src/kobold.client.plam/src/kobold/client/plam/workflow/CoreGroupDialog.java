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
* $Id: CoreGroupDialog.java,v 1.9 2004/11/05 10:32:31 grosseml Exp $
*
*/

package kobold.client.plam.workflow;

import org.apache.log4j.Logger;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author bettina
 *
 * This dialog helps to determine the recipient of the core group suggestion.
 * This can either be a PE or a PLE. If the user decides to send his suggestion
 * to a PE, the PE can forward the suggestion to his PLE if he supports the suggestion.
 */
public class CoreGroupDialog extends TitleAreaDialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(CoreGroupDialog.class);

	private Button pe;
	private Button ple;
	private Shell shell;
	private AbstractAsset selection;
	
	public CoreGroupDialog(Shell parentShell, AbstractAsset asset)
	{
	    super(parentShell);
	    shell = parentShell;
	    selection = asset;
	}

	protected Control createDialogArea(Composite parent) {
	
	    getShell().setText("Core Group Suggestion");
	    setTitle("Core Group Suggestion...");
	    setMessage("Suggest asset to Core Group. Choose the kind of recipient of your suggestion.");
		
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
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

		// add radiobuttons
		pe = new Button(panel, SWT.RADIO);
		pe.setText("Product Engineer (PE)");
		ple = new Button(panel, SWT.RADIO);
		ple.setText("Productline Enginer (PLE)");
   	   	
		pe.setSelection(true);
		pe.setFocus();
  	  	return panel;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "&Next", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "&Cancel", true);
	}
	
	public void okPressed(){
		
		if (pe.getSelection()){
			WorkflowMessage msg = new WorkflowMessage("Core Group Suggestion");
			UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
		    					
			msg.setSender(ctx.getUserName());	
			msg.setStep(1);
			if (selection != null) {
    			AbstractRootAsset rootAsset = selection.getRoot();
    			msg.setProductline(rootAsset.getName());
			}
			msg.setReceiver("PE");
			msg.setRole("Product Engineer");
			msg.putWorkflowData("P", ctx.getUserName());
		
			msg.setSubject("Core Group Suggestion");
			
			WorkflowItem recipient = new WorkflowItem ("recipient", "Recipient: ", WorkflowItem.TEXT);

			msg.addWorkflowControl(recipient);
							
			if (selection == null) {
				WorkflowItem asset = new WorkflowItem("asset", "Asset: ", WorkflowItem.TEXT);
				msg.addWorkflowControl(asset);
				msg.setMessageText("Enter the name of the data you want to suggest:");
			} else {
				msg.putWorkflowData("asset", selection.getName());
				msg.setMessageText("Suggesting the asset: " + selection.getName());
			}
			
			WorkflowDialog wfDialog = new WorkflowDialog(shell, msg);
			wfDialog.open();

		}
		else {
			WorkflowMessage msg = new WorkflowMessage("Core Group Suggestion");
			UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
		    					
			msg.setSender(ctx.getUserName());	
			msg.setStep(2);
			if (selection != null) {
    			AbstractRootAsset rootAsset = selection.getRoot();
    			msg.setProductline(rootAsset.getName());
			}
			msg.setReceiver("PLE");
			msg.setRole("PLE");
			msg.putWorkflowData("PE", ctx.getUserName());
			msg.putWorkflowData("P", "-");
			msg.putWorkflowData("decision", "true");
		
			msg.setSubject("Core Group Suggestion");

			WorkflowItem recipient = new WorkflowItem ("recipient", "Recipient: ", WorkflowItem.TEXT);
			msg.addWorkflowControl(recipient);

			if (selection == null) {
				WorkflowItem asset = new WorkflowItem("asset", "Asset: ", WorkflowItem.TEXT);
				msg.addWorkflowControl(asset);
				msg.setMessageText("Enter the name of the data you want to suggest:");
			} else {
				msg.putWorkflowData("asset", selection.getName());
				msg.setMessageText("Suggesting the asset: " + selection.getName());
			}

			WorkflowDialog wfDialog = new WorkflowDialog(shell, msg);
			wfDialog.open();

		}
	    this.close();
	}
	
	public void cancelPressed(){
		if (logger.isDebugEnabled()) {
			logger.debug("cancelPressed() - close pressed");
		}
		this.close(); 
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select the Recipient");
	}
}
