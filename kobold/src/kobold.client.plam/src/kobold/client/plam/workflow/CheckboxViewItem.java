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
 * $Id: CheckboxViewItem.java,v 1.1 2004/05/15 02:10:23 vanto Exp $
 *
 */
package kobold.client.plam.workflow;


import kobold.common.data.WorkflowItem;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Tammo
 */
public class CheckboxViewItem extends AbstractViewItem {

	private Button checkbox;
	/**
	 */
	public CheckboxViewItem(WorkflowItem item) {
		super(item);
	}

	/**
	 * @see kobold.client.plam.workflow.AbstractViewItem#createViewControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createViewControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		control.setLayout(gridLayout);
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		control.setLayoutData(data);

		control.setBackground(ColorConstants.white);

		checkbox = new Button(control, SWT.CHECK);
		checkbox.setSelection(item.getValue().equals("true"));
		checkbox.setText(item.getDescription());
		
		data = new GridData(GridData.FILL_HORIZONTAL);
		checkbox.setLayoutData(data);

		return control;
	}

	/**
	 * @see kobold.client.plam.workflow.AbstractViewItem#applyValues()
	 */
	public void applyValues() {
		if (checkbox != null) {
			item.setValue(checkbox.getSelection()?"true":"false");
		}
	}

}
