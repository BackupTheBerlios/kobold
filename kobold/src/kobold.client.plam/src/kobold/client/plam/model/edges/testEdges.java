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
 * $Id: testEdges.java,v 1.3 2004/10/21 21:32:41 martinplies Exp $
 */

package kobold.client.plam.model.edges;

import kobold.client.plam.model.productline.Component;

/**
 * @author pliesmn
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
/*      EdgeContainer ec = new EdgeContainer();
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
 */   }
    
    static void o(boolean b, String s){
      if (b) {System.out.println("OK Test: "+s);}
      else {System.out.println("Failur Test: "+s);};
    }
    
    
    
}
