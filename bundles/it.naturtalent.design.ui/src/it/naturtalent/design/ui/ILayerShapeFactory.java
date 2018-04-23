package it.naturtalent.design.ui;

public interface ILayerShapeFactory
{
	public String getLabel();
	
	public ILayerShapeAdapter createShapeAdapter();
	
}
