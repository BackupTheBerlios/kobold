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
 * $Id: GraphFactory.java,v 1.4 2004/08/04 12:56:32 martinplies Exp $
 *
 */
package kobold.client.plam.graphimport;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.widgets.Shell;


/**
 * @author pliesmn
 *
 * Imports a Bauhausgraph or accordant GXL graphs that can be mapped to 
 * the product. 
 * It create only additional edges between existing Filedescriptors.
 * Nodes that can not mapped on the file descriptors are ignored.
 * Edges form or to ignored nodes are also ignored.
 * Structure of a node:
 * 
 *  <node id="[NODE ID]">
 *     <type  xlink:href=["Directory"|"Module"]/>
 *     <attr name="Object_Name">
 *       <string>[NODE NAME]</string>
 *     </attr>
 *     <attr name="Path_Name">
 *       <string>[PATH OF THE NODE]</string>
 *     </attr>
 *   </node>
 *  
 * or:
 * 
 * <node id="[NODE ID]">
 *     <type  xlink:href="Module"/>
 *     <attr name="File_Name">
 *       <string>[NODE NAME]</string>
 *     </attr>
 *     <attr name="Path_Name">
 *       <string>[PATH OF THE NODE]</string>
 *     </attr>
 *   </node>
 * 
 * Structure of an edge:
 * <edge from="[NODE ID]" to="[NODE ID]">
 *     <type  xlink:href="Call"/>
 *     <attr name="Edge_Cover_Counter">
 *       <int> 19</int>
 *     </attr>
 *   </edge>
 *
 * 
 *  Additional atttributes, subnodes(attribute nodes) or subgraphs are ignored.
 *  [Node Name] must be equal to filename of a FileDescriptor otherwise the node is ignored.
 *  Nodes, their href is not equal to "Directoy" or "Module" are ignored.
 *  The attribute nodes "Path_Name" and "Edge_Cover_Counter" are not need necessarily.
 *  
 */
public class GraphFactory {
 private Map m = new HashMap();
 
 public GraphFactory(){
 }
 
 
 /**
  * 
 * @param map
 * @param filedesc
 */
private void addFileDescsToMap(Map map, FileDescriptor filedesc){
     if (filedesc.isDirectory()){
         for (Iterator ite = filedesc.getFileDescriptors().iterator(); ite.hasNext();){
             addFileDescsToMap(map, (FileDescriptor) ite.next());
         }
     } else if (map.containsKey(filedesc.getFilename())){
        List descriptors = (List) map.get(filedesc.getFilename());
        descriptors.add(filedesc);        
     } else {
        List descriptors = new LinkedList(); 
        descriptors.add(filedesc);
        map.put(filedesc.getFilename(), descriptors);
     }
 }

  private void addFileDescsToMap(Map map, ProductComponent productC){           
      for (Iterator ite = productC.getFileDescriptors().iterator(); ite.hasNext();){
          addFileDescsToMap(map, (FileDescriptor) ite.next());
      }
      for (Iterator ite = productC.getProductComponents().iterator(); ite.hasNext();){
          addFileDescsToMap(map, (ProductComponent) ite.next());
      }    
  }
  
 
 public EdgeContainer importGraph(File gxlGraph, Product product) throws DocumentException{
     EdgeContainer edgeContainer = new EdgeContainer(product);
     Set koboldTypes = Edge.getKoboldEdgeTypes();
     // parse xml
     SAXReader xmlReader = new SAXReader();
     Document doc = xmlReader.read(gxlGraph);
     Element root = doc.getRootElement();
     Element graph = root.element(new QName("graph"));
     
     // create an map of all file descriptors
     HashMap fileDescriptors = new HashMap();
     for (Iterator ite =  product.getRelatedComponents().iterator(); ite.hasNext();){
         addFileDescsToMap(fileDescriptors, (ProductComponent) ite.next());
     }
     for (Iterator ite =  product.getSpecificComponents().iterator(); ite.hasNext();){
         addFileDescsToMap(fileDescriptors, (ProductComponent) ite.next());
     } 
     
     // get Directoy nodes of the gxlgraf and map they to the kobold files.
     Map nodes = mapFilenodesToFiledescriptors(graph, fileDescriptors);
         
     // parse the gxl Edges and create kobold edges
     for (Iterator ite = graph.elementIterator("edge"); ite.hasNext();) {
        Element gxlEdge = (Element) ite.next();
        String fromId = gxlEdge.attributeValue("from");
        String toId = gxlEdge.attributeValue("to");
        if(nodes.containsKey(fromId) && nodes.containsKey(toId)) {
           // found edge between two directorys
           String edgeCStr = getAttributeValue(gxlEdge, "Edge_Cover_Counter", "int");
           int edgeCount = 1;
           if (edgeCStr != null) {
              edgeCount = Integer.parseInt( edgeCStr.trim());
           } 
           Element  type = gxlEdge.element("type");
           if (type != null &&  type.attributeValue("href") != null){
               String typeStr = type.attributeValue("href");
               if(koboldTypes.contains(type)){                   
                   // extern edges must not have the same type likekobold edges.
                   // so type is renamed.
                   typeStr = typeStr +".extern";
               }
             // create new Edge
             edgeContainer.addEdge((INode) nodes.get(fromId), (INode) nodes.get(toId), typeStr, edgeCount);
           }          
        }
     } 
          
     return edgeContainer;
     // look for Edges of the directory nodes and create kobold edges.
 }
 
