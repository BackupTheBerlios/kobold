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
 * $Id: DeleteCommand.java,v 1.2 2004/06/23 13:37:00 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.common.model.AbstractAsset;
import kobold.common.model.IComponentContainer;
import kobold.common.model.IReleaseContainer;
import kobold.common.model.IVariantContainer;
import kobold.common.model.Release;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Variant;

import org.eclipse.gef.commands.Command;


/**
 * @author Tammo
 */
public class DeleteCommand extends Command
{
    private AbstractAsset asset, parent;
    private int index = -1;
    
    public DeleteCommand()
    {
        super("delete command");
    }
    
    public void setAsset(AbstractAsset asset)
    {
        this.asset = asset;
        parent = asset.getParent();
    }
    
    public void execute()
    {
        redo();
    }

    public void redo()
    {
        if (parent instanceof IComponentContainer
                && asset instanceof Component) {
            IComponentContainer cc = (IComponentContainer)parent;
            index = cc.getComponents().indexOf(asset);
            cc.removeComponent((Component)asset);
        } else if (parent instanceof IVariantContainer
                && asset instanceof Variant) {
            IVariantContainer vc = (IVariantContainer)parent;
            index = vc.getVariants().indexOf(asset);
            vc.removeVariant((Variant)asset);
        } else if (parent instanceof IReleaseContainer
                && asset instanceof Release) {
            IReleaseContainer rc = (IReleaseContainer)parent;
            index = rc.getReleases().indexOf(asset);
            rc.removeRelease((Release)asset);
        }
    }
    public void undo()
    {
        if (parent instanceof IComponentContainer
                && asset instanceof Component) {
            IComponentContainer cc = (IComponentContainer)parent;

            cc.addComponent((Component)asset, index);
        } else if (parent instanceof IVariantContainer
                && asset instanceof Variant) {
            IVariantContainer vc = (IVariantContainer)parent;

            vc.addVariant((Variant)asset, index);
        } else if (parent instanceof IReleaseContainer
                && asset instanceof Release) {
            IReleaseContainer rc = (IReleaseContainer)parent;

            rc.addRelease((Release)asset, index);
        }
    }
}
