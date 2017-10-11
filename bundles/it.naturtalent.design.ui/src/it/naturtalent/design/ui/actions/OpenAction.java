package it.naturtalent.design.ui.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;

public class OpenAction extends Action
{


	// kill Watchdog
	private boolean cancel = false;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Design design;
	private DrawDocument drawDocument;	

	/*
	 * 
	 */
	private IEventBroker eventBroker;
	private EventHandler documentEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{						
			// Abbruch des Ladevorgangs (z.B. keine JPIPE Library gefunden) (@see Bootstrap())
			if(StringUtils.equals(event.getTopic(),DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN_CANCEL))
			{
				// Watchdog beenden
				cancel = true;
				return;
			}
				
			// Ladevorgang beendet, @see (openDrawDocumentMap() in DrawDocument)' 
			if(StringUtils.equals(event.getTopic(),DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN))
			{
				// Watchdog beenden
				cancel = true; 
				return;
			}
		}
	};

	
	public OpenAction(Design design)
	{
		super();
		this.design = design;
		
		MApplication currentApplication = E4Workbench.getServiceContext()
				.get(IWorkbench.class).getApplication();
		
		// Handler 'documentEventHandler' hoert auf alle 'DrawDocumentEvent.DRAWDOCUMENT_EVENT' Events
		eventBroker = currentApplication.getContext().get(IEventBroker.class);		
		eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", documentEventHandler);
	}
	

	@Override
	public void run()
	{
		File file = DesignUtils.getDesignFile(design);
		if((file != null) && file.exists())
		{
			drawDocument = new DrawDocument();
			drawDocument.loadPage(file.toString());
			runWatchdog();			
			return;
		}
	}
	
	private void runWatchdog()
	{
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		dialog.open();
		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException,
						InterruptedException
				{
					monitor.beginTask("Zeichnung wird geöffnet",IProgressMonitor.UNKNOWN);
					for (int i = 0;; ++i)
					{
						if (monitor.isCanceled())
						{
							throw new InterruptedException();
						}
						
						if (i == 50)
							break;
						if (cancel)
							break;
						try
						{
							Thread.sleep(500);
						} catch (InterruptedException e)
						{
							throw new InterruptedException();
						}
					}
					monitor.done();		
					
					// Handler 'documentEventHandler' beim EventBroker abmelden
					eventBroker.unsubscribe(documentEventHandler);
				}
			});
		} catch (Exception e)
		{
			log.error("Abbruch Zeichnung laden", e);
		}
	}
	
	
}
