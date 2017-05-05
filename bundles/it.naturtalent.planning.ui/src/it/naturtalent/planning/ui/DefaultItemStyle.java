package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.ItemStyle;

public class DefaultItemStyle extends ItemStyle
{
	public static final String DEFAULTITEMSTYLE = "defaultitemstyle";

	public DefaultItemStyle()
	{
		setName(DEFAULTITEMSTYLE);
		setLineWidth(new Integer(0));
		setLineColor(new Integer(0x000000));
	}
}
