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
 * $Id: ProductComposer.java,v 1.19 2005/02/06 03:38:07 martinplies Exp $
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
import kobold.common.io.RepositoryDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author pliesmn
 * 
 *  The Productcompomposer help the user to slecet a product. 
 *  Edges are followed to mark the target edges.
 *  After selection a new product can be returned.  
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


    private EdgeContainer container;

    public ProductComposer(Productline pl) {
        productline = pl;
        nodes.put(pl, new NodeAttr());
        container = pl.getEdgeContainer();
    }

    
    /*
     * Create an intern  
     */
    private NodeAttr get(INode node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, new NodeAttr());
        }
        return (NodeAttr) nodes.get(node);
    }

    
    /**
     * Set <code>node</code> to Used and update graph
     * @param node
     */
    public void setUsed(AbstractAsset node) {
        if (node instanceof MetaNode) { return; // User cannot change type of
                                                // Metanodes
        }
        use(node, true);
        listeners
                .firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_USED);
        logger.debug(node + " has new state USED");
    }

    /*
     * Set Use to MetaNode update dependend nodes
     * precondition: node is no MetaNode;
     * 
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
        } else if ( (node instanceof Variant || node instanceof Release)) {
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

        // follow Edges and test, if target nodes, need/can change.
        for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (edge.getType().equals(Edge.INCLUDE)
                    && (!get(edge.getTargetNode()).isUsed())) {
                useMeta( edge.getTargetNode(), newSet(node));
                if (!get(edge.getTargetNode()).isUsed()) {
                    get(node)
                            .setWarning(
                                    "Target node(s) of include edge are not used in product!");
                }
            } else if (edge.getType().equals(Edge.EXCLUDE)
                    && (!get(edge.getTargetNode()).isMustNotUse())) {
                mustNotUseMeta(edge.getTargetNode(), newSet(node));
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
                updateStateMetaInclude(edge.getStartNode(),
                        node, newSet(node));
            } else {
                updateStateMetaExclude(edge.getStartNode(), node, newSet(node));
            }
        }

    }
  

    /*
     * Set Use to MetaNode update dependend nodes
     */
    private void useMeta(INode node, Set visited) {
        visited.add(node);
        if (!get(node).isOpen()) { return; }
        if (!(node instanceof MetaNode)) {
            // // found Variant, Component or release;
            // use right method.
            //           
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
                    useMeta((MetaNode) child, visited);
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
                if (! visited.contains(target)){
                    if (targetAttr.isUsed()) {
                        useNodes++;
                    } else if (targetAttr.isOpen()) {
                        openList.add(target);
                    }
                }
            }
            if (useNodes == 1 && openList.size() > 0) {
                for (Iterator ite = openList.iterator(); ite.hasNext();) {
                    mustNotUseMeta((INode) ite.next(), visited);
                }
                nodeAttr.setUse();
            } else if (useNodes == 0 && openList.size() == 1) {
                
                useMeta((INode) openList.get(0), visited);
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
    private void mustNotUseMeta(INode node, Set visited) {
        visited.add(node);
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
            if(! visited.contains(edge.getTargetNode())){
                if (get(edge.getTargetNode()).isOpen()) {                
                    if (node instanceof MetaNode) {
                        mustNotUseMeta(edge.getTargetNode(), visited);
                    } else {
                        mustNotUse((AbstractAsset) edge.getTargetNode(), false);
                    }
                } else if (get(edge.getStartNode()).isUsed()) {
                    allChildrenAreMustNotUse = false;
                }
            }
        }

        if (allChildrenAreMustNotUse) {
            get(node).setMustNotUse();
        } else {
            get(node).setOpen();
        }
    }

 /*
  * set node to MustNotUse and propergate this decissoins to other nodes
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
        if (wasOldStateUse){
            for (Iterator ite = container.getEdgesFrom(node).iterator(); 
            	 ite.hasNext();) {
                Edge edge = (Edge) ite.next();
                clear(edge, newSet(node));
            }
        }

        //update incoming edges
        for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                .hasNext();) {
            Edge edge = (Edge) ite.next();
            if (edge.getType().equals(Edge.INCLUDE)) {
                updateStateMetaInclude(edge.getStartNode(), node,
                        newSet(node));
            } else {
                updateStateMetaExclude(edge.getStartNode(), node, newSet(node));
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

    /*
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

    /*
     * Test, if this node is need in the product.
     * 
     * @param node
     * @return true  if so
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
        }
        
        // clear outgoing edges
        for (Iterator ite = useNodes.iterator(); ite.hasNext();) {
            INode node = (INode) ite.next();
            for (Iterator upd = container.getEdgesFrom(node)
                    .iterator(); upd.hasNext();) {
                Edge edge = (Edge) upd.next();
                clear(edge, newSet(node));               
            }
        }

        // Try to set the nodes to open, that must be set to USE/MustNotUse, if the nodes
        // in useNodes would be still used.
        for (Iterator ite = useNodes.iterator(); ite.hasNext();) {
            // Update outgoing edges.
            INode node = (INode) ite.next();
            //Call notNeedUse on parent node and clear on outgoing edges.
            // If node is MustNotUsed the edges are alredy updatesd.
            if (get(node).isOpen()) { // clear() call also notNeeded... . State
                                      // is maybe alredy changed.                
                // If parent is used, try to set to open.
                if (get(((AbstractAsset) node).getParent()).isUsed()) {
                    notNeededUse(((AbstractAsset) node).getParent());
                }
                if (get(((AbstractAsset) node).getParent()).isUsed()
                    && (node instanceof Release || node instanceof Variant) ) {
                    // Try to set brothers to open.
                    List children = getChildren(((AbstractAsset) node).getParent());
                    for (Iterator chIte = children.iterator(); chIte.hasNext();){
                        AbstractAsset brother = (AbstractAsset ) chIte.next();
                        if(get(brother).isMustNotUse()){
                            notNeededMustNot(brother);
                        }
                    }
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
        
        // Update exclude edges. Nodes of include edges are in
        // useNodes-list.
        for (Iterator ite = useNodes.iterator(); ite.hasNext();) {
            INode node = (INode) ite.next();
            for (Iterator upd = container.getEdgesTo(node, Edge.EXCLUDE).iterator(); upd
                .hasNext();) {              
               this.updateStateMetaExclude(((Edge) upd.next()).getTargetNode(), node, newSet(node));
           }
        }
        
        // 
        
        return true;
    }

    /*
     * Test if node need state MustNotUse and set all found nodes that not need the state MustNot it back to OPEN.
     * @param node
     */
    private boolean notNeededMustNot(AbstractAsset node) {        
        LinkedList list = new LinkedList(); // start and all parent, grandparent, ...    
        
        // node is MustNotUse => parent is MustNotUse
        while ( (! (node instanceof Productline))  && get(node).isMustNotUse()){
            list.add(node);
            if ( get(node).isUserChanged() 
                    || ((node instanceof Variant || node instanceof Release) && brotherNeedUse(node))) {
                return false;
            }
            
            // test, if a source nodes of incomimg ecxlude edges has USE.
            Set visited = newSet(node);
            boolean usedNodeFound = false;
            for (Iterator ite = container.getEdgesTo(node, Edge.EXCLUDE).iterator(); 
                ite.hasNext() && ! usedNodeFound;){
                INode asset =((Edge) ite.next()).getStartNode();
                usedNodeFound = findUsedNodes(asset, visited);
            }
            if (usedNodeFound){
                return false;
            }
                
            // now look at parent 
            node = ((AbstractAsset) node).getParent();
        }
        
        // All nodes in list can set to open. 
        
        // Set nodes to OPEN and update incoming edges.
        for (Iterator ite =list.iterator(); ite.hasNext();) {
            node = (AbstractAsset) ite.next();
            get(node).setOpen();
            // Update incoming edges.
            for (Iterator upd = container.getEdgesTo(node).iterator(); upd
                    .hasNext();) {
                Edge edge = (Edge) upd.next();
                if (edge.getType().equals(Edge.INCLUDE)) {
                    this.updateStateMetaInclude(edge.getStartNode(), edge
                            .getTargetNode(), newSet(edge.getTargetNode()));
                } else if (edge.getType().equals(Edge.EXCLUDE)) {
                    this.updateStateMetaExclude(edge.getStartNode(), node, newSet(node));
                }
            }
        }
        
        //  call NotNeededMustNot also on children. (Maybe they are also need not the
        // MustNotUse state.)
        for (Iterator ite = list.iterator(); ite.hasNext();) {
            node = (AbstractAsset) ite.next();
            if (!get(node).isMustNotUse()) { // state has maybe already changed
                for (Iterator chIte = getChildren(node).iterator(); chIte
                        .hasNext();) {
                    notNeededMustNot((AbstractAsset) chIte.next());
                }
            }
        }
        
        return true;
    }
    
    /*
     * try to find an used non MetaNode. Follow incoming edges.  
     * @param node  
     * @param visited List with alredy visited nodes.
     * @return Retuns true, if a used node is found.
     */
    private boolean findUsedNodes(INode node, Set visited){
        visited.add(node);
        for (Iterator ite = container.getEdgesTo(node).iterator(); ite.hasNext();){
            INode source = ((Edge) ite.next()).getStartNode();
            if (! visited.contains(source)){
                if (source instanceof MetaNode){
                    findUsedNodes(source, visited);
                }else {
                    visited.add(source);
                    if (get(source).isUsed()){
                        return true;
                    }
                }
            } 
        }
        return false;
    }
    
    
    // test for notNeededMustNot(..) if a brother of node need the use state
    private boolean brotherNeedUse(AbstractAsset node) {
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
     * @param node a target node of an edge from the used node.
     * @visitet cotains all nodes clear is already called.(Is needed to avoid loops.)
     *  
     */  
    private String clear(Edge edge, Set visited) {        
        INode node = edge.getTargetNode();
        visited.add(node);
        NodeAttr nodeAttr = get(node);        
        if (!(node instanceof MetaNode)) {
            if (nodeAttr.isUsed()) {
                notNeededUse((AbstractAsset) node);
            } else if (nodeAttr.isMustNotUse()) {
                notNeededMustNot((AbstractAsset) node);
            }
        } else {
            // test: if any nodes, their edges point to node, have the state use
            // if such a node is used, node cannot clear.
            for(Iterator ite = container.getEdgesTo(node).iterator(); ite.hasNext();){
                INode n = ((Edge) ite.next()).getStartNode();
                if ( (!visited.contains(n)) &&  get(n).isUsed()){
                    
                    return STATE_USED;
                }
            }
            get(node).setUserChanged(false);
            nodeAttr.setOpen(); 
            int sizeOpen =0; int sizeUsed =0; int sizeMustNot =0;          
            for (Iterator ite = container.getEdgesFrom(node).iterator(); ite
                    .hasNext();) {
                
               Edge target = ((Edge) ite.next());
               if (! visited.contains(target.getTargetNode())){
                   String state = clear(target, visited);
                   if (state == STATE_OPEN){
                       sizeOpen++;
                   }else if (state == STATE_USED) {
                       sizeUsed++;
                   } else{
                       sizeMustNot++;
                   }
               }
            }
            setStateMeta(node, edge.getType(), sizeOpen, sizeUsed, sizeMustNot);           
        }
        
        return nodeAttr.getState();
    }
    
    

    /*
     * Update given node and nodes of incoming edges. Sets State to MetaNode of
     * include edges. Looks on nodes, to this node has edges to, gain
     * information and set state
     */
    private void updateStateMetaInclude(INode node, INode targetNode, Set visited) {
        visited.add(node);        
        if (!(node instanceof MetaNode)) {
            if (get(node).isUsed() && get(targetNode).isOpen()){
                this.useMeta(targetNode, newSet(node));
            }
            
            return; 
        }
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
        setStateMeta(node,Edge.INCLUDE, sizeOpen, sizeUse, sizeMustNot);
        
            // state changed => node, that edges point to this node
            // must be updated.
            for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge) ite.next();
                if (! visited.contains(edge.getStartNode())){
                    updateStateMetaInclude(edge.getStartNode(), edge.getTargetNode(), visited);
                }
            }
            
    }
    
    // returns a new Set 
    private Set newSet(INode node){
        Set s = new HashSet();
        s.add(node);
        return s;
    }

    
    /*
     * Set state to a Metanode. 
     * @param node State of this node is set.
     * @param sizeOpen  number of target nodes, that has the state OPEN
     * @param sizeUse  number of target nodes, that has the state USED
     * @param sizeMustNot  number of target nodes, that has the state MUST_NOT_USED
     */
    private void setStateMeta(INode node, String edgeType, int sizeOpen, int sizeUse, int sizeMustNot) {
        if (edgeType.equals(Edge.INCLUDE)){
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
        } else {
            // exclude edges
            if (sizeOpen ==0 && sizeUse == 0){
                get(node).setMustNotUse();
            } else if (sizeOpen > 0){
                get(node).setOpen();
            } else {
                get(node).setUse();
            }
        }
        
        
    }

    /*
     * Update given node and recursive nodes of incoming edges. 
     * Only MetaNodes are updated. 
     * Method is called for source nodes of incoming Edges, when the state of the target node changed form use to an other state.
     * Method is called form targetNode.  
     */   
    // visited contains all nodes that are visisted. This avoids, that the algorithm
    // run loops.
    private void updateStateMetaExclude(INode node, INode targetNode, Set visited) {
        visited.add(node);
        if (!(node instanceof MetaNode)) {
            // none MetaNode node is USE => targetNode need MUST_NOT_USE 
            if (get(node).isUsed() && get(targetNode).isOpen()){
                mustNotUseMeta(targetNode, newSet(node));
            }
            
            return; 
        }
        String state = get(node).getState();
        boolean noOpenNode = true;
        Iterator it = container.getEdgesFrom(node).iterator();
        // set node to open, if this node point to one open node
        while (it.hasNext() && noOpenNode) {
            Edge edgeChi = (Edge) it.next();
            if ( ! visited.contains(edgeChi.getTargetNode())
                  &&  get(edgeChi.getTargetNode()).isOpen()) {
                noOpenNode = false;
            }
        }
        if (noOpenNode) {
            // All outgoing edges point at MUST_NOT_USED nodes.
            // => this node must has the same state.
            get(node).setMustNotUse();
        } else {
            get(node).setOpen();
        }
        if (state != get(node).getState()) {
            // State changed => also nodes, that point to this nodes must be updated.
            for (Iterator ite = container.getEdgesTo(node).iterator(); ite
                    .hasNext();) {
                Edge edge = (Edge)ite.next();
                if (! visited.contains(edge.getStartNode())){
                    updateStateMetaExclude(edge.getStartNode(), node, visited);
                }
            }
        }
        
    }

    
    /**
     * Tell the PrductComposer, that the User want, that <code>node</code> is needed in the new Product
     * @param node
     */
    public void setMustNotUse(AbstractAsset node) { 
        if (node instanceof MetaNode) { 
            return; // User cannot change type of
        }           // Metanodes
        mustNotUse(node, true);
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null,
                STATE_MUST_NOT);
        logger.debug(node + " has new state MUST_NOT_USE");
    }

    /**
     * Tell the PrductComposer, that the User want, that <code>node</code> is should has the state OPEN
     * Removes further decisions of the user on <code>node</code>.
     * State is changed only, if its possible.
     * This action has very low priority.
     * 
     * @param node
     */
    public void setOpen(AbstractAsset node) {
        if (node instanceof MetaNode) { 
            return; // User cannot change type of
        }           // Metanodes
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

    public boolean hasWarning(AbstractAsset node) {
        return get(node).hasWarning();
    }

    public String getWarning(AbstractAsset node) {
        return get(node).getWarning();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }
    
    /**
     * Creates, configures and adds a new project to the 
     * product line.
     */
    public Product createProduct() {      
    	
    	Product product = new Product(productline);
    	//FIXME !!!!
    	product.setRepositoryDescriptor(new RepositoryDescriptor("", "", "", "", ""));
    	HashMap assets = new HashMap();
    	
        // create ProductComponments for used CoreAssets.
        Set resourceNames = new HashSet();
    	for (Iterator ite = productline.getComponents().iterator(); ite.hasNext();){
    	    Component node = (Component) ite.next();
    	    if (get(node).isUsed()){
    	        //product.addProductComponent(generateProductComponent(node, assets));
    	        this.generateProductComponents(product, node, resourceNames);
    	    }    	   
    	}    	
    	
    	
        // add edges to product
    	// assets conatins all used productline assets as key an the productcomponent as value
  /*  	EdgeContainer productCont = product.getEdgeContainer();  
    	Set keySet = assets.keySet();
    	for (Iterator ite = keySet.iterator(); ite.hasNext();){
    	    Set  visited = new HashSet(); // remember visited metanode to run not in loops
    	    INode source = (INode) ite.next();      	    
    	    LinkedList list = new LinkedList();  
    	    list.addAll(container.getEdgesFrom(source));
    	    while (! list.isEmpty()){
    	        Edge edge = (Edge) list.removeFirst();
    	        INode node = edge.getTargetNode();    	            
    	        if (node instanceof MetaNode && !visited.contains(node)){
    	            visited.add(node);
    	            list.addAll(container.getEdgesFrom(node));    	                
    	        } else if(keySet.contains(node) 
    	                && assets.get(source) != assets.get(node) ) {
    	            // node and source are nodes and are in the product.
    	            // And ground has and edge to "node"(maybe there are Metanodes between)
    	            // =>There must also an edge in product.
    	            productCont.addEdge((INode)assets.get(source),(INode) assets.get(node), edge.getType());
    	        }
    	    }    	    
    	}
    */	
    	product.setProject(productline.getKoboldProject());
    	productline.addProduct(product);
    	return product;
    }
    

    private void generateProductComponents(Product product, Component component, Set resourceNames ){
        ProductComponent productComponent;
        for (Iterator ite = component.getVariants().iterator(); ite.hasNext();){
            Variant variant = (Variant) ite.next();
            if (this.get(variant).isUsed()) {
                Release release = null;                 
                Iterator  relIte = variant.getReleases().iterator();
        	    // look for a used Release
        	    while ( relIte.hasNext() &&
        	            ! get( release = (Release) relIte.next() ).isUsed()  
        	           ){}
        	    if (release != null && get(release).isUsed()){
        	        // => a related Component can be construct
        	        productComponent = new RelatedComponent(variant, release);
        	        productComponent.setResource(
        	                this.getResourceName(
        	                        "prodComp_"+component.getResource()+"_"+variant.getResource(),
        	                        resourceNames));
        	        product.addProductComponent(productComponent);
        	    }
            } 
             
        }
    }
    
    private String getResourceName(String name, Set allNames) {
        if(allNames.contains(name)){
           int i = 1;
           while(allNames.contains(name + "_" + i)){
               i++;
           }
           name= name+"_" + i;
        }
        allNames.add(name);
        return name;
    }
    
    /*
     * Create a Productcomponent for a Component.
     * if comp has a used Variant and a used Relase a Related Component is created
     * else a SpecificComponent is created.
     * Create also ProductComponents for all used Componets of the used variant.
     *  
     * @param comp  Component. Create ProductComponent for this Component.
     * @param assets add used productline assets as key an the productcomponent as value
     * @return ProductComponet for comp and its used subnodes
     */
    private ProductComponent generateProductComponent(Component comp, HashMap assets){
        ProductComponent productComponent;
        Variant variant = null;
        Iterator varIte = comp.getVariants().iterator();
        // look for a used variant 
	    while ( varIte.hasNext() &&
	            ! get(variant = (Variant) varIte.next()).isUsed()){}
    	if (variant != null && get(variant).isUsed()){
    	    // found the used Variant    	    
    	    // find Release
    	    Release release = null;
    	    Iterator  relIte = variant.getReleases().iterator();
    	    // look for a used Release
    	    while ( relIte.hasNext() &&
    	            ! get( release = (Release) relIte.next() ).isUsed()  
    	           ){}
    	    if (release != null && get(release).isUsed()){
    	        // => a related Component can be construct
    	        productComponent = new RelatedComponent(variant, release);
    	        assets.put(comp, productComponent);
    	        assets.put(variant, productComponent);
    	        assets.put(release, productComponent);  
    	        productComponent.setResource("prodComp_"+comp.getResource()+"_"+variant.getResource());
    	    } else{
    	        productComponent = new SpecificComponent(comp.getName());
    	        productComponent.setDescription(variant.getDescription());
    	        assets.put(comp, productComponent);
    	        assets.put(variant, productComponent);
    	    }
    	    productComponent.setResource("prodComp_"+comp.getResource()+"_"+variant.getResource());
    	    // add sub components
    	    
    	    for (Iterator subIte = variant.getComponents().iterator(); subIte.hasNext();){
    	        Component subComp = (Component) subIte.next(); 
    	        if (get(subComp).isUsed()){
    	            productComponent.addProductComponent(generateProductComponent(subComp, assets));    	           
    	        }
    	    }
    	} else {
            // no variant of this component is used
    	    productComponent = new SpecificComponent(comp.getName()); 
    	    productComponent.setResource("prodComp_"+comp.getResource());
    	    assets.put(comp, productComponent);
    	    // no sub variant is used => there must not exitst subnodes of variant, that are usesd.
    	}
    	
    	 productComponent.setResource(comp.getResource());
    	 return productComponent;   
	}
    

    /*
     * 
     * @author pliesmn
     * 
     * Contains all additional information, that the alorithm need of an INode.
     */
    class NodeAttr {

        private String status = STATE_OPEN;

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

        String getWarning() {
            return this.warning;
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