package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.LineShape;
import it.naturtalent.libreoffice.draw.OpenBezierShape;
import it.naturtalent.libreoffice.draw.PolyLineShape;
import it.naturtalent.libreoffice.draw.Scale;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planung.LayerContent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


public class LineLenghtLayerContent extends LayerContent
{

	private LengthTableComposite composite;
	
	
	public LineLenghtLayerContent(String layerName)
	{
		super(layerName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite getContentComposite(Composite parent)
	{
		composite = new LengthTableComposite(parent, SWT.NONE);		
		((LengthTableComposite)composite).setEventBroker(eventBroker);
		return(composite);				
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		String [][] data = null;
		BigDecimal length;
		BigDecimal summary = new BigDecimal(0);
				
		if(shapes != null)
		{
			int i = 0;
			for(Shape shape : shapes)
			{
				length = null;
				
				// einfacher Linienzug
				if(shape instanceof LineShape)
				{
					LineShape lineShape = (LineShape) shape;
					length = lineShape.getLength();							
				}
				
				// Polygonzug
				if(shape instanceof PolyLineShape)
				{
					PolyLineShape polyLineShape = (PolyLineShape) shape;	
					length = polyLineShape.getLength();					
				}

				// BezierLinienzug
				if(shape instanceof OpenBezierShape)
				{
					OpenBezierShape bezierLineShape = (OpenBezierShape) shape;	
					length = bezierLineShape.getLength();
					
					/*
					Point[][] bezierPoints = bezierLineShape.getBezierPoints();					
					Layer layer = drawDocument.getLayer(DefaultLayerNames.Masslinien.getType());
					PolyLineShape polyLineShape = new PolyLineShape(layer, bound);
					*/
					
					
					
				}

				if (length != null)
				{
					if (scaleDenominator != null)
					{
						length = length.multiply(scaleDenominator);
						length = length.divide(Scale.measureFactor,
								BigDecimal.ROUND_HALF_UP);
						summary = summary.add(length);
					}

					// Index und Laenge im Datenbereich zwischenspeichern					
					String stgLength = formatLength(length);
					String idx = new Integer(++i).toString();
					composite.setData(idx, shape);

					// ein Zeile generieren und Array hinzufuegen
					String[] line ={ idx, stgLength, shape.getShapeLabel() };
					data = ArrayUtils.add(data, line);
				}

			}
			
			// Summenzeile generieren
			String sum = formatLength (summary);
			String[] line ={"Summe:", sum, "" };
			data = ArrayUtils.add(data, line);

			// Contentarray an Composite uebergeben
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
