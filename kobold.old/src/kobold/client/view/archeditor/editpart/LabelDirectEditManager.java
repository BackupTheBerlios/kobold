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
 * $Id: LabelDirectEditManager.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.editpart;

import kobold.client.view.archeditor.figure.VariantFigure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * LabelDirectEditManager
 * 
 * @author Tammo van Lessen
 */
public class LabelDirectEditManager extends DirectEditManager {

    Font scaledFont;
    private VerifyListener verifyListener;

    public LabelDirectEditManager(
            GraphicalEditPart source,
            Class editorType,
            CellEditorLocator locator)
    {
        super(source, editorType, locator);
    }

    /**
     * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
     */
    protected void bringDown() {
        //This method might be re-entered when super.bringDown() is called.
        Font disposeFont = scaledFont;
        scaledFont = null;
        super.bringDown();
        if (disposeFont != null)
            disposeFont.dispose();  
    }

    protected void initCellEditor() {
        Text text = (Text)getCellEditor().getControl();
        verifyListener = new VerifyListener() {
            public void verifyText(VerifyEvent event) {
                Text text = (Text)getCellEditor().getControl();
                String oldText = text.getText();
                String leftText = oldText.substring(0, event.start);
                String rightText = oldText.substring(event.end, oldText .length());
                GC gc = new GC(text);
                String s = leftText + event.text + rightText;
                Point size = gc.textExtent(leftText + event.text + rightText);
                gc.dispose();
                if (size.x != 0)
                    size = text.computeSize(size.x, SWT.DEFAULT);
                getCellEditor().getControl().setSize(size.x, size.y);
            }
        };
        text.addVerifyListener(verifyListener);

        Label label = ((VariantFigure)((GraphicalEditPart)getEditPart()).getFigure()).getLabel();
        String initialLabelText = label.getText();
        getCellEditor().setValue(initialLabelText);
        IFigure figure = ((VariantFigure)((GraphicalEditPart)getEditPart()).getFigure()).getLabel();
        scaledFont = figure.getFont();
        FontData data = scaledFont.getFontData()[0];
        Dimension fontSize = new Dimension(0, data.getHeight());
        label.translateToAbsolute(fontSize);
        data.setHeight(fontSize.height);
        scaledFont = new Font(null, data);
    
        text.setFont(scaledFont);
        text.selectAll();
    }

    protected void unhookListeners() {
        super.unhookListeners();
        Text text = (Text)getCellEditor().getControl();
        text.removeVerifyListener(verifyListener);
        verifyListener = null;
    }

}
