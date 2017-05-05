package it.naturtalent.planning.ui;

import it.naturtalent.planung.ILayerContentFactory;
import it.naturtalent.planung.LayerContent;

public class AreaContentFactory implements ILayerContentFactory
{
	@Override
	public LayerContent createLayerContent(String contentClass)
	{		
		return new AreaLayerContent(contentClass);
	}

}
