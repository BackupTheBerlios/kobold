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
 * $Id: EdgeContainerTest.java,v 1.3 2004/07/31 08:16:18 martinplies Exp $
 *
 */
package kobold.client.plam.model.edges;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;

import junit.framework.Assert;
import junit.framework.TestCase;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;


/**
 * @author pliesmn
 */
public class EdgeContainerTest extends TestCase {

    public void testAddNode() {
        String var = "t" + "est";
        Assert.assertEquals("test", var);
    }

    public void testStuff() {
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());
        Productline root = new Productline("Linux");
        
        EdgeContainer ec = new EdgeContainer(root);
        Component c1 = new Component();
        root.addComponent(c1);
        Component c2 = new Component();
        root.addComponent(c2);
        Component c3 = new Component();
        root.addComponent(c3);
        Component c4 = new Component();
        root.addComponent(c4);
        c1.setName("c1");
        MetaNode mn1 = new MetaNode(MetaNode.OR);
        root.addMetaNode(mn1);
        ec.addEdge(c1, mn1, Edge.INCLUDE);
        
        assertTrue(ec.containsEdge(c1, mn1));
        assertTrue(! ec.containsEdge(c1, c2));
        ec.addEdge(mn1, c2, Edge.EXCLUDE);
        ec.addEdge(mn1, c3, "Bauh");
        assertNull(ec.getEdge(c1, c2, "Bauh"));
        assertNull(ec.getEdge(mn1, c3, Edge.EXCLUDE));
        assertNotNull(ec.getEdge(mn1, c3, "Bauh"));
        
        Edge e0 = ec.addEdge(c1, c2, Edge.INCLUDE);
        Edge e1 = ec.addEdge(c1, c2, Edge.EXCLUDE);
        Edge e2 = ec.addEdge(c1, c1, Edge.EXCLUDE);
        Edge e3 = ec.addEdge(c1, c3, Edge.INCLUDE);
        Edge e4 = ec.addEdge(c3, c2, Edge.INCLUDE);
        Edge e5 = ec.addEdge(c2, mn1, Edge.INCLUDE);
        
        List list1 = ec.getEdgesFrom(c1);
        Set list1l = new HashSet();
        list1l.add(e0); list1l.add(e1);list1l.add(e2);list1l.add(e3);
        List list2 = ec.getEdgesTo(c2);
        List list2l = new ArrayList();
        list2l.add(e0); list2l.add(e1);list2l.add(e4);
        List list3 = ec.getEdgesTo(c2, Edge.INCLUDE);
        List list3l = new ArrayList();
        list3l.add(e0); list3l.add(e4);
        
       
        
        assertTrue(list1.containsAll(list1l));
        assertTrue(list2.containsAll(list2l));
        assertTrue(list3.containsAll(list3l));
        assertFalse(list2.contains(mn1));
        assertFalse(list3.contains(e1));
    }
    
}
