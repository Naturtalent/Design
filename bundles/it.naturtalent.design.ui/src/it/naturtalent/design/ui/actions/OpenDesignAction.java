package it.naturtalent.design.ui.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import it.naturtalent.application.ChooseWorkspaceData;
import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.ui.DesignUtils;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.libreoffice.DrawDocumentEvent;
import it.naturtalent.libreoffice.draw.DrawDocument;



/**
 * Diese Aktion wird mit dem ExtensionPoint "org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"
 * dem EMF TreeMasterRednerer Menue zugeordnet und wird auch von diesem aufgerufen.
 * 
 * @author dieter
 *
 */
public class OpenDesignAction extends MasterDetailAction
{
	// 'Quelle' der fuer eine Zeichnung benutze Vorlage 
	public static final String DESIGN_TEMPLATE = "/templates/draw.odg";
	
	// 'Ziel' im Projeckdatabereich wird die Vorlage unter diesem Namen abeglegt (ggf. erweitert mit counter)
	public static final String DEFAULT_DESIGNNAME = "zeichnung.odg";
	
	private Design design;
	private DrawDocument drawDocument;	
	
	// Zuordnungstabelle Desing und geoffnetes DrawDocument
	private static Map<Design,DrawDocument> openDrawDocumentMap = new HashMap<Design,DrawDocument>();

