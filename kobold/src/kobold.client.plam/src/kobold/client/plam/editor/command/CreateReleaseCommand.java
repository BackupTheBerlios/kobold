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
 * $Id: CreateReleaseCommand.java,v 1.2 2004/07/01 11:27:25 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.Release;


/**
 * @author Tammo
 */
public class CreateReleaseCommand extends AbstractCreateCommand
{
    private IReleaseContainer parent;
    private Release child;

    public void setParent(IReleaseContainer parent)
    {
        this.parent = parent;
    }
    
    public void setChild(Release child)
    {
        this.child = child;
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    protected void addChildToParent()
    {
        parent.addRelease(child);
    }
    
    /**
     */
    protected void removeChildFromParent()
    {
        parent.removeRelease(child);
    }
}
