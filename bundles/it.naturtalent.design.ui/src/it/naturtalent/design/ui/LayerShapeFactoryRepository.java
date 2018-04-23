package it.naturtalent.design.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Alle ShapeAdapter werden zentral in diesem Repository gespeichert.
 * 
 * @author dieter
 *
 */
public class LayerShapeFactoryRepository implements ILayerShapeFactoryRepository
{
	private static List<ILayerShapeFactory>layerShapeFactories = new ArrayList<ILayerShapeFactory>();

	@Override
	public List<ILayerShapeFactory> getLayerShapeFactories()
	{		
		return layerShapeFactories;
	}

	@Override
	public ILayerShapeFactory getLayerShapeFactory(String label)
	{
		List<ILayerShapeFactory>factories = getLayerShapeFactories();
		
		for(ILayerShapeFactory factory : factories)
		{
			if(StringUtils.equals(label, factory.getLabel()))
				return factory;
		}
		
		return null;
	}
	
	
}
