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
 * $Id: ArchitectureView.java,v 1.4 2004/06/22 23:30:12 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModelContainer;
import kobold.client.plam.model.ModelFactory;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Provides the Architecture Graph Editor View
 * 
 * @author Tammo
 */
public class ArchitectureView extends ViewPart implements IViewModelProvider {
	private GraphicalViewer graphicalViewer;
	private EditDomain editDomain;
	private ActionRegistry actionRegistry;
	private ViewModelContainer vmp = new ViewModelContainer();
	
	public ArchitectureView()
	{
		editDomain = new EditDomain();
		actionRegistry = new ActionRegistry();
	}
	
	public void createPartControl(Composite parent) {
		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);

		// configure viewer
		viewer.getControl().setBackground(ColorConstants.listBackground);

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();

		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);
		
		
		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		actionRegistry.registerAction(zoomIn);
		actionRegistry.registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(zoomIn);
		bars.getToolBarManager().add(zoomOut);
		bars.getToolBarManager().add(new ZoomComboContributionItem(getSite().getPage()));
		
		viewer.setRootEditPart(root);

		viewer.setEditPartFactory(new GraphicalPartFactory());
				
		// hooks
		// TODO Attach to the project change event
		
		initializeGraphicalViewer();
	}

	public void setFocus() {
		getGraphicalViewer().getControl().setFocus();
	}
	
	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(ModelFactory.createTestModel());
	}

	/**
	 * Returns the graphical viewer.
	 * @return the graphical viewer
	 */
	protected GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}


	/**
	 * Sets the graphicalViewer for this EditorPart.
	 * @param viewer the graphical viewer
	 */
	protected void setGraphicalViewer(GraphicalViewer viewer) {
		this.editDomain.addViewer(viewer);
		this.graphicalViewer = viewer;
	}


	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		/*if (adapter == CommandStackInspectorPage.class) {
			return new CommandStackInspectorPage(getCommandStack());
		}*/

		if (adapter == IContentOutlinePage.class) {
			return new OutlinePage(new TreeViewer());
		}

		if (adapter == ZoomManager.class) {
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
													   .getRootEditPart()).getZoomManager();
		}

		return super.getAdapter(adapter);
	}


	class OutlinePage extends ContentOutlinePage implements IAdaptable
	 {
		 static final int ID_OUTLINE = 0;
		 static final int ID_OVERVIEW = 1;
		 private Canvas overview;
		 private Control outline;
		 private IAction showOutlineAction;
		 private IAction showOverviewAction;
		 private PageBook pageBook;
		 private Thumbnail thumbnail;

		 public OutlinePage(EditPartViewer viewer)
		 {
			 super(viewer);
		 }

		 public Object getAdapter(Class type)
		 {
			 if (type == ZoomManager.class) {
				 return ((ScalableFreeformRootEditPart) getGraphicalViewer()
															.getRootEditPart()).getZoomManager();
			 }

			 return null;
		 }

		 public Control getControl()
		 {
			 return pageBook;
		 }

		 public void createControl(Composite parent)
		 {
			 pageBook = new PageBook(parent, SWT.NONE);
			 overview = new Canvas(pageBook, SWT.NONE);

			 initializeOverview();
			 pageBook.showPage(overview);
			 thumbnail.setVisible(true);
			 configureOutlineViewer();
		 }

		 public void dispose()
		 {
			 if (thumbnail != null) {
				 thumbnail.deactivate();
			 }

			 super.dispose();
		 }

		 public void init(IPageSite pageSite)
		 {
			 super.init(pageSite);

			 ActionRegistry registry = actionRegistry;
			 IActionBars bars = pageSite.getActionBars();
			 String id = IWorkbenchActionConstants.UNDO;
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 id = IWorkbenchActionConstants.REDO;
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 id = IWorkbenchActionConstants.DELETE;
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 bars.updateActionBars();
		 }

		 protected void configureOutlineViewer()
		 {
			 getViewer().setEditDomain(editDomain);
			 getViewer().setEditPartFactory(new GraphicalPartFactory());
		 }

		 protected void initializeOverview()
		 {
			 LightweightSystem lws = new LightweightSystem(overview);
			 RootEditPart rep = getGraphicalViewer().getRootEditPart();

			 if (rep instanceof ScalableFreeformRootEditPart) {
				 ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
				 thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
				 thumbnail.setBorder(new MarginBorder(3));
				 thumbnail.setSource(root.getLayer(
						 LayerConstants.PRINTABLE_LAYERS));
				 lws.setContents(thumbnail);
			 }
		 }
	 }


    /* (non-Javadoc)
     * @see kobold.client.plam.editor.model.IViewModelProvider#getViewModelContainer()
     */
    public ViewModelContainer getViewModelContainer()
    {
        return vmp;
    }


}
