/*
 * Created on 10.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.workflow;

/**
 * @author meiner1
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
/**
 * @author scheglov_ke
 */
public class WorkflowDialog extends Dialog {
	Button cb;
	
	public WorkflowDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());
		{
			final TabFolder tabFolder = new TabFolder(area, SWT.NONE);
			{
				final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				tabItem.setText("tab 1");
				{
					final Button button = new Button(tabFolder, SWT.CHECK);
					tabItem.setControl(button);
					button.setText("button 1");
				}
			}
			{
				final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				tabItem.setText("tab 2");
				{
					final Button button = new Button(tabFolder, SWT.NONE);
					tabItem.setControl(button);
					button.setText("button 2");
				}
			}
		}
		// DESIGNER: Add controls before this line.
		return area;
	}
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Workflow Dialog");
	}
	
/*	public boolean getCBValue(){
		cb.g		
	}
 */
}


