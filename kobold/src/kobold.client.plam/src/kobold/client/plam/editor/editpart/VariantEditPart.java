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
 * $Id: VariantEditPart.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import kobold.client.plam.editor.figure.VariantFigure;
import kobold.client.plam.editor.policy.ContainerEditPolicy;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicy;
import kobold.client.plam.model.pline.graph.AbstractNode;
import kobold.client.plam.model.pline.graph.VariantNode;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;

/**
 * VariantEditPart
 * 
 * @author Tammo van Lessen
 * @version $Id: VariantEditPart.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 */
public class VariantEditPart extends AbstractGraphicalEditPart
        implements PropertyChangeListener, NodeEditPart {

    private VariantFigure figure;

    private DirectEditManager manager;
        
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    protected IFigure createFigure() {
        figure = new VariantFigure(((AbstractNode)getModel()).getName());
    
        figure.setSize(150, 150);    
    
        return figure;
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
            new GraphicalNodeEditPolicy());
        //installEditPolicy(EditPolicy.COMPONENT_ROLE,new ElementEditPolicy());
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy());
        //installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy());
    }
    
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("child")) {
           System.out.println("refresh children");
           refreshChildren();
        } else if (prop.equals("name")) {
           System.out.println("refresh name");
           refreshVisuals();
     }

    }
    
    /**
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        // TODO
        return null;
    }
    
    /**
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        // TODO
        return null;
    }
    
    /**
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        // TODO
        return null;
    }
    
    /**
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        // TODO
        return null;
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren() You
     *      must implement this method if you want you root model to have
     *      children!
     */
    protected List getModelChildren()
    {
        List children = ((VariantNode) getModel()).getComponents();
        return children;
    }
    
    /**
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getContentPane() {
        return figure.getContentPane();
    }
    
    /**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate()
    {
        if (isActive() == false) {
            super.activate();
            ((AbstractNode) getModel()).addPropertyChangeListener(this);
        }
    }
    
    /**
     * @see org.eclipse.gef.EditPart#deactivate()
     */
    public void deactivate()
    {
        if (isActive()) {
            super.deactivate();
            ((AbstractNode) getModel()).removePropertyChangeListener(this);
        }
    }
    
    /*private void performDirectEdit(){
        if(manager == null)
            manager = new LabelDirectEditManager(this, 
                TextCellEditor.class, new CellEditorLocator(((VariantFigure)getFigure()).getLabel()));
        manager.show();
    }

    public void performRequest(Request request){
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
            performDirectEdit();
    }

    public class ElementEditPolicy
        extends org.eclipse.gef.editpolicies.ComponentEditPolicy
    {

    protected Command createDeleteCommand(GroupRequest request) {
        Object parent = getHost().getParent().getModel();
        DeleteCommand deleteCmd = new DeleteCommand();
        deleteCmd.setParent((AbstractModel)parent);
        deleteCmd.setChild((AbstractModel)getHost().getModel());
        System.out.println("delete");
        return deleteCmd;
    }

    }*/
    
    /*public class DeleteCommand extends Command {
        private AbstractNode parent;
        private AbstractNode child;

        public void setChild (AbstractModel c) {
            child = c;
        }

        public void setParent(AbstractModel p) {
            parent = p;
        }

        
        public boolean canUndo() {
            return false;
        }

        public void execute() {
            parent.removeChild(child);
        }

        public void redo() {
            parent.removeChild(child);
        }

    }*/
}
