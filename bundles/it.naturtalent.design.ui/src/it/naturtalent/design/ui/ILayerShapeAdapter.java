package it.naturtalent.design.ui;

import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.libreoffice.draw.DrawDocument;

public interface ILayerShapeAdapter
{
	public void init(DrawDocument drawDocument, Layer layer, TableViewer tableViewer);
}
