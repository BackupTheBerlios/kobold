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
 * $Id: ConnectionCommand.java,v 1.3 2004/08/03 15:16:40 vanto Exp $
 *
 */
package kobold.client.plam.editor.command;

import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;

import org.eclipse.gef.commands.Command;


/**
 * @author Tammo
 */
public class ConnectionCommand extends Command
{
    private INode sourceNode;
    private INode targetNode;
    
    private String targetType;
    private String sourceType;
    
    private String type;
    
    private Edge edge;
    
    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute()
    {
        EdgeContainer ec = sourceNode.getRoot().getEdgeContainer();
        edge = ec.addEdge(sourceNode, targetNode, type);
        if (sourceNode instanceof MetaNode) {
            sourceType = ((MetaNode)sourceNode).getEdgeType();
            ((MetaNode)sourceNode).setEdgeType(type);
        }
        if (targetNode instanceof MetaNode) {
            targetType = ((MetaNode)targetNode).getEdgeType();
            ((MetaNode)targetNode).setEdgeType(type);
        }
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    public void redo()
    {
        EdgeContainer ec = sourceNode.getRoot().getEdgeContainer();
        ec.addEdge(edge.getStartNode(), edge.getTargetNode(), edge.getType());
        if (sourceNode instanceof MetaNode) {
            sourceType = ((MetaNode)sourceNode).getEdgeType();
            ((MetaNode)sourceNode).setEdgeType(type);
        }
        if (targetNode instanceof MetaNode) {
            targetType = ((MetaNode)targetNode).getEdgeType();
            ((MetaNode)targetNode).setEdgeType(type);
        }
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo()
    {
        EdgeContainer ec = sourceNode.getRoot().getEdgeContainer();
        ec.removeEdge(edge.getStartNode(), edge.getTargetNode(), edge.getType());
        if (sourceNode instanceof MetaNode) {
            ((MetaNode)sourceNode).setEdgeType(sourceType);
        }
        if (targetNode instanceof MetaNode) {
            ((MetaNode)targetNode).setEdgeType(targetType);
        }

    }
    
    /**
     * @return Returns the sourceNode.
     */
    public INode getSourceNode()
    {
        return sourceNode;
    }
    
    /**
     * @param sourceNode The sourceNode to set.
     */
    public void setSourceNode(INode sourceNode)
    {
        this.sourceNode = sourceNode;
    }
    
    /**
     * @return Returns the targetNode.
     */
    public INode getTargetNode()
    {
        return targetNode;
    }
    
    /**
     * @param targetNode The targetNode to set.
     */
    public void setTargetNode(INode targetNode)
    {
        this.targetNode = targetNode;
    }
    
    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }
}
