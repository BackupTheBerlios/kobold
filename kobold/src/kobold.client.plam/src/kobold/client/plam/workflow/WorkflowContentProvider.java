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
 * $Id: WorkflowContentProvider.java,v 1.3 2004/10/08 15:47:03 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;

import kobold.client.plam.listeners.IMessageQueueListener;
import kobold.common.data.AbstractKoboldMessage;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


class WorkflowContentProvider implements IStructuredContentProvider,
		IMessageQueueListener
{
	private final WorkflowView view;
	private LocalMessageQueue input;
	private boolean filtered;
	
	WorkflowContentProvider(WorkflowView view) 
	{
		this.view = view;
	}
	

	public void inputChanged(Viewer v, Object oldInput, Object newInput) 
	{
		if (this.input != null) {
			this.input.removeListener(this);
		}

		this.input = (LocalMessageQueue)newInput;

		if (this.input != null) {
			this.input.addListener(this);
		}
	}
	
	public void dispose() 
	{
		if (input != null) {
			input.removeListener(this);
			input = null;
		}
	}
	
	public Object[] getElements(Object parent) {
		return (input == null)?new Object[0]:input.getMessages();
	}

	
	/**
	 * @see kobold.client.plam.listeners.IMessageQueueListener#addMessage(kobold.common.data.KoboldMessage)
	 */
	public void addMessage(final AbstractKoboldMessage msg) 
	{
		view.getTableViewer().getControl().getDisplay().syncExec(new Runnable() {		
				public void run() {					
					view.getTableViewer().add(msg);
				}
			});
	}

	/**
	 * @see kobold.client.plam.listeners.IMessageQueueListener#removeMessage(kobold.common.data.KoboldMessage)
	 */
	public void removeMessage(final AbstractKoboldMessage msg) {
		view.getTableViewer().getControl().getDisplay().syncExec(new Runnable() {		
				public void run() {					
					view.getTableViewer().remove(msg);
				}
			});
	}

	/**
	 * @see kobold.client.plam.listeners.IMessageQueueListener#rebuild()
	 */
	public void rebuild() {
		view.getTableViewer().getControl().getDisplay().syncExec(new Runnable() {		
			public void run() {					
				view.getTableViewer().refresh();
			}
		});
	}
}