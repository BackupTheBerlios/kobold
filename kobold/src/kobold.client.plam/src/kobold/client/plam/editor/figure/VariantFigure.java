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
 * $Id: VariantFigure.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.editor.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * VariantFigure
 * 
 * @author Tammo van Lessen
 * @version $Id: VariantFigure.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 */
public class VariantFigure extends RoundedRectangle {
    
    public static Color classColor = new Color(null, 255, 206, 255);
    private IFigure pane;
    private Label label;
    
    public VariantFigure(String name) 
    {
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        //setBorder(new LineBorder(ColorConstants.black, 1));
        setBackgroundColor(classColor);
        //setOpaque(true);

        //Display d = Display.getCurrent();

        Font classFont = JFaceResources.getHeaderFont(); //new Font(null, SWT."Arial, sans-serif", 12, SWT.BOLD);
        label = new Label(name);
        label.setFont(classFont);
        label.setLabelAlignment(PositionConstants.CENTER);
        add(label);
        
        /*pane = new FreeformLayer();
        //pane.setLayoutManager(new FreeformLayout());
        ToolbarLayout tl = new ToolbarLayout();
        tl.setSpacing(5);
        pane.setLayoutManager(tl);

        ScrollPane scrollpane = new ScrollPane();
        
        scrollpane.setViewport(new FreeformViewport());
        scrollpane.setContents(pane);
        
        add(scrollpane);*/
        
        pane = new Figure();
        pane.setBorder(new MarginBorder(5));
        ToolbarLayout tl = new ToolbarLayout();
        tl.setSpacing(5);
        tl.setVertical(false);
        pane.setLayoutManager(tl);
        add(pane);

    }
    
    /**
     * @return
     */
    public IFigure getContentPane() {
        return pane;
    }

    public void setName(String name) 
    {
        label.setText(name);
    }
    
    public Label getLabel()
    {
        return label;
    }
}
