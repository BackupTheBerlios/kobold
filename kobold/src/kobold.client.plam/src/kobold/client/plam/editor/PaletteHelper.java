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
 * $Id: PaletteHelper.java,v 1.9 2004/08/24 11:10:03 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.model.KoboldAssetFactory;
import kobold.client.plam.editor.tool.AssetCreationToolEntry;
import kobold.client.plam.editor.tool.ProductComposerToolEntry;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.edges.Edge;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;


/**
 * Constructs the palette entries of the graphical editor.
 * 
 * @author Tammo
 */
public class PaletteHelper
{

    static List createProductlineCategories(){
    	List categories = new ArrayList();
    	
    	categories.add(createProductlineComponentsDrawer());
    	categories.add(createMetaNodesDrawer());
    	categories.add(createEdgeDrawer());
    	categories.add(createProductlineTools());
    	return categories;
    }

    static List createProductCategories(){
    	List categories = new ArrayList();
    	
    	return categories;
    }

    static PaletteContainer createControlGroup(PaletteRoot root){
    	PaletteGroup controlGroup = new PaletteGroup("Control");
    	List entries = new ArrayList();

    	ToolEntry tool = new PanningSelectionToolEntry();
    	entries.add(tool);
    	root.setDefaultEntry(tool);

    	tool = new MarqueeToolEntry();
    	entries.add(tool);
    	
    	PaletteSeparator sep = new PaletteSeparator(
    			"kobold.client.plam.editor.sep1"); //$NON-NLS-1$
    	sep.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
    	entries.add(sep); //$NON-NLS-1$
   	
    	controlGroup.addAll(entries);
    	return controlGroup;
    }

    static private PaletteContainer createMetaNodesDrawer() 
    {
        PaletteDrawer drawer = new PaletteDrawer("Meta Nodes");
    	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
    	
    	List entries = new ArrayList();
    	
    	AssetCreationToolEntry toolEntry = new AssetCreationToolEntry(
    	    "AND Node",
    		"Insert a new AND node.",
    		MetaNode.AND,
    		new KoboldAssetFactory(MetaNode.AND),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	entries.add(toolEntry);

    	toolEntry = new AssetCreationToolEntry(
    	    "OR Node",
    		"Insert a new OR node.",
    		MetaNode.OR,
    		new KoboldAssetFactory(MetaNode.OR),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	entries.add(toolEntry);
    	
    	drawer.addAll(entries);
    	return drawer;

    }
    
    static private PaletteContainer createProductlineComponentsDrawer(){

    	PaletteDrawer drawer = new PaletteDrawer("Productline Assets");
    	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

    	List entries = new ArrayList();

       	AssetCreationToolEntry combined = new AssetCreationToolEntry(
    		"Component",
    		"Insert a new Component.",
    		AbstractAsset.COMPONENT,
    		new KoboldAssetFactory(AbstractAsset.COMPONENT),
    		KoboldPLAMPlugin.getImageDescriptor("icons/component.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/component.gif")
    	);
    	entries.add(combined);

    	combined = new AssetCreationToolEntry(
    		"Variant",
    		"Insert a new Variant.",
    		AbstractAsset.VARIANT,
    		new KoboldAssetFactory(AbstractAsset.VARIANT),
    		KoboldPLAMPlugin.getImageDescriptor("icons/variant.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/variant.gif")
    	);
    	entries.add(combined);
    	
    	combined = new AssetCreationToolEntry(
    		"Release",
    		"Insert a new Release.",
    		AbstractAsset.RELEASE,
    		new KoboldAssetFactory(AbstractAsset.RELEASE),
    		KoboldPLAMPlugin.getImageDescriptor("icons/release.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/release.gif")
    	);
    	entries.add(combined);
    	
    	drawer.addAll(entries);
    	return drawer;
    }
    
    static PaletteContainer createProductlineTools()
    {
    	PaletteDrawer drawer = new PaletteDrawer("Tools");
    	    //KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"));
    	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
    	
    	drawer.add(new ProductComposerToolEntry());
    	return drawer;
    }
    
    static PaletteContainer createEdgeDrawer()
    {
    	PaletteDrawer drawer = new PaletteDrawer("Connections");
    	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
    	
       	ToolEntry tool = new ConnectionCreationToolEntry(
    		"Include Edge",
    		"Include Edge",
    		new KoboldAssetFactory(Edge.INCLUDE),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	drawer.add(tool);
    	tool = new ConnectionCreationToolEntry(
    		"Exclude Edge",
    		"Exclude Edge",
    		new KoboldAssetFactory(Edge.EXCLUDE),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	drawer.add(tool);
    	
    	return drawer;
    }
    
}
