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
 * $Id: ComponentContainerEditPolicy.java,v 1.3 2004/09/21 20:54:30 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import kobold.client.plam.editor.command.CreateVariantCommand;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;


/**
 * @author Tammo
 */
public class ComponentContainerEditPolicy extends ContainerEditPolicy
{

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request)
    {
        Command resultCmd = UnexecutableCommand.INSTANCE;
        Object parent = getHost().getModel();
        
        IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart();
        ViewModel vmc = vmp.getViewModel(); 
        
        if (request.getNewObjectType().equals(AbstractAsset.VARIANT)
                && parent instanceof IVariantContainer) {
               
               CreateVariantCommand cmd = new CreateVariantCommand();
               cmd.setParent((IVariantContainer) parent);
               cmd.setChild((Variant) request.getNewObject());
               
               resultCmd = cmd;
        }
        
        return resultCmd;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command getAddCommand(GroupRequest request)
    {
        System.out.println("add");
        return null;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCloneCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    protected Command getCloneCommand(ChangeBoundsRequest request)
    {
        System.out.println("clone");
        return null;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command getOrphanChildrenCommand(GroupRequest request)
    {
        System.out.println("orphan");
        return null;
    }
}
