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
 * $Id: ProductlineContainerEditPolicy.java,v 1.6 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.editor.policy;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.command.CreateComponentCommand;
import kobold.client.plam.editor.command.CreateMetaNodeCommand;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.productline.Component;

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
public class ProductlineContainerEditPolicy extends ContainerEditPolicy
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ProductlineContainerEditPolicy.class);

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request)
    {
        Command resultCmd = UnexecutableCommand.INSTANCE;
        Object parent = getHost().getModel();
        
        IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart();
        ViewModel vmc = vmp.getViewModel(); 
        
        if (request.getNewObjectType().equals(AbstractAsset.COMPONENT)
                && parent instanceof IComponentContainer) {
            
            CreateComponentCommand cmd = new CreateComponentCommand();
            cmd.setParent((IComponentContainer) parent);
            cmd.setChild((Component) request.getNewObject());
            
            resultCmd = cmd;
        } else if ((request.getNewObjectType().equals(MetaNode.OR) || request.getNewObjectType().equals(MetaNode.AND))
                && parent instanceof AbstractRootAsset) {
            
            CreateMetaNodeCommand cmd = new CreateMetaNodeCommand();
            cmd.setParent((AbstractRootAsset) parent);
            cmd.setChild((MetaNode) request.getNewObject());
            
            resultCmd = cmd;
        }
       
        return resultCmd;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command getAddCommand(GroupRequest request)
    {
		if (logger.isDebugEnabled()) {
			logger.debug("getAddCommand(GroupRequest) - add");
		}
        return null;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCloneCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    protected Command getCloneCommand(ChangeBoundsRequest request)
    {
		if (logger.isDebugEnabled()) {
			logger.debug("getCloneCommand(ChangeBoundsRequest) - clone");
		}
        return null;
    }
    
    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command getOrphanChildrenCommand(GroupRequest request)
    {
		if (logger.isDebugEnabled()) {
			logger.debug("getOrphanChildrenCommand(GroupRequest) - orphan");
		}
        return null;
    }
}
