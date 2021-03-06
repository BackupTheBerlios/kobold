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
 * $Id: AssetConfigurationDialog.java,v 1.41 2005/02/04 15:50:19 neco Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.AbstractStatus;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.common.data.User;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;


/**
 * Provides a Dialog for editing all asset properties.
 * 
 * @author Tammo
 */
public class AssetConfigurationDialog extends TitleAreaDialog
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AssetConfigurationDialog.class);

    private AbstractAsset asset;
    private Text resource;
    private Text name;
    private Text description;
    private Button deprecated;
	private Button released;
    
    private CheckboxTableViewer cbViewer;
    private TableViewer viewer;
    private Table maintainer;
    
	private Listener textModifyListener = new Listener() {
		public void handleEvent(Event e) {
			if (validatePage()) {
			    getButton(IDialogConstants.OK_ID).setEnabled(true);
			} else {
			    getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
	};
	
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
        getShell().setText("Asset Configuration");
        setMessage("Please configure your " + asset.getType());
        Composite composite = (Composite) super.createDialogArea(parent);
        createAssetProps(composite);
        return composite;
    }
    
    private void createAssetProps(final Composite parent) {
		final Composite panel = new Composite(parent, SWT.NONE);
		
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
		name.addModifyListener(  new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (!(asset instanceof Release) && !asset.isResourceDefined())
                {                    
                        resource.setText(generateResourceName(name.getText()));;
                	//resource name now generated automaically
                	
                }
            }
		  });
		
		GridData gd = null;

		//FIX for products --> doen't work??
		//release/product
		if (asset instanceof Release) {
			//asset.setName("product");
		}

		
		if (!(asset instanceof Release)) {
    		label = new Label(panel, SWT.NONE);
    		label.setText("Resource:");
    		
    		resource = new Text(panel, SWT.BORDER | SWT.LEAD);
    		//disabled by default to set the resource automatically
    		//resource.setEnabled(!asset.isResourceDefined());
    		resource.setEnabled(false);
    		
    		gd = new GridData(GridData.GRAB_HORIZONTAL
    		        | GridData.FILL_HORIZONTAL);
    		resource.setLayoutData(gd);
    		
    		
    		
    		//set the resource name
    		if (asset.getResource() != null) {
    		    resource.setText(asset.getResource());
    		} else {
    		    //create a new resource name for the asset
    		    //1) erase all blanks and special chars
    		    //		2) test if resource name still exist
    		    resource.setText(generateResourceName(asset.getName()));
    		}
		}

		
		label = new Label(panel, SWT.NONE);
		label.setText(IDEWorkbenchMessages
				.getString("Description:")); //$NON-NLS-1$

		description = new Text(panel, SWT.BORDER | SWT.LEAD | SWT.WRAP
		                              | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		gd = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;
		description.setLayoutData(gd);
		
		if (asset.getDescription() != null) {
		    description.setText(asset.getDescription());
		}

		if (!(asset instanceof Productline)) {
		    label = new Label(panel, SWT.NONE);
		    label.setText("Deprecated:");
		    
		    deprecated = new Button(panel, SWT.CHECK);
			deprecated.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
					| GridData.FILL_HORIZONTAL));
			
			deprecated.setSelection(AbstractStatus.isDeprecated(asset));
			if (AbstractStatus.isDeprecated(asset.getParent())) {
			    deprecated.setEnabled(false);
			} else {
			    deprecated.setEnabled(true);
			}
		}

		name.addListener(SWT.Modify, textModifyListener);
		if (!(asset instanceof Release)) {
    		resource.addListener(SWT.Modify, textModifyListener);
		}
		
		if (asset instanceof AbstractMaintainedAsset) {
		    createMaintainerArea(panel);
		}
		else if (asset instanceof Release) {
		    createReleaseArea(panel);
		}
		else if (asset instanceof Variant) {
    		createVariantArea(panel);
		}
		
		if (asset instanceof AbstractRootAsset){

			Composite buttonPanel = new Composite(panel, SWT.NONE);
	    	layout = new GridLayout(2, false);
			layout.marginHeight =
			    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
			layout.marginWidth =
			    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.verticalSpacing =
			    convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
			layout.horizontalSpacing =
			    convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
			buttonPanel.setLayout(layout);
			buttonPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
			buttonPanel.setFont(parent.getFont());

			Button scripts = new Button(buttonPanel, SWT.NONE);
			scripts.setText("&VCM Scripts");
			scripts.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
				    ScriptChooserDialog dlg =
				        new ScriptChooserDialog(panel.getShell(), asset);
				    dlg.open();
				}
			});
	        
			if (asset instanceof Product) {
	    		Button repDesc = new Button(buttonPanel, SWT.NONE);
	    		repDesc.setText("&Repository...");
	    		repDesc.addSelectionListener(new SelectionAdapter() {
	    			public void widgetSelected(SelectionEvent event) {
	    			    EditRepositoryDescriptorDialog erdd =
	    			        new EditRepositoryDescriptorDialog(panel.getShell(), asset);
	    			    erdd.open();
	    			}
	    		});
	   		}
			
		}
		else{
			Button scripts = new Button(panel, SWT.NONE);
			scripts.setText("&VCM Scripts");
			scripts.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
				    ScriptChooserDialog dlg =
				        new ScriptChooserDialog(panel.getShell(), asset);
				    dlg.open();
				}
			});
	        
			if (asset instanceof Product) {
	    		Button repDesc = new Button(panel, SWT.NONE);
	    		repDesc.setText("&Repository...");
	    		repDesc.addSelectionListener(new SelectionAdapter() {
	    			public void widgetSelected(SelectionEvent event) {
	    			    EditRepositoryDescriptorDialog erdd =
	    			        new EditRepositoryDescriptorDialog(panel.getShell(), asset);
	    			    erdd.open();
	    			}
	    		});
	   		}
		}
    }
    
	/**
	 * prints the root node and all children
	 * @param fd
	 */
	private void prettyPrintFD(FileDescriptor fd, String prefix) {
	    String newPrefix = prefix + IPath.SEPARATOR +   fd.getFilename();
		if (logger.isDebugEnabled()) {
			logger.debug("prettyPrintFD(FileDescriptor, String) - fd: "
					+ newPrefix
					+ "t"
					+ fd.getRevision()
					+ ((fd.getLastChange() != null) ? "t"
							+ fd.getLastChange().toString() : ""));
		}

	    //get all children
	    for (Iterator iterator = fd.getFileDescriptors().iterator();
	         iterator.hasNext(); )
	    {
	    	FileDescriptor fdActual = (FileDescriptor)iterator.next();
	    	
	        prettyPrintFD(fdActual, newPrefix);

	    }
	}
	
    private void createVariantArea(Composite composite) {
        
        final Variant variant = (Variant) asset;
        
        
    }
    
    private void createReleaseArea(Composite composite) {
    
        final Release release = (Release) asset;
        final Variant variant = (Variant) release.getParent();
  	    Label label = new Label(composite, SWT.NONE);
	      
	    if (release.isReleased()) {
	        
    	    label.setText("File revision:");
            Table table = new Table(composite, SWT.BORDER | SWT.MULTI
                    				| SWT.FULL_SELECTION | SWT.LEAD | SWT.WRAP
                    				| SWT.V_SCROLL | SWT.VERTICAL);
            table.setHeaderVisible(true);
            table.setLinesVisible(true);
            TableColumn col1 = new TableColumn(table, SWT.NONE);
            col1.setText("Name");
            TableColumn col2 = new TableColumn(table, SWT.NONE);
            col2.setText("Revision");
    		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
    		        				   | GridData.FILL_HORIZONTAL);
    		gd.heightHint = 200;
    	    table.setLayoutData(gd);
    	    TableLayout layout = new TableLayout();
    	    layout.addColumnData(new ColumnWeightData(50));
    	    layout.addColumnData(new ColumnWeightData(50));
    	    table.setLayout(layout);
    	    TableViewer tbViewer = new TableViewer(table);
            tbViewer.setContentProvider(new IStructuredContentProvider() {
                public Object[] getElements(Object input) {
                    if (input instanceof Release) {
                        Release release = (Release) input;
                        return release.getFileRevisions().toArray();
                    }
                    return new Object[0];
                }
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    //                 don't need to hang onto input for this example, so do nothing
                }
                public void dispose() {
                }
            });
            tbViewer.setLabelProvider(new ITableLabelProvider() {
                public String getColumnText(Object element, int columnIndex) {
                    Release.FileRevision fr = (Release.FileRevision) element;
                    switch (columnIndex) {
                    case 0 :
                        return fr.getPath();
                    case 1 :
                        return (fr.getRevision() != null) ? fr.getRevision() : "";
                    default :
                        return "";
                    }
                }
                public Image getColumnImage(Object element, int columnIndex) {
                    return null;
                }
                public void addListener(ILabelProviderListener listener) {
                    // TODO Auto-generated method stub
                    
                }
                public void dispose() {
                    // TODO Auto-generated method stub
                    
                }
                public boolean isLabelProperty(Object element, String property) {
                    // TODO Auto-generated method stub
                    return false;
                }
                public void removeListener(ILabelProviderListener listener) {
                    // TODO Auto-generated method stub
                    
                }
            });
            tbViewer.addSelectionChangedListener(new ISelectionChangedListener() {
                public void selectionChanged(SelectionChangedEvent event) {
                }
            });
            tbViewer.setInput(release);
	    }
	    else {
    	    label.setText("Select resources:");
            Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.MULTI
                    				| SWT.FULL_SELECTION | SWT.LEAD | SWT.WRAP
                    				| SWT.V_SCROLL | SWT.VERTICAL);
            table.setHeaderVisible(true);
            table.setLinesVisible(true);
            TableColumn col1 = new TableColumn(table, SWT.NONE);
            col1.setText("Name");
            TableColumn col2 = new TableColumn(table, SWT.NONE);
            col2.setText("Revision");
            TableColumn col3 = new TableColumn(table, SWT.NONE);
            col3.setText("Date");
    		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
    		        				   | GridData.FILL_HORIZONTAL);
    		gd.heightHint = 200;
    	    table.setLayoutData(gd);
    	    TableLayout layout = new TableLayout();
    	    layout.addColumnData(new ColumnWeightData(33));
    	    layout.addColumnData(new ColumnWeightData(33));
    	    layout.addColumnData(new ColumnWeightData(33));
    	    table.setLayout(layout);
            cbViewer = new CheckboxTableViewer(table);
            cbViewer.setContentProvider(new IStructuredContentProvider() {
                public void prepList(List l, FileDescriptor fd) {
                    if (fd == null) {
                        return;
                    }
                    List fds = fd.getFileDescriptors();
                    if (fds != null) {
	                    for (Iterator it = fds.iterator(); it.hasNext();) {
	                        prepList(l, (FileDescriptor)it.next());
	                    }
                    }
                    //to avoid directories in the list
                    if(fd.isDirectory()){
                    	return;
                    }
                    l.add(fd);
                }
                public Object[] getElements(Object input) {
                    if (input instanceof Variant) {
                        List l = new ArrayList();
                        Variant variant = (Variant) input;
                        for (Iterator it = variant.getFileDescriptors().iterator(); it.hasNext();) {
                			prepList(l, (FileDescriptor)it.next());
            			}
                        return l.toArray();
                    }
                    else if (input instanceof IFileDescriptorContainer) {
                        List l = new ArrayList();
                        IFileDescriptorContainer fd = (IFileDescriptorContainer) input;
                        for (Iterator it = fd.getFileDescriptors().iterator(); it.hasNext();) {
                			prepList(l, (FileDescriptor)it.next());
            			}
                        return l.toArray();
                    }
                    return new Object[0];
                }
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    //                 don't need to hang onto input for this example, so do nothing
                }
                public void dispose() {
                }
            });
            cbViewer.setLabelProvider(new ITableLabelProvider() {
                DateFormat dateFormat = DateFormat.getInstance();
                public String getColumnText(Object element, int columnIndex) {
                    FileDescriptor fd = (FileDescriptor) element;
                    switch (columnIndex) {
                    case 0 :
                        return fd.getFilename();
                    case 1 :
                        return (fd.getRevision() != null) ? fd.getRevision() : "";
                    case 2 :
                        return (fd.getLastChange() != null) ? 
                                dateFormat.format(fd.getLastChange()) : "";
                    default :
                        return "";
                    }
                }
                public Image getColumnImage(Object element, int columnIndex) {
                    return null;
                }
                public void addListener(ILabelProviderListener listener) {
                    // TODO Auto-generated method stub
                    
                }
                public void dispose() {
                    // TODO Auto-generated method stub
                    
                }
                public boolean isLabelProperty(Object element, String property) {
                    // TODO Auto-generated method stub
                    return false;
                }
                public void removeListener(ILabelProviderListener listener) {
                    // TODO Auto-generated method stub
                    
                }
            });
            cbViewer.addSelectionChangedListener(new ISelectionChangedListener() {
                public void selectionChanged(SelectionChangedEvent event) {
                }
            });
            cbViewer.setInput(variant);
            label = new Label(composite, SWT.NONE);
            label.setText("Release:");
    		released = new Button(composite, SWT.CHECK);
    		released.setText("");
	    }
    }
    
    private void createMaintainerArea (final Composite composite) {
        
        final AbstractMaintainedAsset maintainedAsset = (AbstractMaintainedAsset) asset;
	    Label label = new Label(composite, SWT.NONE);
	    label.setText("Maintainer:");
	
		maintainer = new Table(composite, SWT.BORDER | SWT.LEAD | SWT.WRAP 
				   | SWT.MULTI | SWT.V_SCROLL | SWT.VERTICAL);
		maintainer.setLinesVisible(false);
        TableColumn colUserNames = new TableColumn(maintainer, SWT.NONE);
        colUserNames.setText("User");
        TableLayout tableLayout = new TableLayout();
        maintainer.setLayout(tableLayout);
        tableLayout.addColumnData(new ColumnWeightData(100));
        colUserNames.pack();

        viewer = new TableViewer(maintainer);
        //allUserViewer.getTable().getLayoutData().
        viewer.setLabelProvider(new LabelProvider() {
        private Image image;
        public String getText(Object element) {
        User user = (User)element;     
        return user.getUsername() + " (" + user.getFullname() + ")";
        }
        
        public Image getImage(Object element) {
        if (image == null) {
        	image = KoboldPLAMPlugin.getImageDescriptor("icons/user.gif").createImage();
        }
        return image;
        }
        });
        GridData gd = new GridData(GridData.GRAB_HORIZONTAL
        				   | GridData.FILL_HORIZONTAL);
        gd.heightHint = 100;
        maintainer.setLayoutData(gd);
		refreshMaintainers(maintainedAsset);
		
		Button edit = new Button(composite, SWT.NONE);
		edit.setText("&Edit Maintainer");
		edit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
		        KoboldProject proj = KoboldPLAMPlugin.getCurrentKoboldProject();
		        User user = (User)proj.getUserPool().get(proj.getUserName());
		        Shell shell = Display.getDefault().getActiveShell();
		        boolean isAllowed = false;
		        AbstractAsset a = asset;
		        while (a != null) {
		            if (a instanceof AbstractMaintainedAsset) {
		                AbstractMaintainedAsset ama = (AbstractMaintainedAsset)a;
    		            if (ama.getMaintainers().contains(user)) {
    		                isAllowed = true;
    		                break;
    		            }
		            }
		            a = a.getParent();
		        }
		        if (!isAllowed) {
		            MessageDialog.openError(shell, "Permission denied",
		                    "You don't have permission to perform this action.");
		            return;
		        }
			    EditMaintainerDialog dlg =
			        new EditMaintainerDialog(composite.getShell(), maintainedAsset);
			    dlg.open();
				refreshMaintainers(maintainedAsset);
			}
		});
    }
    
    private void refreshMaintainers(AbstractMaintainedAsset asset) {
        viewer.getTable().removeAll();
        viewer.add(asset.getMaintainers().toArray());
    }

    protected void okPressed()
    {
        if (!name.getText().equals(asset.getName())) {
            asset.setName(name.getText());                               
        }

        if (!description.getText().equals(asset.getDescription())) {
            asset.setDescription(description.getText());
        }
        
        if (deprecated != null) {
	        boolean dep = AbstractStatus.isDeprecated(asset); 
	        if (deprecated.getSelection() && !dep) {
	            makeAssetDeprecated(asset);
	        } else if (!deprecated.getSelection() && dep) {
	            asset.removeStatus(AbstractStatus.DEPRECATED_STATUS);
	        }
        }
        
        if (asset instanceof Release) {
            Release release = (Release) asset;
            if (!release.isReleased()) {
                Object[] result = cbViewer.getCheckedElements();
                for (int i = 0; i < result.length; i++) {
                    FileDescriptor fd = (FileDescriptor) result[i];
                    release.addFileRevision(
                        new Release.FileRevision(fd.getFilename(),
                        						 fd.getRevision()));
                }
                if ((released != null) && released.getSelection()) {
                    release.setReleased(true);
                    Productline pl = release.getParent().getRoot().getProductline();
                    pl.getKoboldProject().tagRelease(release);
                }
            }
        }
        else if (!resource.getText().equals(asset.getResource())) {
            asset.setResource(resource.getText());
        }
        
        if (!asset.isResourceDefined()) {
            asset.setResourceDefined(true);
        }
        super.okPressed();
        
        // store Model
        //this.asset.getRoot().getKoboldProject().store();
        ModelStorage.storeModel(asset.getRoot().getProductline());
    }
    
    private void makeAssetDeprecated(AbstractAsset asset) 
    {
        asset.addStatus(AbstractStatus.DEPRECATED_STATUS);
        List assetList = new ArrayList();
        if (asset instanceof IComponentContainer) {
            assetList.addAll(((IComponentContainer)asset).getComponents());
        }
        if (asset instanceof IVariantContainer) {
            assetList.addAll(((IVariantContainer)asset).getVariants());
        }
        if (asset instanceof IReleaseContainer) {
            assetList.addAll(((IReleaseContainer)asset).getReleases());
        }
        if (asset instanceof ProductComponent) {
            assetList.addAll(((ProductComponent)asset).getProductComponents());
        }
        

        Iterator it = assetList.iterator();
        while (it.hasNext()) {
            makeAssetDeprecated((AbstractAsset)it.next());
        }
    }
    
    private boolean validatePage()
    {
        //check the name
    	String str = name.getText();
        if (str.length() == 0) {
            setMessage("Name should not be empty.",  IMessageProvider.WARNING);
            return false;
        }
        if (! (asset instanceof Release)){
            //check the resource
            str = resource.getText();
            if (str.length() == 0) {
                setErrorMessage("Resource must not be empty - please choose an unique resource name.");
                return false;
            }
        }

        
    	setErrorMessage(null);
		setMessage(null);
        return true;
    }
    
    /**
     * Generated unique resource names
     * @param name the name of the resource
     * @return the generated resourceName
     */
    private String generateResourceName (String name)
    {
    	String resName = name;
    	//convert all to lower case
    	resName = resName.toLowerCase();
    	
    	//replace special chars
    	//do a search for special chars
    	for (int count=0; count <= (resName.length()-1); count++)
    	{
	    	char teil = resName.charAt(count);
	    	boolean what = Character.isLetterOrDigit(teil);
	    	if (!what)
	    	{
	    		what = Character.isWhitespace(teil);
	    	}
	    	if (!what)
	    	{
	    		resName = resName.substring(0,count).
						  concat(resName.substring(count+1, resName.length()));
	    	}
	    	
    	}

    	//replace all white spaces
    	resName = resName.replaceAll(" ","_");

    	resName = avoidEqualRecourceNames(resName);
    	
    	//returns the resourceName
    	return resName; 
    }
    
    String avoidEqualRecourceNames(String resName) {
         //      get all children of the parent
        List assets;
        if (asset instanceof Product){
    	    assets =  ((Product)asset).getProductline().getProducts();
        } else{
            assets = asset.getParent().getChildren();
        }
    	Iterator it = assets.listIterator();
    	while (it.hasNext())
    	{
    		AbstractAsset asset = (AbstractAsset)it.next();
    		if (asset.getResource() != null)
			{
        		//System.out.println ("HALLO.element: "+asset.getResource());
    			if ((asset.getResource()).equals(resName))
				{
					//System.out.println ("HALLO.dupl: "+resName);
    				//rename the resourceName
    				resName = resName.concat("_1");
    				//do return
    				return avoidEqualRecourceNames(resName);    				
				}
			}
    	}
    	//returns the resourceName
    	return resName;
    }
}
