/*
 * Created on 17.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.workflow;

import kobold.common.data.WorkflowItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author meiner1
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TextViewItem extends AbstractViewItem {
	private Text textWidget;
	
	/**
	 * @param item
	 */
	public TextViewItem(WorkflowItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see kobold.client.plam.workflow.AbstractViewItem#createViewControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createViewControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		control.setLayout(gridLayout);
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		control.setLayoutData(data);

		//control.setBackground(ColorConstants.white);
		Label label = new Label(control,SWT.NONE);
		label.setText(item.getDescription());
		textWidget = new Text(control, SWT.NONE);
		//button.setSelection(item.getValue().equals("true"));
		textWidget.setText(item.getDescription());
		
		data = new GridData(GridData.FILL_HORIZONTAL);
		textWidget.setLayoutData(data);

		return control;
	}
	/* (non-Javadoc)
	 * @see kobold.client.plam.workflow.AbstractViewItem#applyValues()
	 */
	public void applyValues() {
		// TODO Auto-generated method stub
	}
}
