package it.naturtalent.design.ui;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizardPage;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsFactory;
import it.naturtalent.e4.project.INtProject;
import it.naturtalent.e4.project.INtProjectProperty;

public class DesignProjectProperty implements INtProjectProperty
{

	// die eigentlichen Propertydaten
	private DesignGroup designGroup = DesignsFactory.eINSTANCE.createDesignGroup();
	
	// ID des Projekts, auf das sich die Eigenschaft bezieht
	private String ntProjectID;
	
	private IEclipseContext context;
	
	private IEventBroker eventBroker;
	
	@PostConstruct
	public void postConstruct(@Optional IEclipseContext context, @Optional IEventBroker eventBroker)
	{
		this.context = context;
		this.eventBroker = eventBroker;
	}
	
	@Override
	public void setNtProjectID(String ntProjectID)
	{
		this.ntProjectID = ntProjectID;		

		DesignGroup designGroup = DesignUtils.findDesignGroup(ntProjectID);
		if(designGroup != null)
			this.designGroup = designGroup;		
	}

	@Override
	public String getNtProjectID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getNtPropertyData()
	{
		return designGroup;
	}

	@Override
	public Object init()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commit()
	{
		//ECPProject ecpProject = DesignUtils.getDesignProject();
		//if (ecpProject.hasDirtyContents() && StringUtils.isNotEmpty(ntProjectID))
		if (StringUtils.isNotEmpty(ntProjectID))	
		{
			designGroup.setIProjectID(ntProjectID);
			
			if (StringUtils.isEmpty(designGroup.getName()))
			{
				// DesignGroupName nicht definiert - Projektname wird uebernommen
				IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(ntProjectID);
				try
				{
					String name = iProject.getPersistentProperty(INtProject.projectNameQualifiedName);
					designGroup.setName(name);
				} catch (CoreException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			DesignGroup persistenceDesignGroup = DesignUtils.findDesignGroup(ntProjectID);
			if (persistenceDesignGroup == null)
			{
				// neuer Datensatz - zum Modell hinzufuegen
				Designs designs = DesignUtils.getDesigns();
				designs.getDesigns().add(designGroup);
			}
			else
			{
				persistenceDesignGroup.setName(designGroup.getName());
			}

			// Modell speichern
			ECPProject designProject = DesignUtils.getDesignProject();
			designProject.saveContents();

		}
		
	}

	/*
	 * loescht die DesignGroup des Projekts und die ProjectDesignFiles 
	 * 
	 */
	@Override
	public void delete()
	{
		if (StringUtils.isNotEmpty(ntProjectID))	
		{
			DesignGroup projectDesignGroup = DesignUtils.findDesignGroup(ntProjectID);
			if (projectDesignGroup != null)
			{
				// die ProjectDesignFiles muessen nicht explizit enfernt werden, da sie im
				// ProjectData-Bereich gespeichert sind und dieser wird mit dem Project ohnehin geloescht
				/*
				for(Design design : projectDesignGroup.getDesigns())
					DesignUtils.deleteProjectDesignFile(design.getDesignURL());
					*/
				
				// DesignGroup loeschen
				Designs designs = DesignUtils.getDesigns();
				designs.getDesigns().remove(projectDesignGroup);
				
				// das geaenderte Modell zurueckspeichern
				DesignUtils.getDesignProject().saveContents();				
			}			
		}
	}

	@Override
	public IWizardPage createWizardPage()
	{
		DesignProjectPropertyWizardPage designWizardPage = ContextInjectionFactory.make(DesignProjectPropertyWizardPage.class, context);
		designWizardPage.setDesignProjectProperty(this);			
		return designWizardPage;
	}

	@Override
	public void undo()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void exportProperty()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean importProperty(Object importData)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Action createAction()
	{
		// TODO Auto-generated method stub
		//return null;
		Action action = new Action()
		{

			@Override
			public void run()
			{
				System.out.println("DesingProperty: "+designGroup);
				super.run();
			}			
		};
		
		return action;
	}

	@Override
	public String getLabel()
	{		
		return null;
	}

	@Override
	public String toString()
	{		
		//return super.toString();
		return "Zeichnung";
	}
	
	

}