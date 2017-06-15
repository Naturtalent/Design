 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

import it.naturtalent.e4.project.IProjectData;

import javax.inject.Named;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class OpenDesign
{
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional IResource selectedResource)
	{
		if(selectedResource != null)
		{
			IProject iProject = selectedResource.getProject();
			if(iProject != null)
				openDesign(iProject);
		}
		
		
	}

	private void openDesign(IProject iProject)
	{
		IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
		IPath path = folder.getLocation();
		
		System.out.println("OpenDesign: "+path.toFile());
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional IResource selectedResource)
	{
		if(selectedResource != null)
		{
			IProject iProject = selectedResource.getProject();
			if(iProject != null)
				return true;
		}
		
		//return (selectedResource != null && (selectedResource.getType() & (selectedResource.FOLDER) | (selectedResource.PROJECT)) != 0);
		return false;
	}

}