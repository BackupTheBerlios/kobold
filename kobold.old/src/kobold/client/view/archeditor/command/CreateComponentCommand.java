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
 * $Id: CreateComponentCommand.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.command;

import kobold.client.view.archeditor.model.ArchitectureModel;
import kobold.client.view.archeditor.model.ComponentModel;
import kobold.client.view.archeditor.model.VariantModel;

import org.eclipse.gef.commands.Command;


public class CreateComponentCommand extends Command
    {
        private final ComponentModel component;
        private final ArchitectureModel architecture;
        private final VariantModel variant;
        
        public CreateComponentCommand(VariantModel parent, ComponentModel component)
        {
            super();
            this.component = component;
            this.architecture = null;
            this.variant = parent;
        }
        
        public CreateComponentCommand(ArchitectureModel parent, ComponentModel component)
        {
            super();
            this.component = component;
            this.architecture = parent;
            this.variant = null;
        }
        
        /**
         * @see org.eclipse.gef.commands.Command#execute()
         */
        public void execute() {
            if (architecture != null) {
                architecture.addComponent(component);
            } else if (variant != null) {
                variant.addComponent(component);    
            }
        }

        /**
         * @see org.eclipse.gef.commands.Command#redo()
         */
        public void redo() {
            if (architecture != null) {
                architecture.addComponent(component);
            } else if (variant != null) {
                variant.addComponent(component);    
            }
        }

        /**
         * @see org.eclipse.gef.commands.Command#undo()
         */
        public void undo() {
            if (architecture != null) {
                architecture.removeComponent(component);
            } else if (variant != null) {
                variant.removeComponent(component);    
            }
        }

}