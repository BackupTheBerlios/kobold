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
 * $Id: SetConstraintCommand.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

/**
 * SetConstraintCommand
 * 
 * @author Tammo van Lessen
 * @version $Id: SetConstraintCommand.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 */
public class SetConstraintCommand extends Command {

    private EditPart editpart;
    private Rectangle oldRect;
    private Rectangle newRect;

    //~ Methods ----------------------------------------------------------------


    public void setRect(Rectangle r)
    {
        newRect = r ;
    }

    public void setEditPart(EditPart editpart)
    {
        this.editpart = editpart;
    }

    public void execute()
    {
        ((GraphicalEditPart) editpart.getParent()).setLayoutConstraint(editpart,
           ((GraphicalEditPart)editpart).getFigure(), newRect);

    }

    public void redo()
    {
        //model.setSize(newSize);
        //model.setLocation(newPos);
    }

    public void undo()
    {
        //model.setSize(oldSize);
        //model.setLocation(oldPos);
    }

}