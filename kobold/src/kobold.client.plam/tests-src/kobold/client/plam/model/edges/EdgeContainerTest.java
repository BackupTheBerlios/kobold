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
 * $Id: EdgeContainerTest.java,v 1.2 2004/07/23 20:31:54 vanto Exp $
 *
 */
package kobold.client.plam.model.edges;

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
        Productline root = new Productline();
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
        ec.addEdge(mn1, c3, Edge.BAUHAUS);
        assertNull(ec.getEdge(c1, c2, Edge.BAUHAUS));
        assertNull(ec.getEdge(mn1, c3, Edge.EXCLUDE));
        assertNotNull(ec.getEdge(mn1, c3, Edge.BAUHAUS));
    }
    
}
