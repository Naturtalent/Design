package it.naturtalent.planning.ui;

import it.naturtalent.planung.ILayerContentFactory;
import it.naturtalent.planung.LayerContent;

public class DefaultContentFactory implements ILayerContentFactory
{
	@Override
	public LayerContent createLayerContent(String contentClass)
	{		
		return new DefaultLayerContent(contentClass);
	}

}
