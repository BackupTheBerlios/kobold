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
 * $Id: LayoutAction.java,v 1.3 2004/09/21 20:54:30 vanto Exp $
 *
 */
package kobold.client.plam.editor.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.editor.ArchitectureEditor;
import kobold.client.plam.editor.model.AssetView;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;


/**
 * @author Tammo
 */
public class LayoutAction extends WorkbenchPartAction
{
    public static final String ID = "kobold.client.plam.editor.action.LayoutAction";
    private ArchitectureEditor editor;
    
    
    /**
     * @param part
     */
    public LayoutAction(ArchitectureEditor part)
    {
        super(part);
        this.editor = part;
    }

    /**
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    protected boolean calculateEnabled()
    {
        return true;
    }

    /**
     * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
     */
    protected void init() {
    	setId(ID);
    	setText("Layout");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        DirectedGraph cdg = new DirectedGraph();
        NodeList nodes = new NodeList();
        EdgeList edges = new EdgeList();
        AbstractRootAsset root = editor.getModel();
        EdgeContainer ec = root.getEdgeContainer();
        List mnodes = new ArrayList();
        mnodes.addAll(root.getChildren());
        
        ViewModel vmc = editor.getViewModel();
        
        Map m = new HashMap();
        
        for (int i = 0; i < mnodes.size(); i++) {
            AbstractAsset a = (AbstractAsset)mnodes.get(i);
            AssetView vm = vmc.getAssetView(a);
            Node n = new Node(a);
            n.x = vm.getLocation().x;
            n.y = vm.getLocation().y;
            if (vm.getSize().width > 0) {
                n.width = vm.getSize().width;
            }
            if (vm.getSize().height > 0) {
                n.height = vm.getSize().height;
            }
            nodes.add(n);
            m.put(a, n);
        }
        
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            AbstractAsset a = (AbstractAsset)it.next();
            List es = ec.getEdgesFrom(a);
            for (int i = 0; i<es.size(); i++) {
                Edge e = (Edge)es.get(i);
                Node s =(Node)m.get(e.getStartNode());
                Node t =(Node)m.get(e.getTargetNode());
                if (s != null && t != null) {
                    edges.add(new org.eclipse.draw2d.graph.Edge(s, t));
                }
            }
        }
        
        cdg.edges = edges;
        cdg.nodes = nodes;
        
        new DirectedGraphLayout().visit(cdg);
        
        it = cdg.nodes.iterator();
        while (it.hasNext()) {
            Node n = (Node)it.next();
            AssetView vm = vmc.getAssetView((AbstractAsset)n.data);
            vm.setLocation(new Point(n.x+10, n.y+40));
            vm.setSize(new Dimension(n.width, n.height));
        }
    }

}
