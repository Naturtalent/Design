package it.naturtalent.design.ui;


public class LayerShapeCounterAdapterFactory implements ILayerShapeFactory
{
	@Override
	public String getLabel()
	{		
		return "Zähler";
	}

	@Override
	public ILayerShapeAdapter createShapeAdapter()
	{		
		return new LayerShapeCounterAdapter();
	}

}
