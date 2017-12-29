 
package it.naturtalent.design.ui.actions;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;

/**
 * Wird ueber die Toolbar aufgerufen
 * 
 * Das der Zeichnungsgruppe zugeordnete Projekt (falss eins existiert) im ResourceNavigator selektieren.
 * 
 * @author dieter
 *
 */
public class LinkProjectAction
{
	@Execute
	public void execute(@Optional EPartService partService, @Optional MPart part)	
	{
		if(partService != null)
		{			
			
			//MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
			
			DesignsView designView = (DesignsView) part.getObject();
			Object selObject = designView.getSelection();
			
			
			IProject iProject = getIProject(selObject);
			if(iProject != null)
			{
				// ResourceNavigator suchen und Projekt selektiern
				StructuredSelection selection = new StructuredSelection(iProject);	
				MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();				
				part = partService.findPart(ResourceNavigator.RESOURCE_NAVIGATOR_ID);
				ResourceNavigator navigator = (ResourceNavigator) part.getObject();
				navigator.setSelection(iProject);				
			}
		}
	}

	/*
	 * Sollte die Zeichnungsgruppe ein IProject referenzieren (ueber ProjectID) dann gibt diese Funktion
	 * das entsprechende IProject zurueck. Je nach selektierten Object (DesignGroup oder Design) muss das
	 * IProject unterschiedlliche ermittelt werden.
	 */
	private IProject getIProject(Object selectedDesignObjec)
	{
		String projectID;
		IProject iProject = null;
		
		if(selectedDesignObjec instanceof DesignGroup)
		{
			// DesignGroup ist selektiert -> IProject direkt ueber ProjektID ermitteln
			projectID = ((DesignGroup)selectedDesignObjec).getIProjectID();
			if(StringUtils.isNotEmpty(projectID))
				iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
		}
		else
		{
			if (selectedDesignObjec instanceof Design)
			{
				// Design ist selektiert -> IProject indirekt ueber Parent-DesignGroup ermitteln
				Design design = ((Design) selectedDesignObjec);
				DesignGroup designGroup = DesignUtils.findDesignGroup(design);

				projectID = designGroup.getIProjectID();
				if (StringUtils.isNotEmpty(projectID))
					iProject = ResourcesPlugin.getWorkspace().getRoot()
							.getProject(projectID);
			}
			else
			{
				EObject eObject = (EObject) selectedDesignObjec;
				while(!(eObject instanceof DesignGroup))
					eObject = eObject.eContainer();
					
				// DesignGroup ist selektiert -> IProject direkt ueber ProjektID ermitteln
				projectID = ((DesignGroup)eObject).getIProjectID();
				if(StringUtils.isNotEmpty(projectID))
					return(ResourcesPlugin.getWorkspace().getRoot().getProject(projectID));			
			}
		}

		return iProject;
	}

}