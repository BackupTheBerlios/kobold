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
 * $Id: ScriptChooserDialog.java,v 1.10 2004/11/08 12:17:55 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.common.io.ScriptDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchPlugin;


/**
 * Dialog for choosing scripts for pre-/post-invocation of assets.
 * 
 * @author garbeam
 */
public class ScriptChooserDialog extends TitleAreaDialog
{
    public static final Log logger = LogFactory.getLog(ScriptChooserDialog.class);
 
    private AbstractAsset asset;
    protected TreeViewer viewer;
	private SDTreeItem befRoot;
	private SDTreeItem afRoot;
    
    private Button add;
    private Button remove;
    
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

		viewer = new TreeViewer(panel);
		viewer.setContentProvider(new ITreeContentProvider() {
        	protected TreeViewer viewer;	    
            public Object[] getChildren(Object parentElement) {
                if (parentElement instanceof SDTreeItem) {
                    SDTreeItem item = (SDTreeItem)parentElement;
                    if (item.getSDItems().length > 0) {
                        return item.getSDItems();
                    }
                }
                return new Object[0];
            }
            public Object getParent(Object element) {
                if (element instanceof SDTreeItem) {
                    SDTreeItem item = (SDTreeItem) element;
                    return item.getParent();
                }
                return null;
            }
            public boolean hasChildren(Object element) {
                return getChildren(element).length > 0;
            }
            
            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }
            public void dispose() {
            }
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                this.viewer = (TreeViewer)viewer;
            }
		});
		viewer.setLabelProvider(new LabelProvider() {
		    private Image sdImage =
    		        WorkbenchPlugin.getDefault().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE).createImage();
		    private Image tImage =
        			KoboldPLAMPlugin.getImageDescriptor("icons/container.gif").createImage();
		    public String getText(Object element) {
		        if (element instanceof SDTreeItem) {
		            SDTreeItem item = (SDTreeItem) element;
		            return item.getName();
		        }
		        return "unknown";
            }
            public Image getImage(Object element) {
                if (element instanceof SDTreeItem) {
                    SDTreeItem item = (SDTreeItem) element;
                    if (item.getSd() != null) {
                        return sdImage;
                    }
                    else {
                        return tImage;
                    }
                }
        		return null;
            }
        });
  
		viewer.setInput(getInitialInput());
		viewer.expandAll();
		
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
				   | GridData.FILL_HORIZONTAL);
        gd.heightHint = 200;
        viewer.getTree().setLayoutData(gd);
		
		Composite buttons = new Composite(panel, SWT.NONE);
    	layout = new GridLayout(2, true);
		layout.marginHeight =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing =
		    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing =
		    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		buttons.setLayout(layout);
		buttons.setLayoutData(new GridData(GridData.FILL_BOTH));
		buttons.setFont(parent.getFont());
		add = new Button(buttons, SWT.NONE);
		add.setText("&Assign Script...");
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    FileDialog fd = new FileDialog(getShell());
			    fd.setText("Choose your script to add");
			    String result = fd.open();
			    if (result != null) {
			        IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
			        if (sel != null) {
    			        for (Iterator iter = sel.iterator(); iter.hasNext();) {
        			        SDTreeItem sdItem = (SDTreeItem) iter.next();
        			        ScriptDescriptor sd = new ScriptDescriptor(result, sdItem.getType());
        			        sd.setPath(result);
        			        sdItem.addSDItem(new SDTreeItem(sd.getName(), sd.getVcmActionType(), sd));
    			        }
    			        viewer.refresh();
			        }
			    }
			}
		});
		remove = new Button(buttons, SWT.NONE);
		remove.setText("&Unassign Script(s)");
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			    IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
			    if (sel != null) {
			        for (Iterator iter = sel.iterator(); iter.hasNext();) {
    			        SDTreeItem item = (SDTreeItem) iter.next();
    			        if (item.getSd() != null) {
        			        item.getParent().removeSDItem(item);
    			        }
			        }
			        viewer.refresh();
			    }
			}
		});
		
        return panel;
    }
    
	private  void fillItemWithSDs(SDTreeItem item, List scripts) {
		for (Iterator iterator = scripts.iterator(); iterator.hasNext(); ) {
		    ScriptDescriptor sd = (ScriptDescriptor) iterator.next();
		    if (sd.getVcmActionType().equals(item.getType())) {
		        item.addSDItem(new SDTreeItem(sd.getName(), sd.getVcmActionType(), sd));
		    }
		}
	}
	
    private SDTreeItem getInitialInput() {
    
        SDTreeItem root = new SDTreeItem("root","root", null);
        
		befRoot = new SDTreeItem("Before VCM action", "", null);
		SDTreeItem item = new SDTreeItem(ScriptDescriptor.VCM_ADD, ScriptDescriptor.VCM_ADD, null);
		fillItemWithSDs(item, asset.getBeforeScripts());
		befRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_COMMIT, ScriptDescriptor.VCM_COMMIT, null);
		fillItemWithSDs(item, asset.getBeforeScripts());
		befRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_REMOVE, ScriptDescriptor.VCM_REMOVE, null);
		fillItemWithSDs(item, asset.getBeforeScripts());
		befRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_IMPORT, ScriptDescriptor.VCM_IMPORT, null);
		fillItemWithSDs(item, asset.getBeforeScripts());
		befRoot.addSDItem(item);
		root.addSDItem(befRoot);
				
        afRoot = new SDTreeItem("After VCM action", "", null);
        item = new SDTreeItem(ScriptDescriptor.VCM_ADD, ScriptDescriptor.VCM_ADD, null);
		fillItemWithSDs(item, asset.getAfterScripts());
		afRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_COMMIT, ScriptDescriptor.VCM_COMMIT, null);
		fillItemWithSDs(item, asset.getAfterScripts());
		afRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_REMOVE, ScriptDescriptor.VCM_REMOVE, null);
		fillItemWithSDs(item, asset.getAfterScripts());
		afRoot.addSDItem(item);
        item = new SDTreeItem(ScriptDescriptor.VCM_IMPORT, ScriptDescriptor.VCM_IMPORT, null);
		fillItemWithSDs(item, asset.getAfterScripts());
		afRoot.addSDItem(item);
		root.addSDItem(afRoot);
		
		return root;
    }

    protected void okPressed() {
    
        asset.getBeforeScripts().clear();
        for (Iterator iterator = befRoot.getIterator(); iterator.hasNext(); ) {
            SDTreeItem item = (SDTreeItem) iterator.next();
            for (Iterator it = item.getIterator(); it.hasNext();) {
                ScriptDescriptor sd = ((SDTreeItem) it.next()).getSd();
                if (sd != null) {
                    asset.getBeforeScripts().add(sd);
                }
            }
        }
    
        asset.getAfterScripts().clear();
        for (Iterator iterator = afRoot.getIterator(); iterator.hasNext(); ) {
            SDTreeItem item = (SDTreeItem) iterator.next();
            for (Iterator it = item.getIterator(); it.hasNext();) {
                ScriptDescriptor sd = ((SDTreeItem) it.next()).getSd();
                if (sd != null) {
                    asset.getAfterScripts().add(sd);
                }
            }
        }
       
        super.okPressed();
    }
}

class SDTreeItem {
    
    private String name;
    private String type;
    private List scripts = new ArrayList();
    private List sdItems = new ArrayList();
    private ScriptDescriptor sd;
    private SDTreeItem parent;

    public SDTreeItem(String name, String type, ScriptDescriptor sd)
    {
        this.name = name;
        this.type = type;
        this.sd = sd;
    }
    
    public void addSDItem(SDTreeItem item) {
        item.setParent(this);
        sdItems.add(item);
    }
    
    public void removeSDItem(SDTreeItem item) {
        item.setParent(null);
        sdItems.remove(item);
    }
    
    public Iterator getIterator() {
        return sdItems.iterator();
    }
    
    public Object[] getSDItems() {
        return sdItems.toArray();
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public SDTreeItem getParent() {
        return parent;
    }
    
    public void setParent(SDTreeItem parent) {
        this.parent = parent;
    }
    
    public ScriptDescriptor getSd() {
        return sd;
    }
}
