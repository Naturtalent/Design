 
package it.naturtalent.design.ui;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.emf.model.EMFModelUtils;

import javax.annotation.PostConstruct;

public class DesignsView
{
	
	// Name des ECP Projekts indem alle Zeichnungen gespeichert werden
	public final static String DESIGNPROJECTNAME = "DesignsEMFProject";
	
	@Inject
	public DesignsView()
	{

	}

	@PostConstruct
	public void postConstruct(Composite parent)
	{
		Designs designs = getDesigns();
		
		if (designs != null)
		{
			try
			{
				ECPSWTViewRenderer.INSTANCE.render(parent, designs);
			}

			catch (ECPRendererException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Designs getDesigns()
	{
		Designs designs = null;
		
		ECPProject designsProject = ECPUtil.getECPProjectManager().getProject(DESIGNPROJECTNAME);	
		
		// ggf. Projekt 'ARCHIVESPROJECT' erzeugen
		if(designsProject == null)
			designsProject = new EMFModelUtils().createProject(DESIGNPROJECTNAME);
		
		// im ECPProject das Modell Archives suchen 
		EList<Object>projectContents = designsProject.getContents();
		if(!projectContents.isEmpty())
		{
			for(Object projectContent : projectContents)
			{
				if (projectContent instanceof Designs)
				{
					designs = (Designs) projectContent; 
					break;
				}
			}			
		}
		else
		{
			// das Modell Archives erzeugen und im ECPProject speichern
			EClass designsClass = DesignsPackage.eINSTANCE.getDesigns();
			designs = (Designs)EcoreUtil.create(designsClass);
			projectContents.add(designs);
			designsProject.saveContents();			
		}

		
		return designs;
	}
	
}