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
 * $Id: AssetConfigurationDialog.java,v 1.7 2004/08/05 14:19:36 garbeam Exp $
 *
 */
package kobold.client.plam.editor.dialog;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.productline.Variant;
import kobold.common.data.User;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Label;
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

    private AbstractAsset asset;
    private Text resource;
    private Text name;
    private Text description;
    private TableViewer viewer;
    private Table maintainer;
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
		label.setText("Resource:");
    
		resource = new Text(panel, SWT.BORDER | SWT.LEAD);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.FILL_HORIZONTAL);
		resource.setLayoutData(gd);
    		
		if (asset.getResource() != null) {
		    resource.setText(asset.getResource());
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

		if (asset instanceof AbstractMaintainedAsset) {
		    createMaintainerArea(panel);
		}
		else if (asset instanceof Release) {
		    createReleaseArea(panel);
		}
		else if (asset instanceof Variant) {
    		createVariantArea(panel);
		}
    }
    
    private void createVariantArea(Composite composite) {
        
        final Variant variant = (Variant) asset;
        
        for (Iterator iterator = variant.getFileDescriptors().iterator();
        	 iterator.hasNext(); )
        {
            FileDescriptor fd = (FileDescriptor)iterator.next();
            System.out.println("fd: " + fd.getFilename() + ", rev=" + fd.getRevision());
        }
        
  	    Label label = new Label(composite, SWT.NONE);
	    label.setText("Select resources:");
        Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.MULTI
                				| SWT.FULL_SELECTION | SWT.LEAD | SWT.WRAP
                				| SWT.V_SCROLL | SWT.VERTICAL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn col1 = new TableColumn(table, SWT.NONE);
        col1.setText("Name");
        TableColumn col2 = new TableColumn(table, SWT.NONE);
        col2.setText("Size");
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
        final CheckboxTableViewer viewer = new CheckboxTableViewer(table);
        viewer.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object input) {
                if (input instanceof File) {
                    File dir = (File) input;
                    String[] names = dir.list();
                    if (names != null) {
                        File[] files = new File[names.length];
                        for (int i = 0; i < names.length; i++)
                            files[i] = new File(dir, names[i]);
                        return files;
                    }
                }
                return new File[0];
            }
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//                 don't need to hang onto input for this example, so do nothing
            }
            public void dispose() {
            }
        });
        viewer.setLabelProvider(new ITableLabelProvider() {
            DateFormat dateFormat = DateFormat.getInstance();
            public String getColumnText(Object element, int columnIndex) {
                File file = (File) element;
                switch (columnIndex) {
                case 0 :
                    return file.getName();
                case 1 :
                    return file.isDirectory() ? ""
                            : ((file.length() + 1023) / 1024) + " KB ";
                case 2 :
                    Date date = new Date(file.lastModified());
                    return dateFormat.format(date);
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
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection sel =
                    (IStructuredSelection) event.getSelection();
                System.out.println(
                    sel.size()
                    + " items selected, "
                    + viewer.getCheckedElements().length
                    + " items checked");
            }
        });
        
        viewer.setInput(new File(variant.getLocalPath().toOSString()));    
    }
    
    private void createReleaseArea(Composite composite) {
    
        final Release release = (Release) asset;
  	    Label label = new Label(composite, SWT.NONE);
	    label.setText("Select resources:");
	      
        Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.MULTI
                				| SWT.FULL_SELECTION | SWT.LEAD | SWT.WRAP
                				| SWT.V_SCROLL | SWT.VERTICAL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn col1 = new TableColumn(table, SWT.NONE);
        col1.setText("Name");
        TableColumn col2 = new TableColumn(table, SWT.NONE);
        col2.setText("Size");
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
        final CheckboxTableViewer viewer = new CheckboxTableViewer(table);
        viewer.setContentProvider(new IStructuredContentProvider() {
            public Object[] getElements(Object input) {
                if (input instanceof File) {
                    File dir = (File) input;
                    String[] names = dir.list();
                    if (names != null) {
                        File[] files = new File[names.length];
                        for (int i = 0; i < names.length; i++)
                            files[i] = new File(dir, names[i]);
                        return files;
                    }
                }
                return new File[0];
            }
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//                 don't need to hang onto input for this example, so do nothing
            }
            public void dispose() {
            }
        });
        viewer.setLabelProvider(new ITableLabelProvider() {
            DateFormat dateFormat = DateFormat.getInstance();
            public String getColumnText(Object element, int columnIndex) {
                File file = (File) element;
                switch (columnIndex) {
                case 0 :
                    return file.getName();
                case 1 :
                    return file.isDirectory() ? ""
                            : ((file.length() + 1023) / 1024) + " KB ";
                case 2 :
                    Date date = new Date(file.lastModified());
                    return dateFormat.format(date);
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
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection sel =
                    (IStructuredSelection) event.getSelection();
                System.out.println(
                    sel.size()
                    + " items selected, "
                    + viewer.getCheckedElements().length
                    + " items checked");
            }
        });
        viewer.setInput(new File("/"/*((Variant)release.getParent()).getLocalPath().toOSString()*/));
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
		edit.setText("&Edit...");
		edit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
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

        super.okPressed();
    }
}
