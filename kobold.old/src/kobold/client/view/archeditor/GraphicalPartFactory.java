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
 * $Id: GraphicalPartFactory.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor;

import kobold.client.view.archeditor.editpart.ArchitectureEditPart;
import kobold.client.view.archeditor.editpart.ComponentEditPart;
import kobold.client.view.archeditor.editpart.VariantEditPart;
import kobold.client.view.archeditor.model.ArchitectureModel;
import kobold.client.view.archeditor.model.ComponentModel;
import kobold.client.view.archeditor.model.VariantModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * GraphicalPartFactory
 * 
 * @author Tammo van Lessen
 */
public class GraphicalPartFactory implements EditPartFactory {

    /**
     * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
     */
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart = null;

        if (model instanceof ArchitectureModel) {
            editPart = new ArchitectureEditPart();
        } else if (model instanceof ComponentModel) {
            editPart = new ComponentEditPart();
        } else if (model instanceof VariantModel) {
            editPart = new VariantEditPart();
        }

        if (editPart != null) {
            editPart.setModel(model);
        }

        return editPart;
    }

}
