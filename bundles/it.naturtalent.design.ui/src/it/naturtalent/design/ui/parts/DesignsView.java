 
package it.naturtalent.design.ui.parts;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPProjectContentChangedObserver;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.ui.Activator;
import it.naturtalent.design.ui.actions.OpenDesignAction;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class DesignsView
{
	
	public final static String DESIGNSVIEW_ID = "it.naturtalent.design.ui.part.designs";
	
	// Name des ECP Projekts indem alle Zeichnungen gespeichert werden
	public final static String DESIGNPROJECTNAME = "DesignsEMFProject";
	
	// Eventkey zur Selektion einer DesignGroup (@see DesignMasterDetailRenderer)
	public final static String DESIGN_SELECTGROUP_EVENT = "designselectgroupevent";
	
	private IResource selectedResource;
	
	
	/*
	 * 'ECPProjectContentChangedObserver' Überwacht Aenderungen im Modell
	 */
	private ProjectModelChangeObserver projectModelChangedObserver;
	private class ProjectModelChangeObserver implements ECPProjectContentChangedObserver
	{
		@Override
		public Collection<Object> objectsChanged(ECPProject project,Collection<Object> objects)
		{
			// Aenderungen am Modell werden vie Broker weitergemeldet	
			//eventBroker.send(UndoProjectAction.PROJECTCHANGED_MODELEVENT, "projectModelData changed");
			
			if(objects.iterator().next() instanceof Design)
			{
				Design design = (Design) objects.iterator().next();
				
				if(StringUtils.isEmpty(design.getDesignURL()))
				{
					//IFolder folder = ntProject.getIProject().getFolder(IProjectData.PROJECTDATA_FOLDER);
					//IPath path = folder.getLocation();
					
					System.out.println("selected Resource: "+selectedResource);

				}
				
				System.out.println(project.getName());
				
			}
			
			return null;
		}
	}
	
	
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
		
		projectModelChangedObserver = new ProjectModelChangeObserver();
		ECPUtil.getECPObserverBus().register(projectModelChangedObserver);
		
		boolean check = Activator.checkLibraryPath("jurt");
		System.out.println(check);
		
		//Activator.setLibraryPath("/usr/lib/libreoffice/program/classes");
		
	}
	
	@PreDestroy
	public void dispose()
	{
		Map<Design, DrawDocument>openDrawDocumentMap = OpenDesignAction.getOpenDrawDocumentMap();
		for(DrawDocument drawDocument : openDrawDocumentMap.values())
			drawDocument.closeDocument();
		
		if((openDrawDocumentMap != null) && (!openDrawDocumentMap.isEmpty()))
			openDrawDocumentMap.values().iterator().next().closeDesktop();
		
		ECPUtil.getECPObserverBus().unregister(projectModelChangedObserver);
	}
	
	/**
	 * Die selektierte Resource (ResourceNavigator) wird erkannt und gespeichert
	 *  
	 * @param selectedResource
	 */
	@Inject
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional IResource selectedResource)
	{
		this.selectedResource = selectedResource;
	}
	
	/*
	 * Das Modell, indem die Designdaten gespeichert werden. wird zurueckgegeben (ggf. neu erzeugt) 
	 */
	private Designs getDesigns()
	{
		Designs designs = null;
		
		ECPProject designsProject = ECPUtil.getECPProjectManager().getProject(DESIGNPROJECTNAME);	
		
		// ggf. Projekt 'DESIGNPROJECT' erzeugen
		if(designsProject == null)
			designsProject = Activator.createProject(DESIGNPROJECTNAME);
		
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
			// das Modell Designs erzeugen und im ECPProject speichern
			EClass designsClass = DesignsPackage.eINSTANCE.getDesigns();
			designs = (Designs)EcoreUtil.create(designsClass);
			projectContents.add(designs);
			designsProject.saveContents();			
		}
		
		return designs;
	}
	
	

	
	
}