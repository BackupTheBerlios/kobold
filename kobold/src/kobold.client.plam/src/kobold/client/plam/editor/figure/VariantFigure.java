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
 * $Id: VariantFigure.java,v 1.5 2004/07/23 22:27:11 vanto Exp $
 *
 */
package kobold.client.plam.editor.figure;

import kobold.client.plam.editor.KoboldColors;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * VariantFigure
 * 
 * @author Tammo van Lessen
 * @version $Id: VariantFigure.java,v 1.5 2004/07/23 22:27:11 vanto Exp $
 */
public class VariantFigure extends AbstractNodeFigure {
    
	protected Dimension corner = new Dimension(8, 8);
	   
    private IFigure releasePane;
    
    public VariantFigure() 
    {
        super();
        setBackgroundColor(KoboldColors.variant);
        
        releasePane = new Figure();
       
        FlowLayout tbl = new FlowLayout();
        tbl.setStretchMinorAxis(false);
        tbl.setMajorSpacing(2);
        tbl.setMinorSpacing(2);
        releasePane.setLayoutManager(tbl);
        //TitleBarBorder border = new TitleBarBorder("Releases");
        //SchemeBorder border = new SchemeBorder(new SchemeBorder.Scheme());
        SchemeBorder border = new SchemeBorder(SchemeBorder.SCHEMES.BUTTON_PRESSED);
        //border.setBackgroundColor(ColorConstants.lightGray);
        releasePane.setBorder(border);
        add(releasePane);
        getLayoutManager().setConstraint(releasePane, BorderLayout.BOTTOM);
    }
	 
	/**
	 * @see Shape#fillShape(Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
	}

	/**
	 * @see Shape#outlineShape(Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		Rectangle f = Rectangle.SINGLETON;
		Rectangle r = getBounds();
		f.x = r.x + lineWidth / 2;
		f.y = r.y + lineWidth / 2;
		f.width = r.width - lineWidth;
		f.height = r.height - lineWidth;
		graphics.drawRoundRectangle(f, corner.width, corner.height);
	}

	/**
	 * Sets the dimensions of each corner. This will form the radii of the arcs which form the
	 * corners.
	 *
	 * @param d the dimensions of the corner
	 * @since 2.0
	 */
	public void setCornerDimensions(Dimension d) {
		corner.width = d.width;
		corner.height = d.height;
	}

	public IFigure getReleasePane()
	{
	    return releasePane;
	}
}
