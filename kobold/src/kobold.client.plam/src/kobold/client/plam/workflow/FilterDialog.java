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
 * $Id: FilterDialog.java,v 1.1 2004/10/08 15:46:01 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author pliesmn
 *
 * Dialog where the user can choose, which messages are filtered out.
 * The filter is set here, but filtering is not started here
 */
public class FilterDialog extends TitleAreaDialog {

    private KoboldMessageFilter filter;
    private Table table;

    /**
     * @param parent
     */
    public FilterDialog(Shell parent, KoboldMessageFilter filter) {
        super(parent);
        this.filter = filter;
 
    }
    
    protected Control createDialogArea(Composite parent)
    {
        setTitle("Set filter");
        setMessage("Please select the elements to exclude from view");
        Composite composite = (Composite) super.createDialogArea(parent);
        table = new Table(composite, SWT.SINGLE | SWT.CHECK | SWT.BORDER);
        table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        TableColumn column = new TableColumn(table, SWT.LEFT );
        TableItem item0 = new TableItem(table, SWT.NONE);
        TableItem item1  = new TableItem(table, SWT.NONE);
        item0.setText("Workflow Messages");
        item1.setText("Kobold Messages");
		column.pack();
		
        return composite;
    }
    
    public void okPressed(){
        filter.setFilterWorkflowMessagesOut(table.getItem(0).getChecked());
        filter.setFilterKoboldMessagesOut(table.getItem(1).getChecked());
        close();
    }    
    

}
