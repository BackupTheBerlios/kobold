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
 * $Id: AssetConfigurationDialog.java,v 1.4 2004/08/04 17:52:58 vanto Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.Iterator;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.common.data.User;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;


/**
 * Provides a Dialog for editing all asset properties.
 * 
 * @author Tammo
 */
public class AssetConfigurationDialog extends TitleAreaDialog
{

    private AbstractAsset asset;
    private Text name;
    private Text description;
    private List list;
    
    /**
     * @param parentShell
     */
    public AssetConfigurationDialog(Shell parentShell, AbstractAsset asset)
    {
        super(parentShell);
        this.asset = asset;
    }

    protected Control createDialogArea(Composite parent)
    {
        setTitle("Asset Configuration");
        setMessage("Please configure your " + asset.getType());
        Composite composite = (Composite) super.createDialogArea(parent);
        createAssetProps(composite);
        return composite;
    }
    
    private void createAssetProps(final Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		boolean withMaintainer = (asset instanceof AbstractMaintainedAsset);
		
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
				.getString("Name:")); //$NON-NLS-1$

		name = new Text(panel, SWT.BORDER | SWT.LEAD);
		name.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		
		if (asset.getName() != null) {
		    name.setText(asset.getName());
		}
		
		label = new Label(panel, SWT.NONE);
		label.setText(IDEWorkbenchMessages
				.getString("Description:")); //$NON-NLS-1$

		description = new Text(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP
		                              | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;
		description.setLayoutData(gd);
		
		if (asset.getDescription() != null) {
		    description.setText(asset.getDescription());
		}

		if (withMaintainer) {
		    final AbstractMaintainedAsset maintainedAsset = (AbstractMaintainedAsset) asset;
		    label = new Label(panel, SWT.NONE);
		    label.setText("Maintainer:");
		
		    list = new List(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP 
		                           | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
			gd = new GridData(GridData.GRAB_HORIZONTAL
					| GridData.FILL_HORIZONTAL);
			gd.heightHint = 100;
			list.setLayoutData(gd);
		
			refreshMaintainers(maintainedAsset);
			
			Button edit = new Button(panel, SWT.NONE);
			edit.setText("&Edit...");
			edit.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
				    EditMaintainerDialog dlg =
				        new EditMaintainerDialog(parent.getShell(), maintainedAsset);
				    dlg.open();
					refreshMaintainers(maintainedAsset);
				}
			});
		}
    }
    
    private void refreshMaintainers(AbstractMaintainedAsset asset) {
        list.removeAll();
		for (Iterator iterator = asset.getMaintainers().iterator();
			 iterator.hasNext(); )
		{
		    User user = (User) iterator.next();
		    list.add(user.getFullname());
		}
    }

    protected void okPressed()
    {
        if (!name.getText().equals(asset.getName())) {
            asset.setName(name.getText());
        }

        if (!description.getText().equals(asset.getDescription())) {
            asset.setDescription(description.getText());
        }

        super.okPressed();
    }
}
