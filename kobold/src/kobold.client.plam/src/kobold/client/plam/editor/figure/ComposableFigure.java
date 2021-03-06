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
 * $Id: ComposableFigure.java,v 1.6 2004/08/04 09:45:07 vanto Exp $
 *
 */
package kobold.client.plam.editor.figure;

import kobold.client.plam.editor.tool.ProductComposer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.graphics.Color;


/**
 * @author Tammo
 */
public abstract class ComposableFigure extends Shape
{
	private static final Log logger = LogFactory.getLog(ComposableFigure.class);
    private Color tmpColor;
	private boolean composing;

	public void startComposing()
	{
	    if (!composing) {
	        tmpColor = getBackgroundColor();
	        setBackgroundColor(ColorConstants.lightGray);
	        composing = true;
	    }
	}
	
	public void stopComposing()
	{
	    if (composing) {
	        setBackgroundColor(tmpColor);
	        composing = false;
	    }
	}
	
	public void setState(String state) 
	{
	    logger.debug("state change: "+state);
	    if (state == ProductComposer.STATE_OPEN) {
	        setBackgroundColor(ColorConstants.lightGray);
	    } else if (state == ProductComposer.STATE_USED) {
	        setBackgroundColor(ColorConstants.lightBlue);
	    } else if (state == ProductComposer.STATE_MUST_NOT) {
	        setBackgroundColor(ColorConstants.darkGray);
	    }
	}
}
