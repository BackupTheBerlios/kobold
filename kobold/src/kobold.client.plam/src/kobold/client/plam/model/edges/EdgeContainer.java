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
 * $Id: EdgeContainer.java,v 1.10 2004/07/29 11:49:01 martinplies Exp $
 * 
 */
package kobold.client.plam.model.edges;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.workflow.LocalMessageQueue;
import kobold.common.data.ISerializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

/**
 * An EdgeContainer contains all edges of a product or a 
 * productline. The Edges are magaged form their EdgeContainer.
 * All Edges are directed. To get a fast Return. 
 * The EdgeContainer has two Adazentslists to manage the edges.  
 * 
 * @author pliesmn
 *
 * 
 */
public class EdgeContainer implements ISerializable {
    
    private static final Log logger = LogFactory.getLog(EdgeContainer.class);
    
    /* IDs for event notification */
    public static final String ID_SOURCE_CHANGED = "source";
    public static final String ID_TARGET_CHANGED = "target";
    
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    private Map startNodesList = new HashMap();
    private Map targetNodesList = new HashMap();
    private AbstractRootAsset root;

    private String type = "EdgeContainer"; 

    
    public EdgeContainer(AbstractRootAsset root) {
        this.root = root;
    }

    /**
     * Creates and adds a node to this edgecontainer. 
     * The nummer tells, for how many edges this edge represent.
     * Number is only an additonal Information for the user. 
     * Number is only user of extern edges.
     * If the target node or the start node are not understartNodes of
     * the root asset of this conatainer or if the edge exitst, this method return null
     * and the edge is not created.
     * If the edge is added this method return the edge; 
     * 
     * @param startNode
     * @param targetNode
     * @param type
     * @param number
     * @return true, if the edege can added.
     */
    public Edge addEdge(INode startNode, INode targetNode, String type, int number){
        if (startNode.getRoot()!= root || targetNode.getRoot() != root){          
            return null;
        }

        // create Edge
        List sourceEdges = getSourceEdges(startNode);
        if (! containsEdge(sourceEdges, targetNode, type)) {
            Edge newEdge = new Edge(startNode, targetNode, type, number);
            
            sourceEdges.add(newEdge);          
            listeners.firePropertyChange(ID_SOURCE_CHANGED, null, newEdge);
            
            // addEdge also to targetNodesList
            getTargetEdges(targetNode).add(newEdge);
            listeners.firePropertyChange(ID_TARGET_CHANGED, null, newEdge);
            logger.debug("Edge added: " + newEdge);
            return newEdge;
        }
        return null;
    }
    
    
    /**
     * @param targetNode
     */
    private List getTargetEdges(INode targetNode) 
    {
        List targetEdges = (List)targetNodesList.get(targetNode);
        if (targetEdges == null) {
            targetEdges = new ArrayList();
            targetNodesList.put(targetNode, targetEdges);
        }
        return targetEdges;
    }
    
    /**
     * @param startNode
     */
    private List getSourceEdges(INode startNode) 
    {
        List sourceEdges = (List)startNodesList.get(startNode);
        if (sourceEdges == null) {
            sourceEdges = new ArrayList();
            startNodesList.put(startNode, sourceEdges);
        }
        return sourceEdges;
    }
    
    /**
     * Creates and adds a node to this edgecontainer. 
     * If the target node or the start node are not understartNodes of
     * the root asset of this conatainer or if the edge exitst, this method returns null
     * and the edge is not created.
     * If the edge is added this method returns the edge.; 
     * 
     * @param startNode
     * @param targetNode
     * @param type
     */
    public Edge addEdge(INode startNode, INode targetNode, String type){
        return addEdge(startNode,targetNode, type, 1);
    }
    
    private boolean containsEdge(List edges, INode targetNode) {
        Iterator ite = edges.iterator();
        while (ite.hasNext()) {
            Edge edge = (Edge)ite.next();
            if (edge.getTargetNode().equals(targetNode)){
                return true;
            }
        }
        return false;      
    }
    
    /**
     * @param startNode
     * @param targetNode
     * @return returns true, if an edge exists from the start node to the target node, otherwise false.
     */
    public boolean containsEdge(INode startNode, INode targetNode) {
        if (this.startNodesList.containsKey(startNode)){
            return containsEdge((List) this.startNodesList.get(startNode), targetNode);
        }  else { return false; }      
    }
    
    private boolean containsEdge(List edges, INode targetNode, String type) {
        Iterator ite = edges.iterator();
        while (ite.hasNext()) {
            Edge edge = (Edge)ite.next();
            if (edge.getTargetNode().equals(targetNode) && edge.getType().equals(type) ){
                return true;
            }
        }
        return false;      
    }
    
    /**
     * @param startNode
     * @param targetNode
     * @param type
     * @return returns true, if an edge with this type exists form the start node to the target node , otherwise flase.
     */
    public boolean containsEdge (INode startNode, INode targetNode, String type){
        if (this.startNodesList.containsKey(startNode)){
            return containsEdge((List) this.startNodesList.get(startNode), targetNode, type);
        } else { 
            return false; 
        }  
    }
    
    public void removeEdges(INode startNode, INode targetNode){
        if (this.startNodesList.containsKey(startNode)){
            List edges = (List) startNodesList.get(startNode);
            for (ListIterator ite = edges.listIterator(); ite.hasNext();){
                Edge edge = (Edge) ite.next();
                if(edge.getTargetNode().equals(targetNode)){
                    edges.remove(edge);
                }
            }
            // remove also from targetNodesList
            edges = (List) targetNodesList.get(targetNode);
            for (ListIterator ite = edges.listIterator(); ite.hasNext();){
                Edge edge = (Edge) ite.next();
                if(edge.getStartNode().equals(startNode)){
                    edges.remove(edge);
                }
            }
            
        }  
    }
    
