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
 * $Id: FileDescriptorEditPart.java,v 1.4 2004/09/20 06:42:05 martinplies Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.editor.figure.AbstractNodeFigure;
import kobold.client.plam.editor.figure.FileDescriptorFigure;
import kobold.client.plam.editor.figure.ReleaseFigure;
import kobold.client.plam.editor.figure.SpecificComponentFigure;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.editor.policy.FlowLayoutEditPolicyImpl;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicyImpl;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.product.SpecificComponent;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;


/**
 * EditPart for Release instances.
 * 
 * @author Tammo
 */
public class FileDescriptorEditPart extends AbstractAssetEditPart
{
    private FileDescriptorFigure figure;
    
    /**
     * @see kobold.client.plam.editor.editpart.AbstractAssetEditPart#createNodeFigure()
     */
    protected IFigure createNodeFigure()
    {
        figure = new FileDescriptorFigure();
		figure.setTitle(getAsset().getName());
    
        return figure;
    }
    
    protected void refreshVisuals()
    {
        super.refreshVisuals();
        figure.setTitle(((FileDescriptor)getAsset()).getFilename());
    }
    
    /**
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getContentPane() {
        return figure.getContentPane();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren()
    {
        List children = new ArrayList(); 
        children.addAll(((FileDescriptor)getModel()).getFileDescriptors());
        return children;
    }
}