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
 * $Id: ArchitectureEditorInput.java,v 1.4 2004/08/04 23:03:02 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import kobold.client.plam.model.AbstractRootAsset;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;


/**
 * @author Tammo
 */
public class ArchitectureEditorInput implements IEditorInput, IPersistableElement
{

    private AbstractRootAsset asset;

    public ArchitectureEditorInput(AbstractRootAsset asset) 
    {
        this.asset = asset;
    }
    
    public AbstractRootAsset getAsset()
    {
        return asset;
    }
    
    /**
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists()
    {
        return false;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName()
    {
        return asset.getName();
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable()
    {
        return this;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText()
    {
        return ""+asset.getDescription();
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter)
    {
        return null;
    }

    /**
     * @see org.eclipse.ui.IPersistableElement#getFactoryId()
     */
    public String getFactoryId()
    {
        return ArchitectureEditorInputFactory.getFactoryId();
    }

    /**
     * @see org.eclipse.ui.IPersistableElement#saveState(org.eclipse.ui.IMemento)
     */
    public void saveState(IMemento memento)
    {
        ArchitectureEditorInputFactory.saveState(memento, this);
    }
 
}
