/*
 * Created on 16.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.workflow;



import kobold.common.data.WorkflowItem;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import java.util.*;

/**
 * @author meiner1
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ContainerViewItem extends AbstractViewItem {
	private WorkflowItem[] items; 
	private List viewItems = new ArrayList(); // no Radio Items
	private List radioItems = new ArrayList();
	/**
	 * @param item
	 */
	public ContainerViewItem(WorkflowItem item) {
		super(item);
		items = item.getChildren();
	}

	private Group container;
	
	

	/* (non-Javadoc)
	 * @see kobold.client.plam.workflow.AbstractViewItem#createViewControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createViewControl(Composite parent) {
		
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		control.setLayout(gridLayout);
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		control.setLayoutData(data);

		//control.setBackground(ColorConstants.white);

		container = new Group(control, SWT.NONE);
		container.setText(item.getDescription());
		
		data = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(data);

		// add Children ViewItems
		for (int i = 0; i < items.length; i++) {
			AbstractViewItem vi = null;
			if (items[i].getType().equals(WorkflowItem.CHECK)) {
				vi = new ButtonViewItem(items[i], SWT.CHECK);
				viewItems.add(vi);	
			} else if (items[i].getType().equals(WorkflowItem.CONTAINER)) {
				vi = new ContainerViewItem(items[i]);
				viewItems.add(vi);
			} if (items[i].getType().equals(WorkflowItem.TEXT)) {
				vi = new TextViewItem(items[i]);  
				viewItems.add(vi);
			}if (items[i].getType().equals(WorkflowItem.RADIO)) {
				vi = new ButtonViewItem(items[i], SWT.RADIO);
				radioItems.add(vi);	
			}
	
			if (vi != null) {
				vi.createViewControl(container);						
			}
		}
		
		return control;			
	}

	/* (non-Javadoc)
	 * @see kobold.client.plam.workflow.AbstractViewItem#applyValues()
	 */
	public void applyValues() {
		// TODO Auto-generated method stub
		
	}
}
