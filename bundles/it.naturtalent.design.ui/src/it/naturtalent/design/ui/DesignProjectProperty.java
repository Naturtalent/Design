package it.naturtalent.design.ui;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizardPage;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.e4.project.INtProjectProperty;


/**
 * Realisierung der Designeigenschaft des NtProjects.
 * 
 * @author dieter
 *
 */
public class DesignProjectProperty implements INtProjectProperty
{
	// die mit diesem Adapter erzugte WizardPage
	private DesignProjectPropertyWizardPage designWizardPage;
	
	// die eigentlichen Propertydaten
	//private DesignGroup designGroup = DesignsFactory.eINSTANCE.createDesignGroup();
	private DesignGroup designGroup;
	
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
		designGroup = DesignUtils.findProjectDesignGroup(ntProjectID);
	}

	@Override
	public String getNtProjectID()
	{
		return (designGroup != null) ? designGroup.getIProjectID() : null; 
	}
	
	@Override
	public void setNtPropertyData(Object eObject)
	{
		if(eObject instanceof DesignGroup)
			designGroup = (DesignGroup) eObject;		
	}

	@Override
	public Object getNtPropertyData()
	{
		return designGroup;
	}

	@Override
	public Object getPropertyContainer()
	{		
		return DesignUtils.getDesigns();
	}

	@Override
	public void commit()
	{
		// ohne WizardPage keine bearbeitetes DesignGroup 
		if(designWizardPage == null)
			return;
		
		// das ContainerModell
		Designs designs = DesignUtils.getDesigns();
		
		// die mit dem WizardPage bearbeitete Mappe
		DesignGroup editedDesignGroup = designWizardPage.getWizardDesignGroup();
		
		// hat das Projekt eine Zeichnungsmappe
		DesignGroup persistenceDesignGroup = DesignUtils.findProjectDesignGroup(ntProjectID);
		
		if(editedDesignGroup.getDesigns().size() == 0)
		{
			// eine bereits vorhandene Group laden			
			if(persistenceDesignGroup != null)
			{
				// existierende Group entfernen
				designs.getDesigns().remove(persistenceDesignGroup);	
				
				// Modell speichern
				ECPProject designProject = DesignUtils.getDesignProject();
				designProject.saveContents();				
			}	
			return;
		}
		else
		{
			if(persistenceDesignGroup == null)
			{
				// DesignGroup ist neu
				editedDesignGroup.setIProjectID(ntProjectID);
				
				// die erforderlichen Dateien im Datenbereich des Projekts erzeugen
				IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(ntProjectID);
				for(Design design : editedDesignGroup.getDesigns())		
					design.setDesignURL(DesignUtils.createProjectDesignFile(iProject));
						
				// neue DesignGroup zum Modell hinzufuegen
				designs.getDesigns().add(editedDesignGroup);
			}
		}

	}
	
	public void commitOLD()
	{
		// ohne WizardPage keine bearbeitetes DesignGroup 
		if(designWizardPage == null)
			return;
		
		// die mit dem WizardPage bearbeitete DesignGroup
		DesignGroup editedDesignGroup = designWizardPage.getWizardDesignGroup();
		
		// eine bereits vorhandene Group laden
		DesignGroup persistenceDesignGroup = DesignUtils.findProjectDesignGroup(ntProjectID);
		
		// das ContainerModell
		Designs designs = DesignUtils.getDesigns();
		
		// ist der DesingGroupName null wird eine evtl. existierende Gruppe geloescht
		if(StringUtils.isEmpty(editedDesignGroup.getName()))
		{			
			if(persistenceDesignGroup != null)
			{				
				// existierende Group entfernen
				designs.getDesigns().remove(persistenceDesignGroup);	
				
				// Modell speichern
				ECPProject designProject = DesignUtils.getDesignProject();
				designProject.saveContents();
			}
			
			return;
		}
				
		if(persistenceDesignGroup != null)
		{
			// Name der existierenden DesignGroup aktualisieren
			persistenceDesignGroup.setName(editedDesignGroup.getName());			
		}
		else
		{
			// neue DesignGroup hinzufuegen
			editedDesignGroup.setIProjectID(ntProjectID);
			designs.getDesigns().add(editedDesignGroup);
		}
		
		// Modell speichern
		ECPProject designProject = DesignUtils.getDesignProject();
		designProject.saveContents();
	}

	/*
	 * DesingPropertyWizarPage erzeugen
	 * 
	 */
	@Override
	public IWizardPage createWizardPage()
	{
		designWizardPage = ContextInjectionFactory.make(DesignProjectPropertyWizardPage.class, context);
		designWizardPage.setDesignProjectProperty(this);			
		return designWizardPage;
	}

	public String toString()
	{		
		//return super.toString();
		return "Zeichnung";
	}

	@Override
	public void undo()
	{
		// TODO Auto-generated method stub
	
	}

	@Override
	public void delete()
	{
		if (StringUtils.isNotEmpty(ntProjectID))	
		{
			DesignGroup projectDesignGroup = DesignUtils.findProjectDesignGroup(ntProjectID);
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
	public String getLabel()
	{		
		return null;
	}

	/* Definiert die Aktion, die durch Aktivierung des Hyperlinks im NtProjekt-Details ausgeloest wird.
	 * Die Aktion oeffnet DesignView (falls erforderlich) und sendet ein Map-Event mit der dort zu selektierenden
	 * DesignGroup.
	 * 
	 * @see it.naturtalent.e4.project.INtProjectProperty#createAction()
	 */
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
				// ViewDesign offnen
				MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
				EPartService partService = currentApplication.getContext().get(EPartService.class);
				MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
				part.setVisible(true);
				partService.activate(part);
				
				// Event senden
				//Map<String, DesignGroup>map = new HashMap<String, DesignGroup>();
			//	map.put("DESIGNGROUP", designGroup);
				eventBroker.send(DesignsView.DESIGN_SELECTGROUP_EVENT, designGroup);
				
				super.run();
			}			
		};
		
		return action;
	}

	@Override
	public Object init()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyFactoryName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public boolean importProperty(Object importData)
	{
		// TODO Auto-generated method stub
		return false;
	}
	*/


	@Override
	public void exportProperty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importProperty() {
		// TODO Auto-generated method stub
		
	}

	/*
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
	*/

}
