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
 * $Id: ButtonViewItem.java,v 1.4 2004/10/21 21:32:40 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;


import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Tammo
 */
public class ButtonViewItem extends AbstractViewItem {

	private Button button;
	private int type; 
	/**
	 */
	public ButtonViewItem(WorkflowItem item,int swtType) {
		super(item);
		type = swtType;
	}

	/**
	 * @see kobold.client.plam.workflow.AbstractViewItem#createViewControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createViewControl(Composite parent) {
		button = new Button(parent, type);
		button.setText(item.getDescription());
		return parent;
	}
	
	/**
	 * @see kobold.client.plam.workflow.AbstractViewItem#applyValues(WorkflowDialog)
	 */
	public void applyValues(WorkflowDialog wd){
	  if (isSelected()) {
        wd.setAnswer(this.getValue(), WorkflowMessage.DATA_VALUE_TRUE );     
	  }else {
	  	wd.setAnswer(this.getValue(), WorkflowMessage.DATA_VALUE_FALSE );  
	  }
	}
	

	public boolean isSelected() {
		return this.button.getSelection();
	}
	
	public String getValue() {
		return item.getValue();
	}

}
