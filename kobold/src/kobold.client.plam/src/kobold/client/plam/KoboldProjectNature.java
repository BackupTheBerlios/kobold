package kobold.client.plam;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * @see IProjectNature
 */
public class KoboldProjectNature implements IProjectNature {

	public static final String NATURE_ID = "kobold.client.plam.KoboldProjectNature"; //$NON-NLS-1$
	public static final String WORKFLOW_FILENAME = ".kworkflows"; //$NON-NLS-1$

	private IProject project;
	
	/**
	 *
	 */
	public KoboldProjectNature() 
	{
		System.out.println("inst nature");
	}

	/**
	 * @see IProjectNature#configure
	 */
	public void configure() throws CoreException 
	{
		System.out.println("configure nature");
	}

	/**
	 * @see IProjectNature#deconfigure
	 */
	public void deconfigure() throws CoreException 
	{
	}

	/**
	 * @see IProjectNature#getProject
	 */
	public IProject getProject()  
	{
		return project;
	}

	/**
	 * @see IProjectNature#setProject
	 */
	public void setProject(IProject project)  
	{
		this.project = project;
	}
	
	/**
	 * Returns the local workflow file
	 */
	protected IFile getWorkflowFile() 
	{
		return project.getFile(WORKFLOW_FILENAME);	
	}

}
