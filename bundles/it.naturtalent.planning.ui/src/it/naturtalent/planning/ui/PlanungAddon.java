package it.naturtalent.planning.ui;


import it.naturtalent.e4.project.ProjectDataAdapterRegistry;
import it.naturtalent.planung.AlternativeRegistry;
import it.naturtalent.planung.DefaultAlternative;
import it.naturtalent.planung.DefaultAlternativeFactory;
import it.naturtalent.planung.IAlternativeFactoryRegistry;
import it.naturtalent.planung.IItemStyleRepository;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.LayerContent;
import it.naturtalent.planung.LayerContent.DefaultContentNames;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;

public class PlanungAddon
{	
	@Optional @Inject IAlternativeFactoryRegistry alternativeFactoryRepository;
	@Optional @Inject ILayerContentRepository layerContentRepository;
	@Optional @Inject IEventBroker eventBroker;
	@Inject @Optional IEclipseContext context;
	@Optional @Inject IItemStyleRepository itemStyleRepository;
	
	
	@PostConstruct
	public void init()
	{
		if(layerContentRepository != null)
		{
			String contentName = DefaultContentNames.LineLength.name();
			layerContentRepository.registerLayerContentFactory(contentName, new LineLengthContentFactory());
			
			contentName = DefaultContentNames.ShapeTypes.name();
			layerContentRepository.registerLayerContentFactory(contentName, new DefaultContentFactory());
			
			contentName = DefaultContentNames.RectangleArea.name();
			layerContentRepository.registerLayerContentFactory(contentName, new AreaContentFactory());
			
			contentName = DefaultContentNames.ShapeCounter.name();
			layerContentRepository.registerLayerContentFactory(contentName, new CounterContentFactory());
			
			// Projektdataadapter Planung
			PlanningProjectAdapter planningProjectAdapter = ContextInjectionFactory.make(PlanningProjectAdapter.class, context);
			ProjectDataAdapterRegistry.addAdapter(planningProjectAdapter);
			
			// Factory der Defaultalternative registrieren		
			DefaultAlternativeFactory alternativeFactory = new DefaultAlternativeFactory();
			alternativeFactory.setLayerContentRepository(layerContentRepository);
			alternativeFactoryRepository.registerAlternativeFatory(DefaultAlternative.DEFAULT_ALTERNATIVE_NAME, alternativeFactory);
			
			// DefaultItemStyle
			DefaultItemStyleFactory defaultItemStyleFactory = new DefaultItemStyleFactory();
			itemStyleRepository.registerItemStyleFactory(DefaultItemStyle.DEFAULTITEMSTYLE, defaultItemStyleFactory);
		}
	}
}
