package it.naturtalent.libreoffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XLayer;
import com.sun.star.drawing.XLayerManager;
import com.sun.star.drawing.XLayerSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XSelectionSupplier;

public class DrawDocumentUtils
{
	
	
	/*
	 * 
	 *  Page
	 * 
	 */
	
	/**
	 * Alle DrawPage in einer Liste zurueckgeben.
	 * 
	 */
	public static List<XDrawPage> getDrawPages(XComponent xComponent)
	{
		List<XDrawPage>pageList = new ArrayList<XDrawPage>();
		int pageCount = getDrawPageCount(xComponent);
		
		for(int i = 0; i < pageCount; i++)
			pageList.add(getDrawPageByIndex(xComponent, i));
		
		return pageList;
	}
	
	/**
	 * Eine Page durch Index zurueckgenen.
	 * 
	 * @param xComponent
	 * @param nIndex
	 * @return
	 */
	public static XDrawPage getDrawPageByIndex(XComponent xComponent, int nIndex)
	{
		XDrawPagesSupplier xDrawPagesSupplier = UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
		XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();

		try
		{
			return UnoRuntime.queryInterface(XDrawPage.class,xDrawPages.getByIndex(nIndex));
		} catch (IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Anzahl der Seiten zurueckgeben.
	 * 
	 * @param xComponent
	 * @return
	 */
	public static int getDrawPageCount(XComponent xComponent)
	{
		XDrawPagesSupplier xDrawPagesSupplier = UnoRuntime
				.queryInterface(XDrawPagesSupplier.class, xComponent);
		XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
		return xDrawPages.getCount();
	}

	/**
	 * Den Namen der aktuellen Page zurueckgeben
	 * 
	 * @param xComponent
	 * @return
	 */
	public static String getCurrentPageName(XComponent xComponent)
	{
		try
		{
			XModel xModel = UnoRuntime.queryInterface(XModel.class,xComponent);
 			XController xController = xModel.getCurrentController();
 			XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class, xController);
			Object objPage = xPropertySet.getPropertyValue("CurrentPage");
			if(objPage instanceof Any)
			{		
				Any any = (Any) objPage;
				XDrawPage xDrawPage = UnoRuntime.queryInterface(XDrawPage.class, any);				
				XNamed xNamed = UnoRuntime.queryInterface(XNamed.class, xDrawPage);
		        return xNamed.getName();      				
			}

		} catch (UnknownPropertyException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (WrappedTargetException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}			
		
		return null;
	}
	
	/*
	 * 
	 *  Layer
	 * 
	 */
	
	/**
	 * Alle Layernamen in einer Liste zurueckgeben.
	 * 
	 */
	public static List<String> readLayer(XComponent xComponent)
	{
		List<String>layerList = new ArrayList<String>();
		
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
				if(ArrayUtils.isNotEmpty(names))
					for(String name : names)
						layerList.add(name);
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
		return layerList;
	}
	
	
	/**
	 * Einen Layer selektiern.
	 * 
	 * @param xComponent
	 * @param pageName
	 * @param layerName
	 */
	public static void selectLayer(XComponent xComponent, String layerName)
	{
		 try
		{			 
			XLayer xLayer = findLayer(xComponent, layerName);
			if (xLayer != null)
			{
				XModel xModel = UnoRuntime.queryInterface(XModel.class,xComponent);
				XController xController = xModel.getCurrentController();

				XPropertySet xPageProperties = UnoRuntime
						.queryInterface(XPropertySet.class, xController);
				xPageProperties.setPropertyValue("ActiveLayer", xLayer);
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt den Namen des Layers zurueck in dem 'xShape' definiert ist.
	 * 
	 * @param xComponent
	 * @param pageName
	 * @param xShape
	 * @return
	 */
	public static String findLayer(XComponent xComponent, String pageName, XShape xShape)
	{
		List<String>layerNames = readLayer(xComponent);
		if(!layerNames.isEmpty())
		{
			for(String layerName : layerNames)
			{
				List<XShape>layerShapes = getLayerShapes(xComponent, pageName, layerName);
				if(layerShapes.contains(xShape))
					return layerName;				
			}
		}
		
		return null;
	}
	
	/**
	 * Einen Layer ueber seinen Namen suchen.
	 * 
	 * @param xComponent
	 * @param pageName
	 * @param layerName
	 * @return
	 */
	public static XLayer findLayer(XComponent xComponent, String layerName)
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
			for(String name : names)
			{
				Any any = (Any) xNameAccess.getByName(name);
				XLayer xLayer = (XLayer) UnoRuntime.queryInterface(XLayer.class, any);
				if(StringUtils.equals(name, layerName))
					return xLayer;
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}
	
	/*
	 * 
	 *  Shapes
	 * 
	 * 
	 */
	
	
	/**
	 * Die Shapes eines Layers auflisten.
	 * 
	 * @param xComponent
	 * @param pageName
	 * @param layerName
	 * @return
	 */
	public static List<XShape> getLayerShapes(XComponent xComponent, String pageName, String layerName)
	{
		List<XShape> shapeList = new ArrayList<XShape>();

		try
		{
			XDrawPage drawPage = PageHelper.getDrawPageByName(xComponent,pageName);
			if (drawPage != null)
			{
				XShapes xShapes = UnoRuntime.queryInterface(XShapes.class,drawPage);
				if (xShapes != null)
				{
					int n = xShapes.getCount();
					for (int i = 0; i < n; i++)
					{
						Object obj = xShapes.getByIndex(i);
						if (obj instanceof Any)
						{
							Any any = (Any) xShapes.getByIndex(i);
							XShape xShape = UnoRuntime
									.queryInterface(XShape.class, any);
							if (xShape != null)
							{
								XPropertySet xPropSet = (XPropertySet) UnoRuntime
										.queryInterface(XPropertySet.class,xShape);
								String name = (String) xPropSet.getPropertyValue("LayerName");
								if (StringUtils.equals(layerName, name))
								{
									if (xShape != null)
										shapeList.add(xShape);
								}
							}
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownPropertyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shapeList;
	}
	
	/**
	 * Alle Shapes der Seite 'pageName' in einer Liste zurueckgeben
	 *  
	 * @param xComponent
	 * @param pageName
	 * @return
	 */
	public static List<XShape> getSelectedShapes(XComponent xComponent) throws DisposedException
	{
		Any any;
		List<XShape>shapeList = new ArrayList<XShape>();
		
		try
		{
			XModel xModel = UnoRuntime.queryInterface(XModel.class, xComponent);
			XController xController = xModel.getCurrentController();
			XSelectionSupplier selectionSupplier = UnoRuntime
					.queryInterface(XSelectionSupplier.class, xController);
			Object selObj = selectionSupplier.getSelection();
			if (selObj instanceof Any)
			{
				any = (Any) selObj;
				XShapes xShapes = UnoRuntime.queryInterface(XShapes.class, any);
				if (xShapes != null)
				{
					int n = xShapes.getCount();

					// die Shapes einer Liste sammeln
					for (int i = 0; i < n; i++)
					{
						Object obj = xShapes.getByIndex(i);
						if (obj instanceof Any)
						{
							any = (Any) xShapes.getByIndex(i);
							XShape xShape = UnoRuntime
									.queryInterface(XShape.class, any);
							if (xShape != null)
								shapeList.add(xShape);
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shapeList;
	}
	
	/**
	 * Alle Shapes der Seite 'pageName' in einer Liste zurueckgeben
	 *  
	 * @param xComponent
	 * @param pageName
	 * @return
	 */
	public static List<XShape> readPageShapes(XComponent xComponent, String pageName)
	{		
		List<XShape>shapeList = new ArrayList<XShape>();
		try
		{
			// Shapes einer Page ermitteln
			XDrawPage drawPage = PageHelper.getDrawPageByName(xComponent, pageName);
			XShapes xShapes = UnoRuntime.queryInterface(XShapes.class, drawPage);	
			int n = xShapes.getCount();
			
			// die Shapes einer Liste sammeln
			for(int i = 0;i < n;i++)
			{
				Object obj = xShapes.getByIndex(i);
				if(obj instanceof Any)
				{
					Any any = (Any) xShapes.getByIndex(i);
					XShape xShape = UnoRuntime.queryInterface(XShape.class, any);
					if(xShape != null)
						shapeList.add(xShape);			
				}
			}
		} catch (IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shapeList;
	}

	/**
	 * Den Label des Shapes (z.Formen, Linien etc) zurueckgeben.
	 * 
	 * @param xShape
	 * @return
	 */
	public static String getShapeLabel(XShape xShape)
	{
		String label = null;
		try
		{
			XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xShape);
			label =  (String) xPropSet.getPropertyValue("UINamePlural");
		} catch (UnknownPropertyException | WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return label;			
	}
}
