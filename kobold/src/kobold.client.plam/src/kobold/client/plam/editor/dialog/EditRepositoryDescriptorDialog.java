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
 *MiG31.08.2004
 */
package kobold.client.plam.editor.dialog;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.controller.UserManager;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.product.Product;
import kobold.common.data.User;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EditRepositoryDescriptorDialog extends TitleAreaDialog{
	
    private Label labelType;
    private Label labelProtocol;
    private Label labelHost;
    private Label labelRoot;
    private Label labelPath;
    
    private Text textType;
    private Text textProtocol;
    private Text textHost;
    private Text textRoot;
    private Text textPath;
    
    private RepositoryDescriptor repDesc;
    private AbstractAsset abAsset;
    private Product prod;
    private RepositoryDescriptor tmpDesc;
    
	
    /**
     * @param parentShell
     */
    public EditRepositoryDescriptorDialog(Shell parentShell, AbstractAsset asset)
    {
        super(parentShell);
        abAsset = asset;
        
    }
    
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Edit Repository Descriptor");
        setTitle("Add/Update the Repository Descriptor");
        setMessage("Add or edit the settings of the repository.");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        //if abAsset instance of kobold.client.plam.model.Product
        if (abAsset instanceof Product){
        	prod = (Product)abAsset;
            createContent(composite);
            return composite;      	
        } else {
        	cancelPressed();
        	return composite;
        }
    }
    
    private void createContent(Composite parent){
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
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
		
		//getting the RepositoryDescriptor from the product
		tmpDesc = prod.getRepositoryDescriptor();
		
		//Type
		labelType = new Label(panel, SWT.NONE);
		labelType.setText("Type (e.g.:cvs, svn, arch):");
		labelType.setToolTipText("Defines the repository type, e.g. cvs, svn, arch, ...");
		textType = new Text(panel, SWT.BORDER);
		textType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textType.setToolTipText("Defines the repository type, e.g. cvs, svn, arch, ...");
		textType.setText(tmpDesc.getType());
		
		textType.setFocus();
		
		//Protocol
		labelProtocol = new Label(panel, SWT.NONE);
		labelProtocol.setText("Protocol (e.g. ssh, pserver, svn, WebDAV, ...):");
		labelProtocol.setToolTipText("Defines the protocol, e.g. ssh, pserver, svn, WebDAV, ...");
		textProtocol = new Text(panel, SWT.BORDER);
		textProtocol.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textProtocol.setToolTipText("Defines the protocol, e.g. ssh, pserver, svn, WebDAV, ...");
		textProtocol.setText(tmpDesc.getProtocol());
		
		//Host
		labelHost = new Label(panel, SWT.NONE);
		labelHost.setText("Host (e.g. cvs.berlios.de):");
		labelHost.setToolTipText("Defines the hostname, e.g. cvs.berlios.de");
		textHost = new Text(panel, SWT.BORDER);
		textHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textHost.setToolTipText("Defines the hostname, e.g. cvs.berlios.de");
		textHost.setText(tmpDesc.getHost());
		
		//Root
		labelRoot = new Label(panel, SWT.NONE);
		labelRoot.setText("Root (e.g. /cvsroot/kobold/):");
		labelRoot.setToolTipText("Defines the repository root, e.g. /cvsroot/kobold/");
		textRoot = new Text(panel, SWT.BORDER);
		textRoot.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textRoot.setToolTipText("Defines the repository root, e.g. /cvsroot/kobold/");
		textRoot.setText(tmpDesc.getRoot());
		
		//Path
		labelPath = new Label(panel, SWT.NONE);
		labelPath.setText("Path (e.g. kobold):");
		labelPath.setToolTipText("Defines the module path without repository root, e.g. kobold");
		textPath = new Text(panel, SWT.BORDER);
		textPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textPath.setToolTipText("Defines the module path without repository root, e.g. kobold");
		textPath.setText(tmpDesc.getPath());
    }
   
    protected void okPressed()
    {
    
	    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
	    
	    tmpDesc.setHost(textHost.getText());
	    tmpDesc.setPath(textPath.getText());
	    tmpDesc.setProtocol(textProtocol.getText());
	    tmpDesc.setRoot(textRoot.getText());
	    tmpDesc.setType(textType.getText());
	    
	    prod.setRepositoryDescriptor(tmpDesc);
	    
        super.okPressed();
    }
    
    protected void cancelPressed(){
    	super.cancelPressed();
    }
 
}
