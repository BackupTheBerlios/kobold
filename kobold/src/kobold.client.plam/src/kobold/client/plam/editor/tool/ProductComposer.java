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
 */
package kobold.client.plam.editor.tool;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author pliesmn
 * 
 * UNDER CONSTRUCTION
 *  
 */
public class ProductComposer {

    private static final Log logger = LogFactory.getLog(ProductComposer.class);

    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(
            this);

    public static final String STATE_OPEN = "sopen";

    public static final String STATE_USED = "sused";

    public static final String STATE_MUST_NOT = "sdont";

    private HashMap nodes = new HashMap();

    private Productline productline; // the productline, that is use to create a

    // new

    private EdgeContainer container;

    public ProductComposer(Productline pl) {
        productline = pl;
        nodes.put(pl, new NodeAttr());
        container = pl.getEdgeContainer();
    }

    private NodeAttr get(INode node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, new NodeAttr());
        }
        return (NodeAttr) nodes.get(node);
    }

    public void setUsed(AbstractAsset node) {
        if (node instanceof MetaNode) { return; // User cannot change type of
                                                // Metanodes
        }
        use(node, true);
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_USED);
        logger.debug(node + " has new state USED");
    }

    /**
     * precondition: node is no MetaNode;
     * 
     * @param node
     */
    private void use(AbstractAsset node, boolean firstAction) {
        NodeAttr attr = get(node);
        if (attr.isUsed()) { return; }
        attr.setChanged(true);
        attr.setUserChanged(firstAction);

        // Change Status use. This is only here allowed for non metanodes.
        attr.setUse();

        // set the status of the parents also to USE
        // overwrite any other status. If the child is set to USE, then
        // the parent must also has the status USE.
        AbstractAsset parent = node.getParent();
        if (!(parent instanceof Productline) && !get(parent).isUsed()) {
            use(parent, false);
        } else if (attr.isUserChanged
                && (node instanceof Variant || node instanceof Release)) {
            // the brothers of this node has set to MUST_NOT_USE. The parent has
            // already set to
            //  USE at further time, so the parent donnot do set the brothers to
            // MUST_NOT_USE.
            //this.brotherNotNeedUse(node);
            // sets all brothers of node to mustNotUse
            List brothers;
            if (node instanceof Variant) {
                brothers = ((Component) node.getParent()).getVariants();
            } else {
                brothers = ((Variant) node.getParent()).getReleases();
            }
            for (Iterator ite = brothers.iterator(); ite.hasNext();) {
                AbstractAsset asset = (AbstractAsset) ite.next();
                if (asset != node) {
                    mustNotUse(asset, false);
                }
            }
        } 

        // look if also the children are need and have to/can set to USE
        // childs can not set To USE, if the say, that he donnot want them
        // or if only one of the child must set to USE and the User must
        // decide which child.

        // set children to USE
        setUSEtoChildren(node);

        //      [ follow incomeing inclued edges to test, if
        // the start nodes have(need) the status to work on]
        // actualize Nodes of incoming edges

        //actualizeIncomingIncludeEdges(node);
        // Now Follow the edges.

        // follow Edges and test, if target nodes, need/can change.

        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (edge.getType().equals(Edge.INCLUDE)
                    && (!get(edge.getTargetNode()).isUsed())) {
                useMeta((AbstractAsset) edge.getTargetNode());
                if (!get(edge.getTargetNode()).isUsed()) {
                    get(node)
                            .setWarning(
                                    "Target node(s) of include edge are not used in product!");
                }
            } else if (edge.getType().equals(Edge.EXCLUDE)
                    && (!get(edge.getTargetNode()).isMustNotUse())) {
                mustNotUseMeta(edge.getTargetNode());
                if (get(edge.getTargetNode()).isMustNotUse()) {
                    get(node)
                            .setWarning(
                                    "Target node(s) of exclude edge have all not to be used!");
                }
            }
        }

        // update incoming edges
        for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (edge.getType().equals(Edge.INCLUDE)) {
                updateStateMetaInclude((AbstractAsset) edge.getStartNode(),
                        (AbstractAsset) edge.getTargetNode());
            } else {
                updateStateMetaExclude((AbstractAsset) edge.getStartNode());
            }
        }

    }

    /*
     * Test if a Variant or Comonent or Release form which the edgesystem start
     * isUsed. Returns true, if a start of the edgeSystem is used. otherwiese
     * false; *
     */
    /*
     * private boolean isStartUsed(AbstractAsset node) { if (!(node instanceof
     * MetaNode)) { return get(node).isUsed(); } boolean startUsed = false; for
     * (Iterator ite = container.getEdgesTo(node).iterator(); ite .hasNext();) {
     * Edge edge = (Edge) ite.next(); AbstractAsset start = (AbstractAsset)
     * edge.getStartNode(); if (isStartUsed(edge.getStartNode())) { startUsed =
     * true; } } return startUsed; }
     */

    /*
     * try to find one way that can be used
     */
    private void useMeta(INode node) {
        if (!get(node).isOpen()) { return; }
        if (!(node instanceof MetaNode)) {
            // // found Variant, Component or release;
            // use right method.
            //            7
            use((AbstractAsset) node, false);
            return;
        }

        MetaNode meta = (MetaNode) node;

        NodeAttr nodeAttr = get(meta);
        nodeAttr.delWarning();
        if (meta.getType().equals(MetaNode.AND)) {
            // handel AND nodes
            boolean allChildrenUsed = true;
            for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                AbstractAsset child = (AbstractAsset) edge.getTargetNode();
                NodeAttr childAttr = get(meta);
                if (child instanceof MetaNode) {
                    useMeta((MetaNode) child);
                } else {
                    if (childAttr.isOpen()) {
                        use(child, false);
                    }
                }
                if (!childAttr.isUsed()) {
                    allChildrenUsed = false;
                }
            }

            if (allChildrenUsed) {
                nodeAttr.setUse();

            }
            nodeAttr.setOpen();
            nodeAttr.setWarning("Not all target nodes can set to true");
        } else {// handel OR nodes
            int useNodes = 0;
            int mustNotUseNodes = 0;
            List openList = new LinkedList();

            // at first gain Information
            for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                INode target = edge.getTargetNode();
                NodeAttr targetAttr = get(target);
                if (targetAttr.isUsed()) {
                    useNodes++;
                } else if (targetAttr.isOpen()) {
                    openList.add(target);
                }
            }
            if (useNodes == 1 && openList.size() > 0) {
                for (Iterator ite = openList.iterator(); ite.hasNext();) {
                    mustNotUseMeta((INode) ite.next());
                }
                nodeAttr.setUse();
            } else if (useNodes == 0 && openList.size() == 1) {
                useMeta((INode) openList.get(0));
                nodeAttr.setUse();
            } else {
                nodeAttr.setOpen();
            }
        }
    }

    /*
     * Follow edges and set targets to mustNotUse. if non Metanodes found,
     * use(AbstractAsset) is call for theese nodes. This fuctionn is used to set
     * the targets of exclude edges to MustNotUse. If on include edgegraphs a
     * metanode and its targets should be set to MustNotUse this function ís
     * also used @param node
     */
    private void mustNotUseMeta(INode node) {
        if (!get(node).isOpen()) { return; // parents can only set status to
        // open children.
        }
        if (!(node instanceof MetaNode)) {
            mustNotUse((AbstractAsset) node, false);
            return;
        }
        get(node).setUserChanged(true);

        // node can only set to mustNotUse, if all children have the status
        // MustNotUse
        boolean allChildrenAreMustNotUse = true;
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (get(edge.getTargetNode()).isOpen()) {                
                if (node instanceof MetaNode) {
                    mustNotUseMeta(edge.getTargetNode());
                } else {
                    mustNotUse((AbstractAsset) edge.getTargetNode(), false);
                }
            } else if (get(edge.getStartNode()).isUsed()) {
                allChildrenAreMustNotUse = false;
            }
        }

        if (allChildrenAreMustNotUse) {
            get(node).setMustNotUse();
        } else {
            get(node).setOpen();
        }
    }

    /*
     * void setUserChangedToTargets(INode node){ if(! (node instanceof
     * MetaNode)){ return; } for (Iterator ite = container.getEdgesTo(node);
     * ite.hasNext();){ setUserChangedToTargets(((Edge)
     * ite.next()).getTargetNode()); } }
     */

    /**
     * @param node
     */
    private void mustNotUse(AbstractAsset node, boolean firstAction) {
        NodeAttr attr = get(node);
        if (attr.isMustNotUse()) { return; }

        boolean wasOldStateUse= attr.isUsed();
        attr.setChanged(true);
        attr.setUserChanged(firstAction);
        attr.setMustNotUse();

        // children must also have state MustNotUse
        for (Iterator ite = getChildren(node).iterator(); ite.hasNext();) {
            mustNotUse((AbstractAsset) ite.next(), false);
        }

        // update outgoingEdges
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            clear(edge);
        }

        //update incoming edges
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (edge.getType().equals(Edge.INCLUDE)) {
                updateStateMetaInclude(edge.getStartNode(), edge
                        .getTargetNode());
            } else {
                updateStateMetaExclude(edge.getStartNode());
            }
        }

        // try to set the parent back to open
        if (wasOldStateUse && get(node.getParent()).isUsed()) {
            notNeededUse(node.getParent());
        }
    }

    private List getChildren(INode node) {
        List children;
        if (node instanceof Variant) {
            children = new LinkedList();
            children.addAll(((Variant) node).getComponents());
            children.addAll(((Variant) node).getReleases());
        } else if (node instanceof Component) {
            children = ((Component) node).getVariants();
        } else if (node instanceof Productline) {
            children = ((Productline) node).getComponents();
        } else {
            children = Collections.EMPTY_LIST;
        }
        return children;
    }

    /**
     * 
     * 
     * @param parent
     */
    private void setUSEtoChildren(AbstractAsset parent) {
        if (parent instanceof Variant) {
            // all components have to set to USE.
            for (Iterator ite = ((Variant) parent).getComponents().iterator(); ite
                    .hasNext();) {
                AbstractAsset child = (AbstractAsset) ite.next();
                if ((!get(child).isUsed()) && (!get(child).isUserChanged())) {
                    use(child, false);
                } else if ((!get(child).isUsed())
                        && (get(child).isUserChanged())) {
                    //warn
                }
            }
        }
        if (parent instanceof Component || parent instanceof Variant) {
            // one and only one of the of the releases and of the variants of a
            // node must set to USE;
            List children;
            if (parent instanceof Component) {
                children = ((Component) parent).getVariants();
            } else {
                children = ((Variant) parent).getReleases();
            }

            // at first gain information
            LinkedList useList = new LinkedList();
            LinkedList openList = new LinkedList();
            for (Iterator ite = children.iterator(); ite.hasNext();) {
                AbstractAsset node = (AbstractAsset) ite.next();
                if (get(node).isUsed()) {
                    useList.add(node);
                } else if (get(node).isOpen()) {
                    openList.add(node);
                }
            }

            if (useList.size() == 0 && openList.size() == 1) {
                use((AbstractAsset) openList.getFirst(), false);
            }

            if (useList.size() > 1) {
                // try to set some back.
                // nodes that set from the user to USE cannot be changed, but
                // the others can set to MUST_NOT_USE.
                for (int i = 0; useList.size() < i; i++) {
                    AbstractAsset node = (AbstractAsset) useList.get(i);
                    if (!get(node).isUserChanged()) {
                        mustNotUse(node, false);
                        useList.remove(node);
                        i--;
                    }
                }
            }

            if (useList.size() > 0 && openList.size() > 0) {
                // set last open nodes to MUST_NOT_USE.
                for (Iterator ite = openList.listIterator(); ite.hasNext();) {
                    AbstractAsset node = (AbstractAsset) ite.next();
                    mustNotUse(node, false);
                }
            }
        }

    }

    /**
     * Test, if this node is need in the product.
     * 
     * @param node
     * @return
     */

    private boolean notNeededUse(AbstractAsset start) {
        // if this node or
        //its children is set to USE from the // user the node is need.
        LinkedList list = new LinkedList();
        Set visited = new HashSet();
        LinkedList useNodes = new LinkedList();
        list.add(start);
        while (list.size() > 0) {
            AbstractAsset node = (AbstractAsset) list.removeFirst();
            if (!visited.contains(node)) {
                visited.add(node);
                if (get(node).isUsed()
                        || (node instanceof MetaNode && get(node).isOpen())) {
                    if (get(node).isUserChanged()
                            && !(node instanceof MetaNode)) {
                        // node start is needed.
                        return false;
                    } else {
                        useNodes.add(node);
                        // add all children. If a child has set to used this
                        // node and the other used nodes in the list are used.
                        //it is easyer to test later, if the nodes are used
                        if (node instanceof Component) {
                            list.addAll(((Component) node).getVariants());
                        } else if (node instanceof Variant) {
                            list.addAll(((Variant) node).getComponents());
                            list.addAll(((Variant) node).getReleases());
                        }
                        // also on the incoming edges must looked.
                        for (Iterator ite = container.getEdgesTo(node,
                                Edge.INCLUDE).iterator(); ite.hasNext();) {
                            list.add(((Edge) ite.next()).getStartNode());
                        }
                    }
                }
            }
        }
        /// !!! no node found that is changed from the user !!
        // all used nodes in the list can be set to open.
        // (maybe, they are then set to MustNotUse, if required.)
        // now set all used nodes to open and update edges so, that
        // MethaNode represent no wrong.

        LinkedList excludeEdges = new LinkedList();
        for (Iterator ite = useNodes.iterator(); ite.hasNext();) {
            INode node = (INode) ite.next();
            get(node).setOpen();
            // Update exclude edges. Nodes of include edges are in
            // useNodes-list.
            for (Iterator upd = container.getEdgesTo(node, Edge.EXCLUDE)
                    .iterator(); upd.hasNext();) {
                Edge edge = (Edge) upd.next();
                this.updateStateMetaExclude(node);
            }
        }

        //      Try to set the nodes to open, that must be set to USE/MustNotUse, if the nodes
        // in useNodes would be still used.
        for (Iterator ite = useNodes.iterator(); ite.hasNext();) {
            // Update outgoing edges.
            INode node = (INode) ite.next();
            //Call notNeedUse on parent node and clear on outgoing edges.
            // If node is MustNotUsed the edges are alredy updatesd.
            if (get(node).isOpen()) { // clear() call also notNeeded... . State
                                      // is maybe alredy changed.
                for (Iterator upd = container.getEdgesFrom(node).iterator(); upd
                        .hasNext();) {
                    clear((Edge) upd.next());
                }
                // If parent is used, try to set to open.
                if (get(((AbstractAsset) node).getParent()).isUsed()) {
                    notNeededUse(((AbstractAsset) node).getParent());
                }
                
                // try also to set MustNotUsed Children to open
                for (Iterator chIte = getChildren(node).iterator(); chIte.hasNext();){
                    AbstractAsset child = (AbstractAsset) chIte.next();
                    if(get(child).isMustNotUse()){
                        notNeededMustNot(child);
                    }                    
                }
            }
        }
        
        // 

        return true;
    }

    /*
     * @param asset
     */
    private boolean notNeededMustNot(AbstractAsset start) {
        LinkedList list = new LinkedList();
        Set visited = new HashSet();
        LinkedList mnuNodes = new LinkedList();
        list.add(start);
        while (list.size() > 0) {
            AbstractAsset node = (AbstractAsset) list.removeFirst();
            if (!visited.contains(node)) {
                visited.add(node);
                if (get(node).isMustNotUse()
                        || (node instanceof MetaNode && get(node).isOpen())) {
                    // look at this node
                    if ((get(node).isUserChanged() && !(node instanceof MetaNode))
                            || ((node instanceof Variant || node instanceof Release) && brotherIsNeededUse(node))) {
                        // node start is not needed.
                        return false;
                    } else {
                        mnuNodes.add(node);
                        //it is easyer to test later, if the nodes are used
                        if (!(node.getParent() instanceof Productline)) {
                            list.add(node.getParent());
                        }
                        // also on the incoming edges must looked.
                        for (Iterator ite = container.getEdgesTo(node)
                                .iterator(); ite.hasNext();) {
                            list.add(((Edge) ite.next()).getStartNode());
                        }
                    }
                }
            }
        }
        /// !!! no node found that is changed from the user !!
        // all used nodes in the list can be set to open.
        // (maybe, they are then set to MustNotUse, if required.)
        // now set all used nodes to open and update edges so, that
        // MethaNode represent no wrong. Find and set also nodes with the state
        // mustNotUse to open, if possible.
        // Gain start nodes of updated MetaNodes. After all update edges of
        // gained startnodes.
        // Then updated nodes can also set to use again.
        // eingehenden include edges müssen geupdatet werden deren Knoten sind
        // schon in deruse nodes
        for (Iterator ite = mnuNodes.iterator(); ite.hasNext();) {
            INode node = (INode) ite.next();
            get(node).setOpen();
            // update incoming edges
            for (Iterator upd = container.getEdgesTo(node).iterator(); upd
                    .hasNext();) {
                Edge edge = (Edge) upd.next();
                if (edge.getType().equals(Edge.INCLUDE)) {
                    this.updateStateMetaInclude(edge.getStartNode(), edge
                            .getTargetNode());
                } else if (edge.getType().equals(Edge.EXCLUDE)) {
                    this.updateStateMetaExclude(node);
                }
            }
        }
        for (Iterator ite = mnuNodes.iterator(); ite.hasNext();) {
            // update outgoing edges
            INode node = (INode) ite.next();
            if (get(node).isOpen()) { // clear call also notNeeded... . State is
                                      // maybe alredy changed.
                for (Iterator upd = container.getEdgesFrom(node).iterator(); upd
                        .hasNext();) {
                    clear((Edge) upd.next());
                }
            }
        }
        // call NotNeededUse also on children. (Maybe they are also need not the
        // MustNotUse state.)
        for (Iterator ite = mnuNodes.iterator(); ite.hasNext();) {
            AbstractAsset node = (AbstractAsset) ite.next();
            if (!get(node).isMustNotUse()) {
                for (Iterator chIte = getChildren(node).iterator(); chIte
                        .hasNext();) {
                    notNeededMustNot((AbstractAsset) chIte.next());
                }
            }
        }
        return true;

    }
    
    // test for notNeededMustNot if a brother of node need the use state
    private boolean brotherIsNeededUse(AbstractAsset node) {
        List brothers;
        if (node instanceof Release){
            brothers = ((Variant) node.getParent()).getReleases();            
        } else if (node instanceof Variant){
            brothers = ((Component) node.getParent()).getVariants();  
        } else {
            return false;
        }
        for (Iterator ite = brothers.iterator(); ite.hasNext();){
            AbstractAsset brother = (AbstractAsset) ite.next();
            if (brother != node && get(brother).isUsed()){
                return notNeededUse(brother);                  
            }            
        }
        return false;
    }

    /*
     * node need use
     * try to set the brothers  of node to open. 
     * returns false, if one brother need USE.  
     */
    private boolean brotherNotNeedUse(AbstractAsset node) {
        List brothers;
        if (node instanceof Release) {
            brothers = ((Variant) node.getParent()).getReleases();
        } else if (node instanceof Variant) {
            brothers = ((Component) node.getParent()).getVariants();
        } else {
            return false;
        }
        
        boolean stateStillUse = false;
        for (Iterator ite = brothers.iterator(); ite.hasNext();) {
            AbstractAsset brother = (AbstractAsset) ite.next();
            if (brother != node && get(brother).isUsed()) { 
                if ( !notNeededUse(brother)) {
                    stateStillUse = true;  }
             } else {
                 mustNotUse(brother, false);
             }
        }
        return stateStillUse;
    }
    
    /*
     * clear settings of a used node.
     * 
     */
    private String clear(Edge edge) {
        INode node = edge.getTargetNode();
        NodeAttr nodeAttr = get(node);        
        if (!(node instanceof MetaNode)) {
            if (nodeAttr.isUsed()) {
                notNeededUse((AbstractAsset) node);
            } else if (nodeAttr.isMustNotUse()) {
                notNeededMustNot((AbstractAsset) node);
            }

        } else {
            // test if all nodes, their edges point to node, have not the state use
            // if such a node is used, node cannot clear.
            for(Iterator ite = container.getEdgesTo(node).iterator(); ite.hasNext();){
                if (get( ((Edge) ite.next()).getStartNode() ).isUsed()){
                    return STATE_USED;
                }
            }
            get(node).setUserChanged(false);
            nodeAttr.setOpen(); 
            int sizeOpen =0; int sizeUsed =0; int sizeMustNot =0;          
            for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                    .hasNext();) {
               String state = clear((Edge) ite.next());
               if (state == STATE_OPEN){
                   sizeOpen++;
               }else if (state == STATE_USED) {
                   sizeUsed++;
               } else{
                   sizeMustNot++;
               }
            }
            setStateMeta(node, sizeOpen, sizeUsed, sizeMustNot);           
        }
        return nodeAttr.getState();
    }
    
    

    /*
     * Update given node and nodes of incoming edges. Sets State to MetaNode of
     * include edges. Looks on nodes, to this node has edges to, gain
     * information and set state
     */
    private void updateStateMetaInclude(INode node, INode targetNode) {
        if (!(node instanceof MetaNode)) { return; }
        String acState = get(node).getState();
        int sizeOpen = 0;
        int sizeUse = 0;
        int sizeMustNot = 0;
        INode otherUsedNode = null; //a node, that was used, before
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edgeChi = (Edge) ite.next();
            if (get(edgeChi.getTargetNode()).isOpen()) {
                sizeOpen++;
            } else if (get(edgeChi.getTargetNode()).isUsed()) {
                sizeUse++;
                if (edgeChi.getTargetNode() != targetNode) {
                    otherUsedNode = edgeChi.getTargetNode();
                }
            } else {
                sizeMustNot++;
            }
        }
            
        // try to set the other used node back to open.
        if ( ((MetaNode) node).getType().equals(MetaNode.OR) 
                && get(node).isUsed() && get(node).isUserChanged
                && otherUsedNode != null) {           
            if (notNeededUse((AbstractAsset) otherUsedNode)) { 
                return; // node has been updatet.
            }
        }
        
        // aktualize state of this node.
        setStateMeta(node, sizeOpen, sizeUse, sizeMustNot);
        
        if (acState != get(node).getState()) {
            // state changed => node, that edges point to this node
            // must be updated.
            for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
            }
        }
    }

    
    /*
     * Set state to a Metanode. 
     * @param node State of this node is set.
     * @param sizeOpen  number of target nodes, that has the state OPEN
     * @param sizeUse  number of target nodes, that has the state USED
     * @param sizeMustNot  number of target nodes, that has the state MUST_NOT_USED
     */
    private void setStateMeta(INode node, int sizeOpen, int sizeUse, int sizeMustNot) {
        if (((MetaNode) node).getType().equals(MetaNode.AND)) {
            if (sizeOpen == 0 && sizeMustNot == 0) {
                get(node).setUse();
            } else if (sizeOpen == 0 && sizeUse == 0) {
                get(node).setMustNotUse();
            } else {
                get(node).setOpen();
            }
        } else {
            // handel or nodes
            if ((sizeUse == 1) && (sizeOpen == 0)) {
                get(node).setUse();
            } else if ((sizeUse == 0) && (sizeOpen == 0)) {
                get(node).setMustNotUse();
            } else {
                get(node).setOpen();
            }
        }
    }

    /*
     * Update given node and nodes of incoming edges. Do not update non
     * MetaNodes.
     */
    private void updateStateMetaExclude(INode node) {
        if (!(node instanceof MetaNode)) { return; }
        String state = get(node).getState();
        boolean noOpenNode = true;
        Iterator it = container.getEdgesFrom(node).iterator();
        // set node to open, if this node point to one open node
        while (it.hasNext() && noOpenNode) {
            Edge edgeChi = (Edge) it.next();
            if (get(edgeChi.getTargetNode()).isOpen()) {
                noOpenNode = false;
            }
        }
        if (noOpenNode) {
            get(node).setMustNotUse();
        } else {
            get(node).setOpen();
        }
        if (state != get(node).getState()) {
            // also nodes, that point to this nodes must be updated
            for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                    .hasNext();) {
                updateStateMetaExclude(node);
            }
        }
    }

    public void setMustNotUse(AbstractAsset node) {
        if (node instanceof MetaNode) { return; // User cannot change type of
                                                // Metanodes
        }
        mustNotUse(node, true);
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null,
                STATE_MUST_NOT);
        logger.debug(node + " has new state MUST_NOT_USE");
    }

    public void setOpen(AbstractAsset node) {
        
        if (node instanceof MetaNode) { return; // User cannot change type of
                                                // Metanodes
        }
        get(node).setUserChanged(false);
        if (get(node).isUsed()) {
            notNeededUse(node);
        } else if (get(node).isMustNotUse()) {
            notNeededMustNot(node);
        }
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
        logger.debug(node + " has new state OPEN");
    }

    public boolean isUsed(AbstractAsset node) {
        return get(node).isUsed();
    }

    public boolean isMustNotUse(AbstractAsset node) {
        return get(node).isMustNotUse();
    }

    public boolean isOpen(AbstractAsset node) {
        return get(node).isOpen();
    }

    public String getState(AbstractAsset node) {
        return get(node).getState();
    }

    public boolean isToWorkOn(AbstractAsset node) {
        return get(node).isToWorkOn();
    }

    public boolean hasWarning(AbstractAsset node) {
        return get(node).hasWarning();
    }

    public String getWarning(AbstractAsset node) {
        return get(node).getWarnig();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }
    
    public Product generateProduct() {      
    	
    	Product product = new Product(productline);
        // create ProductComponments for used CoreAssets.    	
    	for (Iterator ite = productline.getComponents().iterator(); ite.hasNext();){
    	    Component node = (Component) ite.next();
    	    if (get(node).isUsed()){
    	        product.addComponent(generateProductComponent(node));
    	    }    	   
    	}
    	//TODO add edges

    	return product;
    }
    

    /*
     * Create a Productcomponent for a Component.
     * if comp has a used Variant and a used Relase a Related Component is created
     * else a SpecificComponent is created.
     * Create also ProductComponents for all used Componets for the used variant.
     *  
     * @param comp  Component. Create ProductComponent for this Component.
     * @return ProductComponet for comp and its used subnodes
     */
    private ProductComponent generateProductComponent(Component comp){
        ProductComponent productComponent;
        Variant variant = null;
        Iterator varIte = comp.getVariants().iterator();
	    while ( varIte.hasNext() &&
	            ! get(variant = (Variant) varIte.next()).isUsed()){}
    	if (variant != null && get(variant).isUsed()){
    	    // find Release
    	    Release release = null;
    	    Iterator  relIte = variant.getReleases().iterator();
    	    while ( relIte.hasNext() &&
    	            ! get( release = (Release) relIte.next() ).isUsed()  
    	           ){}
    	    if (release != null && get(release).isUsed()){
    	        // => a related Component can be construct
    	        productComponent = new RelatedComponent(variant, release);
    	    } else{
    	        productComponent = new SpecificComponent(comp.getName());    	        
    	    }
    	    // add sub components
    	    for (Iterator subIte = variant.getComponents().iterator(); subIte.hasNext();){
    	        Component subComp = (Component) subIte.next(); 
    	        if (get(subComp).isUsed()){
    	            productComponent.addProductComponent(generateProductComponent(subComp));
    	        }
    	    }
    	} else {
    	    productComponent = new SpecificComponent(comp.getName()); 
    	    // no sub variant is used => there must not exitst subnodes of variant, that are usesd.
    	}
    	
    	 return productComponent;   
	}
    

    /*
     * 
     * @author pliesmn
     * 
     * Contains all additionl information, that the alorithm need of an INode.
     */
    class NodeAttr {

        private String status = STATE_OPEN;

        private boolean isToWorkOn = false;

        private boolean hasWarning = false;

        private String warning;

        private boolean isUserChanged = false;

        private boolean changed = false;

        NodeAttr() {

        }

 
        void removeChangeState() {
            changed = false;
        }


        /**
         * @return Returns the changed.
         */
        boolean isChanged() {
            return changed;
        }

        /**
         * @param changed
         *            The changed to set.
         */
        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        void setOpen() {
            status = STATE_OPEN;
            isUserChanged = false;
            // Now alsp the algorithm is allowed to change the status.
        }

        void setMustNotUse() {
            status = STATE_MUST_NOT;
        }

        String getState() {
            return status;
        }

        String getWarnig() {
            return "warningf";
        }

        void setUse() {
            status = STATE_USED;
        }

        boolean isUsed() {
            return status == STATE_USED;
        }

        boolean isMustNotUse() {
            return status == STATE_MUST_NOT;
        }

        boolean isOpen() {
            return status == STATE_OPEN;
        }

        boolean isToWorkOn() {
            return isToWorkOn;
        }

        void setToWorkOn(boolean b) {
            isToWorkOn = b;
        }

        boolean hasWarning() {
            return true;
        }

        void setWarning(String s) {
            this.warning = s;
            this.hasWarning = true;
        }

        void delWarning() {
            hasWarning = false;
        }

        void setUserChanged(boolean b) {
            isUserChanged = b;
        }

        boolean isUserChanged() {
            return isUserChanged;
        }

    }

}