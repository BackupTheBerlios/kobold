/*
 * Created on 10.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.model.edges;

import kobold.client.plam.model.productline.Component;

/**
 * @author meiner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class testEdges {

    public testEdges() {}
    public static void main(String[] args) {
        testEdges t = new testEdges();
        t.test1();
    }
    
    void test1() {
        EdgeContainer ec = new EdgeContainer();
        Component c1 = new Component();
        Component c2 = new Component();
        Component c3 = new Component();
        Component c4 = new Component();
        c1.setName("c1");
        MetaNode mn1 = new MetaNode(MetaNode.OR);
        ec.addEdge(c1, mn1);
        
        o( ec.containsEdge(c1, mn1), "cont 1");
        o( ! ec.containsEdge(c1, c2), "cont 2");
        ec.addEdge(mn1, c2);
        ec.addEdge(mn1, c3);
        o( ec.getEdge(c1, c2) == null, "getE 2");
    }
    
    static void o(boolean b, String s){
      if (b) {System.out.println("OK Test: "+s);}
      else {System.out.println("Failur Test: "+s);};
    }
    
    
    
}
