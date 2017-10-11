package it.naturtalent.libreoffice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.container.XNameAccess;
import com.sun.star.drawing.XLayer;
import com.sun.star.drawing.XLayerManager;
import com.sun.star.drawing.XLayerSupplier;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;

public class Utils
{
	
	public enum OfficeTypes
	{
	  WRITER, CALC, DRAW, IMPRESS;

	  public String getType()
	  {
	    if ( this == WRITER)
	      return "swriter";
	    
	    if ( this == CALC)
		      return "scalc";
	    
	    if ( this == DRAW)
		      return "sdraw";

	    if ( this == IMPRESS)
		      return "simpress";

	    return null;
	  }
	}
	
	public enum LayerProperties
	{
	  Name, IsVisible, IsLocked;
	}
	
	
	// local component context
	private static XComponentContext xContext = null;
	public static XComponentContext getxContext()
	{
		try
		{
			if(xContext == null)
				xContext = Bootstrap.bootstrap();
		} catch (BootstrapException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xContext;
	}
	
	// Remote office service Manager
	/*
	private static XMultiComponentFactory xServiceManager = null;
	public static XMultiServiceFactory getMultiServiceFactory()
	{
		if(xServiceManager == null)
		{			
			try
			{
				xContext = Bootstrap.bootstrap();
				xServiceManager = xContext.getServiceManager();
				
				XInterface xint= (XInterface)
						xServiceManager.createInstanceWithContext("com.sun.star.bridge.oleautomation.Factory",xContext);
				
				return (XMultiServiceFactory) UnoRuntime.queryInterface(
					      XMultiServiceFactory.class, xint);
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return null;
	}
	*/
	
	public static XComponent newDocComponent(String docType) throws java.lang.Exception
	{
		String loadUrl = "private:factory/" + docType;
		
		XComponentContext xContext = getxContext();
		
		XMultiComponentFactory xMCF = xContext.getServiceManager();
		Object desktop = xMCF.createInstanceWithContext(
				"com.sun.star.frame.Desktop", xContext);
		
		XComponentLoader xComponentLoader = UnoRuntime.queryInterface(
				XComponentLoader.class, desktop);
		PropertyValue[] loadProps = new PropertyValue[0];
		return xComponentLoader.loadComponentFromURL(loadUrl, "_blank", 0,loadProps);
	}
	
   /** Load a document as template
    */
    public static XComponent newDocComponentFromTemplate(String loadUrl) throws java.lang.Exception
    {
    	
    	File sourceFile = new java.io.File(loadUrl);
    	StringBuffer sTemplateFileUrl = new StringBuffer("file:///");
        sTemplateFileUrl.append(sourceFile.getCanonicalPath().replace('\\', '/'));
    	
        XComponentContext xContext = getxContext();
		XMultiComponentFactory xMCF = xContext.getServiceManager();
    	    	
        // retrieve the Desktop object, we need its XComponentLoader
        Object desktop = xMCF.createInstanceWithContext(
            "com.sun.star.frame.Desktop", xContext);
        
        XComponentLoader xComponentLoader = UnoRuntime.queryInterface(XComponentLoader.class, desktop);

        // define load properties according to com.sun.star.document.MediaDescriptor
        // the boolean property AsTemplate tells the office to create a new document
        // from the given file
        
        /*
        PropertyValue[] loadProps = new PropertyValue[1];
        loadProps[0] = new PropertyValue();
        loadProps[0].Name = "AsTemplate";
        loadProps[0].Value = new Boolean(true);
        */
        
        // load      
        PropertyValue[] loadProps = new PropertyValue[0];
        
        return xComponentLoader.loadComponentFromURL(sTemplateFileUrl.toString(), "_blank", 0, loadProps);
    }
    
    // Remote office service Manager
/*
private static XMultiComponentFactory xServiceManager = null;
public static XMultiServiceFactory getMultiServiceFactory()
{
	if(xServiceManager == null)
	{			
		try
		{
			xContext = Bootstrap.bootstrap();
			xServiceManager = xContext.getServiceManager();
			
			XInterface xint= (XInterface)
					xServiceManager.createInstanceWithContext("com.sun.star.bridge.oleautomation.Factory",xContext);
			
			return (XMultiServiceFactory) UnoRuntime.queryInterface(
				      XMultiServiceFactory.class, xint);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	return null;
}
*/

private static XComponentLoader officeComponentLoader = null;
public static XComponentLoader	getComponentLoader(String unoUrl) throws java.lang.Exception
{
	if (officeComponentLoader == null)
	{
		XComponentContext ctx = getxContext();

		// instantiate connector service
		Object x = ctx.getServiceManager().createInstanceWithContext(
				"com.sun.star.connection.Connector", ctx);

		XConnector xConnector = (XConnector) UnoRuntime.queryInterface(
				XConnector.class, x);

		// helper function to parse the UNO URL into a string array
		String a[] = parseUnoUrl(unoUrl);
		if (null == a)
		{
			throw new com.sun.star.uno.Exception("Couldn't parse UNO URL "
					+ unoUrl);
		}
		
		 // connect using the connection string part of the UNO URL only.
         XConnection connection = xConnector.connect(a[0]);

		// connect using the connection string part of the UNO URL only.
		x = ctx.getServiceManager().createInstanceWithContext(
				"com.sun.star.bridge.BridgeFactory", ctx);

		XBridgeFactory xBridgeFactory = (XBridgeFactory) UnoRuntime
				.queryInterface(XBridgeFactory.class, x);

		// create a nameless bridge with no instance provider
		// using the middle part of the UNO URL
		XBridge bridge = xBridgeFactory.createBridge("", a[1], connection,null);
		
		// query for the XComponent interface and add this as event listener
		XComponent xComponent = (XComponent) UnoRuntime.queryInterface(
				XComponent.class, bridge);
		//xComponent.addEventListener(this);

		// get the remote instance
		x = bridge.getInstance(a[2]);

		// Did the remote server export this object ?
		if (null == x)
		{
			throw new com.sun.star.uno.Exception(
					"Server didn't provide an instance for" + a[2], null);
		}

		// Query the initial object for its main factory interface
		XMultiComponentFactory xOfficeMultiComponentFactory = (XMultiComponentFactory) UnoRuntime
				.queryInterface(XMultiComponentFactory.class, x);

		// retrieve the component context (it's not yet exported from the
		// office)
		// Query for the XPropertySet interface.
		XPropertySet xProperySet = (XPropertySet) UnoRuntime
				.queryInterface(XPropertySet.class,
						xOfficeMultiComponentFactory);

		// Get the default context from the office server.
		Object oDefaultContext = xProperySet
				.getPropertyValue("DefaultContext");

		// Query for the interface XComponentContext.
		XComponentContext xOfficeComponentContext = (XComponentContext) UnoRuntime
				.queryInterface(XComponentContext.class, oDefaultContext);

		// now create the desktop service
		// NOTE: use the office component context here !
		Object oDesktop = xOfficeMultiComponentFactory
				.createInstanceWithContext("com.sun.star.frame.Desktop",
						xOfficeComponentContext);

		officeComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, oDesktop);

		if (officeComponentLoader == null)
		{
			throw new com.sun.star.uno.Exception(
					"Couldn't instantiate com.sun.star.frame.Desktop", null);
		}
		
	}

	return officeComponentLoader;
}

