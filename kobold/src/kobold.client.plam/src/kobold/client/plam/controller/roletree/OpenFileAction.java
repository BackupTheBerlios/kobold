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
 * $Id: OpenFileAction.java,v 1.1 2004/09/19 22:27:23 martinplies Exp $
 *
 */
package kobold.client.plam.controller.roletree;

import java.util.Iterator;

import kobold.client.plam.model.FileDescriptor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.ide.IDE;



/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpenFileAction extends SelectionListenerAction{

    private IWorkbenchPage workbenchPage;

    /**
     * @param text
     */
    public OpenFileAction(IWorkbenchPage workbenchPage) {
        super("OpenFileAction");
        this.workbenchPage = workbenchPage;
    }
     
    public void run(){
       for (Iterator ite = this.getSelectedResources().iterator(); ite.hasNext();){
           Object obj = ite.next();
           if (obj instanceof IFile){
               boolean activate = OpenStrategy.activateOnOpen();
          try {                
                   IDE.openEditor(workbenchPage, (IFile) obj, activate);
            } catch (PartInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         }
       }
        
    }

}
