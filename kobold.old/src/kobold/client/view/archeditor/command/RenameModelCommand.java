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
 * $Id: RenameModelCommand.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.command;

import kobold.client.view.archeditor.model.AbstractModel;

import org.eclipse.gef.commands.Command;

/**
 * RenameModelCommand
 * 
 * @author Tammo van Lessen
 */
public class RenameModelCommand extends Command {

    private AbstractModel model;
    private String name, oldName;

    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute() {
        model.setName(name);
    }

    /**
     * Sets the new name
     * @param string the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the old name
     * @param string the old name
     */
    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    /**
     * Sets the model
     * @param activity the model
     */
    public void setModel(AbstractModel model) {
        this.model = model;
    }

    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo() {
        model.setName(oldName);
    }

}
