package it.naturtalent.planning.ui.wizards;

import it.naturtalent.e4.project.INtProject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ToProjectNameConverter extends Converter
{

	public ToProjectNameConverter()
	{
		super(String.class, String.class);			
	}

	@Override
	public Object convert(Object fromObject)
	{
		if (fromObject instanceof String)
		{				
			String projectID = (String) fromObject;
			if(StringUtils.isNotEmpty(projectID))
			{
				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(projectID);
				if(project != null)
				{
					try
					{
						String name = project.getPersistentProperty(INtProject.projectNameQualifiedName);
						return name;
					} catch (CoreException e)
					{							
					}
				}
			}				
		}
					
		return null;
	}

}
