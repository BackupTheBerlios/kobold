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
 * $Id: NewCertificateDialog.java,v 1.2 2004/08/02 16:52:31 garbeam Exp $
 *
 */
package kobold.client.plam.wizard;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Enumeration;

import kobold.client.plam.controller.SSLHelper;
import kobold.client.plam.model.AbstractAsset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;


/**
 * Provides a Dialog for importing a new certificate.
 * 
 * @author garbeam
 */
public class NewCertificateDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(NewCertificateDialog.class);
    private Combo combo;
    private Text text;
    
    /**
     * @param parentShell
     */
    public NewCertificateDialog(Shell parentShell)
    {
        super(parentShell);
    }

    protected Control createDialogArea(Composite parent)
    {
        setTitle("Import certificate");
        setMessage("Please paste your certificate here");
        Composite composite = (Composite) super.createDialogArea(parent);
        createCertProps(composite);
        return composite;
    }
    
    private void createCertProps(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		panel.setFont(parent.getFont());

		Label label = new Label(panel, SWT.NONE);
		label.setText(IDEWorkbenchMessages
				.getString("Alias: ")); //$NON-NLS-1$

		// combo box for certificates
		Combo combo = new Combo (panel, SWT.NONE);
		KeyStore keyStore = SSLHelper.getKeyStore();
	        
	    try {
	        Enumeration e = keyStore.aliases();
            while (e.hasMoreElements()) {
                combo.add((String)e.nextElement());
            }
        } catch (KeyStoreException e1) {
            logger.error("Can't determine keystore aliases", e1);
        }
	    
	    //combo.setItems (new String [] {"Werkbold1-Certificate"});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		combo.setLayoutData(data);
		combo.setFont(parent.getFont());

		combo.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		
		label = new Label(panel, SWT.NONE);
		label.setText(IDEWorkbenchMessages
				.getString("Certificate: ")); //$NON-NLS-1$

		text = new Text(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP |SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.FILL_HORIZONTAL);
		gd.verticalSpan = 3;
		gd.heightHint = 100;
		text.setLayoutData(gd);

    }

    protected void okPressed()
    {
         super.okPressed();
    }
    
    public String getName() {
        return combo.getText();
    }
    
    public String getCertificateText() {
        return text.getText();
    }
}
