 
package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.ui.DesignUtils;
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
	public void execute(@Optional MPart part, @Optional EPartService partService)
	{		
		EObject eObject = null;		
		
		DesignsView designView = (DesignsView) part.getObject();
		Object selObj = designView.getSelection();
		
		eObject = DesignUtils.rollUpToDesing((EObject) selObj);
		if (eObject instanceof Design)
		{	
			// Design im Treeviewer selektieren (wird 'active Design')
			designView.setSelection(eObject);
			
			OpenDesignAction openAction = new OpenDesignAction();
			openAction.execute((Design)eObject);
			
			// DesignView aktivieren (verhindert No Active Window Error)			
			partService.activate(part);		
		}
	}


}