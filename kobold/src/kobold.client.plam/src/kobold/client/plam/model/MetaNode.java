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
 * The above copyri
 * ght notice and this permission notice shall be included in 
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
 * $Id: MetaNode.java,v 1.5 2004/08/06 03:40:44 martinplies Exp $
 *
 */ 
package kobold.client.plam.model;

import org.dom4j.Element;


/**
 * @author pliesmn
 *
 * 
 */
public class MetaNode  extends AbstractAsset {
    public static final String OR = META_NODE + ".OR"; // a logic xor
    public static final String AND = META_NODE + ".AND";
    
    private String type;
    private String edgeType;
    
    private MetaNode() 
    {
        super();
    }
    
    public MetaNode(String type){
        this();
        this.type = type;
    }
    
    public MetaNode(Element el)
    {
        this();
        deserialize(el);
    }
    
    /**
     * returns node type. 
     */
    public String getType() {
        return type;
    }
    
    public String getEdgeType() 
    {
        return edgeType;
    }
    
    public void setEdgeType(String edgeType) 
    {
        this.edgeType = edgeType;
    }
    
    public void deserialize(Element element)
    {
        super.deserialize(element);
        if (element.attributeValue("type").equals(MetaNode.AND)) {
            type = MetaNode.AND;
        } else if (element.attributeValue("type").equals(MetaNode.OR)) {
            type = MetaNode.OR;
        }
    }
    public Element serialize()
    {
        Element root = super.serialize();
		root.setName(MetaNode.META_NODE);
        root.addAttribute("type", type);
        
        return root;
    }
}
