package it.naturtalent.design.ui;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.Layer;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.design.ui.dialogs.MasslineLengthDialog;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.libreoffice.DrawDocumentUtils;
import it.naturtalent.libreoffice.DrawShape;
import it.naturtalent.libreoffice.DrawShape.SHAPEPROP;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class MassstabShapeAdapter extends DefaultShapeAdapter
{
	
	// MeasurementUnits (Auswahl http://api.libreoffice.org/docs/idl/ref/MeasureUnit_8idl.html)
	public final static Short MEASUREUNIT_MM_100TH = 0; 	// hundertstel mm
	public final static Short MEASUREUNIT_MM_10TH = 1;	 	// zehntel mm
	public final static Short MEASUREUNIT_MM = 2;		 	// mm
	public final static Short MEASUREUNIT_CM = 3;	 		// cm
	public final static Short MEASUREUNIT_M = 10;	 		// m
	
	// Massstab Zaehler und Nenner
	private Integer scaleNumerator = 1;
	private Integer scaleDenominator = 50;
	
	// Die Masseinheit
	public Short measureUnit = MEASUREUNIT_M;
	
	private static final String MASSLINELAYER = "Maßlinien"; 
	
	
	// Faktor (m) (Shapes werden original in 1/100 mm gespeichert)
	public static BigDecimal measureFactor = new BigDecimal(100000.0);
	
	@Override
	public void push(DrawDocument drawDocument, Layer layer)
	{				
		// eine Referenzlinie generieren und im der selektierten 'page' anzeigen
		drawDocument.selectLayer(MASSLINELAYER);
		Rectangle bound = DrawDocumentUtils.getCurrentPageRandomBound(drawDocument.getxComponent());
		DrawShape drawShape = new DrawShape(bound.x, bound.y, bound.width, bound.height);
		drawShape.setDrawShapeType(SHAPEPROP.Line);
		drawShapes.clear();
		drawShapes.add(drawShape);
		drawDocument.setLayerShapes(MASSLINELAYER, drawShapes);
		
		// eine Laengenangabe zur Referenzlinie definieren
		MasslineLengthDialog lengthDialog = new MasslineLengthDialog(
				Display.getDefault().getActiveShell());
		if(lengthDialog.open() == MasslineLengthDialog.OK)
		{
			BigDecimal lineLength = (BigDecimal) drawDocument.getDrawShapeProperty(drawShape);
			BigDecimal dialogLength = lengthDialog.getDialogLength();
			
			BigDecimal doubleMass = dialogLength.multiply(measureFactor);
			doubleMass = doubleMass.divide(lineLength, BigDecimal.ROUND_UP);
			scaleDenominator = doubleMass.intValue(); 

			// Setting-Daten an das DrawDocument uebertragen
			/*
			drawDocument.setDocumentSettings(DrawDocumentUtils.SCALENUMERATOR, scaleNumerator);
			drawDocument.setDocumentSettings(DrawDocumentUtils.SCALEDENOMINATOR, scaleDenominator);
			drawDocument.setDocumentSettings(DrawDocumentUtils.MEASUREUNIT, measureUnit);
			*/
			
			setScaleSettings(drawDocument);
		}
		
		// Referenzlinie wieder entfernen
		drawDocument.removeShapes(drawShapes);
	}
	
	// Scaledaten als Settings im DrawDocument speichern
	public void setScaleSettings(DrawDocument drawDocument)
	{
		drawDocument.setDocumentSettings(DrawDocumentUtils.SCALENUMERATOR, scaleNumerator);
		drawDocument.setDocumentSettings(DrawDocumentUtils.SCALEDENOMINATOR, scaleDenominator);
		drawDocument.setDocumentSettings(DrawDocumentUtils.MEASUREUNIT, measureUnit);					
	}

	public Integer getScaleDenominator()
	{
		return scaleDenominator;
	}

	public void setScaleDenominator(Integer scaleDenominator)
	{
		this.scaleDenominator = scaleDenominator;
	}

	
	
	
	

}