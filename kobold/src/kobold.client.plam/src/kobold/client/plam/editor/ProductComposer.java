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
package kobold.client.plam.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;
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
        // TODO
        get(node).setUse();
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_USED);
        logger.debug(node + " has new state USED");
    }

    /**
     * precondition: node is no MetaNode;
     * 
     * @param node
     */
    private void use(AbstractAsset node) {
        NodeAttr attr = get(node);
        if (attr.isUse()) { return; }
        boolean isOldStatOpen = attr.isOpen();
        attr.setChanged(true);
        List edgesFollowToStart = new LinkedList();

        // Change Status use. This is only here allowed for non metanodes.
        attr.setUse();
        attr.setChanged(true);

        // set the status of the parents also to USE
        // overwrite any other status. If the child is set to USE, then
        // the parent must also has the status USE.
        AbstractAsset parent = node.getParent();
        if (!(parent instanceof Productline) && !get(parent).isUse()) {
            use(parent);
        } else if (attr.isUserChanged
                && (node instanceof Variant || node instanceof Release)) {
            // the brothers of this node has set to MUST_NOT_USE. The parent has
            // already set to
            //  USE at further time, so the parent donnot do set the brothers to
            // MUST_NOT_USE.
            List brothers;
            if (node instanceof Component) {
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
        // if(node.) {}

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
                    && (!get(edge.getTargetNode()).isUse())) {
                useMeta((AbstractAsset) edge.getTargetNode());
                if (!get(edge.getTargetNode()).isUse()) {
                    get(node)
                            .setWarning(
                                    "Target node(s) of include edge are not used in product!");
                }
            } else if (edge.getType().equals(Edge.EXCLUDE)
                    && (!get(edge.getTargetNode()).isMustNotUse())) {
                mustNotUseMeta(node);
                if (get(edge.getTargetNode()).isMustNotUse()) {
                    get(node)
                            .setWarning(
                                    "Target node(s) of exclude edge have all not to be used!");
                }
            }
        }

        // update incoming edges
        for (Iterator ite = container.getEdgesTo(node, Edge.INCLUDE).iterator(); ite
        .hasNext();) {
            Edge edge = (Edge) ite.next();
            targetNodeIsNowUsed ((AbstractAsset)edge.getStartNode(), (AbstractAsset)edge.getTargetNode());
        }

    }


    private void targetNodeIsNowUsed(AbstractAsset node, AbstractAsset target) {
        if (!(node instanceof MetaNode)) {
            get(node).setChanged(true);// so warning and toWorkOn is tested.
            return;
        }
        if (((MetaNode) node).getType().equals(MetaNode.AND)) {
            boolean oneChildMustNotUsed = false;
            boolean oneChildOpen = false;
            for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                if (get(edge.getTargetNode()).isOpen()) {
                    oneChildOpen = true;
                } else {
                    oneChildMustNotUsed = true;
                }
            }

            if (oneChildMustNotUsed) {
                // do nothing. Node has alredy state MustNotUse
            } else if (oneChildOpen) {
                if (!get(node).isOpen()) {
                    get(node).setOpen();
                    for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                            .hasNext();) {
                        Edge edge = (Edge) ite.next();
                        targetNodeIsNowOpen(
                                (AbstractAsset) edge.getStartNode(), node);
                    }
                }
            }

        } else if (((MetaNode) node).getType().equals(MetaNode.OR)) {
            // gain information
            int useNodes = 0;
            int mustNotUseNodes = 0;
            AbstractAsset otherUsedNode = null; //a node, that was used, before
            // target was set to USE.
            List openList = new LinkedList();
            // at first gain Information
            for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                AbstractAsset child = (AbstractAsset) edge.getTargetNode();
                NodeAttr childAttr = get(node);
                if (childAttr.isUse()) {
                    useNodes++;
                    if (!child.equals(target)) {
                        otherUsedNode = child;
                    }
                } else if (childAttr.isOpen()) {
                    openList.add(child);
                }
            }
            if (get(node).isUse() && startUsed(node)) {
                // it is neccessary, that this node keeps the USE state.
                // this node is used an target is set to use. So this target had
                // state MustNotUse. So the other target with USE state must set
                // to MustNotUse
                //if (need()) {
//TODO
                //}

            }
            // only the the state of node must may be set.

        }
    }

    /*
     * Test if a Variant or Comonent or Release form which the edgesystem start
     * isUsed. Returns true, if a start of the edgeSystem is used. otherwiese
     * false; *
     */
    private boolean startUsed(AbstractAsset node) {
        if (!(node instanceof MetaNode)) { return get(node).isUse(); }
        boolean startUsed = false;
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            AbstractAsset start = (AbstractAsset) edge.getStartNode();
            if (get(start).isUse()) {
                startUsed = true;
            }
        }
        return startUsed;
    }

    /**
     * @param startNode
     */
    private void targetNodeIsNowOpen(AbstractAsset startNode,
            AbstractAsset target) {

    }

    /*
     * try to find one way that can be used
     */
    private void useMeta(INode node) {
        if (!get(node).isOpen()) { return; }
        if (!(node instanceof MetaNode)) {
            // // found Variant, Component or release;
            // use right method.
            use((AbstractAsset) node);
            return;
        }
        MetaNode meta = (MetaNode) node;

        NodeAttr nodeAttr = get(meta);
        if (meta.getType().equals(MetaNode.AND)) {
            // handel AND nodes
            boolean allChildrenUsed = true;
            for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                AbstractAsset child = (AbstractAsset) edge.getTargetNode();
                NodeAttr childAttr = get(meta);
                if (edge.getType() == Edge.INCLUDE) {
                    if (child instanceof MetaNode) {
                        useMeta((MetaNode) child);
                    } else {
                        if (childAttr.isOpen()) {
                            use(child);
                        }
                    }
                    if (!childAttr.isUse()) {
                        //nodeAttr.setToWorkOn(true);
                        allChildrenUsed = false;
                    }
                } else {
                    nodeAttr
                            .addWarning("You must not mix edgetypes at one metaode");
                }
            }
            if (allChildrenUsed) {
                nodeAttr.setUse();
            } else {
                nodeAttr.setMustNotUse();
            }
        } else {// handel OR nodes
            int useNodes = 0;
            int mustNotUseNodes = 0;
            List openList = new LinkedList();

            // at first gain Information
            for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                if (edge.getType() == Edge.INCLUDE) {
                    INode child = edge.getTargetNode();
                    NodeAttr childAttr = get(meta);
                    if (childAttr.isUse()) {
                        useNodes++;
                    } else if (childAttr.isOpen()) {
                        openList.add(child);
                    }
                } else {
                    nodeAttr
                            .addWarning("You must not mix edgetypes at one metaode");
                }
            }
            if (useNodes == 1 && openList.size() > 0) {
                for (Iterator ite = openList.iterator(); ite.hasNext();) {
                    mustNotUseMeta((INode) ite.next());
                }
                nodeAttr.setUse();
            } else if (useNodes == 0 && openList.size() == 1) {
                useMeta((INode) openList);
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
        if (!get(node).isOpen()) { return; // parents can olny set status to
        // open children.
        }

        // node can only set to mustNotUse, if all children have the status
        // MustNotUse
        boolean allChildrenAreMustNotUse = true;
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (get(edge.getStartNode()).isOpen()) {
                mustNotUseMeta(node);
                if (node instanceof MetaNode) {
                    mustNotUseMeta(edge.getTargetNode());
                } else {
                    mustNotUse((AbstractAsset) edge.getTargetNode(), false);
                }
            } else if (get(edge.getStartNode()).isUse()) {
                allChildrenAreMustNotUse = false;
            }
        }

        if (allChildrenAreMustNotUse) {
            get(node).setMustNotUse();
        } else {
            get(node).setOpen();
        }
    }

    /**
     * @param node
     */
    private void mustNotUse(AbstractAsset node, boolean firstAction) {
        if(firstAction){
            // decition of the use to set this node to this state.
            get(node).setUserChanged(true);
        } else {
            get(node).setUserChanged(false);
        }
        // set must not use to all children
        get(node).setMustNotUse();
        List children = Collections.EMPTY_LIST;
        if (node instanceof Variant) {
            children = ((Variant) node).getReleases();
            children.addAll(((Variant) node).getComponents());
        } else if (node instanceof Component) {
            children = ((Component) node).getVariants();
        }
        for (Iterator ite = children.iterator(); ite.hasNext();) {
            AbstractAsset child = (AbstractAsset) ite.next();
            if (!get(child).isMustNotUse()) {
                if (get(child).isUserChanged) {
                    get(child).removeChangeState();
                }
            }
        }

        // actualize edges
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
                if ((!get(child).isUse()) && (!get(child).isUserChanged())) {
                    use(child);
                } else if ((!get(child).isUse())
                        && (get(child).isUserChanged())) {
                    //warn
                }
            }
        } else if (parent instanceof Component || parent instanceof Variant) {
            // one and only one of the of teh releases and of the variants of a
            // node must set to USE;
            List children;
            if (parent instanceof Component) {
                children = ((Component) parent).getVariants();
            } else {
                children = ((Variant) parent).getReleases();
            }

            // at first gain information
            List useList = new LinkedList();
            List mustNotList = new LinkedList();
            List openList = new LinkedList();
            for (Iterator ite = children.iterator(); ite.hasNext();) {
                AbstractAsset node = (AbstractAsset) ite.next();
                if (get(node).isUse()) {
                    useList.add(node);
                } else if (get(node).isOpen()) {
                    mustNotList.add(node);
                }
            }

            if (useList.size() > 1) {
                // try to set some back.
                // nodes that set from the user to USE cannot be changed, but
                // the others can set to MUST_NOT_USE.
                for (Iterator ite = useList.listIterator(); ite.hasNext();) {
                    AbstractAsset node = (AbstractAsset) ite.next();
                    if (!get(node).isUserChanged()) {
                        useList.remove(node);
                        setMustNotUse(node);
                    }
                }
            }

            if (useList.size() > 0 && openList.size() > 0) {
                // set last open nodes to MUST_NOT_USE.
                for (Iterator ite = openList.listIterator(); ite.hasNext();) {
                    AbstractAsset node = (AbstractAsset) ite.next();
                    setMustNotUse(node);
                    openList.remove(node);
                }
            }

            if (openList.size() > 0) {
                get(parent).setToWorkOn(true);
            } else {
                get(parent).setToWorkOn(false);
            }

            if (useList.size() > 0) {
                // warn
            } else {
                // may unwarn
            }
        }

    }

    /**
     * Test, if this node is need in the product.
     * 
     * @param node
     * @return
     */

    private void notNeeded(AbstractAsset start) { 
        // if this node or
        //its children is set to USE from the // user the node is need.
        LinkedList list = new LinkedList();
        Set visited = new HashSet();
        LinkedList  useNodes = new LinkedList();
        list.add(start);
        while (list.size() > 0) {
            AbstractAsset node = (AbstractAsset) list.removeFirst();
            if (! visited.contains(node)){
               visited.add(node); 
            }
            if (get(node).isUse()) {
                if (get(node).isUserChanged()) {                   
                    return;
                } else {
                    useNodes.add(node);
                    // it is easyer to test later, if the nodes are used
                    // add all children. If a child has set to used this node and the other used nodes in the list are used.
                    if (node instanceof Component) {
                       list.add(((Component)node).getVariants());
                    } else if (node instanceof Variant) {
                        list.add(((Variant)node).getComponents());
                        list.add(((Variant)node).getReleases());
                    }
                    // also on the incoming edges must looked.
                    list.add(container.getEdgesTo(node, Edge.INCLUDE));
                }
            }
        }
        /// !!! no node found tha is changed from the user !!
        // all used nodes in the list can be set to open.
        // (maybe, they are then set to MustNotUse, if required.)
        // now set all used nodes to open and update edges so, that 
        // MethaNode represent no wrong. Find and set also nodes with the state mustNotUse to open, if possible.
        // Gain start nodes of updated MetaNodes.  After all update edges of gained startnodes.
        // Then updated nodes can also set to use again.
        // eingehenden include edges müssen geupdatet werden deren Knoten sind schon in deruse nodes
        Set updateEdges = new HashSet(); // Edges their ground nodes must be upadated;
        LinkedList exludeNodes = new LinkedList();
        for(Iterator ite = useNodes.iterator();;){
           INode node = (INode) ite.next();
           get(node).setOpen();
          // for (Iterator ite = container.getEdgesTo(Edge.EXCLUDE).iterator();;){
            //TODO   
       //    }
        }
        
    }
    


    /**
     * @param parent
     * @return
     */
    private boolean isAllowedToSetUSE(AbstractAsset node) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setMustNotUse(AbstractAsset node) {
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
        logger.debug(node + " has new state MUST_NOT_USE");
    }

    public void setOpen(AbstractAsset node) {
        get(node).setOpen();
        // TODO
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
        logger.debug(node + " has new state OPEN");
    }

    public boolean isUsed(AbstractAsset node) {
        return get(node).isUse();
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
        return get(node).hasWarning;
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

        int nonUSEChildren = -1;

        public NodeAttr() {

        }

        /**
         *  
         */
        public void removeChangeState() {
            // TODO Auto-generated method stub

        }

        /**
         * @param string
         */
        public void addWarning(String string) {
            // TODO Auto-generated method stub

        }

        /**
         * @return Returns the changed.
         */
        public boolean isChanged() {
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
            return warning;
        }

        void setUse() {
            status = STATE_USED;
        }

        boolean isUse() {
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
            return hasWarning;
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