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
 * $Id: ReleaseFigure.java,v 1.3 2004/07/23 22:27:11 vanto Exp $
 *
 */
package kobold.client.plam.editor.figure;

import kobold.client.plam.editor.KoboldColors;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author Tammo
 */
public class ReleaseFigure extends ComposableFigure
{
    private Label titleLabel;
    
    /**
     * 
     */
    public ReleaseFigure()
    {
		 super();
		 setBackgroundColor(KoboldColors.release);
		 setForegroundColor(ColorConstants.lightBlue);
		 titleLabel = new Label();
		 ToolbarLayout tbl = new ToolbarLayout();
		 tbl.setVertical(false);
		 setLayoutManager(tbl);
		 setBorder(new MarginBorder(2));
		 add(titleLabel);
    }
    
    public void setTitle(String title) 
    {
        titleLabel.setText(title);
    }

    /**
     * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
     */
    protected void fillShape(Graphics graphics)
    {
        graphics.fillRectangle(getBounds());
    }

    /**
     * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
     */
    protected void outlineShape(Graphics graphics)
    {
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		int w = r.width - lineWidth;
		int h = r.height - lineWidth;
		graphics.drawRectangle(x, y, w, h);
	}
}
