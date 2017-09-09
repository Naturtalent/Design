package it.naturtalent.libreoffice.draw;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.sun.star.awt.Size;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.drawing.HomogenMatrixLine3;
import com.sun.star.drawing.PolyPolygonBezierCoords;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XLayer;
import com.sun.star.drawing.XLayerManager;
import com.sun.star.drawing.XLayerSupplier;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.view.XSelectionChangeListener;
import com.sun.star.view.XSelectionSupplier;

import it.naturtalent.libreoffice.Bootstrap;
import it.naturtalent.libreoffice.DesignHelper;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.DrawPagePropertyListener;
import it.naturtalent.libreoffice.PageHelper;
import it.naturtalent.libreoffice.Utils;

 
public class DrawDocument
{
	protected XComponent xComponent;
	private XComponentContext xContext;
	private XDesktop xDesktop;
	
	private static boolean atWork = false;
	
	
	// die aktuelle Seite
	//protected int drawPageIndex = 0;
	protected XDrawPage drawPage;
	
	protected String documentPath;
	
	// Name der aktuellen Seite
	protected String pageName;
	
	protected IEventBroker eventBroker;
	
	// Massstab und Masseinheit
	protected Scale scale;
	
	// registriert die Layer des Documents
	private Map <String, Layer> layerRegistry = new HashMap<String, Layer>();
	
	// der zuletzt selektierte Layer
	private Layer lastSelectedLayer;
	
	public PolyPolygonBezierCoords aCoords; 
	
	// Map beinhaltet das momentan geoffente Dokument (@see TerminateListener) 
	public static Map<TerminateListener, String>openDocumentsMap = new HashMap<TerminateListener, String>();	

	private DrawPagePropertyListener drawPagePropertyListener;
	
	
	public DrawDocument()
	{
		MApplication currentApplication = E4Workbench.getServiceContext()
				.get(IWorkbench.class).getApplication();
		eventBroker = currentApplication.getContext()
				.get(IEventBroker.class);
	}

