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
 * $Id: EditUserManager.java,v 1.3 2004/08/05 10:19:05 grosseml Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.Iterator;
import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.common.data.User;
import kobold.client.plam.useractions.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;


/**
 * Provides a Dialog for importing a new certificate.
 * 
 * @author garbeam
 */
public class EditUserManager extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(EditUserManager.class);
 
    private AbstractMaintainedAsset asset;
    private List maintainer;
    private List allUser;
    private Map userPool;
    
    /**
     * @param parentShell
     */
    public EditUserManager(Shell parentShell)
    {
        super(parentShell);
        KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
        if (kp!=null){
        	
        	userPool = kp.getUserPool();	
        }
    
    }

    public Control createDialogArea(Composite parent)
    {
        this.setTitle("Edit UserManager");
        this.setMessage("Manages user purposes like creation and deletion of users.");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createUserLists(composite);
        return composite;
    }
    
    private void createUserLists(final Composite parent) {
        
		final Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setFont(parent.getFont());
		
	    
		Button newUser = new Button(panel, SWT.NONE);
		newUser.setText("&Create new user");
		newUser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				UINewUser nU = new UINewUser(parent.getShell()); 					
				nU.open();
				refillList(panel);
			}
		});

    }
    
    private void refillList(Composite panel){
    	allUser.removeAll();
	    allUser = new List(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP 
				  | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
	    GridData gd = new GridData(GridData.GRAB_HORIZONTAL
	    			| GridData.FILL_HORIZONTAL);
	    gd.heightHint = 50;
	    allUser.setLayoutData(gd);

	    if (userPool != null) {

	    	for (Iterator iterator = userPool.values().iterator();
	    		iterator.hasNext(); )
	    	{
	    		allUser.add(((User)iterator.next()).getUsername());
	    	}
	    }
    }
 
    protected void okPressed()
    {
    	this.close();
    }
    
}
