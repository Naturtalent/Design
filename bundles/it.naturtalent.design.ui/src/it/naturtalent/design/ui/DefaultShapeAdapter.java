package it.naturtalent.design.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.libreoffice.DrawDocumentUtils;
import it.naturtalent.libreoffice.DrawShape;
import it.naturtalent.libreoffice.DrawShape.SHAPEPROP;
import it.naturtalent.libreoffice.draw.DrawDocument;

/**
 * Adapter zeigt den Standard-Shapeinhalt (Klasse des Shapes), wenn keine spezifischen
 * Adapter definiert sind.
 *    
 * @author dieter
 *
 */
public class DefaultShapeAdapter implements ILayerShapeAdapter
{
	protected DrawDocument drawDocument;
	
	protected List<DrawShape>drawShapes = new ArrayList<DrawShape>();
		
	/**
	 * LabelProvider
	 * 
	 * @author dieter
	 *
	 */
	private class ShapeLabelProvider extends LabelProvider
	{
		@Override
		public String getText(Object element)
		{
			if (element instanceof DrawShape)
			{
				DrawShape drawShape = (DrawShape) element;	
				return (String) drawDocument.getDrawShapeProperty(drawShape);
				//return shape.type;				
			}
			return super.getText(element);
		}
	}

	@Override
	public void pull(String pageName, Layer layer, DrawDocument drawDocument)
	{
		this.drawDocument = drawDocument;
		String layerName = layer.getName();
		String sysLayerName = DrawDocumentUtils.getDrawLayerName(layerName);
		layerName = (sysLayerName != null) ? sysLayerName : layerName; 

		drawShapes.clear();
		List<DrawShape>pageDrawShapes = drawDocument.getLayerShapes(pageName, layerName);
		drawShapes.addAll(pageDrawShapes);
	}

	/*
	@Override
	public void pull(Layer layer, DrawDocument drawDocument)
	{
		this.drawDocument = drawDocument;
		String layerName = layer.getName();
		String sysLayerName = DrawDocumentUtils.getDrawLayerName(layerName);
		layerName = (sysLayerName != null) ? sysLayerName : layerName; 

		drawShapes.clear();
		List<String>pageNames = drawDocument.getAllPages(false);
		for(String pageName : pageNames)
		{			
			List<DrawShape>pageDrawShapes = drawDocument.getLayerShapes(pageName, layerName);
			drawShapes.addAll(pageDrawShapes);
		}		
	}
	*/

	@Override
	public void push(DrawDocument drawDocument, Layer layer)
	{
		if(layer == null)
		{
			
		}
	}

	@Override
	public void showLayerContent(TableViewer tableViewer)
	{
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ShapeLabelProvider());
		tableViewer.setInput(drawShapes);			
	}



	/*
	@Override
	public void init(DrawDocument drawDocument, Layer layer, TableViewer tableViewer)
	{
		this.drawDocument = drawDocument;
		String layerName = layer.getName();
		String sysLayerName = DrawDocumentUtils.getDrawLayerName(layerName);
		layerName = (sysLayerName != null) ? sysLayerName : layerName; 

		List<DrawShape>drawShapes = new ArrayList<DrawShape>();
		List<String>pageNames = drawDocument.getAllPages(false);
		for(String pageName : pageNames)
		{			
			List<DrawShape>pageDrawShapes = drawDocument.getLayerShapes(pageName, layerName);
			drawShapes.addAll(pageDrawShapes);
		}
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ShapeLabelProvider());
		tableViewer.setInput(drawShapes);		
	}
	*/

	@Override
	public List<DrawShape> getShapes()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
