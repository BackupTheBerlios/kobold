/*
 * Created on 20.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.editor.command;

import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.Release;


/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateFileDescriptorCommand extends AbstractCreateCommand{
    
    private IFileDescriptorContainer parent;
    private FileDescriptor child;
    
    public void setParent(IFileDescriptorContainer parent)
    {
        this.parent = parent;
    }
    
    public void setChild(FileDescriptor child)
    {
        this.child = child;
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#redo()
     */
    protected void addChildToParent()
    {
        parent.addFileDescriptor(child);
    }
    
    /**
     */
    protected void removeChildFromParent()
    {
        parent.removeFileDescriptor(child);
    }
    
}
