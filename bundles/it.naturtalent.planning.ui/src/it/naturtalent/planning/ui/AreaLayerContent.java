package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planung.LayerContent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class AreaLayerContent extends LayerContent
{

	private LengthTableComposite composite;
	
	public AreaLayerContent(String layerName)
	{
		super(layerName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite getContentComposite(Composite parent)
	{
		composite = new AreaTableComposite(parent, SWT.NONE);
		return(composite);				
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		String [][] data = null;
		BigDecimal length = null;
				
		if(shapes != null)
		{
			int i = 0;
			for(Shape shape : shapes)
			{
				/*
				// einfaches Rechteck
				if(shape instanceof LineShape)
				{
					LineShape lineShape = (LineShape) shape;
					length = lineShape.getLength();					
				}
				
				
				if(scaleDenominator != null)
				{
					length = length.multiply(scaleDenominator);
					length = length.divide(Scale.measureFactor, BigDecimal.ROUND_HALF_UP);
				}
				
				//String stgLength = ((length != null) ? length.toString() : new Double("0").toString());
				String stgLength = formatLength (length);
				String idx = new Integer(++i).toString();					
				String [] line = {idx,stgLength,shape.getShapeLabel()};
				data = ArrayUtils.add(data, line);
				*/

			}
			composite.initComposite(data);
		}
	}

	private String formatLength (BigDecimal length)
	{
		double bigDecimal = length.doubleValue();		
		DecimalFormat df = new DecimalFormat( "#,###,###,##0.0" );		
		return df.format(bigDecimal);
	}

	
	
	
}