	// Remote office service Manager
/*
private static XMultiComponentFactory xServiceManager = null;
public static XMultiServiceFactory getMultiServiceFactory()
{
	if(xServiceManager == null)
	{			
		try
		{
			xContext = Bootstrap.bootstrap();
			xServiceManager = xContext.getServiceManager();
			
			XInterface xint= (XInterface)
					xServiceManager.createInstanceWithContext("com.sun.star.bridge.oleautomation.Factory",xContext);
			
			return (XMultiServiceFactory) UnoRuntime.queryInterface(
				      XMultiServiceFactory.class, xint);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	return null;
}
*/

/** separates the uno-url into 3 different parts.
 */
protected static String[] parseUnoUrl(String url)
{
	String[] aRet = new String[3];

	if (!url.startsWith("uno:"))
	{
		return null;
	}

	int semicolon = url.indexOf(';');
	if (semicolon == -1)
		return null;

	aRet[0] = url.substring(4, semicolon);
	int nextSemicolon = url.indexOf(';', semicolon + 1);

	if (semicolon == -1)
		return null;
	aRet[1] = url.substring(semicolon + 1, nextSemicolon);

	aRet[2] = url.substring(nextSemicolon + 1);
	return aRet;
}

	/**
     * Zugriffexample: layer.getPropertyValue(LayerProperties.Name.name());
     * @param xDrawComponent
     * @return
     */
    public static List<XPropertySet> getLayerPropertySet(XComponent xDrawComponent)
	{
    	List<XPropertySet>propSet = new ArrayList<XPropertySet>();
    	
    	try
		{
			XLayerManager xLayerManager = null;
			XLayerSupplier xLayerSupplier = UnoRuntime.queryInterface(
			        XLayerSupplier.class, xDrawComponent);
			XNameAccess xNameAccess = xLayerSupplier.getLayerManager();
			xLayerManager = UnoRuntime.queryInterface(XLayerManager.class, xNameAccess );
			
			XNameAccess nameAccess = (XNameAccess) UnoRuntime.queryInterface( 
					XNameAccess.class, xLayerManager);        
			String [] names = nameAccess.getElementNames();
			
			for(String name : names)
			{
				 Any any = (Any) nameAccess.getByName(name);
				 XPropertySet xLayerPropSet = UnoRuntime.queryInterface(XPropertySet.class, any);
				 propSet.add(xLayerPropSet);
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return propSet;
	}    
    

    public static Property[] propertiesInfo( XPropertySet xPropertySet)
	{
		 XPropertySetInfo info = xPropertySet.getPropertySetInfo();
		 return info.getProperties();
	}

    public static String [] printPropertyNames( XPropertySet xPropertySet)
	{
    	String [] names = null;
    	
		 Property [] props = propertiesInfo(xPropertySet);
		 for(Property property : props)
		 {
			 names = ArrayUtils.add(names, property.Name);
			 System.out.println(property.Name+" | "+property.Attributes);
		 }	
		 
		 return names;
	}

    public static void printPropertyValues( XPropertySet xPropertySet)
	{
    	try
		{
			String [] names = printPropertyNames(xPropertySet);
			if (ArrayUtils.isNotEmpty(names))
			{
				for (String name : names)
				{
					Object obj = xPropertySet.getPropertyValue(name);
					System.out.println(name+"  :  "+obj.getClass().getName()+"  :  "+obj);
				}
			}
		} catch (UnknownPropertyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrappedTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void testLayerProperty(XComponent xComponent) throws UnknownPropertyException, WrappedTargetException
 	{
		XModel xM = UnoRuntime.queryInterface(XModel.class, xComponent);
		XController xC = xM.getCurrentController();
		XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class, xC);
		
		Any any  = (Any) xPropertySet.getPropertyValue("ActiveLayer");
		XLayer xLayer = UnoRuntime.queryInterface(XLayer.class, any);		
		XPropertySet xLayerPropSet = UnoRuntime.queryInterface(XPropertySet.class, xLayer);
		
		Utils.printPropertyValues(xLayerPropSet);
		
		String name = (String) xLayerPropSet.getPropertyValue("Name");
		
		System.out.println("Layer: "+name);

 	}
    
    public static void testDocumentSettings(XComponent xComponent)
	{
		try
		{
			XMultiServiceFactory xFactory = UnoRuntime.queryInterface(
					XMultiServiceFactory.class, xComponent);
			XInterface settings = (XInterface) xFactory
					.createInstance("com.sun.star.drawing.DocumentSettings");
			XPropertySet xPropertySet = UnoRuntime.queryInterface(
					XPropertySet.class, settings);
			
			Utils.printPropertyValues(xPropertySet);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    

}