 /**
  * Maps the "Directoy" and "Module" nodes in the subgraph to the right
  * Filedescriptors. Nodes that cannot be mapped to a Filedescriptor are
  * ignored.
  * 
 * @param graph
 * @param fileDescriptors
 * @return map  with the Filenodes as keys Ids and the right Filedescriptor as value the right Filedescriptor
 */
private Map mapFilenodesToFiledescriptors(Element graph, HashMap fileDescriptors) {
    Map nodes = new HashMap(); 
     Map map = new HashMap();
     for (Iterator ite = graph.elementIterator("node"); ite.hasNext();) {
         Element node  = (Element) ite.next();
         Element  type = node.element("type");
         if (type != null && (
                 type.attributeValue("href").equalsIgnoreCase("Directory" )
              || type.attributeValue("href").equalsIgnoreCase("Module")     ) ){
           // take nodeinfos
            String nodeName;
           if(type.attributeValue("href").equalsIgnoreCase("Module"))  {
               nodeName = getAttributeValue(node, "File_Name", "string");
           }else{
               nodeName = getAttributeValue(node, "Object_Name", "string");
           }
           
           String nodeBauhausPath = getAttributeValue(node, "Path_Name", "string");
           FileDescriptor fd = findFileDescriptor((List) fileDescriptors.get(nodeName), nodeName, nodeBauhausPath);
           String nodeId = node.attributeValue("id");
           if (fd != null && nodeId != null)  {
              nodes.put(nodeId, fd);
           }           
         } 
         
     }
    return nodes;
} 

/**
 * finds  the right Filedescritpor.
 * Exist more than one Filedescriptor, that filename is equal to nodename, the 
 * user is asked, which Filedescriptor should mapped to the node. 
 * @param container
 * @param fileDescriptors
 * @param nodeName 
 * @param nodeBauhausPath additional information
 */
private FileDescriptor findFileDescriptor(List fileDescriptors, String nodeName, String nodeBauhausPath) {
    if(fileDescriptors == null || fileDescriptors.size() == 0) {
        // In the koboldproduct existst no File with nodeName as Filename.
        return null;
    } 
    if (fileDescriptors.size() == 1){
      // it exists only one filedescriptor that Filename is equal to the nodename
      return  (FileDescriptor) fileDescriptors.get(0);
    } else {
        // starte einen Dialog um den Benutzer zu fragen, welcher Fildescriptor zu dem Knoten gehört.
      NodeAssignmentDialog nad = new NodeAssignmentDialog(fileDescriptors, nodeName, nodeBauhausPath, new Shell());
      nad.open();      
      return nad.getSelectedFileDesciptor();
    }
}


private String getAttributeValue(Element node, String attributeName, String type) {
    Iterator nodeAttrIte = node.elementIterator("attr");
    while (nodeAttrIte.hasNext()) {
      Element attr = (Element) nodeAttrIte.next();
      if (attr.attributeValue("name").equals(attributeName)) {
          return attr.elementText(type);                                     
      }          
    }
    return null;
}

}
