package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planung.LayerContent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class CounterLayerContent extends LayerContent
{

	private CounterTableComposite composite;
	
	public CounterLayerContent(String layerName)
	{
		super(layerName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite getContentComposite(Composite parent)
	{
		composite = new CounterTableComposite(parent, SWT.NONE);
		((CounterTableComposite)composite).setEventBroker(eventBroker);
		return(composite);				
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		String [][] data = null;
		Integer summary = new Integer(0);
				
		if(shapes != null)
		{
			String stgCount = new Integer(shapes.size()).toString();
			//String [] line = {"",stgCount,""};
			//data = ArrayUtils.add(data, line);
			
			int i = 0;
			for(Shape shape : shapes)
			{		
				String idx = new Integer(++i).toString();	
				composite.setData(idx, shape);
				String [] line = {idx,"",shape.getShapeLabel()};
				data = ArrayUtils.add(data, line);
				summary++;
			}
			
			// Summenzeile generieren			
			//String[] line ={"Summe:", summary.toString(), "" };
			//data = ArrayUtils.add(data, line);

			composite.initComposite(data);
		}
	}

}
