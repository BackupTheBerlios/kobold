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
 * $Id: MetaNode.java,v 1.10 2004/10/21 21:32:41 martinplies Exp $
 *
 */ 
package kobold.client.plam.model;

import java.util.List;

import kobold.client.plam.model.edges.Edge;

import org.dom4j.Element;


/**
 * @author pliesmn
 *
 * 
 */
public class MetaNode  extends AbstractAsset {
    public static final String OR = META_NODE + ".OR"; // a logic xor
    public static final String AND = META_NODE + ".AND";
    public static final String OR_GXL_TYPE = "http://kobold.berlios.de/types#OrNode";
    public static final String AND_GXL_TYPE = "http://kobold.berlios.de/types#AndNode";
    
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
    
    /**
     * Returns the type of the edges the node can get connected with.
     * Returns null if type is undefined.
     * 
     */
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
        
        if (element.attributeValue("edgeType", "").equals(Edge.EXCLUDE)) {
            edgeType = Edge.EXCLUDE;
        } else if (element.attributeValue("edgeType", "").equals(Edge.INCLUDE)) {
            edgeType = Edge.INCLUDE;
        } else {
            edgeType = null;
        }
    }
    
    public Element serialize()
    {
        Element root = super.serialize();
		root.setName(MetaNode.META_NODE);
        root.addAttribute("type", type);
        root.addAttribute("edgeType", edgeType);
        
        return root;
    }

    /**
     * @see kobold.client.plam.model.AbstractAsset#getGXLChildren()
     */
    public List getGXLChildren() {
        return null;
    }

    /**
     * @see kobold.client.plam.model.AbstractAsset#getGXLType()
     */
    public String getGXLType() {
        if (this.type.equals(OR)){
          return OR_GXL_TYPE;
        } else {
           return AND_GXL_TYPE;
        }
    }

}
