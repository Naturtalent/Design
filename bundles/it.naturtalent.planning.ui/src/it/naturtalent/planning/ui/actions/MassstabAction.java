package it.naturtalent.planning.ui.actions;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.PageHelper;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.Layer.DefaultLayerNames;
import it.naturtalent.libreoffice.draw.LineShape;
import it.naturtalent.libreoffice.draw.Scale;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.libreoffice.draw.Shape.ShapeType;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planning.ui.dialogs.ScaleSettingDialog;
import it.naturtalent.planning.ui.parts.PlanungContentProvider;
import it.naturtalent.planning.ui.parts.PlanungDetailsComposite;
import it.naturtalent.planning.ui.parts.PlanungMasterComposite;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.ILayerContentRepository;
import it.naturtalent.planung.LayerContent;
import it.naturtalent.planung.Page;
import it.naturtalent.planung.PlanningMaster;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class MassstabAction extends AbstractPlanungAction
{

	private Log log = LogFactory.getLog(this.getClass());
	
	
	public MassstabAction()
	{
		super();
		setImageDescriptor(Icon.ICON_MASSSTAB.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText(Messages.MassstabAction_ToolTipMassstab);
	}

	@Override
	public void run()
	{
		Page page = null;
		
		if(drawDocument != null)
		{
			// Layer 'Masslinien' aktivieren
			Layer layer = drawDocument.getLayer(DefaultLayerNames.Masslinien.getType());			
			layer.setLocked(false);
			layer.setVisible(true);
			
			// alle vorhandenen Shapes loeschen
			layer.deleteShapes();
						
			drawDocument.selectLayer(layer);
			createReferenceShape(layer);
			
			// Dialog Massstab
			ScaleSettingDialog dialog = new ScaleSettingDialog(Display.getDefault().getActiveShell());
			dialog.create();

			// das Pendant der im Dokument selektierte Seite im selektierten Design suchen
			Design design = planungMasterComposite.getCurDesign();
			if(design != null)	
			{
				// Page ueber Namen ermitteln				
				String pageName = drawDocument.getPageName();
				Page [] pages = design.getPages();
				for(Page pageSeek : pages)
				{
					if(StringUtils.equals(pageName, pageSeek.getName()))
					{
						page = pageSeek;
						break;
					}
				}
				
				if(page == null)
				{
					log.error("keine Seite in Design definiert");
					return;
				}
				
				// der in Page gespeicherte Masstab an den Dialog uebergeben
				dialog.setDenominator(page.getScaleDenominator());
				
				// Detailcomposite auf Laengenmessung umstellen
				PlanungDetailsComposite detailPage = planungMasterComposite.getDetailPage();
				ILayerContentRepository layerContentRepository = detailPage.getLayerContentRepository();			
				detailPage.setLayerContent(LayerContent.DefaultContentNames.LineLength.name());
			}
		
			if (dialog.open() == ScaleSettingDialog.OK)
			{
				Scale scale = drawDocument.getScale();
				
				// Massstab (Direkteingabe) vom Dialog abrufen
				Integer denominator = dialog.getDenominator();
				if(denominator != null)
				{
					// direkte Eingabe des Massstabes
					scale.setScaleDenominator(denominator);
					
					// im DrawDokument einstellen
					scale.pushScaleProperties();
					
					//design.setScaleDenominator(denominator);
					
					// Massstab in Page aktualisieren
					page.setScaleDenominator(denominator);
					planungMasterComposite.initPage();
					
					updateAndSavePlanningModel(design);
					
				}
				else
				{
					// indirekte Eingabe ueber Linienlaenge in 'Masslinien'
					List<Shape> shapes = layer.pullLayerShapes();
					
					if(shapes.isEmpty())
					{
						MessageDialog.openInformation(Display.getDefault()
								.getActiveShell(), Messages.MassstabAction_NoScaleFoundTitle,
								Messages.MassstabAction_NoReferenceLine);					
					}
					else
					{

						for (Shape shape : shapes)
						{
							if (shape instanceof LineShape)
							{
								LineShape lineShape = (LineShape) shape;
								BigDecimal lineLength = lineShape.getLength();
								BigDecimal dialogLenght = dialog
										.getLineLenght();

								// Divisor berechnen, in Scale speichern und im
								// DrawDokument (via push) einstellen
								scale.calculateScaleByLineLength(lineLength,
										dialogLenght);

								// design.setScaleDenominator(scale.getScaleDenominator());

								page.setScaleDenominator(scale
										.getScaleDenominator());
								planungMasterComposite.initPage();

								updateAndSavePlanningModel(design);
							}
						}
					}
				}
			}
			
			// alle vorhandenen Shapes loeschen
			layer.deleteShapes();
		}
	}
	
	
	private void updateAndSavePlanningModel(Design design)
	{
		PlanungContentProvider contentProvider = (PlanungContentProvider) planungMasterComposite
				.getTreeViewer().getContentProvider();
		Alternative alternative = (Alternative) contentProvider.getParent(design);
				
		if(planningModel == null)
			planningModel = planungMasterComposite.getModel();
		
		PlanningMaster planningMaster = planningModel.getData(alternative.getKey());		
		planningModel.update(planningMaster);
		planningModel.saveModel();		
	}
		
	private void createReferenceShape(Layer layer)
	{		
		//Rectangle bound = new Rectangle(2000,2000,5000,5000);
		Rectangle bound = drawDocument.getPageBound();
		
		try
		{
			Shape refShape = new Shape(layer, bound, ShapeType.LineShape);			
			refShape.select(drawDocument);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
