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
 * $Id: WorkflowView.java,v 1.2 2004/05/16 22:25:55 vanto Exp $
 *
 */
package kobold.client.plam.workflow;

import java.awt.Dialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.listeners.IProjectChangeListener;
import kobold.common.data.KoboldMessage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Tammo
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowView extends ViewPart implements IProjectChangeListener {
	private TableViewer viewer;

	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	
	private String[] titles = {null, null, "Subject", "Sender", "Date" };
	private ColumnLayoutData columnLayouts[] =	{
			new ColumnPixelData(19, false),
			new ColumnPixelData(19, false),
			new ColumnWeightData(200),
			new ColumnWeightData(75),
			new ColumnWeightData(150),
			new ColumnWeightData(60)};
	
	
	public void createPartControl(Composite parent) 
	{
		Table table = new Table (parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);	
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);

		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			if (i == 0) {
			} else if (i == 1) {
				column.setImage(KoboldPLAMPlugin.getImageDescriptor("icons/header_priority.gif").createImage()); //$NON-NLS-1$				
			} else {
				column.setText (titles [i]);	
			}
			
			layout.addColumnData(columnLayouts[i]);
		}	

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	

		viewer = new TableViewer(table);
		viewer.setContentProvider(new WorkflowContentProvider(this));
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new NameSorter());

		viewer.setInput(KoboldPLAMPlugin.getCurrentMessageQueue());
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		KoboldPLAMPlugin.getDefault().addProjectChangeListener(this);
	}

	TableViewer getTableViewer()
	{
		return viewer;
	}
	
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				WorkflowView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions"));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
				LocalMessageQueue mq = KoboldPLAMPlugin.getCurrentMessageQueue();
				WorkflowView.this.getTableViewer().setInput(mq);
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
				viewer.refresh();
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_TASK_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				//showMessage("Double-click detected on "+obj.toString());
				WorkflowDialog wfDialog = new WorkflowDialog(viewer.getControl().getShell(), (KoboldMessage)obj);
				wfDialog.open();
				
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
	}

	/**
	 * @see kobold.client.plam.listeners.IProjectChangeListener#projectChanged(org.eclipse.core.resources.IProject, org.eclipse.core.resources.IProject)
	 */
	public void projectChanged(IProject oldProject, IProject newProject) 
	{
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				viewer.setInput(KoboldPLAMPlugin.getCurrentMessageQueue());
				viewer.refresh();	
			}
		});
	}

	public void dispose() {
		KoboldPLAMPlugin.getDefault().removeProjectChangeListener(this);
		super.dispose();
	}




	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		private final Image kImage = KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif").createImage();
		private final Image wImage = KoboldPLAMPlugin.getImageDescriptor("icons/wflow.gif").createImage();		
		private final Image hiImage = KoboldPLAMPlugin.getImageDescriptor("icons/high.gif").createImage();
		private final Image loImage = KoboldPLAMPlugin.getImageDescriptor("icons/low.gif").createImage();		
		
		private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		public String getColumnText(Object obj, int index) {
			switch (index) {
				case 2: return ((KoboldMessage)obj).getSubject();
				case 3: return ((KoboldMessage)obj).getSender();
				case 4: return df.format(((KoboldMessage)obj).getDate());				
			}
			return "";
		}
		public Image getColumnImage(Object obj, int index) 
		{
			KoboldMessage msg = (KoboldMessage)obj;
			if (msg == null) return null;
			
			switch (index) {
				case 0: return (msg.getType() == KoboldMessage.TYPE)?kImage:wImage;
				case 1: return getPriorityImage((KoboldMessage)obj);
			}

			return null;
		}
		
		private Image getPriorityImage(KoboldMessage msg)
		{
			if (msg.getPriority() == null)
				return null;
				
			if (msg.getPriority().equals(KoboldMessage.PRIORITY_HIGH)) {
				return hiImage;
			} else if (msg.getPriority().equals(KoboldMessage.PRIORITY_LOW)) {
				return loImage;
			} else {
				return null;
			}
		}
	}
}

