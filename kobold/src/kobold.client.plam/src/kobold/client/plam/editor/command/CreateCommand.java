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
 * $Id: CreateCommand.java,v 1.1 2004/06/23 02:26:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.common.model.AbstractAsset;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Productline;

import org.eclipse.gef.commands.Command;


/**
 * @author Tammo
 */
public class CreateCommand extends Command
{
    private AbstractAsset parent, child;
    
    public CreateCommand()
    {
        super("Create Asset");
    }

    public void setParent(AbstractAsset parent)
    {
        this.parent = parent;
    }
    
    public void setChild(AbstractAsset child)
    {
        this.child = child;
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute()
    {
        if (parent instanceof Productline
             && child instanceof Component) {
            ((Productline)parent).addCoreAsset((Component)child);
        }
        // TODO usw.
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    public void redo()
    {
        if (parent instanceof Productline
                && child instanceof Component) {
               ((Productline)parent).addCoreAsset((Component)child);
        }
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo()
    {
        if (parent instanceof Productline
                && child instanceof Component) {
               ((Productline)parent).removeCoreAsset((Component)child);
        }
        super.undo();
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    public boolean canExecute()
    {
        // TODO implement it!!!
        return super.canExecute();
    }
}
