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
 * $Id: GXLExportDialog.java,v 1.6 2004/06/28 14:50:04 martinplies Exp $
 *
 */
package kobold.client.plam.wizard;

import java.io.File;
import java.io.IOException;

import kobold.common.data.User;
import kobold.common.exception.GXLException;
import kobold.common.model.AbstractAsset;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Variant;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGraph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author meiner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GXLExportDialog extends Dialog{
    
	private Text textJarFile;
	private Text textGxlFile;
	private Button buttonJarExport;
	private Button searchJARFileButton;
    private static final Log logger = LogFactory.getLog(GXLExportDialog.class);

	private AbstractAsset exportAsset; // asset that should be exportet
	private Group jarExportGroup;
	/**
	 * @param parentShell
	 */
	public GXLExportDialog(Shell parentShell, AbstractAsset asset) {
		super(parentShell);
		exportAsset = asset;
	}
	
	
	protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, Messages.getString("GXLExportDialog.OK"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("GXLExportDialog.Close"), false);		 //$NON-NLS-1$
	}
	
	protected Control createDialogArea(Composite parent) {
		
		Composite exportGroup = new Composite(parent, SWT.NONE);
		try{
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		exportGroup.setLayout(layout);
		exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// jar export
		buttonJarExport = new Button(exportGroup, SWT.CHECK);
		buttonJarExport.setEnabled(false); // is not yet implemented
		buttonJarExport.setText(Messages.getString("GXLExportDialog.CheckBNameJarExport")); //$NON-NLS-1$
		
		Label label1 = new Label(exportGroup,SWT.NONE);
		label1.setText(Messages.getString("GXLExportDialog.GroupNameJarExport")); //$NON-NLS-1$
		jarExportGroup = new Group(exportGroup, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		jarExportGroup.setLayout(layout);
		jarExportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		textJarFile = new Text(jarExportGroup, SWT.SINGLE);
		GridData gd = new GridData();
        //gd.heightHint = 150;
        gd.widthHint = 300;
        textJarFile.setLayoutData(gd);
		searchJARFileButton = new Button(jarExportGroup,SWT.PUSH);
	    searchJARFileButton.setText(Messages.getString("GXLExportDialog.ButtonBrowse")); //$NON-NLS-1$
		searchJARFileButton.addListener(SWT.Selection ,new Listener(){
			public void handleEvent(Event event) {
				FileDialog fd = new FileDialog(GXLExportDialog.this.getShell(),SWT.SAVE);
				GXLExportDialog.this.textJarFile.setText(fd.open());
				GXLExportDialog.this.textJarFile.redraw();
			}
		});
		
		GXLExportDialog.this.textJarFile.setEnabled(false);
        GXLExportDialog.this.searchJARFileButton.setEnabled(false);
		buttonJarExport.addSelectionListener( new SelectionListener(){
            public void widgetSelected(SelectionEvent e) {
              if (GXLExportDialog.this.buttonJarExport.getSelection()) {
                  GXLExportDialog.this.textJarFile.setEnabled(true);
                  GXLExportDialog.this.searchJARFileButton.setEnabled(true);
              }else {
                  GXLExportDialog.this.textJarFile.setEnabled(false);
                  GXLExportDialog.this.searchJARFileButton.setEnabled(false);
              }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);               
            }
		});
		
		// gxl export
		new Label(exportGroup,SWT.NONE);
		Label label2 = new Label(exportGroup,SWT.NONE);
		label2.setText(Messages.getString("GXLExportDialog.GroupNameGXLExport")); //$NON-NLS-1$
		Group gxlExportGroup = new Group(exportGroup, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		gxlExportGroup.setLayout(layout);
		gxlExportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textGxlFile = new Text(gxlExportGroup, SWT.SINGLE);
		GridData gd1 = new GridData();
        //gd.heightHint = 150;
        gd1.widthHint = 300;    
        textGxlFile.setLayoutData(gd1);
		Button searchGXLFileButton = new Button(gxlExportGroup,SWT.PUSH);
		searchGXLFileButton.setText(Messages.getString("GXLExportDialog.Browse")); //$NON-NLS-1$
		searchGXLFileButton.addListener(SWT.Selection ,new Listener(){
			public void handleEvent(Event event) {
				FileDialog fd = new FileDialog(GXLExportDialog.this.getShell(),SWT.SAVE);
				GXLExportDialog.this.textGxlFile.setText(fd.open());
				GXLExportDialog.this.textGxlFile.redraw();
			}
		});
		} catch (Exception e) {e.printStackTrace();}
		return exportGroup;
		
	}
	
	public void okPressed(){
		
			try {
				File gxlFile = new File(textGxlFile.getText());
				if (gxlFile.canWrite()){
				  exportGraph(exportAsset,gxlFile);
				  this.close();
			    } else {
			        MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR| SWT.OK);
			        mb.setMessage(Messages.getString("GXLExportDialog.InvalidPathMessage")+"\n\""+textGxlFile.getText()+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			        mb.open();
			    }
			}catch (Exception ex) {
			    MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR| SWT.OK);
		        mb.setMessage(Messages.getString("GXLExportDialog.ErrorMessage")+ ex.getMessage()); //$NON-NLS-1$
		        mb.open();
		    }			
	}
	
	private AbstractAsset createGraph() {
		Component c0 = new Component("name c0"); //$NON-NLS-1$
		User u = new User("pliesmn"); //$NON-NLS-1$
		c0.addMaintainer(u);
		c0.setDescription("description c0"); //$NON-NLS-1$
		Component c1 = new Component("name c1"); //$NON-NLS-1$
		c1.addMaintainer(u);
		c1.setDescription("description c1"); //$NON-NLS-1$
		Component c2 = new Component("name c2"); //$NON-NLS-1$
		c2.addMaintainer(u);
		c2.setDescription("description c2"); //$NON-NLS-1$
		Variant v1 = new Variant ("name v1"); //$NON-NLS-1$
		v1.addComponent(c1);
		v1.addComponent(c2);
		c0.addVariant(v1);
		 return c0;
	}
	
	public void cancelPressed(){
		this.close(); 
	}
	
	public void exportFiles(AbstractAsset asset,File jarFile)  {
      System.out.println(Messages.getString("GXLExportDialog.exportFiles")); //$NON-NLS-1$
	}
	
	public void exportGraph(AbstractAsset asset,File gxlFile)  {
		GXLGraph graph = new GXLGraph("koboldgraph"); //$NON-NLS-1$
		GXLDocument doc = new GXLDocument();
		try {
            graph.add(asset.getGXLGraph());
    		doc.getDocumentElement().add(graph);
        	doc.write(gxlFile);
        } catch (GXLException e) {
            logger.debug(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.warn(e.getLocalizedMessage(), e);
        }

	}
  
}
