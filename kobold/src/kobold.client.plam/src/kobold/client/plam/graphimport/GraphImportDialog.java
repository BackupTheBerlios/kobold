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
 * $Id: GraphImportDialog.java,v 1.7 2004/09/01 01:08:04 vanto Exp $
 *
 *
 */
package kobold.client.plam.graphimport;

import java.io.File;

import kobold.client.plam.model.product.Product;
import kobold.client.plam.wizard.Messages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphImportDialog  extends TitleAreaDialog {

    private Text textFile;
    private File GXLFile = new File("");
    private Product product;
    private static final Log logger = LogFactory.getLog(GraphImportDialog.class);

    /**
     * @param parentShell
     */
    public GraphImportDialog(Shell parentShell, Product product) {
        super(parentShell);
        this.product = product;
    }
    
    
    
    protected Control createDialogArea(Composite parent) {
          this.setMessage("Please select a GXL file to import", SWT.NONE);            
          this.getShell().setText("GXL Import");
          
          Composite  comp = new Composite(parent, SWT.NONE);
          GridLayout layout = new GridLayout();
          layout.numColumns = 2;
          comp.setLayout(layout);
          comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          textFile = new Text(comp, SWT.SINGLE | SWT.BORDER);
          textFile.addModifyListener( new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                handelFileEvent();                  
            }            
          });
          GridData gd1 = new GridData();
          gd1.widthHint = 350;
          textFile.setLayoutData(gd1);
          
     
          Button searchGXLFileButton = new Button(comp, SWT.PUSH);
          searchGXLFileButton.setText(Messages
                  .getString("GXLExportDialog.Browse")); 
          searchGXLFileButton.addListener(SWT.Selection, new Listener() {
              public void handleEvent(Event event) {
                  FileDialog fd = new FileDialog(GraphImportDialog.this
                          .getShell(), SWT.OPEN);
                  String fileStr = fd.open();                 
                  // causes a new changeevent in textFile
                  GraphImportDialog.this.textFile.setText(fileStr);
                  GraphImportDialog.this.textFile.redraw();
              }
          });
     
          return comp;
    }
    
    private void handelFileEvent(){
       File file = new File(textFile.getText());
       if (! file.exists()){
           this.setMessage( "File not exists!", MessageDialog.ERROR);
           getButton(IDialogConstants.PROCEED_ID).setEnabled(false);
       }
       if (! file.canRead()){
           this.setMessage("File cannot be read!", MessageDialog.ERROR);
           getButton(IDialogConstants.PROCEED_ID).setEnabled(false);
       } else {
           this.setMessage( "", MessageDialog.NONE); 
           getButton(IDialogConstants.PROCEED_ID).setEnabled(true);
           GXLFile = file;
       }
       
    }
    

    
    protected void buttonPressed(int buttonid){
       switch (buttonid) {
        case IDialogConstants.CLOSE_ID: this.close(); break;
        case IDialogConstants.PROCEED_ID:
            this.setMessage( "Import is started", MessageDialog.INFORMATION);
            try {
              GraphFactory gf = new GraphFactory();
              gf.importGraph(GXLFile, product);
              this.setMessage("Edges imported.", MessageDialog.NONE);
            }catch (Exception e) {
                setMessage("An error has been occured:" + e.getMessage());
                logger.error(e.getLocalizedMessage(), e);                
            }
       }
        
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.PROCEED_ID,IDialogConstants.PROCEED_LABEL, true);
        createButton(parent, IDialogConstants.CLOSE_ID,IDialogConstants.CLOSE_LABEL, false);        
        getButton(IDialogConstants.PROCEED_ID).setEnabled(false);
        getButton(IDialogConstants.CLOSE_ID).setEnabled(true);        
    }
    
   
    
    
    
}
