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
 * $Id: AbstractStatus.java,v 1.2 2004/06/25 12:58:28 rendgeor Exp $
 *
 */
package kobold.common.model;

import kobold.common.data.ISerializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * TODO: Kopfkommentar
 * @author Tammo
 */
public abstract class AbstractStatus implements ISerializable
{
    public static final String DEPRECATED_ID = "deprecated";
    
    /**
     * Returns the unique id of this status object.
     * 
     * @return
     */
    public abstract String getId();

    /**
     * Returns a short handy name for this status.
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Returns a more detailed description.
     * 
     * @return
     */
    public abstract String getDescription();

    /**
     * Returns an icon to be displayed in the graphical editor.
     * 
     * @return
     */
    public abstract ImageDescriptor getIcon();
    
    /**
     * Use AbstractStatus::createStatus(Element) instead!
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public final void deserialize(Element element) {}
    
    /**
     * @see kobold.common.data.ISerializable#serialize()
     */
    public final Element serialize()
    {
        Element element = DocumentHelper.createElement("status");
        element.addAttribute("id", getId());
        return element;
    }
    
    /**
     * Factory method.
     * @param element
     * @return
     */
    public static AbstractStatus createStatus(Element element) 
    {
        String id = element.attributeValue("id");
        if (id.equals(AbstractStatus.DEPRECATED_ID)) {
            return new DeprecatedStatus();
        }
        
        return null;
    }
}