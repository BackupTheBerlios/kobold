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
 * $Id: NodeAssignmentDialog.java,v 1.5 2004/08/25 04:06:50 martinplies Exp $
 *
 */
package kobold.client.plam.graphimport;

import java.util.List;

import kobold.client.plam.editor.tool.ProductComposer;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


/**
 * @author pliesmn
 *
 * Shows Dialog. If it exists Files with the same Name. The factory cannot map the paresed files to the model.
 */
public class NodeAssignmentDialog extends TitleAreaDialog {
    private static final Log logger = LogFactory.getLog(NodeAssignmentDialog.class);

    
    private List fileDescriptors;
    private String nodeName;
    private String nodeBauhausPath;
    private org.eclipse.swt.widgets.List list;
    private FileDescriptor selectedFD;
    /**
     * @param parentShell
     */
    public NodeAssignmentDialog(List fileDescriptors, String nodeName, String nodeBauhausPath, Shell parentShell) {
        super(parentShell); 
        this.fileDescriptors = fileDescriptors;
        this.nodeBauhausPath = nodeBauhausPath;
        this.nodeName = nodeName;
    }
    
    protected Control createDialogArea(Composite parent) {
        this.getShell().setText("Assignment Dialog");
        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        
        this.setMessage("If you, select no entry imported node is ignored");
        this.setTitle("Assign imported node to a Kobold node");
        new Label(composite, SWT.NONE);
        Label labelName = new Label(composite, SWT.NONE);
        labelName.setText("Bauhaus/Kobold filename: " + this.nodeName);
        
        
        Label labelPath = new Label(composite, SWT.NONE);
        labelPath.setText("Bauhaus path: " + this.nodeBauhausPath);
        
        Group listGr = new Group(composite, SWT.SHADOW_ETCHED_OUT);
        listGr.setText("Paths of Kobold Filedesciptors");
        listGr.setLayout(new FillLayout(SWT.VERTICAL));
        listGr.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        list = new org.eclipse.swt.widgets.List(listGr, SWT.SINGLE);
        list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        list.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                listSelected(); 
            }
        });
        for (int i = 0; i < fileDescriptors.size(); i++){
            String s;
            try {
                s = ((FileDescriptor)fileDescriptors.get(i)).getLocalPath().toString();
                list.add(s, i);
            } catch (RuntimeException e) {
               logger.error(e.getLocalizedMessage(), e);
               this.setMessage("Error", IMessageProvider.ERROR);
            }
           
        }

        return composite;
    }

    /**
     * 
     */
    protected void listSelected() {
        if (list.getSelectionIndex() == -1){
            this.setMessage("If you, select no entry imported node is ignored");
            selectedFD = null;
        } else {
            selectedFD = (FileDescriptor)this.fileDescriptors.get(list.getSelectionIndex());
            this.setMessage("");
        }
    }
    
    protected FileDescriptor getSelectedFileDesciptor(){
        return this.selectedFD;
    }

}
