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
 * $Id: ContainerViewItem.java,v 1.8 2004/08/26 16:58:05 martinplies Exp $
 *
 */

package kobold.client.plam.workflow;



import java.util.ArrayList;
import java.util.List;

import kobold.common.data.WorkflowItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 * Reperesent a Group with its children on Koboldmessge Dialog
 * C
 * 
 * @author pliesmn
 * 
 */
public class ContainerViewItem extends AbstractViewItem {
	private WorkflowItem[] items; 
	private List viewItems = new ArrayList(); // no Radio Items
	private List radioItems = new ArrayList();


	private Group container;
	
	/**
	 * @param item a Workflowitem that has the type CONTAINER
	 */
	public ContainerViewItem(WorkflowItem item) {
		super(item);
		items = item.getChildren();
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see kobold.client.plam.workflow.AbstractViewItem#createViewControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createViewControl(Composite parent) {
		
		Group container = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		container.setLayout(gridLayout);
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(data);

		//container.setBackground(ColorConstants.white);

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
		
		return container;			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kobold.client.plam.workflow.AbstractViewItem#applyValues()
	 */
	public void applyValues(WorkflowDialog wd) {
		// aplly Values of the RadioButtons
		if (item.getValue().length() > 0) {
			// only from named contaiter, the value of the slected radiobutton can be stored
			String value = "";
			for (int i = 0; i < radioItems.size(); i++) {
				ButtonViewItem wi = (ButtonViewItem) radioItems.get(i);
				if ( wi.isSelected()) {
					value = wi.getValue();
				}
			}
			wd.setAnswer(this.item.getValue(), value);
		}
		//	applyValues of the Rest of the Items
		for (int i=0; i < viewItems.size(); i++)
			((AbstractViewItem) viewItems.get(i)).applyValues(wd);
	}
	
	
	
}
