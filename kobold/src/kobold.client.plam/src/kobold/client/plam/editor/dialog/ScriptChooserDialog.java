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
 * $Id: ScriptChooserDialog.java,v 1.2 2004/08/30 12:06:58 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.common.data.User;
import kobold.common.io.ScriptDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Dialog for choosing scripts for pre-/post-invocation of assets.
 * 
 * @author garbeam
 */
public class ScriptChooserDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(ScriptChooserDialog.class);
 
    private AbstractAsset asset;
    private CheckboxTableViewer beforeScriptsViewer; 
    private CheckboxTableViewer afterScriptsViewer; 
    private Map beforeScripts;
    private Map afterScripts;
    private Table beforeScriptsTable;
    private Table afterScriptsTable;
    
    /**
     * @param parentShell
     */
    public ScriptChooserDialog(Shell parentShell, AbstractAsset asset)
    {
        super(parentShell);
        this.asset = asset;
        beforeScripts = asset.getRoot().getKoboldProject().getBeforeScripts();
        afterScripts = asset.getRoot().getKoboldProject().getAfterScripts();
    }

    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Script Assignment");
        setTitle("Script Assignment");
        setMessage("Assign scripts which are invoked before and after VCM actions to this asset.");
        Composite panel = (Composite) super.createDialogArea(parent);
        
    	GridLayout layout = new GridLayout(2, true);
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

		Label label = new Label(panel, SWT.NONE);
		label.setText("Invoke before VCM action");
		
		label = new Label(panel, SWT.NONE);
		label.setText("Invoke after VCM action");
		
		/// Before Scripts table
		beforeScriptsTable = new Table(panel, SWT.CHECK | SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		beforeScriptsTable.setLinesVisible(false);
		TableColumn colScriptNames = new TableColumn(beforeScriptsTable, SWT.NONE);
		colScriptNames.setText("Script");
		TableLayout tableLayout = new TableLayout();
		beforeScriptsTable.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(100));
		
	    colScriptNames.pack();
		
		beforeScriptsViewer = new CheckboxTableViewer(beforeScriptsTable);
		beforeScriptsViewer.setLabelProvider(new LabelProvider() {
		    private Image image;
		    public String getText(Object element) {
		        ScriptDescriptor sd = (ScriptDescriptor)element;
                return sd.getName();
            }
            
            public Image getImage(Object element) {
                if (image == null) {
        			image = KoboldPLAMPlugin.getImageDescriptor("icons/package.gif").createImage();
        		}
        		return image;
            }
        });
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
		        				   | GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		beforeScriptsTable.setLayoutData(gd);
	    
		if (beforeScripts != null) {
		    beforeScriptsViewer.add(beforeScripts.values().toArray());
	    }
	    // TODO: beforeScriptsViewer.setCheckedElements(asset.getBeforeScripts().toArray());
        
		/// After Scripts table
		afterScriptsTable = new Table(panel, SWT.CHECK | SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		afterScriptsTable.setLinesVisible(false);
		colScriptNames = new TableColumn(afterScriptsTable, SWT.NONE);
		colScriptNames.setText("Script");
		tableLayout = new TableLayout();
		afterScriptsTable.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(100));
		
	    colScriptNames.pack();
		
		afterScriptsViewer = new CheckboxTableViewer(afterScriptsTable);
		afterScriptsViewer.setLabelProvider(new LabelProvider() {
		    private Image image;
		    public String getText(Object element) {
		        ScriptDescriptor sd = (ScriptDescriptor)element;
                return sd.getName();
            }
            
            public Image getImage(Object element) {
                if (image == null) {
        			image = KoboldPLAMPlugin.getImageDescriptor("icons/package.gif").createImage();
        		}
        		return image;
            }
        });
		gd = new GridData(GridData.GRAB_HORIZONTAL
		        				   | GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		afterScriptsTable.setLayoutData(gd);
	    
		if (afterScripts != null) {
		    afterScriptsViewer.add(afterScripts.values().toArray());
	    }
	    // TODO: afterScriptsViewer.setCheckedElements(asset.getafterScripts().toArray());
        return panel;
    }
    
    protected void okPressed()
    {
    // TODO: write selection to model
        super.okPressed();
    }
    
}
