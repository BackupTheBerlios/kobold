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
 * $Id: KoboldActionBarContributor.java,v 1.5 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor;

import org.apache.log4j.Logger;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;


/**
 * @author Tammo
 */
public class KoboldActionBarContributor extends ActionBarContributor
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(KoboldActionBarContributor.class);

    /**
     * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
     */
    protected void buildActions() {
    	addRetargetAction(new UndoRetargetAction());
    	addRetargetAction(new RedoRetargetAction());
    	addRetargetAction(new DeleteRetargetAction());
    	
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
    	addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
    	
    	addRetargetAction(new ZoomInRetargetAction());
    	addRetargetAction(new ZoomOutRetargetAction());
    	
    	addRetargetAction(new MatchWidthRetargetAction());
    	addRetargetAction(new MatchHeightRetargetAction());
    	
    	addRetargetAction(new RetargetAction(
    			GEFActionConstants.TOGGLE_RULER_VISIBILITY, 
    			GEFMessages.ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX));
    	
    	addRetargetAction(new RetargetAction(
    			GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, 
    			GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));

    	addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
    			GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX));

    	//addRetargetAction(new LayoutRetargetAction());
    	
    }

    /**
     * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
     */
    protected void declareGlobalActionKeys() {
    	addGlobalActionKey(ActionFactory.PRINT.getId());
    	addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
    	addGlobalActionKey(ActionFactory.PASTE.getId());
    }

    /**
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
     */
    public void contributeToToolBar(IToolBarManager tbm) {
    	tbm.add(getAction(ActionFactory.UNDO.getId()));
    	tbm.add(getAction(ActionFactory.REDO.getId()));
    	
    	tbm.add(new Separator());
    	
    	tbm.add(new Separator());
    	tbm.add(getAction(GEFActionConstants.ALIGN_LEFT));
    	tbm.add(getAction(GEFActionConstants.ALIGN_CENTER));
    	tbm.add(getAction(GEFActionConstants.ALIGN_RIGHT));
    	tbm.add(new Separator());
    	tbm.add(getAction(GEFActionConstants.ALIGN_TOP));
    	tbm.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
    	tbm.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
    	
    	tbm.add(new Separator());	
    	tbm.add(getAction(GEFActionConstants.MATCH_WIDTH));
    	tbm.add(getAction(GEFActionConstants.MATCH_HEIGHT));
    	
    	tbm.add(new Separator());	
    	String[] zoomStrings = new String[] {	ZoomManager.FIT_ALL, 
    											ZoomManager.FIT_HEIGHT, 
    											ZoomManager.FIT_WIDTH	};
    	tbm.add(new ZoomComboContributionItem(getPage(), zoomStrings));
    	
    	//tbm.add(getAction(LayoutAction.ID));
    }

    /**
     * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(IMenuManager)
     */
    public void contributeToMenu(IMenuManager menubar) {
    	super.contributeToMenu(menubar);
    	MenuManager viewMenu = new MenuManager("Architecture Editor");
    	viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
    	viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
    	viewMenu.add(new Separator());
    	viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
    	viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
    	viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
    	viewMenu.add(new Separator());
    	viewMenu.add(getAction(GEFActionConstants.MATCH_WIDTH));
    	viewMenu.add(getAction(GEFActionConstants.MATCH_HEIGHT));
    	//menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
    }
}
