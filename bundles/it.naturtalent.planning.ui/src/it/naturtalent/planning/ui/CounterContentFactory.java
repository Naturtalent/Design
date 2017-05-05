package it.naturtalent.planning.ui;

import it.naturtalent.planung.ILayerContentFactory;
import it.naturtalent.planung.LayerContent;

public class CounterContentFactory implements ILayerContentFactory
{
	@Override
	public LayerContent createLayerContent(String contentClass)
	{		
		return new CounterLayerContent(contentClass);
	}

}
