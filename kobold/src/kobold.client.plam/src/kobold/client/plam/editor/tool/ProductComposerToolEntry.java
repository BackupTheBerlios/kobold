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
 * $Id: ProductComposerToolEntry.java,v 1.1 2004/07/22 16:42:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.tool;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;


/**
 * A ToolEntry for a {@link ProductComposerTool}.
 * 
 * @author Tammo
 */
public class ProductComposerToolEntry extends ToolEntry
{
    
    /**
     * Creates a new ProductComposerToolEntry.
     */
    public ProductComposerToolEntry()
    {
    	super("Compose Product",
    		"Use this tool to compose products.",
    		// TODO: Create icons
    		SharedImages.DESC_SELECTION_TOOL_16,
    		SharedImages.DESC_SELECTION_TOOL_24);
    	
    	setUserModificationPermission(PERMISSION_NO_MODIFICATION);
    }
    
    /**
     * @see org.eclipse.gef.palette.ToolEntry#createTool()
     */
    public Tool createTool()
    {
        return new ProductComposerTool();
    }

}
