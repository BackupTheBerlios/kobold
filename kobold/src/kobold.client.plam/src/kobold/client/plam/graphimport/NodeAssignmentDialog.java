/*
 * Created on 18.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.graphimport;

import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.product.ProductComponent;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NodeAssignmentDialog extends TitleAreaDialog {

    
    private List fileDescriptors;
    private String nodeName;
    private String nodeBauhausPath;
    private org.eclipse.swt.widgets.List list;
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
        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        
        this.setMessage("Assign the Node to a File");
        new Label(composite, SWT.NONE);
        Label labelName = new Label(composite, SWT.NONE);
        labelName.setText("Bauhaus/Kobold filename: " + this.nodeName);
        
        
        Label labelPath = new Label(composite, SWT.NONE);
        labelPath.setText("Bauhaus path: " + this.nodeBauhausPath);
        
        Group listGr = new Group(composite, SWT.SHADOW_ETCHED_OUT);
        listGr.setText("Assets of the Nodes");
        listGr.setLayout(new FillLayout(SWT.VERTICAL));
        listGr.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        list = new org.eclipse.swt.widgets.List(listGr, SWT.SINGLE);
        list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        list.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                ListSelected(); 
            }
        });
        for (int i = 0; i < fileDescriptors.size(); i++){
            AbstractAsset pc = (AbstractAsset) ((FileDescriptor)fileDescriptors.get(i)).getParentAsset();            
            list.add(pc.getName(), i);
        }
       /* Table table = new Table(composite, SWT.SINGLE);
        TableColumn tc1 = new TableColumn(table, SWT.NONE);
        tc1.setText("lacal path");
        TableColumn tc2 = new TableColumn(table, SWT.NONE);
        tc2.setText("parent asset");
  

        for (Iterator ite = fileDescriptors.iterator(); ite.hasNext;){
           FileDescriptor fd = (FileDescriptor) ite.next();
           TableItem tb = new TableItem(table, SWT.NONE);
           tb.setText(0, fd.getLocalPath());
           fd.getLocalPath();
        }
        
        
*/ 
        //fd.
        return composite;
    }

    /**
     * 
     */
    protected void ListSelected() {
        if (list.getSelectionIndex() == -1){
            ;;
        }
    }
    
    

}
