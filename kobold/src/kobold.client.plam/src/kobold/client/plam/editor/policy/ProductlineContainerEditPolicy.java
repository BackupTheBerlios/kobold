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
 * $Id: ProductlineContainerEditPolicy.java,v 1.1 2004/06/23 02:26:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import kobold.client.plam.editor.command.CreateCommand;
import kobold.common.model.AbstractAsset;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;


/**
 * @author Tammo
 */
public class ProductlineContainerEditPolicy extends ContainerEditPolicy
{

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request)
    {
        CreateCommand cmd = new CreateCommand();
        //FIXME instanceof checks
        cmd.setParent((AbstractAsset)getHost().getModel());
        cmd.setChild((AbstractAsset)request.getNewObject());
        return cmd;
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
