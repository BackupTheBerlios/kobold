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
 * $Id: DeleteCommand.java,v 1.6 2004/08/25 01:50:32 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.AbstractStatus;
import kobold.client.plam.model.DeprecatedStatus;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;


/**
 * @author Tammo
 */
public class DeleteCommand extends Command
{
    private static final int DELETE = 1;
    private static final int DEPRECATED = 2;
    private AbstractAsset asset, parent;
    private int index = -1;
    private List edges = new ArrayList();
    private List depAssets = new ArrayList();
    private int action;
    
    public DeleteCommand()
    {
        super("delete command");
    }
    
    public void setAsset(AbstractAsset asset)
    {
        this.asset = asset;
        parent = (AbstractAsset)asset.getParent();
    }
    
    public void execute()
    {
        if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), 
            	"Deleted or Deprecated", 
            	"Are you sure to delete this asset? Press \"Yes\" to delete or \"No\" to mark the asset deprecated.")) {
            action = DELETE;
        } else {
            action = DEPRECATED;
        }
        
        redo();
    }

    public void redo()
    {
        if (action == DEPRECATED) {
            makeAssetDeprecated(asset);
            return;
        }
        
        if (parent != null) {
            EdgeContainer ec = parent.getRoot().getEdgeContainer();
            edges.addAll(ec.getEdgesTo(asset));
            edges.addAll(ec.getEdgesFrom(asset));

            Iterator it = edges.iterator();
            while (it.hasNext()) {
                Edge edge = (Edge)it.next();
                ec.removeEdge(edge);
            }
        }

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
        } else if (parent instanceof AbstractRootAsset
                && asset instanceof MetaNode) {
            ((AbstractRootAsset)parent).removeMetaNode((MetaNode)asset);
        }
    }
    public void undo()
    {
        if (action == DEPRECATED) {
            Iterator it = depAssets.iterator();
            while (it.hasNext()) {
                AbstractAsset asset = (AbstractAsset)it.next();
                asset.removeStatus(AbstractStatus.DEPRECATED_STATUS);
            }
            return;
        }
        
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
        } else if (parent instanceof AbstractRootAsset
                && asset instanceof MetaNode) {
            ((AbstractRootAsset)parent).addMetaNode((MetaNode)asset);
        }
        
        EdgeContainer ec = parent.getRoot().getEdgeContainer();
        Iterator it = edges.iterator();
        while (it.hasNext()) {
            Edge edge = (Edge)it.next();
            ec.addEdge(edge);
        }
    }
    
    private void makeAssetDeprecated(AbstractAsset asset) 
    {
        if (!DeprecatedStatus.isDeprecated(asset)) {
            asset.addStatus(AbstractStatus.DEPRECATED_STATUS);
            // add asset to undolist if status has changed
            depAssets.add(asset);
        }
        
        List assetList = new ArrayList();
        if (asset instanceof IComponentContainer) {
            assetList.addAll(((IComponentContainer)asset).getComponents());
        }
        if (asset instanceof IVariantContainer) {
            assetList.addAll(((IVariantContainer)asset).getVariants());
        }
        if (asset instanceof IReleaseContainer) {
            assetList.addAll(((IReleaseContainer)asset).getReleases());
        }

        Iterator it = assetList.iterator();
        while (it.hasNext()) {
            makeAssetDeprecated((AbstractAsset)it.next());
        }
    }
}
