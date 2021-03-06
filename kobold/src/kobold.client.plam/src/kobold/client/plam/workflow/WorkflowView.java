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
 * $Id: WorkflowView.java,v 1.29 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.workflow;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Iterator;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.action.SuggestFileAction;
import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.listeners.IProjectChangeListener;
import kobold.client.plam.workflow.KoboldMessageFilter;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.KoboldMessage;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
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
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


/**
 * @author Tammo
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkflowView extends ViewPart implements IProjectChangeListener {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WorkflowView.class);

	private TableViewer viewer;
	private WorkflowContentProvider contentProvider;

	private Action fetchAction;
	private Action messageAction;
	private Action filterAction;
	private SuggestFileAction suggestFileAction;
	private Action doubleClickAction;
	private Action deleteAction;
	
	private String[] titles = {null, null, "Referring to", "Subject", "Sender", "Date" };
	private Comparator[] comparators = {};
	private ColumnLayoutData columnLayouts[] =	{
			new ColumnPixelData(19, false),
			new ColumnPixelData(19, false),
			new ColumnWeightData(75),
			new ColumnWeightData(200),
			new ColumnWeightData(150),
			new ColumnWeightData(60)};
	
	private Table table;
	private KoboldMessageSorter sorter = new KoboldMessageSorter();
    private KoboldMessageFilter filter;
	
	/**
	 * conigure the sorter and start sorting.
	 * @param columnNo Number of column, that title is slectet
	 */
	private void columnHeaderSelected(int columnNo) {
          Comparator comperator = sorter.getComparator();
          switch(columnNo){
            case 0: sorter.setSortByType(); break;
            case 3: sorter.setSortBySubject(); break;
            case 4:	sorter.setSortBySender(); break;
            case 5:	sorter.setSortByDate(); break;
            default : return;
          }
          if (sorter.getComparator().equals(comperator) && ! sorter.isSortInverse()) {
              sorter.setSortInverse(true);
          } else {
              sorter.setSortInverse(false);
          }
          sorter.sort(this.viewer, contentProvider.getElements(null));
          viewer.refresh();
    }
	  
	public void createPartControl(Composite parent) 
	{
		table = new Table (parent, SWT.MULTI | SWT.BORDER |SWT.FULL_SELECTION);
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
			column.addSelectionListener(new KoboldSelectionListener(i));
		}		

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	

		contentProvider = new WorkflowContentProvider(this);
		
		viewer = new TableViewer(table);
		filter = new KoboldMessageFilter();
		viewer.addFilter(filter);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(sorter);
		sorter.setSortByDate();
		sorter.setSortInverse(false);
		
		if (KoboldPLAMPlugin.getCurrentKoboldProject() != null &&
		        KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue() != null)
		    viewer.setInput(KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue());
		
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
		manager.add(fetchAction);
		manager.add(messageAction);
		manager.add(suggestFileAction);
		manager.add(new Separator());
		//manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(filterAction);
		manager.add(fetchAction);
		manager.add(messageAction);
		manager.add(deleteAction);
		manager.add(suggestFileAction);
		//manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions"));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fetchAction);
		manager.add(filterAction);
	}

	private void makeActions() {
		fetchAction = new Action() {
			public void run() {
				KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue().fetchMessages();
				
			}
		};
		fetchAction.setText("Fetch messages");
		fetchAction.setToolTipText("Fetch new messages from server");
		fetchAction.setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/refresh_msg.gif"));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				WorkflowDialog wfDialog = new WorkflowDialog(viewer.getControl().getShell(), (AbstractKoboldMessage)obj);
				wfDialog.open();
				
			}
		};
		
		filterAction = new Action("Filter Messages", IAction.AS_CHECK_BOX) {
			public void run() {
				//contentProvider.setFiltered(!contentProvider.isFiltered());
			    FilterDialog fd = new FilterDialog(new Shell(), filter);
			    fd.open();
			    viewer.refresh();
			}
		};
		filterAction.setText("Filter...");
		filterAction.setToolTipText("Hide messages from this list");
		filterAction.setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/filter_msg.gif"));
		
		deleteAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				
				for (Iterator ite = ((IStructuredSelection)selection).iterator(); ite.hasNext(); ){
				    Object obj = ite.next();
				    if (obj instanceof AbstractKoboldMessage) {
				       // table.remove(table.(obj));				       
						KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue().removeMessage((AbstractKoboldMessage)obj);
                        viewer.getContentProvider().inputChanged(viewer, null,KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue() );
					}
				} 				
			}
		};
		deleteAction.setText("Delete message");
		
		
		messageAction = new Action() {
			public void run() {
				
				WorkflowMessage msg = new WorkflowMessage("TextMail");
				try {
					//UserContext user = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
					UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
				    msg.setSender(ctx.getUserName());
					//msg.setStep(1);
					msg.setReceiver("-");
					msg.setSubject("-");
					msg.setMessageText("");
					WorkflowItem recipient = new WorkflowItem("recipient", "Recipient: ", WorkflowItem.TEXT);
					WorkflowItem subject = new WorkflowItem ("subject", "Subject: ", WorkflowItem.TEXT);
					msg.addWorkflowControl(recipient);
					msg.addWorkflowControl(subject);
					WorkflowDialog wfDialog = new WorkflowDialog(viewer.getControl().getShell(), msg);
					wfDialog.open();	
				} catch (Exception e) {
					
				}
			}
		};
		messageAction.setText("New mail");
		messageAction.setToolTipText("Write a new message");
		
		suggestFileAction = new SuggestFileAction(viewer.getControl().getShell());
		
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
				viewer.setInput(KoboldPLAMPlugin.getCurrentKoboldProject().getMessageQueue());
				viewer.refresh();	
			}
		});
	}

	public void dispose() {
		KoboldPLAMPlugin.getDefault().removeProjectChangeListener(this);
		super.dispose();
	}

	class KoboldSelectionListener implements SelectionListener {
		/**
		 * Logger for this class
		 
		private static final Logger logger = Logger
				.getLogger(KoboldSelectionListener.class);
	    */
	    int columnNo;
	    
	    public KoboldSelectionListener (int columnNo){
	        super();
	        this.columnNo = columnNo;
	    }
        public void widgetSelected(SelectionEvent e) {
           columnHeaderSelected(columnNo);
        }
      
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);                    
        }
	    
	}
    
	

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		/**
		 * Logger for this class
		x
		private static final Logger logger = Logger
				.getLogger(ViewLabelProvider.class);
*/
		private final Image kImage = KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif").createImage();
		private final Image wImage = KoboldPLAMPlugin.getImageDescriptor("icons/wflow.gif").createImage();		
		private final Image hiImage = KoboldPLAMPlugin.getImageDescriptor("icons/high.gif").createImage();
		private final Image loImage = KoboldPLAMPlugin.getImageDescriptor("icons/low.gif").createImage();		
		
		private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		public String getColumnText(Object obj, int index) {
		    AbstractKoboldMessage msg = (AbstractKoboldMessage)obj;
			if (msg != null) {
    			switch (index) {
    				case 2: return msg.getProductline();
    				case 3: return msg.getSubject();
    				case 4: return msg.getSender() + " (" +
    				               msg.getRole() + ")";
    				case 5: 
    				    	if (msg.getDate() != null) {
    				    	    return df.format(msg.getDate());				
    				    	}
    			}
			}
			return "";
		}
		
		public Image getColumnImage(Object obj, int index) 
		
		{
			AbstractKoboldMessage msg = (AbstractKoboldMessage)obj;
			if (msg == null) return null;
			
			switch (index) {
				case 0: return (msg.getType() == KoboldMessage.TYPE)?kImage:wImage;
				case 1: return getPriorityImage((AbstractKoboldMessage)obj);
			}

			return null;
		}
		
		private Image getPriorityImage(AbstractKoboldMessage msg)
		{
			if (msg.getPriority() == null)
				return null;
				
			if (msg.getPriority().equals(AbstractKoboldMessage.PRIORITY_HIGH)) {
				return hiImage;
			} else if (msg.getPriority().equals(AbstractKoboldMessage.PRIORITY_LOW)) {
				return loImage;
			} else {
				return null;
			}
		}
	}
}

