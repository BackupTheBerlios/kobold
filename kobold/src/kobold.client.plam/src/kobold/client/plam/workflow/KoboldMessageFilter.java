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
 * $Id: KoboldMessageFilter.java,v 1.1 2004/10/08 15:46:11 martinplies Exp $
 *
 */

package kobold.client.plam.workflow;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.KoboldMessage;
import kobold.common.data.WorkflowMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * @author pliesmn
 *
 *
 */
public class KoboldMessageFilter extends ViewerFilter {

    boolean isKoboldMessagesFilteredOut = false;
    boolean isFilterSentMessagesOut = false;
    boolean isWorkflowMessagesFilteredOut =false;
    private static final Log logger = LogFactory.getLog(LocalMessageQueue.class);
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (! (element instanceof AbstractKoboldMessage)) {
          logger.error("Wrong Object in Workflowmessage View.(is filtered out) class:" + element.getClass().toString() +  "  ," + element.toString());
          return false;
        }
        AbstractKoboldMessage message = (AbstractKoboldMessage) element;
            
        return !(    (isKoboldMessagesFilteredOut && element instanceof KoboldMessage)
                 ||  (isWorkflowMessagesFilteredOut && element instanceof WorkflowMessage) 
                );               
    } 
    
    public void setFilterKoboldMessagesOut(boolean km){
        isKoboldMessagesFilteredOut = km;
    }
    
    public void setFilterWorkflowMessagesOut(boolean km){
        isWorkflowMessagesFilteredOut = km;
    }
    
    public void setFilterSentMessagesOut(boolean bo){
        isFilterSentMessagesOut = bo;
    }
    
    

}
