package it.naturtalent.design.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.libreoffice.DrawDocumentUtils;
import it.naturtalent.libreoffice.DrawShape;
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
	// unspezifischer Shape Labelprovider   
	private class ShapeLabelProvider extends LabelProvider
	{
		@Override
		public String getText(Object element)
		{
			if (element instanceof DrawShape)
			{
				DrawShape shape = (DrawShape) element;
				return shape.type;				
			}
			return super.getText(element);
		}
	}
	
	@Override
	public void init(DrawDocument drawDocument, Layer layer, TableViewer tableViewer)
	{
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

}
