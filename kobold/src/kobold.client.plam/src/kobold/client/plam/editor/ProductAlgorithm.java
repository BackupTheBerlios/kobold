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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.IProductlineNode;
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
public class ProductAlgorithm {
    private static final Log logger = LogFactory.getLog(ProductAlgorithm.class);
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    public static final String STATE_OPEN = "sopen";
    public static final String STATE_USED = "sused";
    public static final String STATE_MUST_NOT = "sdont";
    
    private Map stateByAsset = new HashMap();
    
    HashMap nodes = new HashMap();

    Productline productline; // the productline, that is use to create a new

    // product.

    EdgeContainer container;

    public ProductAlgorithm(Productline pl) {
        productline = pl;
        nodes.put(pl, new NodeAttr());
        container = pl.getEdgeConatainer();
    }


    private NodeAttr get(INode node) {
        if(! nodes.containsKey(node)){
            nodes.put(node, new NodeAttr());
        }
        return (NodeAttr) nodes.get(node);
    }

    public void setUsed(IProductlineNode node) {
        // TODO
        get(node).setUse();        
        get(node).setUserChanged();
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_USED);
        logger.debug(node + " has new state USED");
    }

    /**
     * precondition: node is no MetaNode;
     * 
     * @param node
     */
    private void use(IProductlineNode node) {
        NodeAttr attr = get(node);
        if(attr.isUse()){
            return;
        }
        boolean isOldStatOpen = attr.isOpen();
        attr.setChanged(true);
        List edgesFollowToStart = new LinkedList();

        // Change Status use. This is only here allowed for non metanodes.
        attr.setUse();
        attr.setChanged(true);

        // set the status of the parents also to USE
        // overwrite any other status. If the child is set to USE, then
        // the parent must also has the status USE.                
        IProductlineNode parent =  (IProductlineNode) ((AbstractAsset)node).getParent();
        if (!(parent instanceof Productline) && ! get(parent).isUse()) {
            use(parent);
        } else if (attr.isUserChanged
                && node.getType().equals(AbstractAsset.COMPONENT)) {
            // the brothers of this node has set to MUST_NOT_USE. The parent has
            // already set to
            //  USE at further time, so the parent donnot do set the brothers to
            // MUST_NOT_USE.
            this.setUSEtoChildren((Variant) parent);
        }
        // if(node.) {}

        // look if also the childs are need and have to/can set to USE
        // childs can not set To USE, if the say, that he donnot want them
        // or if only one of the child must set to USE and the User must
        // decide which child.
        if (node.getType() == AbstractAsset.VARIANT) {
            // all children are need
            for (Iterator ite = node.getChildren().iterator(); ite.hasNext();) {
                IProductlineNode child = (IProductlineNode) ite.next();
                NodeAttr childAttr = get(child);
                if (!childAttr.isUse()) {
                    if (childAttr.isMustNotUse()) {
                        if (childAttr.isUserChanged()) {
                            // warn
                        } else {
                            use(child);
                        }
                    }
                }
            }
        }

        // set children to USE
        setUSEtoChildren(node);

        // Now Follow  the edges.

        // follow Edges and test, if target nodes, need/can change.
        
          for (Iterator ite = container.getEdgesFrom(node).iterator(); ite.hasNext(); ){
              Edge edge = (Edge) ite.next();
              if (edge.getType().equals(Edge.INCLUDE) && (! get(edge.getTargetNode()).isUse()) ){
                  //    setMetaNode(edge.getTargetNode(), node, );   
              }
              
          }

         

        //[ follow incomeing inclued edges to test, if
        // the start nodes have(need) the status to work on]

        // [follow edgesFollowToStart to test del warings]

    }

    /**
     * sets rekursive metanodes to USE, if it possible.
     * Only OPEN methanodes can changed. Component-, Variant- ,and Releasenodes
     * cannot chaneged the status MUST_NOT_USE to USE or the other way,
     * because reason, they have this status, still exists.
     * If such a reason not more exists. It must tested, if the status of theese nodes
     * can be set back to open.
     * @param targetNode
     * @param node
     */
    /*
     * 
     */
    private void useMetaNode(INode metaNode) {
        if (! (metaNode instanceof MetaNode)){
            // found a Variant, Component or release.
            // now call the rigtht function
            use((IProductlineNode) metaNode);
            return;
        }
        MetaNode meta = (MetaNode) metaNode;
        NodeAttr metaAttr = get(meta);        
        if(metaAttr.isUse()){
            return;
        }
        if(metaAttr.isOpen()){
            // change this if possible
            if (meta.getType().equals(MetaNode.AND)){
                metaAttr.setToWorkOn(false);
                metaAttr.delWarning();
                for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite.hasNext(); ){
                    // set all open nodes to setUse.
                    // if exist mustNotUse parent get a warning
                    // if on a child haveToWorkOn parent set also to haveToWorkOn
                    Edge edge = (Edge) ite.next(); 
                    NodeAttr targetAttr = get(edge.getTargetNode());
                    if (edge.getType().equals(Edge.INCLUDE)){ 
                        if ( targetAttr.isOpen() ){                                                   
                              useMetaNode(edge.getTargetNode());
                        } else if(targetAttr.isMustNotUse() ){
                            metaAttr.setWarning("");
                            // warn
                        }
                    } else { // edgetype = exclude
                        if ( targetAttr.isOpen() ){ 
                                  mustNotUseMetaNode(edge.getTargetNode());
                        } else if ( targetAttr.isOpen() ){
                            metaAttr.setWarning("");
                            // warn
                        }
                    } if(targetAttr.hasWarning()){
                              metaAttr.setWarning("");
                              // warn
                         }else if(targetAttr.isToWorkOn){
                              metaAttr.setToWorkOn(true);
                         }
                    } 
                } else if (meta.getType().equals(MetaNode.OR)){
                    int open =0; // number of open nodes
                    Edge openEdge = null; //edge to last open node
                    int truen = 0; // number of nodes that represent true (include edge=>USE; exclude edge=> MUST_NOTUSE)
                    for (Iterator ite = container.getEdgesFrom(meta).iterator(); ite.hasNext(); ){
                        Edge edge = (Edge) ite.next(); 
                        NodeAttr targetAttr = get(edge.getTargetNode());
                        if (  (edge.getType().equals(Edge.INCLUDE) && targetAttr.isUse())
                            ||(edge.getType().equals(Edge.EXCLUDE) && targetAttr.isMustNotUse()) )
                        {        
                            truen++; //warn?
                        }else if ( targetAttr.isOpen()){ 
                            open++; openEdge = edge;
                        }                        
                    }                    
                    NodeAttr targetAttr = get(openEdge.getTargetNode());
                    if(truen == 1){
                        get(meta).setUse();
                    }else if (truen > 1){
                        get(meta).setMustNotUse();
                    }else if (open == 1){
                        useMetaNode(meta);
                        if (  (openEdge.getType().equals(Edge.INCLUDE) && targetAttr.isUse())
                                ||(openEdge.getType().equals(Edge.EXCLUDE) && targetAttr.isMustNotUse()) ){
                            get(meta).setUse();
                        } else if(targetAttr.isOpen()){
                            get(meta).setOpen();
                        } else {
                            get(meta).setMustNotUse();
                        }
                    }
                }
                    
            }
        }
        
        
        
        
    
    /*
     * try to find one way that can be used 
     */
    private void useMeta(INode node) {        
        if (! get(node).isOpen()){
                return;            
        }
        if(! (node instanceof MetaNode)){
            // // found Variant, Component or release;
            // use right method.
            use((IProductlineNode)node);
            return;
        }
        MetaNode meta = (MetaNode) node;        
        
        NodeAttr nodeAttr = get(meta);
        if (meta.getType().equals(MetaNode.AND)){
            // handel AND nodes
        boolean allChildrenUsed = true;        
        for(Iterator ite = container.getEdgesFrom(meta).iterator(); ite.hasNext();){
            Edge edge = (Edge) ite.next();
            INode child = edge.getTargetNode();
            NodeAttr childAttr = get(meta);
            if (edge.getType()== Edge.INCLUDE ){
               if(child instanceof MetaNode){
                  useMeta((MetaNode)child);
               }else { 
                  if(childAttr.isOpen()){
                      use((IProductlineNode) child);
                  }
               }               
                if (! childAttr.isUse()){
                    nodeAttr.setToWorkOn(true);
                    allChildrenUsed = false;                    
                }                              
            }else {
                nodeAttr.addWarning("You must not mix edgetypes at one metaode"); 
            }           
        }
        if(allChildrenUsed){
            nodeAttr.setUse();            
        }else {
            nodeAttr.setMustNotUse();            
        }
        } else {
//          at first gain information
            List useList = new LinkedList();
            List mustNotList = new LinkedList();
            List openList = new LinkedList();                    
            // handel OR nodes
            // at first gain Information
            for(Iterator ite = container.getEdgesFrom(meta).iterator(); ite.hasNext();){
                Edge edge = (Edge) ite.next();            
                if (edge.getType()== Edge.INCLUDE ){                
                    INode child = edge.getTargetNode();
                    NodeAttr childAttr = get(meta);                 
                    if (childAttr.isUse()){                        
                        useList.add(child);                    
                    }else if(childAttr.isOpen()){
                        openList.add(child);
                    }                    
                }else {
                    nodeAttr.addWarning("You must not mix edgetypes at one metaode"); 
                }                
            }
            if(useList.size() == 1 && openList.size() >0){                
                  for (Iterator ite = openList.iterator(); ite.hasNext();){
                      mustNotUseMeta((INode) ite.next());
                  }
                  nodeAttr.setUse();
            }else if (useList.size() == 0  && openList.size() == 1){
                useMeta((INode) openList);
                nodeAttr.setUse();
            } else {
                nodeAttr.setOpen();                 
            }             
        }
    }
    
    
    private void mustNotUseMeta(INode node){
        
    }

    /**
     * @param targetNode
     */
    private void mustNotUseMetaNode(INode node) {
        // TODO Auto-generated method stub
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_MUST_NOT);
        logger.debug(node + " has new state MUST_NOT");
    }

    /**
     * 
     * 
     * @param parent
     */
    private void setUSEtoChildren(IProductlineNode parent) {
        if (parent instanceof Variant) {
            // all children have to set to USE.
            for (Iterator ite = parent.getChildren().iterator(); ite.hasNext();) {
                IProductlineNode child = (IProductlineNode) ite.next();
                if ((!get(child).isUse()) && (!get(child).isUserChanged())) {
                    use(child);
                } else if ((!get(child).isUse())
                        && (get(child).isUserChanged())) {
                    //warn
                }
            }
        } else if (parent instanceof Component) {

            // at first gain information
            List useList = new LinkedList();
            List mustNotList = new LinkedList();
            List openList = new LinkedList();
            for (Iterator ite = ((Component)parent).getVariants().iterator(); ite.hasNext();) {
                IProductlineNode node = (IProductlineNode) ite.next();
                if (get(node).isUse()) {
                    useList.add(node);
                } else if (get(node).isMustNotUse()) {
                    mustNotList.add(node);
                } else {
                    openList.add(node);
                }
            }

            if (useList.size() > 1) {
                // try to set some back.
                for (Iterator ite = useList.listIterator(); ite.hasNext();) {
                    IProductlineNode node = (IProductlineNode) ite.next();
                    if (!get(node).isUserChanged()) {
                        useList.remove(node);
                        mustNotList.add(node);
                        setMustNotUse(node);
                    }
                }
            }

            if (useList.size() > 0 && openList.size() > 0) {
                // set last open nodes to MUST_NOT_USE.
                for (Iterator ite = openList.listIterator(); ite.hasNext();) {
                    IProductlineNode node = (IProductlineNode) ite.next();
                    setMustNotUse(node);
                    mustNotList.add(node);
                    openList.remove(node);
                }
            } 
            
            if(openList.size() > 0){
                get(parent).setToWorkOn(true);
            } else {
                get(parent).setToWorkOn(false);
            }
            
            if (useList.size() > 0){
                // warn
            } else{
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
    /*
     * private List notNeeded(IProductlineNode n, List nodes) { // if this node
     * or its children is set to USE from the // user the node is need. List
     * list = new LinkedList(); list.add(n); Iterator ite = list.listIterator();
     * while (ite.hasNext()) { IProductlineNode node = (IProductlineNode)
     * ite.next(); if (get(node).isUse()) { if (get(node).isfromUserChanged()) {
     * return true; } else { list.add(node.getChildren());
     * list.add(container.getStartNodesFromEdgesTo(node, Edge.INCLUDE)); } } }
     *  }
     */
    /**
     * @param parent
     * @return
     */
    private boolean isAllowedToSetUSE(IProductlineNode node) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setMustNotUse(IProductlineNode node) {
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
        logger.debug(node + " has new state MUST_NOT_USE");
    }

     public void setOpen(AbstractAsset node) {
        get(node).setOpen();
        // TODO
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
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

    public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
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
        
        String getState(){
            return status;
        }
        
        String getWarnig(){
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

        void setUserChanged() {
            isUserChanged = true;
        }

        boolean isUserChanged() {
            return isUserChanged;
        }

    }

}