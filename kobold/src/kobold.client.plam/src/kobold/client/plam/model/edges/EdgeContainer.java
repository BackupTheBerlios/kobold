/*
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

import kobold.common.data.ISerializable;

import org.dom4j.Element;

/**
 * @author Martin Plies
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EdgeContainer implements ISerializable {
    
  
  Map nodes = new HashMap();
  
  
  
  public EdgeContainer() {
      
  }
  
  public void addNode(INode node){
      if (! nodes.containsKey(node)){
          nodes.put(node, new ArrayList());
      }
  }
  
  public void addEdge(INode startNode, INode targetNode){
      addNode(startNode);
      // create Edge
      List edges = ((List) nodes.get(startNode));
      if (! containsEdge(edges, targetNode)){
          edges.add(new Edge(startNode, targetNode));
      } 
  }
  
  public void addBauhausEdge(INode startNode, INode targetNode, String type, int number){
      addNode(startNode);
      // create Edge
      List edges = ((List) nodes.get(startNode));
      if (! containsEdge(edges, targetNode)){
          edges.add(new BauhausEdge(startNode, targetNode, type, number));
      }
  }
  
  private boolean containsEdge(List edges, INode targetNode) {
      Iterator ite = edges.iterator();
      while (ite.hasNext()) {
          if (((Edge)ite.next()).getTargetNode().equals(targetNode) ){
              return true;
          }
      }
      return false;      
  }
  
  public boolean containsEdge(INode startNode, INode targetNode) {
      if (this.nodes.containsKey(startNode)){
        return containsEdge((List) this.nodes.get(startNode), targetNode);
      }  else { return false; }      
  }
  
  public void removeEdges(INode startNode, INode targetNode){
      if (this.nodes.containsKey(startNode)){
          List edges = (List) nodes.get(startNode);
          for (ListIterator ite = edges.listIterator(); ite.hasNext();){
              Edge edge = (Edge) ite.next();
              if(edge.getTargetNode().equals(targetNode)){
                 edges.remove(edge);
              }
          }
      }  
  }
  
  public void removeEdge(INode startNode, INode targetNode, String type){
      if (this.nodes.containsKey(startNode)){
          List edges = (List) nodes.get(startNode);
          for (ListIterator ite = edges.listIterator(); ite.hasNext();){
              Edge edge = (Edge) ite.next();
              if(edge.getTargetNode().equals(targetNode) && edge.getType().equals(type)){
                 edges.remove(edge);
                 return;
              }
          }
      }  
  }
  
  
  /**
   * 
   * @return
   */
  public Edge getEdge(INode startNode, INode targetNode){
    if (this.nodes.containsKey(startNode)) {
      return getEdge((List) nodes.get(startNode), targetNode);  
    }  else {
        return null;
    }
  }
  
  private Edge getEdge(List edges, INode targetNode){
      for (Iterator ite = edges.iterator(); ite.hasNext();){
          Edge edge = (Edge) ite.next();
          if (edge.getTargetNode().equals(targetNode)) {
              return edge;
          }
      }
      return null;
   }
  
  private Edge[] getEdges(INode startNode, INode targetNode) {
    if (! nodes.containsKey(startNode)) {return new Edge[0];}
    List list = new LinkedList();
    List edges = (List) nodes.get(startNode);
    for (Iterator ite = edges.iterator(); ite.hasNext();){
        Edge edge = (Edge) ite.next();
        if (edge.getTargetNode().equals(targetNode)){
            list.add(edge);
        }
    }
    return (Edge[]) list.toArray(new Edge[0]);
  }
  
  private Edge getEdge(List edges, INode targetNode, String Type){
      for (Iterator ite = edges.iterator(); ite.hasNext();){
          Edge edge = (Edge) ite.next();
          if (edge.getTargetNode().equals(targetNode)) {
              return edge;
          }
      }
      return null;
   }
  
  
  
 
  public Element serialize() {
     //TODO
      return null;
  }

  
  
  /* (non-Javadoc)
  * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
  */
  public void deserialize(Element element) {
    // TODO Auto-generated method stub    
  }
  
  

}
