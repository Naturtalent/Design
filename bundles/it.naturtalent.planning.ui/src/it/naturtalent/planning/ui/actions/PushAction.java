 package it.naturtalent.planning.ui.actions;

import java.io.File;
import java.util.List;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.libreoffice.ImportFilter;
import it.naturtalent.libreoffice.ImportGraphicShape;
import it.naturtalent.libreoffice.draw.DrawOdfElementModel;
import it.naturtalent.libreoffice.draw.ImportODF;
import it.naturtalent.libreoffice.draw.ImportSVGShape;
import it.naturtalent.libreoffice.draw.Layer;
import it.naturtalent.libreoffice.draw.OpenBezierShape.PolygonFlags;
import it.naturtalent.libreoffice.odf.shapes.IOdfElementFactory;
import it.naturtalent.libreoffice.odf.shapes.IOdfElementFactoryRegistry;
import it.naturtalent.libreoffice.draw.Shape;
import it.naturtalent.planning.ui.Activator;
import it.naturtalent.planning.ui.parts.PlanungContentProvider;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.IAlternativeFactory;
import it.naturtalent.planung.PlanningItem;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.odftoolkit.odfdom.pkg.OdfElement;

public class PushAction extends AbstractPlanungAction
{

	private Point[][] coords;
	private PolygonFlags[][] flags;
	private Point [] polyLine;
	
	public PushAction()
	{
		super();
		setImageDescriptor(Icon.ICON_PUSH.getImageDescriptor(IconSize._16x16_DefaultIconSize));
		setToolTipText("Daten an Design senden");
	}

	@Override
	public void run()
	{
		PlanningItem planningItem = planungMasterComposite.getCurItem();
		if(planningItem != null)
		{			
			Layer layer = drawDocument.getLayer(planningItem.getLayer());
			Design curDesign = planungMasterComposite.getCurDesign();
			Alternative alternative = (Alternative) ((PlanungContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider()).getParent(curDesign);		
			
			IOdfElementFactory odfElementFactory = odfElementFactoryRegistry
					.getOdfElementFactory(alternative.getName()); 
			
			if(odfElementFactory != null)
			{
				DrawOdfElementModel odfElementModel = (DrawOdfElementModel) odfElementFactory.createOdfElementModel();
				List<OdfElement>odfElements = odfElementModel.getOdfElements(layer.getName());
				if(odfElements != null)
				{
					
					for(OdfElement odfElement : odfElements)
						System.out.println(odfElement);
					
					ImportODF importODF = new ImportODF();
					importODF.setOdfDocumentHandler(odfElementModel.getOdfDrawDocumentHandler());
					importODF.setOdfElements(odfElements);
					importODF.importShapes(layer);
					
					System.out.println(odfElements.size());	
				}
			
				
			}
		}
	}
	
	public void runOLD()
	{

		String path = "/media/dieter/f8ceb1a1-74b6-4dbf-a487-e12e6249ced01/home/dieter/KVz.odg";
		String pageName = "Seite 1";
		//String layerName = "Apl";
		//String layerName = "Kabel";
		String layerName = "Trasse";
		
		//String path = Activator.TEMPLATES+File.separator+"KVz.odg";
		//File file = FileUtils.toFile(Activator.getPluginPath(path));
		
		ImportODF importODF = new ImportODF();
		//importODF.openDrawDocument(path);
		//importODF.readLayerShapes(pageName, layerName);
		PlanningItem planningItem = planungMasterComposite.getCurItem();
		if(planningItem != null)
		{			
			Layer layer = drawDocument.getLayer(planningItem.getLayer());
			importODF.importShapes(layer);
			
			Design curDesign = planungMasterComposite.getCurDesign();
			Alternative alternative = (Alternative) ((PlanungContentProvider) planungMasterComposite
					.getTreeViewer().getContentProvider()).getParent(curDesign);		
			
			IOdfElementFactory odfElementFactory = odfElementFactoryRegistry
					.getOdfElementFactory(alternative.getName()); 
			
			if(odfElementFactory != null)
			{
				DrawOdfElementModel odfElementModel = (DrawOdfElementModel) odfElementFactory.createOdfElementModel();
				List<OdfElement>odfElements = odfElementModel.getOdfElements(layerName);
				if(odfElements != null)
				{
					
					for(OdfElement odfElement : odfElements)
						System.out.println(odfElement);
					
					System.out.println(odfElements.size());	
				}
			
				
			}
			
			
			
			
			//System.out.println("itemok");
		
		//importODF.selectLayer("Seite 1","KVz");
		
		
		/*
		ImportFilter importFilter = new ImportFilter(drawDocument);
		importFilter.doImport(file);
		*/
		
		
		/*
		ImportGraphicShape igs = new ImportGraphicShape(drawDocument);		
		Rectangle bound = new Rectangle(40, 40, 5000, 5000);
		Shape shape = igs.readShape(file, bound);
		Layer layer = drawDocument.getLayer(planungMasterComposite.getCurItem().getLayer());
		layer.addShape(shape);
		*/
		
		//ImportSVGShape importXML = new ImportSVGShape(layer, file.getPath());
		//importXML.readShapes();
					
		
		System.out.println("Pull");
		super.run();
		
		}
	}
}
