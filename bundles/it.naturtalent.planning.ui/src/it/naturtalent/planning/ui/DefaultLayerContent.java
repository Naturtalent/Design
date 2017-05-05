package it.naturtalent.planning.ui;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planung.LayerContent;


public class DefaultLayerContent extends LayerContent
{

	private DefaultLayerContentComposite composite;
	
	public DefaultLayerContent(String layerName)
	{
		super(layerName);		
	}

	@Override
	public Composite getContentComposite(Composite parent)
	{
		composite = new DefaultLayerContentComposite(parent, SWT.NONE); 
		return(composite);				
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		String [][] data = null;
		
		if(shapes != null)
		{
			int i = 0;
			for(Shape shape : shapes)
			{
				String idx = new Integer(++i).toString();
				//String type = shape.getShapeType();
				String [] line = {idx,shape.getShapeLabel()};
				data = ArrayUtils.add(data, line);
			}
			composite.initComposite(data);
		}
	}

	

	
	
	
}
