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
 * $Id: ProductComponentFigure.java,v 1.5 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.editor.figure;

import org.apache.log4j.Logger;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Tammo
 */
public abstract class ProductComponentFigure extends AbstractNodeFigure
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ProductComponentFigure.class);

    protected Dimension corner = new Dimension(8, 8);
    private IFigure fdPane;
//    private Label desc;
    
    /**
     * 
     */
    public ProductComponentFigure() 
    {
        super();
        fdPane = new Figure();
        FlowLayout tbl = new FlowLayout();
        tbl.setStretchMinorAxis(false);
        tbl.setMajorSpacing(2);
        tbl.setMinorSpacing(2);
        fdPane.setLayoutManager(tbl);
        fdPane.setBackgroundColor(ColorConstants.gray);

        add(fdPane);
        
        getLayoutManager().setConstraint(fdPane, BorderLayout.BOTTOM);
    }
    
    /**
     * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
     */
    protected void fillShape(Graphics graphics)
    {
        graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
    }

    /**
     * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
     */
    protected void outlineShape(Graphics graphics)
    {
		Rectangle f = Rectangle.SINGLETON;
		Rectangle r = getBounds();
		f.x = r.x + lineWidth / 2;
		f.y = r.y + lineWidth / 2;
		f.width = r.width - lineWidth;
		f.height = r.height - lineWidth;
		graphics.drawRoundRectangle(f, corner.width, corner.height);
    }
    
	public void setCornerDimensions(Dimension d) {
		corner.width = d.width;
		corner.height = d.height;
	}
	
	public IFigure getFileDescriptorPane()
	{
	    return fdPane;
	}

	public void setDescription(String desc) 
	{
	    setDesc(desc);
	}
}
