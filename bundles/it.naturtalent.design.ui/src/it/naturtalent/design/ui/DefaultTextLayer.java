package it.naturtalent.design.ui;

import it.naturtalent.libreoffice.draw.ILayerLayout;

public class DefaultTextLayer implements ILayerLayout
{
	
	private String name;
	
	@Override
	public void addShape()
	{
		System.out.println("Add Shape");

	}

	@Override
	public String getName()
	{		
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;		
	}

	@Override
	public void showDetails()
	{
		System.out.println("show Details");
		
	}

}
