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
 *
 */
package kobold.client.plam.graphimport;

import java.io.File;

import kobold.client.plam.model.product.Product;
import kobold.client.plam.wizard.GXLExportDialog;
import kobold.client.plam.wizard.Messages;

import org.apache.log4j.Layout;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
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

    /**
     * @param parentShell
     */
    public GraphImportDialog(Shell parentShell, Product product) {
        super(parentShell);
        this.product = product;
    }
    
    
    
    protected Control createDialogArea(Composite parent) {
          this.setMessage("select GXL File", SWT.NONE);  
          this.setTitle("GXL Import"); 
          Composite  comp = new Composite(parent, SWT.NONE);
          GridLayout layout = new GridLayout();
          layout.numColumns = 2;
          comp.setLayout(layout);
          comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          textFile = new Text(comp, SWT.SINGLE);
          textFile.addModifyListener( new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                handelFileEvent();                  
            }            
          });
          GridData gd1 = new GridData();
          gd1.widthHint = 300;
          comp.setLayoutData(gd1);
     
          Button searchGXLFileButton = new Button(comp, SWT.PUSH);
          searchGXLFileButton.setText(Messages
                  .getString("GXLExportDialog.Browse")); 
          searchGXLFileButton.addListener(SWT.Selection, new Listener() {
              public void handleEvent(Event event) {
                  FileDialog fd = new FileDialog(GraphImportDialog.this
                          .getShell(), SWT.SAVE);
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
    
    /*public Control createButtonBar(Composite parent){
        Composite comp = new Composite(parent, SWT.NONE); 
        
        return comp;
    }
*/
    
    protected void buttonPressed(int buttonid){
       switch (buttonid) {
        case IDialogConstants.OK_ID :
        case IDialogConstants.CANCEL_ID: this.close(); break;
        case IDialogConstants.PROCEED_ID:
            this.setMessage( "Import ist started", MessageDialog.INFORMATION);
            try {
              GraphFactory gf = new GraphFactory();
              gf.importGraph(GXLFile, product);
              getButton(IDialogConstants.OK_ID).setEnabled(true);
            }catch (Exception e) {
                setMessage("An error has been occured:" + e.getMessage());
                getButton(IDialogConstants.OK_ID).setEnabled(false);
            }
       }
        
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL , true);
        createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false); 
        createButton(parent, IDialogConstants.PROCEED_ID,IDialogConstants.PROCEED_LABEL, false); 
        getButton(IDialogConstants.PROCEED_ID).setEnabled(false);
        getButton(IDialogConstants.OK_ID).setEnabled(false);        
    }
    
   
    
    
    
}