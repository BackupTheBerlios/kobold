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
 * $Id: ViewModel.java,v 1.10 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.model;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.common.data.ISerializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * @author vanto
 */
public class ViewModel implements ISerializable 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ViewModel.class);

    private Map propertyByModelId = new HashMap();
    private boolean modified = false;
    
    public ViewModel() {}
    public ViewModel(Element element)
    {
        deserialize(element);
    }
    
    public AssetView getAssetView(AbstractAsset node) {
        AssetView prop = (AssetView)propertyByModelId.get(node.getId());
        if (prop == null) {
            prop = new AssetView(this);
            propertyByModelId.put(node.getId(), prop);
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
            AssetView prop = (AssetView)propertyByModelId.get(id);
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
            propertyByModelId.put(el.attributeValue("id"), new AssetView(this, el));
        }
    }

    public void setModified(boolean mod) 
    {
        modified = mod;
    }
    
    public boolean isDirty()
    {
        return modified;
    }
   
}
