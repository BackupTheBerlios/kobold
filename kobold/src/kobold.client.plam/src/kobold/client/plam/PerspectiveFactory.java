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
 * $Id: PerspectiveFactory.java,v 1.1 2004/05/13 14:39:18 vanto Exp $
 *
 */
package kobold.client.plam;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @see IPerspectiveFactory
 */
public class PerspectiveFactory implements IPerspectiveFactory {
	/**
	 *
	 */
	public PerspectiveFactory() 
	{
		super();
	}

	/**
	 * @see IPerspectiveFactory#createInitialLayout
	 */
	public void createInitialLayout(IPageLayout layout)  
	{
 		String editorArea = layout.getEditorArea();
		
		IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		//folder.addView(JavaUI.ID_PACKAGES);
		//folder.addView(JavaUI.ID_TYPE_HIERARCHY);
		folder.addView(KoboldConstants.ID_ROLE_TREE_VIEW);
		folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		
		IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		//outputfolder.addPlaceholder(SearchUI.SEARCH_RESULT_VIEW_ID);
		//outputfolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		//outputfolder.addPlaceholder(JavaUI.ID_SOURCE_VIEW);
		//outputfolder.addPlaceholder(JavaUI.ID_JAVADOC_VIEW);
		
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, (float)0.75, editorArea);
		
		// views - role tree
		layout.addShowViewShortcut(KoboldConstants.ID_ROLE_TREE_VIEW);

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
				
	}
}
