package it.naturtalent.design.ui;

import java.util.List;

/**
 * Interface des ShapeFactoryRepositories
 * 
 * @author dieter
 *
 */
public interface ILayerShapeFactoryRepository
{
	// gibt alle registrierten ShapeFactories in einer Liste zurueck
	public List<ILayerShapeFactory>getLayerShapeFactories();
	
	// sucht ShapeFactory ueber den Label
	public ILayerShapeFactory getLayerShapeFactory(String label);
	
}