	public void loadPage(final String documentPath)
	{
		final Job j = new Job("Load Job") //$NON-NLS-1$
		{
			@Override
			protected IStatus run(final IProgressMonitor monitor)
			{
				try
				{
					loadDocument(documentPath);
					//setDocumentProperties();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return Status.OK_STATUS;
			}
		};
		
			
		/*
		j.addJobChangeListener(new JobChangeAdapter() 
		{

			@Override
			public void done(IJobChangeEvent event)
			{				
				if(!event.getResult().isOK())
				{
					// Fehler (wahrscheinlich keine 'JPIPE' - LibraryPath) 
					IStatus status = event.getResult();
					final String message = status.toString();
					
					Display.getDefault().syncExec(new Runnable()
					{
						public void run()
						{
							// Watchdog (@see OpenDesignAction) abschalten
							eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN_CANCEL, null);
							
							MessageDialog
									.openError(
											Display.getDefault().getActiveShell(),
											"Error Load Dokument",message);
							j.cancel();
						}
					});
				}
				super.done(event);
			}
		
		});
		*/
	
		
		j.schedule();		
	}
	
	private void loadDocument(String documentPath) throws Exception
	{
		this.documentPath = documentPath;
		
		File sourceFile = new java.io.File(documentPath);
		StringBuffer sTemplateFileUrl = new StringBuffer("file:///");
		sTemplateFileUrl.append(sourceFile.getCanonicalPath()
				.replace('\\', '/'));
	
		xContext = Bootstrap.bootstrap();
		if (xContext != null)
		{
			XMultiComponentFactory xMCF = xContext.getServiceManager();
			
			// retrieve the Desktop object, we need its XComponentLoader
			Object desktop = xMCF.createInstanceWithContext(
					"com.sun.star.frame.Desktop", xContext);
	
			XComponentLoader xComponentLoader = UnoRuntime.queryInterface(
					XComponentLoader.class, desktop);
			
			//
			//
			//
			
		    /*
	        Object svgfilter;
			try
			{
				svgfilter = xMCF.createInstanceWithContext( "com.sun.star.document.SVGFilter", xComponentLoader);
		        XFilter               xfilter = (XFilter) UnoRuntime.queryInterface( XFilter.class, svgfilter );
		        XImporter             ximporter = (XImporter) UnoRuntime.queryInterface( XExporter.class, svgfilter );
		        System.out.println("Inserting image...");
	
			} catch (com.sun.star.uno.Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	
			
			//
			//
			//
			
	
			// load
			PropertyValue[] loadProps = new PropertyValue[0];
			xComponent = xComponentLoader.loadComponentFromURL(
					sTemplateFileUrl.toString(), "_blank", 0, loadProps);
			
			
			// empirisch ermittelt
						Thread.sleep(500);
						
			
			xComponent.addEventListener(new XEventListener()
			{				
				@Override
				public void disposing(EventObject arg0)
				{		
					// EventHander informiert ueber das Schliessen des Dokuments
					eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE, DrawDocument.this);					
				}
			});
						
			// TerminateListener installieren
			xDesktop = UnoRuntime.queryInterface(XDesktop.class,desktop);
			TerminateListener terminateListener = new TerminateListener();
			xDesktop.addTerminateListener(terminateListener);
			terminateListener.setEventBroker(eventBroker);
			
			// EventHandler informieren, dass Ladevorgang abgeschlossen ist
			eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN, this);
			
			// PageListener installieren und aktivierten
			drawPagePropertyListener = new DrawPagePropertyListener(xComponent);
			drawPagePropertyListener.activatePageListener();
			
			//drawPagePropertyListener.activatePageListener(xComponent);
			
			
			/*
			 * Ueberwachung Shapeselections 
			 */			
			XModel xModel = UnoRuntime.queryInterface(XModel.class,xComponent);
			XController xController = xModel.getCurrentController();
			XSelectionSupplier selectionSupplier = UnoRuntime.queryInterface(
					XSelectionSupplier.class, xController);
			
			selectionSupplier.addSelectionChangeListener(new XSelectionChangeListener()
			{
				
				@Override
				public void disposing(EventObject arg0)
				{
					// TODO Auto-generated method stub
					System.out.println("selection");
				}
				
				@Override
				public void selectionChanged(EventObject arg0)
				{
					
					//System.out.println("selection");
										
					Object obj = arg0.Source;
					
					/*
					if (obj instanceof XPropertySet)
					{
						XPropertySet selectedPropertySet = (XPropertySet) obj;
						Utils.printPropertyValues(selectedPropertySet);
					}
					*/
					
					System.out.println("Selection: "+obj);
					
				
					
				}
			});
			
		
			// das geoeffnete Dokument mit Listener als Key speichern
			openDocumentsMap.put(terminateListener, documentPath);	
		}
	}

	/**
	 * Setzt den Focus auf diese Zeichnung.
	 * Sind mehrere Zeichnungen geoffnet, wird diese Zeichnung bearbeitbar sichtbar im Desktop gezeigt.
	 */
	public void setFocus()
	{		
		DesignHelper.setFocus(xComponent);
		
	}
	
	public void getAllPages()
	{
		int count = PageHelper.getDrawPageCount(xComponent);
		for(int i = 0;i < count;i++)
		{
			XDrawPage page;
			try
			{
				page = PageHelper.getDrawPageByIndex(xComponent, i);
				
				/*
				XPropertySet xPropertySet =  UnoRuntime.queryInterface(
						XPropertySet.class, page);
				Utils.printPropertyValues(xPropertySet);
				*/
				
				//System.out.println(page);
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void closeDesktop()
	{
		xDesktop.terminate();	
	}

	public void closeDocument()
	{
		xComponent.dispose();		
	}
	
	public void pullScaleData()
	{
		scale = new Scale(xComponent);
		scale.pullScaleProperties();		
	}
	
	public Scale getScale()
	{
		return scale;
	}
	
	public Rectangle getPageBound()
	{
		try
		{
			
			XDrawPage xdrawPage =  PageHelper.getDrawPageByName(xComponent, pageName);
			Size aPageSize = PageHelper.getPageSize(xdrawPage);
			
			//Size aPageSize = PageHelper.getPageSize(drawPage);

			int nHalfWidth = aPageSize.Width / 2;
			int nHalfHeight = aPageSize.Height / 2;

			Random aRndGen = new Random();
			int nRndObjWidth = aRndGen.nextInt(nHalfWidth);
			int nRndObjHeight = aRndGen.nextInt(nHalfHeight);

			int nRndObjPosX = aRndGen.nextInt(nHalfWidth - nRndObjWidth) + nRndObjWidth;
			int nRndObjPosY = aRndGen.nextInt(nHalfHeight - nRndObjHeight) + nHalfHeight;
			
			return new Rectangle(nRndObjPosX, nRndObjPosY,nRndObjWidth, nRndObjHeight);

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	private void pullPageSettings ()  throws Exception
	{
		if (xContext != null)
		{
			XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
					XMultiServiceFactory.class, xComponent);
			XInterface settings = (XInterface) xFactory
					.createInstance("com.sun.star.drawing.DocumentSettings");
			XPropertySet xPageProperties = UnoRuntime.queryInterface(
					XPropertySet.class, settings);
			Integer scaleNumerator = (Integer) xPageProperties
					.getPropertyValue("ScaleNumerator");
			Integer scaleDenominator = (Integer) xPageProperties
					.getPropertyValue("ScaleDenominator");

			System.out.println("Maßstab: " + scaleNumerator + ":"
					+ scaleDenominator);

			scaleDenominator = 100;
			xPageProperties.setPropertyValue("ScaleDenominator",
					scaleDenominator);
			System.out.println("Maßstab: " + scaleNumerator + ":"
					+ scaleDenominator);

			Short measureUnit = (Short) xPageProperties
					.getPropertyValue("MeasureUnit");
			System.out.println("MeasureUnit: " + measureUnit);
			measureUnit = 2;
			xPageProperties.setPropertyValue("MeasureUnit", measureUnit);
		}

	}
	
	public static boolean isAtWork()
	{
		return atWork;
	}

	public XComponent getxComponent()
	{
		return xComponent;
	}
	
	public XComponentContext getxContext()
	{
		return xContext;
	}

	public XDrawPage getDrawPage()
	{
		return drawPage;
	}
	
	public XDrawPage getXDrawPage()
	{
		return PageHelper.getDrawPageByName(xComponent, pageName);
	}



	/**
	 * Die aktuelle Seite einstellen einstellen
	 * 
	 */
	
/*
	public void setDrawPage(int pageIndex)
	{
		try
		{			
			drawPage = PageHelper.getDrawPageByIndex(xComponent, pageIndex);						
			
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	*/
	
	public String getDocumentPath()
	{
		return documentPath;
	}

	/**
	 * Die Seite mit dem Namen 'pageName' aktivieren. Sollte diese Seite nicht existieren,
	 * wird eine Neue erzeugt und an oberster Position eingefuegt.
	 * 
	 * @param pageName
	 */
	public void selectDrawPage(String pageName)
	{
		XDrawPage xDrawPage = PageHelper.getDrawPageByName(xComponent, pageName);
		if(xDrawPage == null)
		{
			try
			{
				// neue Seite mit dem Namen 'pageName' einfuegen
				xDrawPage = PageHelper.insertNewDrawPageByIndex(xComponent, 0);
				XPropertySet xPageProperties = UnoRuntime.queryInterface(
						XPropertySet.class, xDrawPage);

				PageHelper.setPageName(xDrawPage, pageName);
				
			} catch (Exception e)
			{				
				e.printStackTrace();
			}
		}
				
		// Seite akktivieren
		if(xDrawPage != null)
			PageHelper.setCurrentPage(xComponent, xDrawPage);
	}

	public void addDrawPage(String pageName)
	{
		XDrawPage xDrawPage;
		try
		{
			xDrawPage = PageHelper.insertNewDrawPageByIndex(xComponent, 0);
			if(xDrawPage != null)
				PageHelper.setPageName(xDrawPage, pageName);		

		} catch (Exception e)
		{
		}
	}

	public void removeDrawPage(String pageName)
	{
		XDrawPage xDrawPage = PageHelper.getDrawPageByName(xComponent, pageName);
		if(xDrawPage != null)
			PageHelper.removeDrawPage(xComponent, xDrawPage);		
	}

	public void renameDrawPage(String pageName, String newName)
	{		
		XDrawPage xDrawPage = PageHelper.getDrawPageByName(xComponent, pageName);
		if(xDrawPage != null)
			PageHelper.setPageName(xDrawPage, newName);
	}

	/**
	 * Alle Layer der Seite einlesen
	 * 
	 */	

	/*
	public void pullStyle()
	{
		if(xComponent != null)
		{
			Style graphicStyle = new Style(xComponent);
			Integer linecolor = graphicStyle.getLineColor();
			
			System.out.println(Integer.toHexString(linecolor));
			
			//graphicStyle.setLineColor(new Integer( 0xff0000 ));
			
			//Style graphicStyle = new Style(xComponent);
			//XStyle style = graphicStyle.getStyle("LineStyle");
			//System.out.println(style.getName());
		}
	}
	*/
	
	public void pullStyleOLD()
	{
		if(xComponent != null)
		{
			try
			{
				// Graphics Style Container
				XModel xModel = UnoRuntime.queryInterface(XModel.class, xComponent);
				com.sun.star.style.XStyleFamiliesSupplier xSFS = UnoRuntime
						.queryInterface(
								com.sun.star.style.XStyleFamiliesSupplier.class,
								xModel);
				com.sun.star.container.XNameAccess xFamilies = xSFS.getStyleFamilies();
				
				String[] Families = xFamilies.getElementNames();
				for (int i = 0; i < Families.length; i++)
				{
					// this is the family
					System.out.println("\n" + Families[i]);
					
				      // and now all available styles
				    Object aFamilyObj = xFamilies.getByName( Families[ i ] );
				    com.sun.star.container.XNameAccess xStyles =
				        UnoRuntime.queryInterface(
				                com.sun.star.container.XNameAccess.class, aFamilyObj );
				    String[] Styles = xStyles.getElementNames();
				    for( int j = 0; j < Styles.length; j++ )
				    {
		                  System.out.println( "   " + Styles[ j ] );
		                    Object aStyleObj = xStyles.getByName( Styles[ j ] );
		                    com.sun.star.style.XStyle xStyle = UnoRuntime.queryInterface(
		                            com.sun.star.style.XStyle.class, aStyleObj );
		                    // now we have the XStyle Interface and the CharColor for
		                    // all styles is exemplary be set to red.
		                    XPropertySet xStylePropSet = UnoRuntime.queryInterface( XPropertySet.class, xStyle );
		                    
		                    if(xStylePropSet != null)
		                    	Utils.printPropertyNames(xStylePropSet);
		                    
		                    /*
		                    XPropertySetInfo xStylePropSetInfo =
		                        xStylePropSet.getPropertySetInfo();
		                    if ( xStylePropSetInfo.hasPropertyByName( "CharColor" ) )
		                    {
		                        xStylePropSet.setPropertyValue( "CharColor",
		                                                        new Integer( 0xff0000 ) );
		                    }
		                    */
				    }
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Alle Layer der Seite einlesen
	 * 
	 */
	public void pullLayer()
	{
		if(xComponent != null)
		{
			try
			{
				XLayerManager xLayerManager = null;
				XLayerSupplier xLayerSupplier = UnoRuntime.queryInterface(
						XLayerSupplier.class, xComponent);
				XNameAccess xNameAccess = xLayerSupplier.getLayerManager();
				xLayerManager = UnoRuntime.queryInterface(XLayerManager.class, xNameAccess );
				
				// alle Layernamen
				XNameAccess nameAccess = (XNameAccess) UnoRuntime.queryInterface( 
						XNameAccess.class, xLayerManager);        
				String [] names = nameAccess.getElementNames();
				
				layerRegistry.clear();
				for(String name : names)
				{
					Any any = (Any) xNameAccess.getByName(name);
					XLayer xLayer = (XLayer) UnoRuntime.queryInterface(XLayer.class, any);
					Layer layer = new Layer(xLayer);
					layer.setDrawDocument(this);					
					layerRegistry.put(name, layer);
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
	}
	
	/**
	 * einen neuen Layer hinzufuegen
	 * 
	 */
	public void addLayer(String layerName)
	{
		if(xComponent != null)
		{
			try
			{
				if (!layerRegistry.containsKey(layerName))
				{
					XLayerManager xLayerManager = null;
					XLayerSupplier xLayerSupplier = UnoRuntime.queryInterface(
							XLayerSupplier.class, xComponent);
					XNameAccess xNameAccess = xLayerSupplier.getLayerManager();
					xLayerManager = UnoRuntime.queryInterface(
							XLayerManager.class, xNameAccess);

					// create a second layer
					XLayer xLayer = xLayerManager
							.insertNewByIndex(xLayerManager.getCount());
					
					Layer newLayer = new Layer(xLayer);
					newLayer.setDrawDocument(this);
					newLayer.setName(layerName);
					newLayer.setVisible(true);
					newLayer.setLocked(true);				
			        layerRegistry.put(layerName, newLayer);
				}
				
			}catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
		
	
	public void selectLayer(Layer layer)
	{
		// zunaechst alle Layer deaktivieren
		Layer [] allLayers = getLayers();
		for(Layer deactivelayer : allLayers)
		{
			deactivelayer.deactivate();
			deactivelayer.setLocked(true);
		}
		
		// Ziellayer aktivieren
		layer.activate();
		layer.setLocked(false);
		
		// den selektierten Layer speichern
		lastSelectedLayer = layer;
	}
	
	public void visibleLayer(Layer layer)
	{
		// zunaechst alle Layer unsichtbar
		Layer [] allLayers = getLayers();
		for(Layer deactivelayer : allLayers)
			deactivelayer.setVisible(false);
		
		// Ziellayer sichtbar
		layer.setVisible(true);
	}
	
	
	public String getPageName()
	{
		return pageName;
	}

	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}

	public Layer getLastSelectedLayer()
	{
		return lastSelectedLayer;
	}

	/**
	 * Einen bestimmten Layer zuruekgeben.
	 * 
	 * @param layerName
	 * @return
	 */
	public Layer getLayer(String layerName)
	{
		return layerRegistry.get(layerName);
	}

	/**
	 * Alle Layer dieser Seite in einem Array zurueckgeben.
	 * 
	 * @return
	 */
	public Layer [] getLayers()
	{
		Layer [] layers = null;
		for (Iterator<Layer>itLayer = layerRegistry.values().iterator(); itLayer.hasNext();)				
			layers = ArrayUtils.add(layers, itLayer.next());										
		return layers;
	}
	
	public Integer pullScaleDenominator() 
	{
		if (xComponent != null)
		{
			try
			{
				XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
						XMultiServiceFactory.class, xComponent);
				XInterface settings = (XInterface) xFactory
						.createInstance("com.sun.star.drawing.DocumentSettings");
				XPropertySet xPageProperties = UnoRuntime.queryInterface(
						XPropertySet.class, settings);
				Integer scaleDenominator = (Integer) xPageProperties
						.getPropertyValue("ScaleDenominator");
				
				return scaleDenominator;

			} catch (UnknownPropertyException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrappedTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (com.sun.star.uno.Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	
	public Double getScaleFactor()
	{
		if (xComponent != null)
		{
			try
			{
				XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
						XMultiServiceFactory.class, xComponent);
				XInterface settings = (XInterface) xFactory
						.createInstance("com.sun.star.drawing.DocumentSettings");
				XPropertySet xPageProperties = UnoRuntime.queryInterface(
						XPropertySet.class, settings);
				Integer scaleNumerator = (Integer) xPageProperties
						.getPropertyValue("ScaleNumerator");
				Integer scaleDenominator = (Integer) xPageProperties
						.getPropertyValue("ScaleDenominator");
				
				double scaleFactor = ((double)scaleDenominator/(double)scaleNumerator);
				return scaleFactor;

			} catch (UnknownPropertyException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrappedTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (com.sun.star.uno.Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public HomogenMatrixLine3 getScaleFactors()
	{		
		if (xComponent != null)
		{
			try
			{
				HomogenMatrixLine3 scaleFactors = new HomogenMatrixLine3();
				
				XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
						XMultiServiceFactory.class, xComponent);
				XInterface settings = (XInterface) xFactory
						.createInstance("com.sun.star.drawing.DocumentSettings");
				XPropertySet xPageProperties = UnoRuntime.queryInterface(
						XPropertySet.class, settings);
				Integer scaleNumerator = (Integer) xPageProperties
						.getPropertyValue("ScaleNumerator");
				Integer scaleDenominator = (Integer) xPageProperties
						.getPropertyValue("ScaleDenominator");
				
				scaleFactors.Column1 = (double)scaleNumerator;
				scaleFactors.Column2 = (double)scaleDenominator;
				return scaleFactors;

			} catch (UnknownPropertyException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrappedTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (com.sun.star.uno.Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	
	}
	
	public Integer getZoomFaktor()
	{
		 try
		{
			XModel xModel = UnoRuntime.queryInterface(XModel.class,xComponent);
			XController xController = xModel.getCurrentController();

			XPropertySet xPageProperties = UnoRuntime.queryInterface(
					XPropertySet.class, xController);
			return new Integer((int) xPageProperties.getPropertyValue("ZoomValue"));			

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setMeasureUnit(Short measureUnit)
	{
		try
		{
			XPropertySet xDrawDocumentProperties = getDrawDocumentProperties();
			xDrawDocumentProperties.setPropertyValue("MeasureUnit", measureUnit);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private XPropertySet getDrawDocumentProperties()
	{
		try
		{
			XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
					XMultiServiceFactory.class, xComponent);
			XInterface settings = (XInterface) xFactory
					.createInstance("com.sun.star.drawing.DocumentSettings");
			XPropertySet xProperties = UnoRuntime.queryInterface(
					XPropertySet.class, settings);
			
			return xProperties;
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


}
