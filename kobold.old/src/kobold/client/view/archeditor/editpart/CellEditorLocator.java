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
 * $Id: CellEditorLocator.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.editpart;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * CellEditorLocator
 * 
 * @author Tammo van Lessen
 */
public class CellEditorLocator implements org.eclipse.gef.tools.CellEditorLocator {

    private Label label;

    public CellEditorLocator(Label label) {
        setLabel(label);
    }

    public void relocate(CellEditor celleditor) {
        Text text = (Text)celleditor.getControl();
        Point pref = text.computeSize(-1, -1);
        Rectangle rect = label.getTextBounds().getCopy();
        label.translateToAbsolute(rect);
        text.setBounds(rect.x-4, rect.y-1, pref.x+1, pref.y+1); 
    }

    /**
     * Returns the Label figure.
     */
    protected Label getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * @param label The label to set
     */
    protected void setLabel(Label label) {
        this.label = label;
    }

}
