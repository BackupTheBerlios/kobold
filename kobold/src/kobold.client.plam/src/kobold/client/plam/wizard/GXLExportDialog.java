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
 * $Id: GXLExportDialog.java,v 1.3 2004/06/27 18:41:26 vanto Exp $
 *
 */
package kobold.client.plam.wizard;

import java.io.File;
import java.io.IOException;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
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
    
    private static final Log logger = LogFactory.getLog(GXLExportDialog.class);
	
    private Text JARFileUri;
	private Text GXLFileUri;
	private AbstractAsset exportAsset; // asset that should be exportet
	/**
	 * @param parentShell
	 */
	public GXLExportDialog(Shell parentShell, AbstractAsset asset) {
		super(parentShell);
		exportAsset = asset;
	}
	
	
	protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);		
	}
	
	protected Control createDialogArea(Composite parent) {
		
		Composite exportGroup = new Composite(parent, SWT.NONE);
		try{
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		exportGroup.setLayout(layout);
		exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
		Button buttonJarExport = new Button(exportGroup, SWT.CHECK);
		buttonJarExport.setText("Jar Export");
		
		Composite uriGroup = new Composite(parent, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		uriGroup.setLayout(layout);
		uriGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GXLFileUri = new Text(uriGroup, SWT.SINGLE);
		GridData gd = new GridData();
        //gd.heightHint = 150;
        gd.widthHint = 300;    
		GXLFileUri.setLayoutData(gd);
		Button searchGXLFileButton = new Button(uriGroup,SWT.PUSH);
	    searchGXLFileButton.setText("Durchsuchen1");
		searchGXLFileButton.addListener(SWT.Selection ,new Listener(){
			public void handleEvent(Event event) {
				FileDialog fd = new FileDialog(GXLExportDialog.this.getShell(),SWT.SAVE);
				GXLExportDialog.this.GXLFileUri.setText(fd.open());
				GXLExportDialog.this.GXLFileUri.redraw();
			}
		});
		
		JARFileUri = new Text(uriGroup, SWT.SINGLE);
		GridData gd1 = new GridData();
        //gd.heightHint = 150;
        gd1.widthHint = 300;    
		JARFileUri.setLayoutData(gd1);
		//JARFileUri
		Button searchJARFileButton = new Button(uriGroup,SWT.PUSH);
		searchJARFileButton.setText("Durchsuchen2");
		searchJARFileButton.addListener(SWT.Selection ,new Listener(){
			public void handleEvent(Event event) {
				FileDialog fd = new FileDialog(GXLExportDialog.this.getShell(),SWT.SAVE);
				GXLExportDialog.this.JARFileUri.setText(fd.open());
				GXLExportDialog.this.JARFileUri.redraw();
			}
		});
		} catch (Exception e) {e.printStackTrace();}
		return exportGroup;
		
	}
	
	public void okPressed(){
		try {
			try {
				//URI directory = new URI(uri.getText());
				File gxlFile = new File(GXLFileUri.getText());
				exportGraph(exportAsset,gxlFile);
			} catch (Exception e) {
				e.printStackTrace();
			   MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR| SWT.OK);
			   mb.setMessage("The directory \""+GXLFileUri.getText()+"\" is not falid.");
			   mb.open();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private AbstractAsset createGraph() {
		Component c0 = new Component("name c0");
		c0.setOwner("pliesmn");
		c0.setDescription("description c0");
		Component c1 = new Component("name c1");
		c1.setOwner("pliesmn");
		c1.setDescription("description c1");
		Component c2 = new Component("name c2");
		c2.setOwner("pliesmn");
		c2.setDescription("description c2");
		Variant v1 = new Variant ("name v1");
		v1.setOwner("pliesmn");
		v1.addComponent(c1);
		v1.addComponent(c2);
		c0.addVariant(v1);
		 return c0;
	}
	
	public void cancelPressed(){
		this.close(); 
	}
	
	public void exportGraph(AbstractAsset asset,File gxlFile)  {
		GXLGraph graph = new GXLGraph("koboldgraph");
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
