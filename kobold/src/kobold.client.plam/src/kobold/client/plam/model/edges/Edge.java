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
 * $Id: Edge.java,v 1.5 2004/07/23 23:25:14 vanto Exp $
 *
 */
package kobold.client.plam.model.edges;


/**
 * @author pliesmn
 *
 * This Class represent an Edge in the graph. Instances of this class are Elemnts of the EdgeConatainer.
 * The Attriubutes startNode and taregetNode must not change here.  
 */
public class Edge {
    public static final String KOBOLD_EDGE = "Kobold Edge";
    
    private INode startNode;
    private INode targetNode;
    private String type;
    private int edgeCount; // Number of edges, that this this edge represent
    
    // kobold edge types
    public static final String INCLUDE = "edge.include";
    public static final String EXCLUDE = "edge.exclude";
    public static final String BAUHAUS = "edge.bauhaus";
    
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
}
