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
 * $Id: PaletteHelper.java,v 1.4 2004/07/22 16:42:23 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.model.KoboldAssetFactory;
import kobold.client.plam.editor.tool.ProductComposerToolEntry;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.gef.palette.CreationToolEntry;
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
 * @author Tammo
 */
public class PaletteHelper
{

    static private List createCategories(PaletteRoot root){
    	List categories = new ArrayList();
    	
    	categories.add(createControlGroup(root));
    	categories.add(createTemplateComponentsDrawer());
    	return categories;
    }

    static private PaletteContainer createControlGroup(PaletteRoot root){
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

    	/*tool = new ConnectionCreationToolEntry(
    		LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Label,
    		LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Description,
    		null,
    		ImageDescriptor.createFromFile(Circuit.class, "icons/connection16.gif"),//$NON-NLS-1$
    		ImageDescriptor.createFromFile(Circuit.class, "icons/connection24.gif")//$NON-NLS-1$
    	);
    	entries.add(tool);*/
    	controlGroup.addAll(entries);
    	return controlGroup;
    }

    static private PaletteContainer createTemplateComponentsDrawer(){

    	PaletteDrawer drawer = new PaletteDrawer("Assets",
    	    KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"));
    	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

    	List entries = new ArrayList();
    	
       	CreationToolEntry combined = new CreationToolEntry(
    		"Component",
    		"Insert a new Component.",
    		new KoboldAssetFactory(AbstractAsset.COMPONENT),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	entries.add(combined);

    	combined = new CreationToolEntry(
    		"Variant",
    		"Insert a new Variant.",
    		new KoboldAssetFactory(AbstractAsset.VARIANT),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	entries.add(combined);
    	
    	entries.add(new PaletteSeparator());

    	combined = new CreationToolEntry(
    		"Release",
    		"Insert a new Release.",
    		new KoboldAssetFactory(AbstractAsset.RELEASE),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"),
    		KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif")
    	);
    	entries.add(combined);
    	
    	entries.add(new PaletteSeparator());
    	entries.add(new ProductComposerToolEntry());

    	drawer.addAll(entries);
    	return drawer;
    }
    
    static PaletteRoot createPalette() {
    	PaletteRoot palette = new PaletteRoot();
    	palette.addAll(createCategories(palette));
    	return palette;
    }

 
}
