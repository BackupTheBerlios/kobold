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
 * $Id: DeleteDeprecatedDialog.java,v 1.5 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.command.DeleteAssetCommand;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Necati Aydin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteDeprecatedDialog extends TitleAreaDialog{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DeleteDeprecatedDialog.class);
	
	private DeleteAssetCommand cmd;
	private Label label;
	private AbstractAsset selection; 	
   
    public DeleteDeprecatedDialog(Shell parentShell, DeleteAssetCommand cmd)
    {
        super(parentShell);
        this.cmd = cmd;    
    }
    
    protected void createButtonsForButtonBar(Composite parent) 
    {
    		createButton(parent, IDialogConstants.PROCEED_ID, "&Set Deprecated", true);
    		createButton(parent, IDialogConstants.CLOSE_ID, "&Delete", true);
    		createButton(parent, IDialogConstants.CANCEL_ID, "&Cancel", true);
    }

    
    protected Control createDialogArea(Composite parent)
    {
	    getShell().setText("Delete or Set Deprecated");
	    setTitle("Delete or Set Deprecated");
	    setMessage("If you delete this asset, it will be removed permanently.");
	
		Composite composite = (Composite) super.createDialogArea(parent);
	    
	    createContent(composite);
	    return composite;
    }
    
    private void createContent(Composite parent){
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
		       
//		label = new Label(panel,SWT.NONE);
//      label.setText("You have the possibility to delete the selected asset or to set it deprecated.");
       
		
		label = new Label(panel,SWT.NONE);
        label.setText("Press \"Delete\" to remove the selected asset from the architecture.");
        label = new Label(panel,SWT.NONE);
        label.setText("Press \"Set Deprecated\" to change the status of the selected asset.");
    }
    
    public void buttonPressed(int button){
        switch (button){
          case IDialogConstants.PROCEED_ID: deprecatedPressed(); break;
          case IDialogConstants.CANCEL_ID: cancelPressed(); break;  
          case IDialogConstants.CLOSE_ID: deletePressed(); break;   
        }
    }
   
    protected void deprecatedPressed()
    {
    	cmd.execute(DeleteAssetCommand.DEPRECATED);
    	close();
    }
    
    public void deletePressed() {
    	cmd.execute(DeleteAssetCommand.DELETE);
        close();
    }
    
    protected void cancelPressed()
    {    	
    	close();
    }
 
}
