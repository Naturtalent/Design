package it.naturtalent.libreoffice;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;

import com.sun.star.beans.PropertyChangeEvent;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.uno.Any;
import com.sun.star.uno.RuntimeException;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;


/**
 * Ueberwacht im XPropertySet von XComponent die Eigenschaft mit dem Namen 'CurrentPage'. Aendert sich der Wert dieser
 * Eigenschaft ist das gleichbdutend mit einer Selektionsaenderung.
 * 
 * @author dieter
 *
 */
public class DrawPagePropertyListener extends PropertyChangeListenerHelper
{
	
	// relevante Propterynamen
	private static String [] propertyNames = { "CurrentPage" };
		
	/**
	 * Konstruktion
	 * 
	 * @param xEvtSource (XComponent = DrawCoument)
	 */
	public DrawPagePropertyListener(XInterface xEvtSource)
	{
		super(xEvtSource, propertyNames);		
	}

	/**
	 * aktiviert den PageListener des DrawDocumets
	 * 
	 * 
	 * @param xComponent
	 */
	public void activatePageListener()
	{
		XModel xModel = UnoRuntime.queryInterface(XModel.class,GetEvtSource());
		XController xController = xModel.getCurrentController();		
		XPropertySet xPageProperties = UnoRuntime.queryInterface(XPropertySet.class, xController);
		AddAsListenerTo(xPageProperties);
	}

	/**
	 * aktiviert den PageListener des DrawDocumets
	 * 
	 * @param xComponent
	 */
	public void deativatePageListener()
	{
		RemoveAsListener();
	}

	@Override
	public void propertyChange(PropertyChangeEvent aEvt) throws RuntimeException
	{
		Object obj = aEvt.NewValue;
		if(obj instanceof Any)
		{		
			Any any = (Any) aEvt.NewValue;
			XDrawPage xDrawPage = UnoRuntime.queryInterface(XDrawPage.class, any);
			if (xDrawPage != null)
			{
				MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
				IEventBroker eventBroker = currentApplication.getContext().get(IEventBroker.class);
				eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_PAGECHANGE_PROPERTY,xDrawPage);
			}
		}
	}
	
}
