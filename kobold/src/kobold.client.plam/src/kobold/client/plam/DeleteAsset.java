/*
 * Created on 27.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam;

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
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Variant;



/**
 * @author pliesmn, Tammo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteAsset {
    public static final int DELETE = 1;
    public static final int DEPRECATED = 2;
    private AbstractAsset asset;
    private int index = -1;
    private List edges = new ArrayList();
    private List depAssets = new ArrayList();
    private int action;
    AbstractAsset parent;
    

    public DeleteAsset(){        
    }
    
    public void setAsset(AbstractAsset asset)
    {
        this.asset = asset;
        if (asset == null || asset instanceof Productline){
            parent = null;
        }else{
            parent = asset.getParent();
        }
    }
    
    public void execute(int action)
    {
        this.action = action;
        if (action == DEPRECATED) {
            makeAssetDeprecated(asset);
            return;
        }
        AbstractAsset parent = asset.getParent();
        if (asset instanceof AbstractRootAsset) {
            if (asset instanceof Product) {
                ((Productline) parent).removeProduct((Product) asset); 
            }
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
        	//delete the variant directory
        	ModelStorage ms = new ModelStorage();
        	ms.deleteVariantDirectory((Variant)asset);
        	
        	//remove VCM-dir stuff
        	//TODO: acces to the vcm-plugin:
        	//StatusUpdater:deleteVariantVCMDirectory((Variant)asset);
        	
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
        
        if (parent == null){
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


