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
 *//*
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
 * @author pliesmn
 *
 * 
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
          edges.add(new Edge(startNode, targetNode, type, number));
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