    /**
     * Remove the edge from the start node to the target node with this type.
     * @param startNode
     * @param targetNode
     * @param type
     */
    public void removeEdge(INode startNode, INode targetNode, String type){
        if (this.startNodesList.containsKey(startNode)){
            List edges = (List) startNodesList.get(startNode);
            for (ListIterator ite = edges.listIterator(); ite.hasNext();){
                Edge edge = (Edge) ite.next();
                if(edge.getTargetNode().equals(targetNode) && edge.getType().equals(type)){
                    ite.remove(); 
                }
            }
            // removes also from targetNodesList
            edges = (List) targetNodesList.get(targetNode);
            for (ListIterator ite = edges.listIterator(); ite.hasNext();){
                Edge edge = (Edge) ite.next();
                if(edge.getStartNode().equals(startNode) && edge.getType().equals(type)){
                    ite.remove();
                }
            }
        }  
    }
    
    public void removeEdge(Edge edge) {
        List sourceEdges = (List)startNodesList.get(edge.getStartNode());
        List targetEdges = (List)targetNodesList.get(edge.getTargetNode());
        sourceEdges.remove(edge);
        listeners.firePropertyChange(ID_SOURCE_CHANGED, null, edge);
        targetEdges.remove(edge);
        listeners.firePropertyChange(ID_TARGET_CHANGED, null, edge);
    }
    
    public Edge addEdge(Edge edge) {
        if (containsEdge(edge.getStartNode(), edge.getTargetNode(), edge.getType())) {
            return null;
        }
        List sourceEdges = (List)startNodesList.get(edge.getStartNode());
        List targetEdges = (List)targetNodesList.get(edge.getTargetNode());
        sourceEdges.add(edge);
        listeners.firePropertyChange(ID_SOURCE_CHANGED, null, edge);
        targetEdges.add(edge);
        listeners.firePropertyChange(ID_TARGET_CHANGED, null, edge);
        return edge;
    }
    
    /**
     * 
     * @param startNode
     * @param targetNode
     * @return a list of edges that start at the startNode and end at the targetNode 
     * or Collection.EMPTY_LIST
     */
    private List getEdges(INode startNode, INode targetNode) {
        if (! startNodesList.containsKey(startNode)) {
            return Collections.EMPTY_LIST;
        }
        List list = new LinkedList();
        List edges = (List) startNodesList.get(startNode);
        for (Iterator ite = edges.iterator(); ite.hasNext();){
            Edge edge = (Edge) ite.next();
            if (edge.getTargetNode().equals(targetNode)){
                list.add(edge);
            }
        }
        return Collections.unmodifiableList(list);
    }
    
    /**
     * Returns an Edge list with all Edges from startNode. 
     * If no edges exists from this node, Collection.EMPTY_LIST is returned.
     * 
     * @param startNode
     * @return Returns an Edgearray with all Edges from startNode. 
     */
    public List getEdgesFrom(INode startNode){
        if (startNodesList.containsKey(startNode)){
            return Collections.unmodifiableList((List)startNodesList.get(startNode));
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    /**
     * Returns an Edge list with all Edges from targetNode.
     * If no edges exists to this node, Collection.EMPTY_LIST is returned.
     * 
     * @param targetNode
     * @return Returns an Edgearray with all Edges from targetNode.
     */
    public List getEdgesTo(INode targetNode){
        if (targetNodesList.containsKey(targetNode)){
            return Collections.unmodifiableList((List)targetNodesList.get(targetNode));
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    
        
    /**
     * Retruns the edge, or null, if the edge not exists.
     * @param edges
     * @param targetNode
     * @param type
     * @return Returns edge. 
     */
    private Edge getEdge(List edges, INode targetNode, String type){
        for (Iterator ite = edges.iterator(); ite.hasNext();){
            Edge edge = (Edge) ite.next();
            if (edge.getTargetNode().equals(targetNode)
                    && edge.getType().equals(type) ) {
                return edge;
            }
        }
        return null;
    }
    
    public Edge getEdge(INode sourceNode, INode targetNode, String type) {
        List sourceEdges = getSourceEdges(sourceNode);
        return getEdge(getSourceEdges(sourceNode), targetNode, type);
    }
    
    public Element serialize() {
        Element element = DocumentHelper.createElement(getType());
        element.addAttribute("root", root.getId());
        Element edges = element.addElement("edges");
        for (Iterator edgesListIte = this.startNodesList.entrySet().iterator(); edgesListIte.hasNext();){
           for(Iterator edgeIte =((List)edgesListIte).iterator(); edgeIte.hasNext();){
               Edge edge = (Edge) edgeIte;                
               if(edge.getType().equals(Edge.INCLUDE) || edge.getType().equals(Edge.EXCLUDE)){                   
                 try {
                    edges.add(edge.serialize());
                } catch (ClassCastException e) {
                	logger.debug("There are extern edges");
                }
               } // else it is an Bauhaus edge theese are not serialised.
           }            
        }    
        
        return element;
    }
    
    /**
     * @return
     */
    private String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element) {
        // TODO Auto-generated method stub    
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

 public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}
 


 /**
  * Returns all nodes, that have an edge with this type to targetNode.
 * @param targetNode
 * @param type
 * @return
 */
public List getEdgesTo(INode targetNode, String type ){
     LinkedList arrL = new LinkedList();
     for (Iterator ite = ((List) targetNodesList.get(targetNode)).iterator(); ite.hasNext(); ){
        Edge edge = (Edge) ite.next();
        if (edge.getType().equals(type)){
          arrL.add(edge.getStartNode());
        }
     }            
     return arrL;
 }
  

    
}
