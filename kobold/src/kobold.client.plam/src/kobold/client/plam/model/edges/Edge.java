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
 * $Id: Edge.java,v 1.12 2004/08/25 00:19:50 martinplies Exp $
 *
 */
package kobold.client.plam.model.edges;

import java.util.HashSet;
import java.util.Set;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.productline.Productline;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



/**
 * @author pliesmn
 *
 * This Class represent an Edge in the graph. Instances of this class are Elemnts of the EdgeConatainer.
 * The Attriubutes startNode and taregetNode must not change here.  
 */

public class Edge {
    private static final Logger logger = Logger.getLogger(Edge.class);
    public static final String KOBOLD_EDGE = "Kobold Edge";
    
    private INode startNode;
    private INode targetNode;
    private String type;
    private int edgeCount = 1; // Number of edges, that this this edge represent
    
    // kobold edge types
    public static final String INCLUDE = "edge.include";
    public static final String EXCLUDE = "edge.exclude";
    public static final String GXL_INCLUDE = "http://kobold.berlios.de/types#includeEdge";
    public static final String GXL_EXCLUDE = "http://kobold.berlios.de/types#exlcudeEdge";
    public static final String GXL_EXTERN = "http://kobold.berlios.de/types#externEdge";
    /**
     * @param startNode2
     * @param targetNode2
     */
    public Edge(INode startNode, INode targetNode) {
        this.startNode = startNode;
        this.targetNode = targetNode;
        this.type = KOBOLD_EDGE;
    }
    
    
    public Edge(INode startNode, INode targetNode, String type, int number) {
        this.startNode = startNode;
        this.targetNode = targetNode;
        this.type = type;
        this.edgeCount = number;
    }
    
    public Edge(Element element) {
       
    }
    
    
    /**
     * @param element
     * @param productline
     */
    public Edge(Element element, Productline productline) {        
        deserialize (element, productline);
    }

    /**
     * @return Returns the edgeCount.
     */
    public int getEdgeCount() {
        return edgeCount;
    }
    
    /**
     * @param edgeCount The edgeCount to set.
     */
    public void setEdgeCount(int edgeCount) {
        this.edgeCount = edgeCount;
    }
    
    /**
     * @return Returns the startNode.
     */
    public INode getStartNode() {
        return startNode;
    }
    
    
    /**
     * @return Returns the targetNode.
     */
    public INode getTargetNode() {
        return targetNode;
    }
    
    
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public String toString() {
        return getClass().getName() + " [start:"+startNode+", target:" + targetNode 
        	+ ", type:" + type + ", amount:" + edgeCount + "]";
    }
    
    /**
     * @return Returns all Kobold edge typs.
     */
    public static Set getKoboldEdgeTypes (){
        Set types = new HashSet();
        types.add(INCLUDE);
        types.add(EXCLUDE);
        return types;
    }
    

    public Element serialize() throws ClassCastException {
        Element element = DocumentHelper.createElement("edge");
        AbstractAsset startAsset = (AbstractAsset) this.startNode;
        AbstractAsset targetAsset = (AbstractAsset) this.targetNode;
        element.addAttribute("from", startAsset.getId());
        element.addAttribute("to", targetAsset.getId());
        element.addAttribute("type", type);
        return element;
    }
    
    
    public void deserialize (Element element, Productline prodline){
        type = element.attributeValue("type");        
        String fromAssetId = element.attributeValue("from");
        String  toAssetId = element.attributeValue("to"); 
        if (fromAssetId == null){
            logger.error("Doesn't get \"from\" attribute form:" + element);
        }
        if (toAssetId == null){
            logger.error("Doesn't get \"to\" attribute form:" + element);
         }
        if (type == null){
            logger.error("Doesn't get \"type\" attribute form:" + element);
         }
        startNode =  prodline.getAssetById(fromAssetId);
        targetNode = prodline.getAssetById(toAssetId);   
        ;int a =0;
    }
    
    public String getGXLType(){ 
        if (this.type.equals(INCLUDE)){
            return GXL_INCLUDE;            
        } else if(this.type.equals(EXCLUDE)) {
            return GXL_EXCLUDE;
        } else {
            return GXL_EXTERN;
        }
    }
    

}
