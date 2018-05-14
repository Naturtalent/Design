package it.naturtalent.design.ui;


public class LayerShapeLengthAdapterFactory implements ILayerShapeFactory
{
	@Override
	public String getLabel()
	{		
		return "Längen";
	}

	@Override
	public ILayerShapeAdapter createShapeAdapter()
	{		
		return new LayerShapeLengthAdapter();
	}

}
