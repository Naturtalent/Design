package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.Layer.DefaultLayerNames;
import it.naturtalent.libreoffice.draw.LineShape;
import it.naturtalent.libreoffice.draw.Scale;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planning.ui.dialogs.ScaleSettingDialog;
import it.naturtalent.planning.ui.dialogs.TestDialog;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;


public class SettingsAction extends AbstractPlanungAction
{

	
	private static final String LINUX_TESTURL_STICK="/media/dieter/TOSHIBA/planung.odg";
	
	
	public SettingsAction()
	{
		super();
		setImageDescriptor(Icon.COMMAND_EDIT.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("Einstellungen");
	}
	
	@Override
	public void run()
	{
		
		TestDialog dialog = new TestDialog(Display.getDefault().getActiveShell());
		dialog.open();
		
		 
		
		/*
		
		if(drawDocument != null)
		{
			// Layer 'Masslinien' aktivieren
			Layer layer = drawDocument.getLayer(DefaultLayerNames.Masslinien.getType());			
			layer.setLocked(false);
			layer.setVisible(true);
			drawDocument.selectLayer(layer);
			//createReferenceShape(drawDocument, layer);
					
			// Dialog Massstab
			ScaleSettingDialog dialog = new ScaleSettingDialog(Display.getDefault().getActiveShell());
			if (dialog.open() == ScaleSettingDialog.OK)
			{
				Scale scale = drawDocument.getScale();
				Integer denominator = dialog.getDenominator();
				if(denominator != null)
				{
					// direkte Eingabe des Massstabes
					scale.setScaleDenominator(denominator);
				}
				else
				{
					// indirekte Eingabe ueber Linienlaenge in 'Masslinien' 					
					List<Shape> shapes = layer.pullLayerShapes();
					for (Shape shape : shapes)
					{
						if(shape instanceof LineShape)
						{
							LineShape lineShape = (LineShape) shape;
							BigDecimal lineLength = lineShape.getLength();
							scale.calculateScaleByLineLength(lineLength, dialog.getLineLenght());						
							return;
						}
					}
					
					MessageDialog.openInformation(Display.getDefault()
							.getActiveShell(), "Maßstab",
							"keine Referenzlinie gefunden");
					
				}
			}				
		}
		*/
	}
	
	/*
	private void createReferenceShape(DrawDocument drawDocument, Layer layer)
	{
		
		//Rectangle bound = new Rectangle(2000,2000,5000,5000);
		Rectangle bound = drawDocument.getPageBound();
		
		try
		{
			Shape refShape = new Shape(drawDocument, bound, ShapeType.LineShape);
			layer.pushShape(refShape);
			refShape.select(drawDocument);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
}
