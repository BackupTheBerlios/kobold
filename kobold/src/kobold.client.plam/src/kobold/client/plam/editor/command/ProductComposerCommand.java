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
 * $Id: ProductComposerCommand.java,v 1.6 2004/08/05 01:35:08 martinplies Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.client.plam.editor.tool.ProductComposer;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.gef.commands.Command;


/**
 * Toggles the selection of the touched asset to make product composing 
 * possible.
 * 
 * @author Tammo
 */
public class ProductComposerCommand extends Command
{
    private AbstractAsset asset;
    private ProductComposer composer;
    private boolean ctrlKey;

    public ProductComposerCommand(AbstractAsset asset, ProductComposer composer, boolean ctrlKey)
    {
        super("product composer command");
        this.asset = asset;
        this.composer = composer;
        this.ctrlKey = ctrlKey;
    }
    
    public void execute()
    {
        if ((composer.isOpen(asset)  && !ctrlKey) 
                || (composer.isMustNotUse(asset) && !ctrlKey)){
            composer.setUsed(asset);
        }else if ((composer.isOpen(asset)  && ctrlKey) 
                || (composer.isUsed(asset) && ctrlKey)){
            composer.setMustNotUse(asset);
        } else {
            composer.setOpen(asset);
        }
    }
    
    public void redo()
    {
        // FIXME
        super.redo();
    }
    
    public void undo()
    {
        // FIXME
        super.undo();
    }
}
