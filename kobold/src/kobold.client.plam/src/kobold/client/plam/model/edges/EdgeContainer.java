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
 *
 *
 * Created on 07.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.model.edges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import kobold.client.plam.model.AbstractRootAsset;
import kobold.common.data.ISerializable;

import org.dom4j.Element;

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
    
  
  Map startNodesList = new HashMap();
  Map targetNodesList = new HashMap();
  AbstractRootAsset root; 
  
  
  public EdgeContainer(AbstractRootAsset root) {
      this.root = root;
  }
  
  
  
  /**
   * Creates and adds a node to this edgecontainer. 
   * The nummer tells, for how many edges this edge represent.
   * Number is only an additonal Information for the user. 
   * Number is only user of extern edges.
   * If the target node or the start node are not understartNodes of
   * the root asset of this conatainer or if the edge exitst, this method return false
   * and the edge is not created.
   * If the edge is added this method return true; 
   * 
 * @param startNode
 * @param targetNode
 * @param type
 * @param number
 * @return true, if the edege can added.
 */
public boolean addEdge(INode startNode, INode targetNode, String type, int number){
      if (startNode.getRoot()!= root || targetNode.getRoot() != root){          
          return false;
      }
      addStartNode(startNode);
      addTragetNode(targetNode);      
      // create Edge
      List edges = ((List) startNodesList.get(startNode));
      if (! containsEdge(edges, targetNode, type)){
          Edge newEdge = new Edge(startNode, targetNode, type, number);
          edges.add(newEdge);          
          // addEdge also to targetNodesList
          ((List) targetNodesList.get(targetNode)).add(newEdge);
          return true;
      }
      return false;
  }
  

  /**
 * @param targetNode
 */
private void addTragetNode(INode targetNode) {
  if (! targetNodesList.containsKey(targetNode)){
      targetNodesList.put(targetNode, new ArrayList());
  }
}

/**
 * @param startNode
 */
private void addStartNode(INode startNode) {
    if (! startNodesList.containsKey(startNode)){
        startNodesList.put(startNode, new ArrayList());
    }
}

/**
   * Creates and adds a node to this edgecontainer. 
   * If the target node or the start node are not understartNodes of
   * the root asset of this conatainer or if the edge exitst, this method return false
   * and the edge is not created.
   * If the edge is added this method return true; 
   * 
 * @param startNode
 * @param targetNode
 * @param type
 */
public boolean addEdge(INode startNode, INode targetNode, String type){
     return addEdge(targetNode,targetNode, type, 1);
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
private boolean containsEdge (INode startNode, INode targetNode, String type){
      if (this.startNodesList.containsKey(startNode)){
          return containsEdge((List) this.startNodesList.get(startNode), targetNode, type);
        }  else { return false; }  
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
                 edges.remove(edge); 
              }
          }
          // removes also from targetNodesList
          edges = (List) targetNodesList.get(targetNode);
          for (ListIterator ite = edges.listIterator(); ite.hasNext();){
              Edge edge = (Edge) ite.next();
              if(edge.getStartNode().equals(startNode) && edge.getType().equals(type)){
                 edges.remove(edge);
              }
          }
      }  
  }
  
 
  /**
   * 
 * @param startNode
 * @param targetNode
 * @return all edges, that start at the startNode and end at the targetNode
 */
private Edge[] getEdges(INode startNode, INode targetNode) {
    if (! startNodesList.containsKey(startNode)) {return new Edge[0];}
    List list = new LinkedList();
    List edges = (List) startNodesList.get(startNode);
    for (Iterator ite = edges.iterator(); ite.hasNext();){
        Edge edge = (Edge) ite.next();
        if (edge.getTargetNode().equals(targetNode)){
            list.add(edge);
        }
    }
    return (Edge[]) list.toArray(new Edge[0]);
  }

 /**
  * Returns an Edgearray with all Edges from startNode. 
  * If no edges exists from this node the, an empty Edgearray is returned.
 * @param startNode
 * @return Returns an Edgearray with all Edges from startNode. 
 */
public Edge[] getEdgesFrom(INode startNode){
     if (startNodesList.containsKey(startNode)){
         return (Edge[]) ((List) startNodesList.get(startNode)).toArray(new Edge[0]);
     } else {
         return new Edge[0];
     }   
 }
 
 /**
  * Returns an Edgearray with all Edges from targetNode.
  * If no edges exists to this node the, an empty Edgearray is returned.
 * @param targetNode
 * @return Returns an Edgearray with all Edges from targetNode.
 */
public Edge[] getEdgesTo(INode targetNode){
     if(targetNodesList.containsKey(targetNode)){
         return (Edge[]) ((List) targetNodesList.get(targetNode)).toArray(new Edge[0]);
     } else {
         return new Edge[0];
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
  
  
 
  public Element serialize() {
     // TODO
      return null;
  }

  
  
  /* (non-Javadoc)
  * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
  */
  public void deserialize(Element element) {
    // TODO Auto-generated method stub    
  }
  
  

}
