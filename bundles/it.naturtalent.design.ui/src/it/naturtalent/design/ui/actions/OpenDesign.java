 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.e4.project.IProjectData;

import java.io.File;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;

/**
 * Wird ueber die Toolbar aufgerufen
 * 
 * @author dieter
 *
 */
public class OpenDesign
{
	@Execute
	public void execute(@Optional EPartService partService)
	{
		
		MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
		DesignsView designView = (DesignsView) part.getObject();
		Object selObj = designView.getSelection();

		if(selObj instanceof Design)
		{
			
			OpenDesignAction openAction = new OpenDesignAction();
			openAction.execute((Design)selObj);
			
			/*
			Design design = (Design)selObj;
			String url = design.getDesignURL();
			
			if(StringUtils.isNotEmpty(url))
			{
				File file = new File(url);
				if(file.exists() && file.isFile())
				{
					
				}
			}
			*/
			
			
		}
		
		
	}


}