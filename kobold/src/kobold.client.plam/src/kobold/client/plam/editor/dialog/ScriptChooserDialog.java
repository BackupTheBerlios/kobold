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
 * $Id: ScriptChooserDialog.java,v 1.6 2004/08/31 11:03:45 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.common.io.ScriptDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;


/**
 * Dialog for choosing scripts for pre-/post-invocation of assets.
 * 
 * @author garbeam
 */
public class ScriptChooserDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(ScriptChooserDialog.class);
 
    private AbstractAsset asset;
    private TableViewer beforeScriptsViewer; 
    private TableViewer afterScriptsViewer; 
    private Table beforeScriptsTable;
    private Table afterScriptsTable;
    
    /**
     * @param parentShell
     */
    public ScriptChooserDialog(Shell parentShell, AbstractAsset asset)
    {
        super(parentShell);
        this.asset = asset;
    }

    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Script Assignment");
        setTitle("Script Assignment");
        setMessage("Assign scripts which are invoked before and after VCM actions to this asset.");
        Composite root = (Composite) super.createDialogArea(parent);
        Composite panel = new Composite(root, SWT.NONE);
        
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
		beforeScriptsTable = new Table(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		beforeScriptsTable.setLinesVisible(false);
		TableColumn colScriptNames = new TableColumn(beforeScriptsTable, SWT.NONE);
		TableColumn colActionType = new TableColumn(beforeScriptsTable, SWT.NONE);
		colScriptNames.setText("Script");
		colActionType.setText("VCM Action");
		TableLayout tableLayout = new TableLayout();
		beforeScriptsTable.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(70));
		tableLayout.addColumnData(new ColumnWeightData(30));
	    colScriptNames.pack();
	    colActionType.pack();
		
		beforeScriptsViewer = new TableViewer(beforeScriptsTable);
		beforeScriptsViewer.setUseHashlookup(true);
		
	    // Create the cell editors
	    CellEditor[] editors = new CellEditor[2];
	          
	    // Column 1 : Script name
	    TextCellEditor textEditor = new TextCellEditor(beforeScriptsTable);
	    editors[0] = textEditor;

        // Column 2 : Owner (Combo Box) 
	    editors[1] = new ComboBoxCellEditor(beforeScriptsTable,
	            				new String[] { ScriptDescriptor.VCM_CHECKOUT,
	            							   ScriptDescriptor.VCM_UPDATE,
	            							   ScriptDescriptor.VCM_ADD,
	            							   ScriptDescriptor.VCM_DELETE,
	            							   ScriptDescriptor.VCM_COMMIT,
	            							   ScriptDescriptor.VCM_IMPORT },
	            				SWT.READ_ONLY);
		
	    beforeScriptsViewer.setCellEditors(editors);
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
        
		/// After Scripts table
		afterScriptsTable = new Table(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		afterScriptsTable.setLinesVisible(false);
		colScriptNames = new TableColumn(beforeScriptsTable, SWT.NONE);
		colActionType = new TableColumn(beforeScriptsTable, SWT.NONE);
		colScriptNames.setText("Script");
		colActionType.setText("VCM Action");
		tableLayout = new TableLayout();
		beforeScriptsTable.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(70));
		tableLayout.addColumnData(new ColumnWeightData(30));
	    colScriptNames.pack();
	    colActionType.pack();
		
		afterScriptsViewer = new TableViewer(afterScriptsTable);
		afterScriptsViewer.setUseHashlookup(true);
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
	    
		Composite leftButtons = new Composite(panel, SWT.NONE);
    	layout = new GridLayout(2, true);
		layout.marginHeight =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		leftButtons.setLayout(layout);
		leftButtons.setLayoutData(new GridData(GridData.FILL_BOTH));
		leftButtons.setFont(parent.getFont());
		Button add = new Button(leftButtons, SWT.NONE);
		add.setText("&Add...");
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    FileDialog fd = new FileDialog(getShell());
			    fd.setText("Choose your script to add");
			    String result = fd.open();
			    if (result != null) {
			        ScriptDescriptor sd = new ScriptDescriptor(result, ScriptDescriptor.VCM_CHECKOUT);
			        sd.setPath(result);
			        beforeScriptsViewer.add(sd);
			    }
			}
		});
		Button remove = new Button(leftButtons, SWT.NONE);
		remove.setText("Remove");
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    IStructuredSelection selection = (IStructuredSelection)beforeScriptsViewer.getSelection();
			    if (selection != null && selection.size() > 0) {
			        beforeScriptsViewer.remove(selection.toArray());
			    }
			}
		});
		
        Composite rightButtons = new Composite(panel, SWT.NONE);
    	layout = new GridLayout(2, true);
		layout.marginHeight =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		rightButtons.setLayout(layout);
		rightButtons.setLayoutData(new GridData(GridData.FILL_BOTH));
		rightButtons.setFont(parent.getFont());
		add = new Button(rightButtons, SWT.NONE);
		add.setText("&Add...");
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    FileDialog fd = new FileDialog(getShell());
			    fd.setText("Choose your script to add");
			    String result = fd.open();
			    if (result != null) {
			        ScriptDescriptor sd = new ScriptDescriptor(result, ScriptDescriptor.VCM_CHECKOUT);
			        sd.setPath(result);
			        afterScriptsViewer.add(sd);
			    }
			}
		});
		remove = new Button(rightButtons, SWT.NONE);
		remove.setText("Remove");
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    IStructuredSelection selection = (IStructuredSelection)afterScriptsViewer.getSelection();
			    if (selection != null && selection.size() > 0) {
			        afterScriptsViewer.remove(selection.toArray());
			    }
			}
		});
		
		beforeScriptsViewer.add(asset.getBeforeScripts().toArray());
		afterScriptsViewer.add(asset.getAfterScripts().toArray());
		
        return panel;
    }
    
    protected void okPressed() {
        List befScripts = asset.getBeforeScripts();
        List afScripts = asset.getAfterScripts();
        befScripts.clear();
        afScripts.clear();
        for (int i = 0; i < beforeScriptsViewer.getTable().getItemCount(); i++) {
            befScripts.add(beforeScriptsViewer.getElementAt(i));
        }
        for (int i = 0; i < afterScriptsViewer.getTable().getItemCount(); i++) {
            afScripts.add(afterScriptsViewer.getElementAt(i));
        }
        super.okPressed();
    }
}
