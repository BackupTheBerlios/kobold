/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, Bettina Druckenmueller,
 * Anselm Garbe, Michael Grosse, Tammo van Lessen, Martin Plies, Oliver Rendgen,
 * Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * $Id: WorkflowEditor.java,v 1.2 2004/05/16 02:27:55 vanto Exp $
 *  
 */
package kobold.client.plam.workflow;


import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldProjectNature;
import kobold.common.data.KoboldMessage;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author Tammo
 */
public class WorkflowEditor extends EditorPart {

	private static final Log logger = LogFactory.getLog(WorkflowEditor.class);
	private LocalMessageQueue mqueue;
	private Composite itemsWidget;
	private Composite control;
	
	private List viewItems = new ArrayList();

	/**
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {}

	/**
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {}

	/**
	 * @see org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) 
	{
		try {
			String id = (String)marker.getAttribute("msgid");
			KoboldProjectNature n = (KoboldProjectNature)marker.getResource().getProject().getNature(KoboldProjectNature.NATURE_ID);
			renderMessage(n.getMessageQueue().getMessageById(id));
		} catch (CoreException e) {}

	}

	/**
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setInput(input);
		setSite(site);
		try {
			KoboldProjectNature nature = (KoboldProjectNature)((FileEditorInput)input).getFile()
				.getProject().getNature(KoboldProjectNature.NATURE_ID);
			mqueue = nature.getMessageQueue();
		} catch (CoreException e) {}


	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() 
	{
		return false;
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() 
	{
		return false;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) 
	{
		this.control = parent;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);		

		parent.setBackground(ColorConstants.white);
		
		Label title = new Label(parent, SWT.NONE);
		title.setText("Workflow");
		title.setVisible(true);
		title.setFont(JFaceResources.getHeaderFont());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		title.setLayoutData(data);

		itemsWidget = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		itemsWidget.setLayout(gridLayout);
		data = new GridData(GridData.FILL_HORIZONTAL);
		itemsWidget.setLayoutData(data);
		
		Composite buttons = new Composite(parent, SWT.NONE);		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttons.setLayout(gridLayout);
		
		Button ok = new Button(buttons, SWT.NONE);
		ok.setText("Send");

		Button close = new Button(buttons, SWT.NONE);
		close.setText("Close");
	}

	
	private void renderMessage(final KoboldMessage msg) 
	{
		Display.getDefault ().asyncExec (new Runnable () {
			public void run () {
				System.out.println(msg);
				Composite header = new Composite(itemsWidget, SWT.NONE);
				GridLayout gridLayout = new GridLayout();
				gridLayout.numColumns = 2;
				header.setLayout(gridLayout);
				
				new Label(header, SWT.NONE).setText("Sender:");
				new Label(header, SWT.NONE).setText(msg.getSender());
				new Label(header, SWT.NONE).setText("Receiver:");
				new Label(header, SWT.NONE).setText(msg.getReceiver());
				new Label(header, SWT.NONE).setText("Subject:");
				new Label(header, SWT.NONE).setText(msg.getSubject());
				
				if (msg instanceof WorkflowMessage) {
					WorkflowItem[] items = ((WorkflowMessage)msg).getWorkflowControls();
				
					for (int i = 0; i < items.length; i++) {
						AbstractViewItem vi = null;
						if (items[i].getType().equals(WorkflowItem.CHECK)) {
							vi = new CheckboxViewItem(items[i]);	
						} else if (items[i].getType().equals(WorkflowItem.CONTAINER)) {
							//TODO
						} if (items[i].getType().equals(WorkflowItem.TEXT)) {
							//TODO
						}
				
						if (vi != null) {
							vi.createViewControl(itemsWidget);
							viewItems.add(vi);					
						}
					}
				}
				control.redraw(); 
			}
		  });

	}
	
	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {}

}
