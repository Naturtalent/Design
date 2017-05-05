package it.naturtalent.planung;


import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;

public class PlanningModel extends BaseBean
{

	// Property
	public static final String PROP_PLANNINGDATA = "planningData"; //$NON-NLS-N$

	// EventBroker meldet die Ereignisse
	private IEventBroker eventBroker = null;

	// Model Factory
	public IPlanningModelFactory modelFactory = null;

	// Modellspeicher
	private List<PlanningMaster>modelData = new ArrayList<PlanningMaster>();
	private List<String> addedPlanningMaster = new ArrayList<String>();
	private Map<String, PlanningMaster>modifiedPlanningMaster = new HashMap<String, PlanningMaster>();
	private List<PlanningMaster>deletedPlanningMaster = new ArrayList<PlanningMaster>();


	public List<PlanningMaster> getModelData()
	{
		return modelData;
	}

	public void setModelData(List<PlanningMaster> modelData)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PLANNINGDATA, this.modelData, this.modelData = modelData));
	}
	
	public void addPlanningMaster(PlanningMaster planningMaster)
	{
		String id = planningMaster.getId();
		if (StringUtils.isEmpty(id))		
			planningMaster.setId(Activator.makeIdentifier());
		else
		{
			if(modelData.contains(planningMaster))
				return;
		}
		
		modelData.add(planningMaster);
		
		// modifyrelevant nur, wenn noch nicht persistent vorhanden
		PlanningMaster persistData = modelFactory.load(id);
		if(persistData == null)		
			addedPlanningMaster.add(0, id);

		
		if(eventBroker != null)
			eventBroker.send(PlanningEvent.PLANNING_EVENT_ADDED, planningMaster);			
	}
	
	public void deletePlanningMaster(String id)
	{
		if (StringUtils.isNotEmpty(id))
		{
			PlanningMaster planung = getData(id);			
			if(planung != null)
			{
				// aus dem Modell entfernen
				modelData.remove(planung);

				// in deleteList eintragen
				if(!addedPlanningMaster.contains(planung))
					deletedPlanningMaster.add(planung);
				
				// ggf. aus addedList/modifiedlist entfernen		
				addedPlanningMaster.remove(id);
				modifiedPlanningMaster.remove(id);

				if(eventBroker != null)
					eventBroker.send(PlanningEvent.PLANNING_EVENT_DELETED, planung);
			}
		}
	}

	public List<String> getAddedPlanningMaster()
	{
		return addedPlanningMaster;
	}

	public Map<String, PlanningMaster> getModifiedPlanningMaster()
	{
		return modifiedPlanningMaster;
	}

	public List<PlanningMaster> getDeletePlanningMaster()
	{
		return deletedPlanningMaster;
	}

	public PlanningMaster getData(String id)
	{
		for(PlanningMaster planningMaster : modelData)
			if(StringUtils.equals(planningMaster.getId(),id))
				return planningMaster;
		
		return null;
	}

	public IPlanningModelFactory getModelFactory()
	{
		return modelFactory;
	}

	public void setModelFactory(IPlanningModelFactory modelFactory)
	{
		this.modelFactory = modelFactory;
	}

	public IEventBroker getEventBroker()
	{
		return eventBroker;
	}

	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}
	
	public void update(PlanningMaster modifiedData)
	{
		if(modifiedData != null)
		{
			String id = modifiedData.getId();
			if (StringUtils.isNotEmpty(id))
			{
				// den persistenten Datensatz laden
				PlanningMaster persistData = modelFactory.load(id);
				if(persistData == null)
				{
					// noch nicht persistent gespeichert
					if(addedPlanningMaster.contains(id))
					{
						// aber bereits als nochzuspeichern vorgemerkt
						if(eventBroker != null)
							eventBroker.send(PlanningEvent.PLANNING_EVENT_MODIFIED, modifiedData);							
					}

					// persistent nicht vorhanden und nicht vorgemerkt zum
					// hinzufuegen - aus dem ModifyArchiv entfernen
					modifiedPlanningMaster.remove(id);
					return;					
				}
				
				// Vergleich modifizierter Datensatz mit persistenten Datensatz
				if (!persistData.equals(modifiedData))
				{
					// den modifizierten Datensatz registrieren
					modifiedPlanningMaster.put(id, modifiedData);
				}
				else
				{
					// 'modifiedData' (wieder) identisch mit persist - aus
					// ModifyArchiv entfernen
					if (modifiedPlanningMaster.containsKey(id))
						modifiedPlanningMaster.remove(id);
				}

				if(eventBroker != null)
					eventBroker.send(PlanningEvent.PLANNING_EVENT_MODIFIED, modifiedData);	
			}			
		}
	}
	
	public boolean isModified()
	{
		if(!deletedPlanningMaster.isEmpty())
			return true;

		if(!addedPlanningMaster.isEmpty())
			return true;

		if(!modifiedPlanningMaster.isEmpty())
			return true;
		
		return false;
	}

	public void saveModel()
	{
		if(modelFactory != null)
		{
			modelFactory.saveModel(this);

			addedPlanningMaster.clear();
			modifiedPlanningMaster.clear();
			deletedPlanningMaster.clear();
			
			if(eventBroker != null)
				eventBroker.send(PlanningEvent.PLANNING_EVENT_SAVED, this);
		}
	}

	public void loadModel()
	{
		if(modelFactory != null)
		{
			addedPlanningMaster.clear();
			modifiedPlanningMaster.clear();
			deletedPlanningMaster.clear();

			modelFactory.loadModel(this);
		}
	}

	
	
	
}
