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
 * grosseml 23.08.2004
 */
package kobold.client.plam.editor.dialog;


import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.FileDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
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
 * Provides a Dialog for selecting the files of a variant
 * 
 * @author grosseml
 */
public class FileSelectorDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(EditMaintainerDialog.class);
 
    private AbstractMaintainedAsset asset;
    private CheckboxTableViewer allFileViewer;
    private Table allFiles;
    private IFolder aFolder;
    
    
    /**
     * @param parentShell
     */
    public FileSelectorDialog(Shell parentShell, AbstractMaintainedAsset asset)
    {
        super(parentShell);
        this.asset = asset;
	    //userPool = asset.getRoot().getKoboldProject().getUserPool();
    }

    protected Control createDialogArea(Composite parent)
    {
        setTitle("Select Files");
        setMessage("Manages the files belonging to this asset. Beware: only files from the" +
        		"asset folder can be chosen.");
        Composite composite = (Composite) super.createDialogArea(parent);
        
        createFileLists(composite);
        return composite;
    }
    
    private void createFileLists(Composite parent) {
        
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

		Label label = new Label(panel, SWT.NONE);
		label.setText("Available files");
		
		allFiles = new Table(panel, SWT.CHECK | SWT.BORDER | SWT.LEAD | SWT.WRAP 
		        				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		allFiles.setLinesVisible(false);
		TableColumn colUserNames = new TableColumn(allFiles, SWT.NONE);
		colUserNames.setText("Files");
		TableLayout tableLayout = new TableLayout();
		allFiles.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(100));
		
	    colUserNames.pack();
		
		allFileViewer = new CheckboxTableViewer(allFiles);
		allFileViewer.setLabelProvider(new LabelProvider() {
		    private Image image;
		    public String getText(Object element) {
                //User user = (User)element;     
                //return user.getUsername() + " (" + user.getFullname() + ")";
		    	FileDescriptor fd = (FileDescriptor)element;
		    	return fd.getFilename();
		    	
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
		allFiles.setLayoutData(gd);
	    
		//HIER BITTE DEN ENTSPRECHENDEN CODE EINFÜGEN!!!!
		//get asset folder
		//IFolder aFolder = 
		
		try{
			if (aFolder != null) {
		    	allFileViewer.add(aFolder.members());
	    	}
			//HIER FEHLT NOCH DER CONTAINER FÜR DIE FILEDESKRIPTOREN
	    	//allFileViewer.setCheckedElements("HIER DEN CONTAINER DER FILEDESKRIPTOREN");
		} catch (CoreException e) {
            KoboldPLAMPlugin.log(e);
        } finally {
        }
    }
 
    protected void okPressed()
    {
        asset.getMaintainers().clear();
        Object[] checkedElems = allFileViewer.getCheckedElements();
        for (int i = 0; i < checkedElems.length; i++) {
            //HIER NOCH DIE GEWÄHLTEN DATEIEN IN DEN CONTAINER EINFÜGEN
        }
        super.okPressed();
    }
    
}