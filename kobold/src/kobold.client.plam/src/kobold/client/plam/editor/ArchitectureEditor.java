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
 * $Id: ArchitectureEditor.java,v 1.10 2004/06/23 12:58:10 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModelContainer;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Productline;
import kobold.common.model.productline.Variant;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleRulerVisibilityAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * ArchitectureEditor
 *
 * @author Tammo
 */
public class ArchitectureEditor extends GraphicalEditorWithFlyoutPalette 
	implements IViewModelProvider {

    protected static final String PALETTE_DOCK_LOCATION = "Dock location"; //$NON-NLS-1$
    protected static final String PALETTE_SIZE = "Palette Size"; //$NON-NLS-1$
    protected static final String PALETTE_STATE = "Palette state"; //$NON-NLS-1$
    protected static final int DEFAULT_PALETTE_SIZE = 130;
    
    static {
    	KoboldPLAMPlugin.getDefault().getPreferenceStore().setDefault(
    			PALETTE_SIZE, DEFAULT_PALETTE_SIZE);
    }
	private ViewModelContainer viewModel;
	private PaletteRoot root;
	private KeyHandler keyHandler;
	
	/**
	 * Creates a architecture editor
	 */
	public ArchitectureEditor()
	{
		super();
		setEditDomain(new DefaultEditDomain(this));
		viewModel = new ViewModelContainer();
	}

	public ViewModelContainer getViewModelContainer()
	{
	    return viewModel;
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().addDropTargetListener(
			new KoboldTemplateTransferDropTargetListener(getGraphicalViewer()));
		
	    Productline model = new Productline("PL Compiler");
		
		Component ca1 = new Component("CA Frontend");
		Component ca2 = new Component("CA Backend");
		model.addComponent(ca1);
		model.addComponent(ca2);
		
		Variant va1 = new Variant("VA Java");
		Variant va2 = new Variant("VA C++");
		Variant va3 = new Variant("VA ADA");
		
		Variant va4 = new Variant("VA x86");
		Variant va5 = new Variant("VA PPC");
		
		ca1.addVariant(va1);
		ca1.addVariant(va2);
		ca1.addVariant(va3);
		
		ca2.addVariant(va4);
		ca2.addVariant(va5);
	    getGraphicalViewer().setContents(model);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer()
	{
		super.configureGraphicalViewer();
		ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer)getGraphicalViewer();

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();

		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		viewer.setRootEditPart(root);

		viewer.setEditPartFactory(new GraphicalPartFactory());
		
		// Actions
		IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
		getActionRegistry().registerAction(showRulers);
		
		IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
		getActionRegistry().registerAction(snapAction);

		IAction showGrid = new ToggleGridAction(getGraphicalViewer());
		getActionRegistry().registerAction(showGrid);

		ContextMenuProvider provider = new KoboldContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu("kobold.client.plam.editor.contextmenu", //$NON-NLS-1$
				provider, viewer);

		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
				.setParent(getCommonKeyHandler()));
	}

    protected void createActions() {
    	super.createActions();
    	ActionRegistry registry = getActionRegistry();
    	IAction action;
    	
    	action = new CopyTemplateAction(this);
    	registry.registerAction(action);
    
    	action = new MatchWidthAction(this);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    	
    	action = new MatchHeightAction(this);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    	
    	action = new DirectEditAction((IWorkbenchPart)this);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    
    	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
    	registry.registerAction(action);
    	getSelectionActions().add(action.getId());
    }
	
	protected KeyHandler getCommonKeyHandler(){
		if (keyHandler == null){
			keyHandler = new KeyHandler();
			keyHandler.put(
				KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			keyHandler.put(
				KeyStroke.getPressed(SWT.F2, 0),
				getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		}
		return keyHandler;
	}
	
    /**
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		System.out.println("save!");
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		System.out.println("save as!");
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) {
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}


	/* palette stuff */
	protected CustomPalettePage createPalettePage() 
	{
		return new CustomPalettePage(getPaletteViewerProvider()) {
			public void init(IPageSite pageSite) {
				super.init(pageSite);
				IAction copy = getActionRegistry().getAction(ActionFactory.COPY.getId());
				pageSite.getActionBars().setGlobalActionHandler(
						ActionFactory.COPY.getId(), copy);
			}
		};
	}

	protected PaletteViewerProvider createPaletteViewerProvider() 
	{
		return new PaletteViewerProvider(getEditDomain()) {
			private IMenuListener menuListener;
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
	
    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPalettePreferences()
     */
    protected FlyoutPreferences getPalettePreferences()
    {
    	return new FlyoutPreferences() {
    		public int getDockLocation() {
    			return KoboldPLAMPlugin.getDefault().getPreferenceStore()
    					.getInt(PALETTE_DOCK_LOCATION);
    		}
    		public int getPaletteState() {
    			return KoboldPLAMPlugin.getDefault().getPreferenceStore().getInt(PALETTE_STATE);
    		}
    		public int getPaletteWidth() {
    			return KoboldPLAMPlugin.getDefault().getPreferenceStore().getInt(PALETTE_SIZE);
    		}
    		public void setDockLocation(int location) {
    			KoboldPLAMPlugin.getDefault().getPreferenceStore()
    					.setValue(PALETTE_DOCK_LOCATION, location);
    		}
    		public void setPaletteState(int state) {
    		    KoboldPLAMPlugin.getDefault().getPreferenceStore()
    					.setValue(PALETTE_STATE, state);
    		}
    		public void setPaletteWidth(int width) {
    			KoboldPLAMPlugin.getDefault().getPreferenceStore()
    					.setValue(PALETTE_SIZE, width);
    		}
    	};
    }
    
    protected PaletteRoot getPaletteRoot() {
    	if( root == null ){
    		root = PaletteHelper.createPalette();
    	}
    	return root;
    }

	
	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == CommandStackInspectorPage.class) {
			return new CommandStackInspectorPage(getCommandStack());
		}

		if (type == IContentOutlinePage.class) {
			return new OutlinePage(new TreeViewer());
		}

		if (type == ZoomManager.class) {
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
													   .getRootEditPart()).getZoomManager();
		}

		return super.getAdapter(type);
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

			 ActionRegistry registry = getActionRegistry();
			 IActionBars bars = pageSite.getActionBars();
			 String id = ActionFactory.UNDO.getId();
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 id = ActionFactory.REDO.getId();
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 id = ActionFactory.DELETE.getId();
			 bars.setGlobalActionHandler(id, registry.getAction(id));
			 bars.updateActionBars();
		 }

		 protected void configureOutlineViewer()
		 {
			 getViewer().setEditDomain(getEditDomain());
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
}
