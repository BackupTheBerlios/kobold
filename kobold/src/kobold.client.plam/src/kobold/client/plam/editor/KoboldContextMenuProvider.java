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
 * $Id: KoboldContextMenuProvider.java,v 1.6 2004/07/23 20:31:54 vanto Exp $
 *
 */

package kobold.client.plam.editor;

import kobold.client.plam.editor.action.ConfigureAssetAction;
import kobold.client.plam.editor.action.GXLExportAction;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;


/**
 * @author Tammo
 */
public class KoboldContextMenuProvider extends ContextMenuProvider
{
    private ActionRegistry actionRegistry;

    public KoboldContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
    	super(viewer);
    	setActionRegistry(registry);
    }

    /**
     * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    public void buildContextMenu(IMenuManager menu)
    {
    	GEFActionConstants.addStandardActionGroups(menu);

    	IAction action;

    	action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
    	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

    	action = getActionRegistry().getAction(ActionFactory.REDO.getId());
    	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

    	/*action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
    	if (action.isEnabled())
    		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);*/

    	action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
    	if (action.isEnabled())
    		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

    	action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
    	if (action.isEnabled())
    		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

    	action = getActionRegistry().getAction(GXLExportAction.ID);
    	menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
    	
    	action = getActionRegistry().getAction(ConfigureAssetAction.ID);
    	menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

    	// Alignment Actions
    	MenuManager submenu = new MenuManager("Alignment");

    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT);
    	if (action.isEnabled())
    		submenu.add(action);

    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER);
    	if (action.isEnabled())
    		submenu.add(action);

    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT);
    	if (action.isEnabled())
    		submenu.add(action);
    		
    	submenu.add(new Separator());
    	
    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP);
    	if (action.isEnabled())
    		submenu.add(action);

    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE);
    	if (action.isEnabled())
    		submenu.add(action);

    	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM);
    	if (action.isEnabled())
    		submenu.add(action);

    	if (!submenu.isEmpty())
    		menu.appendToGroup(GEFActionConstants.GROUP_REST, submenu);

    	action = getActionRegistry().getAction(ActionFactory.SAVE.getId());
    	menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);

    }
    
    private ActionRegistry getActionRegistry() {
    	return actionRegistry;
    }

    private void setActionRegistry(ActionRegistry registry) {
    	actionRegistry = registry;
    }
}
