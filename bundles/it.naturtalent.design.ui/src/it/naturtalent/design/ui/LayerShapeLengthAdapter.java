package it.naturtalent.design.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;

import it.naturtalent.libreoffice.DrawDocumentUtils;
import it.naturtalent.libreoffice.DrawShape;
import it.naturtalent.libreoffice.DrawShape.SHAPEPROP;

/**
 * Adapter liest die Linien eines Layers und zeigt diese in einem TableViewer an.
 * 
 * @author dieter
 *
 */
public class LayerShapeLengthAdapter extends DefaultShapeAdapter
{
	
	// Labelprovider zur Anzeige von Laengen   
	private class ShapeLabelLengthProvider extends LabelProvider
	{
		@Override
		public String getText(Object element)
		{
			if (element instanceof DrawShape)
			{
				DrawShape shape = (DrawShape) element;
				shape.setDrawShapeType(SHAPEPROP.Line);
				BigDecimal length = (BigDecimal) drawDocument.getDrawShapeProperty(shape);
				
				// Massstab vom Dokument lesen und in BigDecimal wandeln
				Integer scaleDenominator = (Integer) drawDocument
						.getDocumentStettings(
								DrawDocumentUtils.SCALEDENOMINATOR);
				BigDecimal denominator = new BigDecimal(scaleDenominator);
				
				// Laenge in den Massstab umrechnen
				length = length.multiply(denominator);				
				length = length.divide(MassstabShapeAdapter.measureFactor,
						BigDecimal.ROUND_HALF_UP);

				// Laenge formartiert als String zuruekgeben
				return formatLength (length);				
			}
			return super.getText(element);
		}
	}

	@Override
	public void showLayerContent(TableViewer tableViewer)
	{		
		super.showLayerContent(tableViewer);
		tableViewer.setLabelProvider(new ShapeLabelLengthProvider());
	}

	private String formatLength (BigDecimal length)
	{
		double bigDecimal = length.doubleValue();		
		DecimalFormat df = new DecimalFormat( "#,###,###,##0.0" );		
		return df.format(bigDecimal);
	}

}
