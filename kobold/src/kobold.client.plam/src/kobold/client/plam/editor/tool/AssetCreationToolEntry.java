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
 * $Id: AssetCreationToolEntry.java,v 1.1 2004/08/03 19:39:20 vanto Exp $
 *
 */
package kobold.client.plam.editor.tool;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;


/**
 * Provides a palette entry for the {@link kobold.client.plam.editor.tool.AssetCreationTool}.
 * 
 * @author Tammo
 */
public class AssetCreationToolEntry extends CombinedTemplateCreationEntry
{

    /**
     * Constructs a combined CreationTool and Template Entry with the given parameters.
     * 
     * @param label the label
     * @param shortDesc the short description
     * @param template the template
     * @param factory the factory
     * @param iconSmall the small icon
     * @param iconLarge the large icon
     */
    public AssetCreationToolEntry(String label, String shortDesc, Object template, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
    {
        super(label, shortDesc, template, factory, iconSmall, iconLarge);
    }

    /**
     * Create a {@link AssetCreationTool}.
     * @see org.eclipse.gef.palette.ToolEntry#createTool()
     */
    public Tool createTool()
    {
        return new AssetCreationTool(factory);
    }
}
