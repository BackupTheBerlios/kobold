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
 * $Id: ContainerEditPolicy.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.editpolicy;

import java.util.List;

import kobold.client.view.archeditor.command.CreateComponentCommand;
import kobold.client.view.archeditor.command.CreateVariantCommand;
import kobold.client.view.archeditor.model.AbstractModel;
import kobold.client.view.archeditor.model.ComponentModel;
import kobold.client.view.archeditor.model.VariantModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * ContainerEditPolicy
 * 
 * @author Tammo van Lessen
 */
public class ContainerEditPolicy
    extends org.eclipse.gef.editpolicies.ContainerEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request) {
        Command create = null;
        String type = (String)request.getNewObjectType();
        if ((getHost().getModel() instanceof VariantModel) 
                   && (type.equals("component"))) {

            VariantModel variant = (VariantModel)getHost().getModel();
            ComponentModel comp = (ComponentModel)request.getNewObject();

            create = new CreateComponentCommand(variant, comp);
        } else if ((getHost().getModel() instanceof ComponentModel)
                   && (type.equals("variant"))) {

            ComponentModel component = (ComponentModel)getHost().getModel();
            VariantModel variant = (VariantModel)request.getNewObject();

            create = new CreateVariantCommand(component, variant);
        } else {
            return null;
        } 

        return create;
    }

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.requests.GroupRequest)
     */
    protected Command getOrphanChildrenCommand(GroupRequest request) {
        List parts = request.getEditParts();
        CompoundCommand result = 
            new CompoundCommand("orphan");
        for (int i = 0; i < parts.size(); i++) {
            OrphanChildCommand orphan = new OrphanChildCommand();
            orphan.setChild((AbstractModel)((EditPart)parts.get(i)).getModel());
            orphan.setParent((AbstractModel)getHost().getModel());
            orphan.setLabel("orphan");
            result.add(orphan);
        }
        return result.unwrap();
    }

    public class OrphanChildCommand
        extends Command
    {

    private AbstractModel parent;
    private AbstractModel child;
    private int index;

    public OrphanChildCommand () {
        super("orphan");
    }

    public void execute() {
        List children = parent.getChildren();
        index = children.indexOf(child);
        parent.removeChild(child);
    }

    public void redo() {
        parent.removeChild(child);
    }

    public void setChild(AbstractModel child) {
        this.child = child;
    }

    public void setParent(AbstractModel parent) { 
        this.parent = parent;
    }

    public void undo() {
        //parent.addChild(child, index);
        parent.addChild(child);
    }

    }

}
