package it.naturtalent.design.ui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.libreoffice.DrawShape;
import it.naturtalent.libreoffice.draw.DrawDocument;

public interface ILayerShapeAdapter
{
	//public void init(DrawDocument drawDocument, Layer layer, TableViewer tableViewer);
	
	
	/**
	 * Die Shapes eines Layer werden 'ausgelesen'.
	 *  
	 * @param layer
	 * @param drawDocument
	 */
	public void pull(String pageName, Layer layer, DrawDocument drawDocument);
	
	/**
	 * Die Shapes des Adapters werden in das DrawDocument gezeichnet.
	 * Mit layer = null wird der aktuelle Layer des Documents als Ziel definiert.
	 *  
	 * @param layer
	 * @param drawDocument
	 */	
	public void push(DrawDocument drawDocument, Layer layer);
	
	/**
	 * Rueckgabe einer Liste von DrawShapes, die den Layerinhalt repraesentieren.
	 * Je nach konkreter Implementierung koennen hier Filterungen vorgenommen werden.
	 * 
	 * @return
	 */
	public List<DrawShape>getShapes();
	
	
	/**
	 * Zeigt den Layerinhalt in einem TableViewer an.
	 * 
	 * @param tableViewer
	 */
	public void showLayerContent(TableViewer tableViewer);
	
	
}
