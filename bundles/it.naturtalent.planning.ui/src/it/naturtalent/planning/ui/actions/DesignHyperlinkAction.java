package it.naturtalent.planning.ui.actions;

import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.planning.ui.parts.PlanungMasterComposite;
import it.naturtalent.planning.ui.parts.PlanungMasterComposite.PlanungActionID;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.Page;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Aktion, die ausgefuehrt wird, wenn ein Link auf eine bestimmte Seite des Desings aktiviert wird.
 * 
 * @author dieter
 *
 */
public class DesignHyperlinkAction
{
	
	// Zeichnung zu der die Seite gehoert
	private Design design;
	
	private IEventBroker eventBroker;
	
	// kill Watchdog
	private boolean cancel = false;
	
	//private DrawDocument openedDrawDocument;
	
	private PlanungMasterComposite planungMasterComposite;
	
	
	// Ereignisse im DrawDokument 
	private EventHandler draWEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{
			// der Ladeprozess des DesignDokuments ist abgeschlossen
			if(StringUtils.equals(event.getTopic(), DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN))
			{
				// kill Watchdog
				cancel = true;
			}
		}
	};
	
	/**
	 * Konstruktor 
	 * 
	 * @param design
	 */
	public DesignHyperlinkAction(Design design)
	{	
		this.design = design;
	}
	
	/**
	 * Liefert die Seiten, die fuer dieses Dokument definiert sind.
	 * Info wird benoetigt bei der Erstellung der DesignDetailsComposite
	 *  
	 * @return
	 */
	public Page [] getPages()
	{
		return design.getPages();
	}
	

	public void runPageAction(Page page)
	{
		SelectPageAction openPageAction = (SelectPageAction) planungMasterComposite.getRegisteredAction(PlanungActionID.SELECT_PAGE);
		openPageAction.setPage(page);
		openPageAction.run();
	}

	/*
	public void runPageAction(Page page)
	{
		if(planungMasterComposite != null)
		{
			DrawDocument openedDrawDocument = planungMasterComposite.getDrawDocument();
			if (openedDrawDocument != null)
			{
				// bestehendes Dokument schliessen
				Design lastDesign = planungMasterComposite.getLastDesign();			
				if(lastDesign != null)
				{
					if(!lastDesign.equals(planungMasterComposite.getCurDesign()))
					{
						openedDrawDocument.closeDocument();
						
						if(eventBroker != null)
						{							
							eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", draWEventHandler); //$NON-NLS-1$
							eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CHANGEREQUEST, page);
							eventBroker.unsubscribe(draWEventHandler);
						}
						return;
					}
				}

				String pageName = page.getName();
				if (!StringUtils.equals(openedDrawDocument.getPageName(),pageName))
				{
					openedDrawDocument.setPageName(pageName);
					planungMasterComposite.initPage();
					//openedDrawDocument.selectDrawPage(pageName);
				}
				
				return;
			}
		}
		
		DrawDocument drawDocument = new DrawDocument();
		drawDocument.setEventBroker(eventBroker);
		drawDocument.setPageName(page.getName());
		drawDocument.loadPage(design.getDesignURL());
		
		// Open Dokument Watchdog (paralell zu 'loadPage')
		if(eventBroker != null)
			eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", draWEventHandler); //$NON-NLS-1$
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		dialog.open();
		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException
				{
					monitor.beginTask(Messages.DesignHyperlinkAction_TaskOpenLabel,IProgressMonitor.UNKNOWN);
					for (int i = 0;; ++i)
					{
						if (monitor.isCanceled())
						{
							throw new InterruptedException();
						}
						
						//System.out.println(i);
						if (i == 50)			
							break;
						if(cancel)
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
				}
			});
		} catch (Exception e)
		{
			System.out.println(Messages.DesignHyperlinkAction_TaskCancelMessage);
		}
		
		// Watchdog ist beendet, Broker nicht mehr erforderlich
		if(eventBroker != null)
			eventBroker.unsubscribe(draWEventHandler);
	
	}
	*/
	
	/*
	public void runDesíng(Design design)
	{
		
		DrawDocument [] drawDocuments = DesignDrawDocumentRegistry.getAllDocuments();
		if(ArrayUtils.isNotEmpty(drawDocuments))
		{
			for(DrawDocument page : drawDocuments)
				page.closeDocument();
		}
		DesignDrawDocumentRegistry.clearRegistry();

		DrawDocument drawDocument = new DrawDocument();
		drawDocument.setEventBroker(eventBroker);
		drawDocument.loadPage(design.getDesignURL());
		
		// Open Dokument Watchdog
		if(eventBroker != null)
			eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", draWEventHandler); //$NON-NLS-1$
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		dialog.open();
		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException
				{
					monitor.beginTask(Messages.DesignHyperlinkAction_TaskOpenLabel,IProgressMonitor.UNKNOWN);
					for (int i = 0;; ++i)
					{
						if (monitor.isCanceled())
						{
							throw new InterruptedException();
						}
						
						//System.out.println(i);
						if (i == 50)			
							break;
						if(cancel)
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
				}
			});
		} catch (Exception e)
		{
			System.out.println(Messages.DesignHyperlinkAction_TaskCancelMessage);
		}
		
		if(eventBroker != null)
			eventBroker.unsubscribe(draWEventHandler);
	}
	*/
	
	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}

	public void setPlanungMasterComposite(
			PlanungMasterComposite planungMasterComposite)
	{
		this.planungMasterComposite = planungMasterComposite;
	}


	
	
	
}
