package kobold.client.plam.model.edges;


/**
 * @author meiner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Edge {
    public static final String KOBOLD_EDGE = "Kobold Edge";
 
    private INode startNode;
    private INode targetNode;
    private String type;
    private int edgeCount; // Number of edges, that this this edge represent
        
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
 
 
}
