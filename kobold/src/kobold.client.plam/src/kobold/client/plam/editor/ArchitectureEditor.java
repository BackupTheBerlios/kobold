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
 * $Id: ArchitectureEditor.java,v 1.5 2004/05/14 00:30:14 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.Model;
import kobold.client.plam.model.ModelFactory;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SharedImages;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * ArchitectureEditor
 *
 * @author Tammo
 */
public class ArchitectureEditor extends GraphicalEditorWithPalette {

	protected static final String PALETTE_SIZE = "Palette Size"; //$NON-NLS-1$
	protected static final int DEFAULT_PALETTE_SIZE = 130;

	private Model model;
	
	/**
	 * Creates a new HelloGefEditor object.
	 */
	public ArchitectureEditor()
	{
		super();
		setEditDomain(new DefaultEditDomain(this));
		model = ModelFactory.createTestModel();
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite iSite, IEditorInput iInput)
		throws PartInitException
	{
		setSite(iSite);
		System.out.println(iInput.getName());
		setInput(iInput);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getInitialPaletteSize()
	 */
	protected int getInitialPaletteSize() {
		return KoboldPLAMPlugin.getDefault().getPreferenceStore().getInt(PALETTE_SIZE);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#handlePaletteResized(int)
	 */
	protected void handlePaletteResized(int newSize) {
		KoboldPLAMPlugin.getDefault().getPreferenceStore().setValue(PALETTE_SIZE, newSize);
	}

	protected void hookPaletteViewer() {
		super.hookPaletteViewer();
		final CopyTemplateAction copy = 
				(CopyTemplateAction)getActionRegistry().getAction(GEFActionConstants.COPY);
		getPaletteViewer().addSelectionChangedListener(copy);
		getPaletteViewer().getContextMenu().addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.appendToGroup(GEFActionConstants.GROUP_COPY, copy);
			}
		});
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getPaletteRoot()
	 */
	protected PaletteRoot getPaletteRoot() {
		ArrayList categories = new ArrayList();
		List entries = new ArrayList(3);

		entries.add(new SelectionToolEntry());
		entries.add(new MarqueeToolEntry());
		entries.add(new ConnectionCreationToolEntry("Connect",
				"Connection Creation Tool", null,
				SharedImages.DESC_SELECTION_TOOL_16,
				SharedImages.DESC_SELECTION_TOOL_16));

		entries.add(new PaletteSeparator());
        
		/*entries.add(new CreationToolEntry("Component", 
					"Create a new Component",
					new ComponentCreationFactory(),
					null, null)); 
		entries.add(new CreationToolEntry("Variant", 
					"Create a new Variant",
					new VariantCreationFactory(),
					null, null));*/ 
                    
		PaletteRoot palette = new PaletteRoot();
		palette.setChildren(entries);

		return palette;
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(model);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#configurePaletteViewer()
	 */
	protected void configurePaletteViewer() {
		super.configurePaletteViewer();
		PaletteViewer viewer = (PaletteViewer)getPaletteViewer();
		ContextMenuProvider provider = new PaletteContextMenuProvider(viewer);
		getPaletteViewer().setContextMenu(provider);
		//viewer.setCustomizer(new KoboldPaletteCustomizer());
	}

	public void dispose() {
		CopyTemplateAction copy = (CopyTemplateAction)getActionRegistry().getAction(GEFActionConstants.COPY);
		getPaletteViewer().removeSelectionChangedListener(copy);
	
		super.dispose();
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
		//ContextMenuProvider provider = new AEContextMenuProvider(viewer, getActionRegistry());
		//viewer.setContextMenu(provider);
		//getSite().registerContextMenu("org.eclipse.gef.examples.logic.editor.contextmenu", //$NON-NLS-1$
		//	provider, viewer);
		
		//viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
		//	.setParent(getCommonKeyHandler()));

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
