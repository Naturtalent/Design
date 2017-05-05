package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.IItemStyle;
import it.naturtalent.planung.IItemStyleFactory;

public class DefaultItemStyleFactory  implements IItemStyleFactory
{
	@Override
	public IItemStyle createItemStyle()
	{		
		return new DefaultItemStyle();
	}

}
