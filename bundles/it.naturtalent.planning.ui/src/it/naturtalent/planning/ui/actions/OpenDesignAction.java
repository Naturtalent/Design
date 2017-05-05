package it.naturtalent.planning.ui.actions;

import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.planning.ui.Messages;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.Page;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class OpenDesignAction extends AbstractPlanungAction
{

	// kill Watchdog
	private boolean cancel = false;
	
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
				System.out.println("Document geoeffnet");
				cancel = true;
			}
		}
	};

	
	@Override
	public void run()
	{
		if ((drawDocument == null) && (planungMasterComposite != null))
		{
			Design design = null;
			IStructuredSelection selection = (IStructuredSelection) planungMasterComposite
					.getTreeViewer().getSelection();
			Object selObj = selection.getFirstElement();
			if (selObj instanceof Design)
				design = (Design) selObj;
				
			//Design design = planungMasterComposite.getCurDesign();
			if (design != null)
			{
				// das dem Design zugeordnete Dokument wird geoffnet
				DrawDocument drawDocument = new DrawDocument();
				drawDocument.setEventBroker(eventBroker);
				Page page = design.getPages()[0];				
				drawDocument.setPageName(page.getName());
				drawDocument.loadPage(design.getDesignURL());
				
				// dieses Design wird aktuelles Design
				planungMasterComposite.setCurDesign(design);
				planungMasterComposite.getTreeViewer().update(design, null);
				
				// Open Dokument Watchdog (paralell zu 'loadPage')
				cancel = false;
				if (eventBroker != null)
					eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT
							+ "*", draWEventHandler); //$NON-NLS-1$								
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(
						Display.getDefault().getActiveShell());
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
							monitor.beginTask(
									Messages.DesignHyperlinkAction_TaskOpenLabel,
									IProgressMonitor.UNKNOWN);
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
						}
					});
				} catch (Exception e)
				{
					System.out
							.println(Messages.DesignHyperlinkAction_TaskCancelMessage);
				}

				// Watchdog ist beendet, Broker nicht mehr erforderlich
				if (eventBroker != null)
					eventBroker.unsubscribe(draWEventHandler);

			}
		}
	}

}
