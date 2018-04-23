 
package it.naturtalent.design.ui;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import it.naturtalent.e4.project.INtProjectPropertyFactory;
import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;

public class DesignAddon
{

	// das zentrale ProjectPropertyRepository
	private @Inject INtProjectPropertyFactoryRepository ntProjektDataFactoryRepository;

	// Zentrales Repository aller ShapeAdapter
	private @Inject ILayerShapeFactoryRepository layerShapeRepository;
	
	
	@Inject
	@Optional
	public void applicationStarted(
			@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event)
	{
		if(ntProjektDataFactoryRepository != null)
		{
			// NtProjekt-Designeigenschaft
			List<INtProjectPropertyFactory>ntDataFactories = ntProjektDataFactoryRepository.getAllProjektDataFactories();
			ntDataFactories.add(new DesignProjectPropertyFactory());
		}
		
		if(layerShapeRepository != null)
		{
			// diverse allgemeine ShapeAdapter registrieren
			List<ILayerShapeFactory>layerLayoutFactories = layerShapeRepository.getLayerShapeFactories();
			layerLayoutFactories.add(new LayerShapeCounterAdapterFactory());
		}
		
	}
	

}
