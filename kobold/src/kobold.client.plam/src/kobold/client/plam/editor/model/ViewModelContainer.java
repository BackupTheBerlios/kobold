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
 * $Id: ViewModelContainer.java,v 1.3 2004/06/24 03:06:01 vanto Exp $
 *
 */
package kobold.client.plam.editor.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import kobold.common.data.ISerializable;
import kobold.common.model.AbstractAsset;


/**
 * @author vanto
 */
public class ViewModelContainer implements ISerializable 
{
    private Map propertyByModelId = new HashMap();
    
    public ViewModelContainer() {}
    public ViewModelContainer(Element element)
    {
        deserialize(element);
    }
    
    public ViewModel getViewModel(AbstractAsset asset) {
        ViewModel prop = (ViewModel)propertyByModelId.get(asset.getId());
        if (prop == null) {
            prop = new ViewModel();
            propertyByModelId.put(asset.getId(), prop);
        }
        
        return prop;
    }

    /**
     * @see kobold.common.data.ISerializable#serialize()
     */
    public Element serialize()
    {
        Element element = DocumentHelper.createElement("viewmodel");
        
        Iterator it = propertyByModelId.keySet().iterator();
        while (it.hasNext()) {
            String id = (String)it.next();
            ViewModel prop = (ViewModel)propertyByModelId.get(id);
            element.add(prop.serialize().addAttribute("id", id));
        }
        
        return element;
    }

    /**
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element)
    {
        Iterator it = element.elementIterator("prop");
        while (it.hasNext()) {
            Element el = (Element)it.next();
            propertyByModelId.put(el.attribute("id"), new ViewModel(el));
        }
    }
   
}
