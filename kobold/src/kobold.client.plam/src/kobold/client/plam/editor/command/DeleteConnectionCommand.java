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
 * $Id: DeleteConnectionCommand.java,v 1.3 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.editor.command;

import org.apache.log4j.Logger;

import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.edges.Edge;

import org.eclipse.gef.commands.Command;


/**
 * @author Tammo
 */
public class DeleteConnectionCommand extends Command
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DeleteConnectionCommand.class);

    private Edge edge;
    private AbstractRootAsset root;

    public DeleteConnectionCommand(Edge edge) 
    {
        this.edge = edge;
        this.root = edge.getStartNode().getRoot();
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute()
    {
        root.getEdgeContainer().removeEdge(edge);
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    public void redo()
    {
        root.getEdgeContainer().removeEdge(edge);
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo()
    {
        root.getEdgeContainer().addEdge(edge);
    }
}
