package it.naturtalent.planning.ui;

import it.naturtalent.e4.project.AbstractProjectDataAdapter;
import it.naturtalent.planning.ui.parts.PlanungView;
import it.naturtalent.planning.ui.wizards.PlanningWizardPage;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactory;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.IPlanningModelFactory;
import it.naturtalent.planung.PlanningEvent;
import it.naturtalent.planung.PlanningMaster;
import it.naturtalent.planung.PlanningModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PlanningProjectAdapter extends AbstractProjectDataAdapter
{
	
	public static final String PLANNINGPROJECTADAPTERNAME = "Planung"; //$NON-NLS-1$
	
	private PlanningWizardPage planningWizardPage;
	//private PlanningMaster planningMaster;
	
	private PlanningModel planningModel;
	
	@Optional @Inject IPlanningModelFactory planningModelFactory;
	@Optional @Inject IAlternativeFactoryRegistry alternativeFactoryRegistry;
	@Optional @Inject ILayerContentRepository layerContentRepository;
	@Optional @Inject IEventBroker eventBroker;
	@Optional @Inject EPartService partService;
	@Optional @Inject EModelService modelService;
	@Optional @Inject MApplication app;
	
	
	/**
	 * EventHandler zur Ueberwachung der Modellaenderungen
	 */
	private EventHandler modelEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			Object eventObj = event.getProperty(IEventBroker.DATA);
			if(StringUtils.equals(event.getTopic(), PlanningEvent.PLANNING_EVENT_DELETED))
			{
				if (eventObj instanceof PlanningMaster)
				{
					eventBroker.unsubscribe(modelEventHandler);
					PlanningMaster planningMaster = (PlanningMaster) eventObj;
					planningModel.deletePlanningMaster(planningMaster.getId());
					eventBroker.subscribe(PlanningEvent.PLANNING_EVENT+"*",modelEventHandler);
					return;
				}
			}
		}
	};
	
	
	@Override
	public String getName()
	{		
		return PLANNINGPROJECTADAPTERNAME; 
	}
	
	@Inject
	public PlanningProjectAdapter(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
		eventBroker.subscribe(PlanningEvent.PLANNING_EVENT+"*",modelEventHandler);
	}

	@Override
	public Class<?> getProjectDataClass()
	{
		return PlanningMaster.class;
	}
	
	private PlanningModel getPlanningModel()
	{
		if(planningModel == null)
		{
			planningModel = planningModelFactory.createModel();
			planningModel.setEventBroker(eventBroker);
			planningModel.loadModel();	
		}
		return planningModel;
	}

	@Override
	public String [] toText(Object projectData)
	{	
		String [] txtArray = {"Planung"};
		
		if (projectData instanceof PlanningMaster)
		{
			PlanningMaster planung = (PlanningMaster) projectData;
						
			Alternative[] alternatives = planung.getAlternatives();
			for(Alternative alternative : alternatives)
				txtArray = ArrayUtils.add(txtArray, alternative.getName());
		}
		
		return txtArray;
	}
	
	@Override
	public void delete()
	{
		if(ntProject != null)
		{
			PlanningMaster planung = (PlanningMaster) getProjectData(ntProject.getId());
			if(planung != null)
			{
				PlanningModel planningModel = getPlanningModel();
				planningModel.deletePlanningMaster(planung.getId());
				planningModel.saveModel();
			}
		}
	}
	
	// Hyperlinkaction (PlanungView) oeffnen	
	@Override
	public Action getAction(IEclipseContext context)
	{
		Action action = new Action()
		{
			private String message = PlanningEvent.PLANNING_EVENT_SELECT_PLANNINGMASTER;
					
			@Override
			public void run()
			{
				if(partService != null)
				{
					// Part PlannungView suchen
					MPart part = partService.findPart(PlanungView.PLANNINGVIEW_ID);										
					if(part != null)
					{
						Object obj = part.getObject();
						if (obj == null)
						{
							// Part zum Stack hinzufuegen
							MPartStack myPartStack = (MPartStack)modelService.find("it.naturtalent.e4.project.ui.partstack.1", app);
							myPartStack.getChildren().add(part);
						}
							
						// part aktivieren
						part.setVisible(true);
						partService.activate(part);
						
						obj = part.getObject();
						if (obj instanceof PlanungView)
						{
							// den selektierten PlanningMaster ueber den EventBroker melden  							
							Object projectData = getProjectData();
							if(eventBroker != null)
								eventBroker.send(PlanningEvent.PLANNING_EVENT_SELECT_PLANNINGMASTER, projectData);
								
						}
					}
				}				
			}
		};
		return action;
	}

	@Override
	public WizardPage getWizardPage()
	{	
		planningWizardPage = new PlanningWizardPage();
		if(ntProject != null)
		{
			PlanningMaster planningMaster = (PlanningMaster) getProjectData(ntProject.getId());
			planningWizardPage.setAlternativeFactoryRegistry(alternativeFactoryRegistry);
			planningWizardPage.init(planningMaster);			
		}
		return planningWizardPage;
	}
	
	@Override
	public Object getProjectData(String projectID)
	{
		PlanningModel planningModel = getPlanningModel();
		List<PlanningMaster>plannings = planningModel.getModelData();
		if(!plannings.isEmpty())
		{
			// Planung mit der ProjektId wird zurueckgegeben
			for(PlanningMaster planning : plannings)
			{
				if(StringUtils.equals(projectID, planning.getProjektKey()))
					return planning;
			}
		}
		return null;
	}
	
	
	
	@Override
	public Object getProjectData()
	{
		if((ntProject != null) && (planningModel != null))
		{
			String projectID = ntProject.getId();
			List<PlanningMaster>plannings = planningModel.getModelData();
			for(PlanningMaster planningMaster : plannings)
				if(StringUtils.equals(planningMaster.getProjektKey(),projectID))
					return planningMaster;
		}
		return null;
	}

	@Override
	public Object load(String projectId)
	{
		Object dataObj = null;
		
		if(StringUtils.isNotEmpty(projectId))
			dataObj = getProjectData(projectId);
		return dataObj;
	}

	@Override
	public void save()
	{		
		PlanningMaster planningMaster = null;
		
		// ist dem Projekt bereits eine Planung zugeordnet
		PlanningModel planningModel = getPlanningModel();			
		List<PlanningMaster>plannings = planningModel.getModelData();	
		String projectKey = ntProject.getId();
		for(PlanningMaster planning : plannings)
		{
			if(StringUtils.equals(planning.getProjektKey(), projectKey))
			{
				// 'planningMaster ist eine bereits zugeordnete bestehende Planung
				planningMaster = planning;
				break;
			}
		}
		
		// die der Planung effektiv zugeordneten Alternativen				
		List<Alternative>newPlanningAlternatives = new ArrayList<Alternative>();
				
		// die Namen der in der WizardPage selektierten Alternativen
		String [] checkedAlternativeNames = planningWizardPage.getCheckedAlternatives();
		
		//die in Planung existierenden Alternativen aus checked entfernen
		if(planningMaster != null)
		{
			Alternative[] oldPlanningAlternatives = planningMaster.getAlternatives();
			if (ArrayUtils.isNotEmpty(oldPlanningAlternatives))
			{
				// die momentan zugeordneten Alternativen in Map zwischenspeichern
				Map<String, Alternative> existAlternativeMap = new HashMap<String, Alternative>();
				for (Alternative oldAlternative : oldPlanningAlternatives)
					existAlternativeMap.put(oldAlternative.getName(),
							oldAlternative);

				List<String>lCheckedAlternativeNames = Arrays.asList(checkedAlternativeNames);
				for (String checkedAlternativeName : lCheckedAlternativeNames)
				{
					if (existAlternativeMap.containsKey(checkedAlternativeName))
					{
						// eine bereits zugeordnete Alternative bleibt erhalten
						newPlanningAlternatives.add(existAlternativeMap.get(checkedAlternativeName));

						// checkedAlternativeName = erledigt, aus Array entfernen
						checkedAlternativeNames = ArrayUtils
								.removeElement(checkedAlternativeNames,
										checkedAlternativeName);
					}
				}
			}
		}
				
		if(ArrayUtils.isNotEmpty(checkedAlternativeNames))
		{
			for(String checkedAlternativeName : checkedAlternativeNames)
			{
				// existiert eine Factory, dann neue Alternative instanziieren
				IAlternativeFactory alternativeFactory = alternativeFactoryRegistry
						.getAlternativeFactory(checkedAlternativeName);
				if (alternativeFactory != null)
				{
					// neue Alternative hinzufuegen
					Alternative newAlternative = alternativeFactory.createAlternative();					
					newPlanningAlternatives.add(newAlternative);
										
					// die Designs updaten
					Design [] designs = newAlternative.getDesigns();
					for(Design design : designs)
					{
						// Key auf die Alternative
						design.setKey(newAlternative.getId());

						File designFile = PlanningUtils.getProjectDesignFile(
								ntProject.getIProject(), design);
						if (!designFile.exists())
						{
							// Template in die Zeichnungsdokument
							// kopieren
							designFile = PlanningUtils
									.copyDesignTemplate(designFile.getPath());
						}

						// Pfad zum Dokument im Design speichern
						design.setDesignURL(designFile.toString());
					}
				}
			}
		}

		if(!newPlanningAlternatives.isEmpty())
		{			
			if(planningMaster == null)
			{
				// neue Planung erzeugen und dem Modell hinzufuegen
				planningMaster = new PlanningMaster();
				planningMaster.setName(ntProject.getName());
				
				
				Alternative [] planningAlternatives = newPlanningAlternatives
						.toArray(new Alternative[newPlanningAlternatives.size()]);
				for(Alternative alternative : planningAlternatives)					
					alternative.setKey(planningMaster.getId());
				
				
				planningMaster.setAlternatives(planningAlternatives);
				planningModel.addPlanningMaster(planningMaster);
			}
			else
			{
				Alternative [] planningAlternatives = newPlanningAlternatives
						.toArray(new Alternative[newPlanningAlternatives.size()]);
				for(Alternative alternative : planningAlternatives)					
					alternative.setKey(planningMaster.getId());

				// bestehende Planung als modified markieren
				planningMaster.setAlternatives(planningAlternatives);
				planningModel.update(planningMaster);
			}
			
			// Projekt zuordnen 
			planningMaster.setProjektKey(ntProject.getId());
						
			// Model persistent speichern
			planningModel.saveModel();
		}
		else
		{
			// keine Alternative selektiert
			// kann eigentlich nicht sein, da WizardPage Verify dies verhindert
			if(planningMaster != null)
			{
				// vorhandene Planung loeschen
				planningModel.deletePlanningMaster(planningMaster.getId());
				planningModel.saveModel();
			}
		}
	}
	

	
}
