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
 * $Id: CreateVariantCommand.java,v 1.1 2004/06/23 12:58:10 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.common.model.IVariantContainer;
import kobold.common.model.productline.Variant;


/**
 * @author Tammo
 */
public class CreateVariantCommand extends AbstractCreateCommand
{
    private IVariantContainer parent;
    private Variant child;

    public void setParent(IVariantContainer parent)
    {
        this.parent = parent;
    }
    
    public void setChild(Variant child)
    {
        this.child = child;
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    protected void addChildToParent()
    {
        parent.addVariant(child);
    }
    
    /**
     */
    protected void removeChildFromParent()
    {
        parent.removeVariant(child);
    }
}
