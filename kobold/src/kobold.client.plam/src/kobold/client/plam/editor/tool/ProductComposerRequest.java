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
 * $Id: ProductComposerRequest.java,v 1.1 2004/07/22 16:42:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.tool;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.gef.requests.LocationRequest;


/**
 * @author Tammo
 */
public class ProductComposerRequest extends LocationRequest
{
    private int statemask;
    
    /**
     * Sets the statemask for this request.
     * @param mask the statemask
     */
    public void setModifiers(int mask) {
    	statemask = mask;
    }
    
    /**
     * Returns <code>true</code> if the ALT key is currently pressed.
     * @return whether the ALT key is pressed
     */
    public boolean isAltKeyPressed() {
    	return ((statemask & MouseEvent.ALT) != 0);
    }

    /**
     * Returns <code>true</code> if the CTRL key is currently pressed.
     * @return whether the CTRL key is pressed
     */
    public boolean isControlKeyPressed() {
    	return ((statemask & MouseEvent.CONTROL) != 0);
    }

    /**
     * Returns <code>true</code> if the SHIFT key is currently pressed.
     * @return whether the SHIFT key is pressed
     */
    public boolean isShiftKeyPressed() {
    	return ((statemask & MouseEvent.SHIFT) != 0);
    }
}