	// kill Watchdog
	private boolean cancel = false;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	/*
	 * Wenn der Dokumentladevorgang abgeschlossen wurde, kann der Watchdog beedent werden.
	 */
	private IEventBroker eventBroker;
	private EventHandler openDocumentEventHandler = new EventHandler()
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
						DrawDocument openDrawDocument = openDrawDocumentMap.get(design);
						if(openDrawDocument.equals(closedDrawDocument))
						{
							openDrawDocumentMap.remove(design);
							break;
						}
					}
				}				
				return;
			}
					
			cancel = true;
			openDrawDocumentMap.put(design, drawDocument);			
			
			
		}
	};
	
	/**
	 * Konstruktion
	 */
	public OpenDesignAction()
	{
		MApplication currentApplication = E4Workbench.getServiceContext()
				.get(IWorkbench.class).getApplication();
		
		// im EventBus anmelden
		eventBroker = currentApplication.getContext().get(IEventBroker.class);
		//eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT_DOCUMENT_OPEN, openDocumentEventHandler);
		eventBroker.subscribe(DrawDocumentEvent.DRAWDOCUMENT_EVENT+"*", openDocumentEventHandler);
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
				runOpen();
		}
	}
	
	@Override
	public void dispose()
	{
		eventBroker.unsubscribe(openDocumentEventHandler);
		super.dispose();
	}


	/*
	 * Die im Design referenzierte Datei wird geladen und oeoffnet.
	 * 
	 */
	private void runOpen()
	{		
		// das dem Design zugeordnete Dokument wird geoffnet
		File file = new File(design.getDesignURL());
		if(file.exists())
		{
			drawDocument = new DrawDocument();
			drawDocument.loadPage(file.toString());
			runWatchdog();
			return;
		}
		
		String projectName = new Path(file.getPath()).segment(0);				
		IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if(iProject.exists())
		{
			// DesignURL mit Projectdata-Path zusammenfassen
			IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
			IPath path = folder.getLocation();
			File designFile = new File(design.getDesignURL());
			String designName = designFile.getName(); 
			path = path.append(designName);
				
			drawDocument = new DrawDocument();
			//drawDocument.setEventBroker(eventBroker);
			//Page page = design.getPages()[0];				
			//drawDocument.setPageName(page.getName());
			drawDocument.loadPage(path.toString());
			
			runWatchdog();
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
					monitor.beginTask(
							"Zeichnung wird geöffnet",
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
			log.error("Abbruch Zeichnung laden", e);
		}
	}


	/*
	 * Meuepunkt 'Zeichnung oeffnen' zeigen/nicht zeigen
	 * 
	 */
	@Override
	public boolean shouldShow(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			String designFilePath = design.getDesignURL();
			
			// nicht sichtbar, wenn Design bereits geoffnet
			if(openDrawDocumentMap.containsKey(design))
				return false;
						
			// sichtbar, wenn eine Projektdesigndatei existiert  
			if(DesignUtils.existProjectDesignFile(designFilePath))
				return true;
			
			// neue Projektdesigndatei anlegen, wenn im Parent 'DesignGroup' eine iProject-ID definiert ist
			EObject check = eObject.eContainer();
			if (check instanceof DesignGroup)
			{
				DesignGroup designGroup = (DesignGroup) check;
				String projectID = designGroup.getIProjectID();				
				if (StringUtils.isNotEmpty(projectID))
				{
					IProject iProject = ResourcesPlugin.getWorkspace().getRoot()
							.getProject(designGroup.getIProjectID());
					if (iProject.exists())
					{
						// neue Projektdesigndatei anlegen und Pfad im
						// Designobjekt festhalten
						design.setDesignURL(DesignUtils.createProjectDesignFile(iProject));
						DesignUtils.getDesignProject().saveContents();

						return true;
					}
				}
			}
			
			// keine Projectdesingdatei, ist ein Pfad definiert und existiert die Datei
			if (StringUtils.isNotEmpty(designFilePath))
			{
				File file = new File(designFilePath);
				if(file.exists())
					return true;
			}
			
			// eine Designdatei manuell anlegen
			FileDialog dlg = new FileDialog(Display.getDefault().getActiveShell());

			// Change the title bar text
			dlg.setText("Importverzeichnis");
			dlg.setFilterExtensions(new String[]{ ".odg" });
			dlg.setFilterPath(FilenameUtils.getFullPath("/home/dieter/temp/"));
			
			String drawFile = dlg.open();
			if (drawFile != null)
			{		
				design.setDesignURL(drawFile);				
				DesignUtils.createDesignFile(drawFile);				
				return true;	
			}
			
		}
		
		return false;
	}
	
	public boolean shouldShowOLD(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			String stgURL = design.getDesignURL();
			if (!StringUtils.isEmpty(stgURL))
			{
				File file = new File(design.getDesignURL());
				String projectName = new Path(file.getPath()).segment(0);				
				IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
				if(iProject.exists())
				{
					// DesignURL mit Projectdata-Path zusammenfassen
					IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
					IPath path = folder.getLocation();					
					File designFile = new File(design.getDesignURL());
					String designName = designFile.getName(); 
					path = path.append(designName);
					designFile = path.toFile();
					return (designFile.exists());	
				}
			}
			else
			{
				// das vom ResourceNavigator selektierte IProject ermitteln
				MApplication currentApplication = E4Workbench
						.getServiceContext().get(IWorkbench.class)
						.getApplication();
				EPartService partService = currentApplication.getContext()
						.get(EPartService.class);
				MPart part = partService
						.findPart(ResourceNavigator.RESOURCE_NAVIGATOR_ID);
				ESelectionService selectionService = part.getContext()
						.get(ESelectionService.class);
				Object selObj = selectionService.getSelection();
				if (selObj instanceof IResource)
				{
					IProject iProject = ((IResource) selObj).getProject();

					// im 'templates' Pfad dieses PlugIns ist das Drawtemplate gespeichert
					Bundle bundle = FrameworkUtil.getBundle(this.getClass());
					BundleContext bundleContext = bundle.getBundleContext();
					URL urlTemplate = FileLocator.find(bundleContext.getBundle(),new Path(DESIGN_TEMPLATE), null);
					try
					{
						urlTemplate = FileLocator.resolve(urlTemplate);
						IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
						IPath path = folder.getLocation();

						// Defaultdesignname hinzufuegen
						String fileName = getAutoFileName(path.toFile(),DEFAULT_DESIGNNAME);
						path = path.append(fileName);
						File designFile = path.toFile();
						try
						{
							// DesingTamplate in den Projekt-Databereich 'projectData' kopieren
							FileUtils.copyURLToFile(urlTemplate, designFile);
							
							// Projectdatapath im Design speichern
							path = path.removeFirstSegments(path.segmentCount() - 3);						
							design.setDesignURL(path.toPortableString());
						} catch (IOException e)
						{							
							log.error("Fehler beim Erzeugen einer Zeichnung");
							e.printStackTrace();
						}
						
					} catch (IOException e1)
					{						
						log.error("Fehler beim Zugriff auf die Zeichnungsvorlage");
						e1.printStackTrace();
					}
				}
				
				return true;
			}			
		}
		
		return false;	
	}
	
	private static String getAutoFileName(File dir, String originalFileName)
	{
		String autoFileName;

		if (dir == null)
			return ""; //$NON-NLS-1$

		int counter = 1;
		while (true)
		{
			if (counter > 1)
			{
				autoFileName = FilenameUtils.getBaseName(originalFileName)
						+ new Integer(counter) + "." //$NON-NLS-1$
						+ FilenameUtils.getExtension(originalFileName);
			}
			else
			{
				autoFileName = originalFileName;
			}

			//IResource res = dir.findMember(autoFileName);
			
			File res = new File(dir, autoFileName);
			if (!res.exists())
				return autoFileName;

			counter++;
		}
	}


	public static Map<Design, DrawDocument> getOpenDrawDocumentMap()
	{
		return openDrawDocumentMap;
	}
	
}
