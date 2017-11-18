package it.naturtalent.design.ui;

import it.naturtalent.libreoffice.draw.ILayerLayout;
import it.naturtalent.libreoffice.draw.ILayerLayoutFactory;

public class DefaultTextLayerFactory implements ILayerLayoutFactory
{

	private String name = "text";
	
	@Override
	public String getName()
	{		
		return name;
	}

	@Override
	public ILayerLayout createLayout()
	{		
		DefaultTextLayer layerLayout = new DefaultTextLayer();
		layerLayout.setName(name);
		return layerLayout;
	}

}
