package it.naturtalent.planning.ui.actions;

import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.libreoffice.odf.shapes.IOdfElementFactoryRegistry;
import it.naturtalent.planning.ui.parts.PlanungMasterComposite;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.PlanningModel;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;

public abstract class AbstractPlanungAction  extends Action
{
	protected IEventBroker eventBroker;
	
	protected DrawDocument drawDocument;
	
	protected PlanungMasterComposite planungMasterComposite;
	
	protected PlanningModel planningModel;
	
	protected IAlternativeFactoryRegistry alternativeFactoryRepository;
	protected ILayerContentRepository layerContentRepository;
	protected IOdfElementFactoryRegistry odfElementFactoryRegistry;
	

	public IEventBroker getEventBroker()
	{
		return eventBroker;
	}

	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}

	public void setDrawDocument(DrawDocument drawDocument)
	{
		this.drawDocument = drawDocument;
	}

	public PlanungMasterComposite getPlanungMasterComposite()
	{
		return planungMasterComposite;
	}

	public void setPlanungMasterComposite(
			PlanungMasterComposite planungMasterComposite)
	{
		this.planungMasterComposite = planungMasterComposite;
	}

	public void setPlanningModel(PlanningModel planningModel)
	{
		this.planningModel = planningModel;
	}

	public void setLayerContentRepository(
			ILayerContentRepository layerContentRepository)
	{
		this.layerContentRepository = layerContentRepository;
	}

	public void setAlternativeFactoryRepository(
			IAlternativeFactoryRegistry alternativeFactoryRepository)
	{
		this.alternativeFactoryRepository = alternativeFactoryRepository;
	}

	public IOdfElementFactoryRegistry getOdfElementFactoryRegistry()
	{
		return odfElementFactoryRegistry;
	}

	public void setOdfElementFactoryRegistry(
			IOdfElementFactoryRegistry odfElementFactoryRegistry)
	{
		this.odfElementFactoryRegistry = odfElementFactoryRegistry;
	}

	
	
	
}
