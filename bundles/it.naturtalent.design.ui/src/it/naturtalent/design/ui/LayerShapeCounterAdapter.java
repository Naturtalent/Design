package it.naturtalent.design.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.libreoffice.DrawShape;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class LayerShapeCounterAdapter extends DefaultShapeAdapter
{
	
	// unspezifischer Shape Labelprovider   
	private class ShapeLabelProvider extends LabelProvider
	{
		@Override
		public String getText(Object element)
		{
			if (element instanceof DrawShape)
			{
				DrawShape shape = (DrawShape) element;
				return (String) shape.getData();				
			}
			return super.getText(element);
		}
	}

	@Override
	public void pull(String pageName, Layer layer, DrawDocument drawDocument)
	{		
		super.pull(pageName, layer, drawDocument);
		
		String sum = "Summe: " + drawShapes.size();
		String [] sumline = new String [] {sum};
	
		drawShapes.clear();
		DrawShape drawShape = new DrawShape();	
		drawShape.setData(sum);
		drawShapes.add(drawShape);
	}

	@Override
	public void showLayerContent(TableViewer tableViewer)
	{		
		super.showLayerContent(tableViewer);
		tableViewer.setLabelProvider(new ShapeLabelProvider());
	}

	
	
	

	/*
	@Override	
	public void init(DrawDocument drawDocument, Layer layer,TableViewer tableViewer)
	{
		String layerName = layer.getName();
		String sysLayerName = DrawDocumentUtils.getDrawLayerName(layerName);
		layerName = (sysLayerName != null) ? sysLayerName : layerName; 

		// Shapes ueber aller pages einlesen
		List<DrawShape>drawShapes = new ArrayList<DrawShape>();
		List<String>pageNames = drawDocument.getAllPages(false);
		for(String pageName : pageNames)
		{			
			List<DrawShape>pageDrawShapes = drawDocument.getLayerShapes(pageName, layerName);
			drawShapes.addAll(pageDrawShapes);
		}
		
		String sum = "Summe: " + drawShapes.size();
		String [] sumline = new String [] {sum};
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		//tableViewer.setLabelProvider(new ShapeLabelProvider());
		tableViewer.setInput(sumline);		

		
	}
	*/

}
