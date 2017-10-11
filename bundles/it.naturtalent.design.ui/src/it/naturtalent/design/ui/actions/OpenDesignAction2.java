package it.naturtalent.design.ui.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.treemasterdetail.ui.swt.MasterDetailAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import it.naturtalent.application.ChooseWorkspaceData;
import it.naturtalent.application.IPreferenceAdapter;
import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;



/**
 * Wird ueber KontextMenue aufgerufen
 * 
 * Diese Aktion wird mit dem ExtensionPoint "org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"
 * dem EMF TreeMasterRednerer Menue zugeordnet und wird auch von diesem aufgerufen.
 * 
 * @author dieter
 *
 */
public class OpenDesignAction2 extends MasterDetailAction
{
	// 'Quelle' der fuer eine Zeichnung benutze Vorlage 
	public static final String DESIGN_TEMPLATE = "/templates/draw.odg";
	
	// 'Ziel' im Projeckdatabereich wird die Vorlage unter diesem Namen abeglegt (ggf. erweitert mit counter)
	public static final String DEFAULT_DESIGNNAME = "zeichnung.odg";
	
	private Design design;
	private DrawDocument drawDocument;	
	
	// Zuordnungstabelle Desing und geoffnetes DrawDocument
	public static Map<Design,DrawDocument> openDrawDocumentMap = new HashMap<Design,DrawDocument>();

	// kill Watchdog
	private boolean cancel = false;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	@Preference(nodePath = IPreferenceAdapter.PREFERENCE_APPLICATION_TEMPDIR_KEY)
	private IEclipsePreferences preferences;
	
	/*
	 * 
	 */
	private IEventBroker eventBroker;
	private EventHandler documentEventHandler = new EventHandler()
	{
		@Override
		public void handleEvent(Event event)
		{			
			if(StringUtils.equals(event.getTopic(),DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_CLOSE))
			{
				Object obj = event.getProperty(IEventBroker.DATA);
				
				if (obj instanceof DrawDocument)
				{
					DrawDocument closedDrawDocument = (DrawDocument) obj;
					for(Design design : openDrawDocumentMap.keySet())
					{
						if (design != null)
						{
							DrawDocument openDrawDocument = openDrawDocumentMap.get(design);
							if (openDrawDocument.equals(closedDrawDocument))
							{
								// das geschlossene DrawDocument aus 'openDrawDocumentMap'
								openDrawDocumentMap.remove(design);
								break;
							}
						}
					}
				}				
				return;
			}
			
			// Abbruch des Ladevorgangs (z.B. keine JPIPE Library gefunden)
			if(StringUtils.equals(event.getTopic(),DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN_CANCEL))
			{
				cancel = true;
				return;
			}
				
			// Ladevorgang beendet, das geoeffnete DrawDocument im 'openDrawDocumentMap' speichern 
			if(StringUtils.equals(event.getTopic(),DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_JUSTOPENED))
			{
				cancel = true; // Watchdog ausschalten
				if((design != null) && (!openDrawDocumentMap.containsKey(design)))
				{
					// das geoffnete Dokument wird 'openDrawDocumentMap' gespeichert  
					openDrawDocumentMap.put(design, drawDocument);
					
					// erst danach wird ueber ein geoffnetes Dokument informiert
					eventBroker.post(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN, this);
				}
				return;
			}
		}
	};
	
	/**
	 * Konstruktion
	 */
	public OpenDesignAction2()
	{
		MApplication currentApplication = E4Workbench.getServiceContext()
				.get(IWorkbench.class).getApplication();
		
		// Handler 'documentEventHandler' hoert auf alle 'DrawDocumentEvent.DRAWDOCUMENT_EVENT' Events
		eventBroker = currentApplication.getContext().get(IEventBroker.class);		
		eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", documentEventHandler);
	}

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{		
		return null;
	}


	@Override
	public void execute(EObject eObject)
	{
		if (eObject instanceof Design)			
		{
			design = (Design) eObject;
			
			if(!openDrawDocumentMap.containsKey(design))			
				//runOpen();
				doOpen();
		}
	}
	
	@Override
	public void dispose()
	{
		// Handler 'documentEventHandler' beim EventBroker abmelden
		eventBroker.unsubscribe(documentEventHandler);
		super.dispose();
	}
	
	private void doOpen()
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
				}
			});
		} catch (Exception e)
		{
			log.error("Abbruch Zeichnung laden", e);
		}
	}
	
	@Override
	public boolean shouldShow(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			
			// nicht sichtbar, wenn Design bereits geoffnet
			if(openDrawDocumentMap.containsKey(design))
				return false;
			
			// sichtbar, wenn ein zuoeffnender DrawFile existiert  
			String designFilePath = design.getDesignURL();
			
			if(DesignUtils.existProjectDesignFile(designFilePath))
				return true;
			
			if(StringUtils.isEmpty(design.getDesignURL()))
			{
				log.error("kein DrawDateiVerzeichnis definiert");
				return false;
			}
			
			return true;
		}

		return false;
	}


	public static Map<Design, DrawDocument> getOpenDrawDocumentMap()
	{
		return openDrawDocumentMap;
	}
	
}
