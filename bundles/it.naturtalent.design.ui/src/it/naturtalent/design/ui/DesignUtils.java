package it.naturtalent.design.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.Preferences;

import it.naturtalent.application.IPreferenceAdapter;
import it.naturtalent.design.model.design.Design;
import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.model.design.Page;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.libreoffice.draw.DrawDocument;


/**
 * @author dieter
 *
 */
public class DesignUtils
{
	// ToolbarItem IDs @see getToolItem(String toolitemID)
	public final static String TOOLBAR_OPENDESIGN_ID = "it.naturtalent.design.ui.directtoolitem.opendesign"; //$NON-NLS-1$
	public final static String TOOLBAR_CLOSEDESIGN_ID = "it.naturtalent.design.ui.directtoolitem.closedesign"; //$NON-NLS-1$
	public final static String TOOLBAR_LINKPROJECT_ID = "it.naturtalent.design.ui.directtoolitem.syncProject"; //$NON-NLS-1$
	public final static String TOOLBAR_UNDO_ID = "it.naturtalent.design.ui.directtoolitem.undo"; //$NON-NLS-1$
	public final static String TOOLBAR_SAVE_ID = "it.naturtalent.design.ui.directtoolitem.save"; //$NON-NLS-1$
	
	// 'Quelle' der fuer eine Zeichnung benutze Vorlage 
	private static final String DESIGN_TEMPLATE = "/templates/draw.odg"; //$NON-NLS-1$
	
	// 'Ziel' im Projeckdatabereich wird die Vorlage unter diesem Namen abeglegt (ggf. erweitert mit counter)
	private static final String DEFAULT_DESIGNNAME = "zeichnung.odg"; //$NON-NLS-1$

	private static Log log = LogFactory.getLog(DesignUtils.class);
	
	private static ECPProject designsProject;
	private static Designs designs;
	
	
	/**
	 * Ein bestimmtes ToolItem (OpenAction, LinkProject, ...) der ToolBar im DesignView ermitteln.
	 * Uebergeben wird die ID mit der das ToolItem in der fragment.e4xmi definiert ist. 
	 *  
	 * @param toolitemID
	 * @return
	 */
	public static ToolItem getToolItem(String toolitemID)
	{
		// Parenfenster 'DesignView' ermitteln
		MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
		EPartService partService = currentApplication.getContext().get(EPartService.class);		
		MPart part = partService.findPart(DesignsView.DESIGNSVIEW_ID);
		
		partService.activate(part);

		// ToolItem ueber ID filtern
		MToolBar designToolbar = part.getToolbar();
		return getToolItem(toolitemID, part);
	}
	
	public static ToolItem getToolItem(String toolitemID, MPart part)
	{
		// ToolItem ueber ID filtern
		MToolBar designToolbar = part.getToolbar();
		for(MToolBarElement elem : designToolbar.getChildren())
		{			
			if(StringUtils.equals(elem.getElementId(), toolitemID))
			{
				MToolItem mToolItem = (MToolItem) elem; 				
				Object obj = mToolItem.getWidget();
				
				if(obj instanceof ToolItem)
					return (ToolItem) obj;
			}			
		}
		
		return null;
	}

	/*
	 * Das Modell, indem die Designdaten gespeichert werden. wird zurueckgegeben (ggf. neu erzeugt) 
	 */
	public static ECPProject getDesignProject()
	{
		if(designsProject == null)
			designsProject = ECPUtil.getECPProjectManager().getProject(DesignsView.DESIGNPROJECTNAME);			
		
		// ggf. Projekt 'DESIGNPROJECT' erzeugen (bei Erstaufruf)	
		if(designsProject == null)
			designsProject = createProject(DesignsView.DESIGNPROJECTNAME);			

		return designsProject;
	}
	
	public static ECPProject createProject(String projectName)
	{
		ECPProject project = null;
		
		final List<ECPProvider> providers = new ArrayList<ECPProvider>();
		
		for (final ECPProvider provider : ECPUtil.getECPProviderRegistry().getProviders())
		{
			if (provider.hasCreateProjectWithoutRepositorySupport())			
				providers.add(provider);			
		}
		
		if (providers.size() == 0)
		{
			log.error(Messages.DesignUtils_NoEMFProvider); 
			return null;
		}

		try
		{
			project = ECPUtil.getECPProjectManager()
					.createProject(providers.get(0), projectName, ECPUtil.createProperties());
		} catch (ECPProjectWithNameExistsException e)
		{
			log.error(Messages.DesignUtils_NoEĆPProjectinstalled); 
		}
		
		return project;
	}

	
	/**
	 * Designs ist Root aller Designdaten (Container aller DesignGroup's).
	 * Die Funktion generiert 'Designs' falls noch kein Root existiert.
	 * 
	 * @return
	 */
	public static Designs getDesigns()
	{
		if(designs != null)
			return designs;
		
		// im ECPProject 'designsProject'  das Modell Archives suchen 		
		EList<Object>projectContents = getDesignProject().getContents();
		if(!projectContents.isEmpty())
		{
			for(Object projectContent : projectContents)
			{
				if (projectContent instanceof Designs)
				{
					designs = (Designs) projectContent; 
					break;
				}
			}			
		}
		else
		{
			// das Modell Designs erzeugen und im ECPProject speichern
			EClass designsClass = DesignsPackage.eINSTANCE.getDesigns();
			designs = (Designs)EcoreUtil.create(designsClass);
			projectContents.add(designs);
			designsProject.saveContents();			
		}
		
		return designs;
	}
	
	/**
	 * Die 'DesignGroup' ist ein Container fuer Designs. Ueblicherweise wird fuer jedes IProject ein Container angelegt
	 * und in diesem alle ProjektDesigns gespeichert. Mit dieser Funktion wird die dem IProject zugeordnete
	 * DesignGroup ermittelt und zurueckgegeben.
	 *  
	 * @param ntProjectID
	 * @return
	 */
	public static DesignGroup findDesignGroup(String ntProjectID)
	{
		Designs designs = DesignUtils.getDesigns();
		EList<DesignGroup>designGroups = designs.getDesigns();
		if(designGroups != null)
		{
			for(DesignGroup designGroup : designGroups)
			{
				if(StringUtils.equals(ntProjectID, designGroup.getIProjectID()))
					return designGroup;
			}
		}
		
		return null;
	}
	
	/**
	 * Rueckgabe der Gruppe in der die Zeichnung 'design' gespeichert ist.
	 * 
	 * @param design
	 * @return
	 */
	public static DesignGroup findDesignGroup(Design design)
	{
		
		EObject eObject = design.eContainer();
		
		return (DesignGroup) design.eContainer();
		
		/*
		Designs designs = DesignUtils.getDesigns();
		EList<DesignGroup>designGroups = designs.getDesigns();
		if(designGroups != null)
		{
			for(DesignGroup designGroup : designGroups)
			{
				for(Design groupedDesign : designGroup.getDesigns())
				{
					if(groupedDesign.equals(design))
						return designGroup;
				}
			}
		}
		
		return null;
		*/
	}
	
	/**
	 * 
	 * @param eObject
	 * @return
	 */
	public static EObject rollUpToDesing(EObject eObject)
	{
		while ((eObject != null) && (!(eObject instanceof Design)))
			eObject = eObject.eContainer();
	
		return eObject;
	}

	/**
	 * Ueberprueft, ob eine ProjektDesignDatei existiert. Der 'designFilePath' ist relativ und zeigt auf eine
	 * Datei im Datenbereich des Projekts (der relative Pfad wird im Modell gespeichert). Die ProjektID kann aus dem 
	 * Pfad abgeleitet werden (IPath.Segment(0).
	 * Ueber das IProject kann der absolute Pfad rekonstruiert werden.
	 * 
	 * @param designFilePath
	 * @return
	 */
	public static boolean existProjectDesignFile(String designFilePath)
	{
		if (StringUtils.isNotEmpty(designFilePath))
		{
			// IProject vom Pfad ableiten
			File file = new File(designFilePath);
			String projectID = new Path(file.getPath()).segment(0);
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
			if (iProject.exists())
			{
				// den absoluten Pfad der Datei rekonstruiert und Existenz ueberpruefen
				IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
				IPath path = folder.getLocation();
				File designFile = new File(designFilePath);
				String designName = designFile.getName();
				path = path.append(designName);
				designFile = path.toFile();
				return (designFile.exists());
			}

		}
		return false;
	}
	
	/**
	 * Der 'designFilePath' ist relativ und zeigt auf eineDatei im Datenbereich des Projekts 
	 * (der relative Pfad wird im Modell 'Design' gespeichert).
	 * Die ProjektID kann aus dem Pfad abgeleitet werden (IPath.Segment(0). 
	 * Ueber das IProject kann der absolute Pfad rekonstruiert werden.
	 * 
	 * @param iProject
	 * @return
	 */
	public static void deleteProjectDesignFile(String designFilePath)
	{
		if (StringUtils.isNotEmpty(designFilePath))
		{
			// IProject vom Pfad ableiten
			File file = new File(designFilePath);
			String projectID = new Path(file.getPath()).segment(0);
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
			if (iProject.exists())
			{
				// den absoluten Pfad der Datei rekonstruiert und Existenz ueberpruefen
				IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
				IPath path = folder.getLocation();
				File designFile = new File(designFilePath);
				String designName = designFile.getName();
				path = path.append(designName);
				designFile = path.toFile();
				designFile.delete();
			}	
		}	
	}

	/**
	 * Generiert eine neue ProjektDesigndatei im Datenbereich des Projekts und gibt im
	 * Erfolgsfall den relativen Pfad zurueck.
	 * 
	 * @param iProject
	 * @return
	 */
	public static String createProjectDesignFile(IProject iProject)
	{
		// URL des im PlugIn gespeicherten DesingTemplates
		URL urlTemplate = getDesingTemplateURL();
		if(urlTemplate != null)
		{
			// Pfad zum projekteigenen Datenbereich
			IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
			IPath path = folder.getLocation();

			// Defaultdesignname hinzufuegen, bei mehreren Dateien entsprechende Erweiterunen (1,2,..)
			String fileName = getAutoFileName(path.toFile(),DEFAULT_DESIGNNAME);
			path = path.append(fileName);
			File designFile = path.toFile();
			try
			{
				// Master-DesignTamplate in den Projektdatabereich 'projectData' kopieren
				FileUtils.copyURLToFile(urlTemplate, designFile, 3000, 3000);
				
				// Pfad zum neuen Design 
				path = path.removeFirstSegments(path.segmentCount() - 3);
				return(path.toPortableString());				
			} catch (IOException e)
			{
				log.error(Messages.DesignUtils_ErrorCreateProjectDrawFile); 
			}
		}
		
		log.error(Messages.DesignUtils_NoTemplateFounded); 
		return null;
	}
	
	/**
	 * Generiert eine neue Designdatei durch kopieren des DesingTemplates.
	 * Eine neue Datei 'drawFilePath' wird erzeugt und das Template hierher kopiert.
	 *  
	 * @param iProject
	 * @return
	 */
	public static void createDesignFile(String drawFilePath)
	{
		File designFile = new File(drawFilePath);		
		if (!designFile.exists())
		{
			// URL des im PlugIn gespeicherten DesingTemplates
			URL urlTemplate = getDesingTemplateURL();
			if (urlTemplate != null)
			{
				try
				{
					FileUtils.copyURLToFile(urlTemplate, designFile, 3000, 3000);
				} catch (IOException e)
				{
					log.error(Messages.DesignUtils_ErrorCreateDrawFile);
				}
			}
		}
		else
		{
			log.error(Messages.DesignUtils_NoTemplateFounded); 
		}
	}

	/**
	 * Das dem Design zugeordnete File wird zurueckgegeben.
	 * Existiert die Datei nicht, wird der URL-Eintrag im Design zurueckgesetzt.
	 * 
	 * @param design
	 * @return
	 */
	public static File getDesignFile(Design design)
	{
		String desingnFilePath = getDesignFilePath(design);
		
		if(desingnFilePath != null)
		{
			File desingFile = new File(desingnFilePath);
			if(desingFile.exists())
				return desingFile;
			
			log.error(NLS.bind(Messages.DesignUtils_IvalidDrawFilePath, desingnFilePath));
		}
		
		MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
		IEventBroker eventBroker = currentApplication.getContext().get(IEventBroker.class);
		
		// es wird automatisch eine neue Datei erzeugt
		DesignGroup group = findDesignGroup(design);
		if((group != null) && (StringUtils.isNotEmpty(group.getIProjectID())))
		{
			// Datei wird im Projectdatenbereich angelegt
			String projectID = group.getIProjectID();
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
				
			// existiert ein NtProjekt mit der extrahierten ID
			if (iProject.exists())
			{
				String drawFilePath = createProjectDesignFile(iProject);
				design.setDesignURL(drawFilePath);		
				DesignUtils.getDesignProject().saveContents();		
				eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");	
				return null;
			}
		}
		
		// eine Datei wird im temporaeren Verzeichnis angelegt
		IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(IPreferenceAdapter.ROOT_APPLICATION_PREFERENCES_NODE);
		String drawFile = instancePreferenceNode.get(IPreferenceAdapter.PREFERENCE_APPLICATION_TEMPDIR_KEY,null);
		String drawFilePath = getAutoFileName(new File(drawFile),DEFAULT_DESIGNNAME);		
		drawFilePath = drawFile + File.separator+drawFilePath;
		createDesignFile(drawFilePath);
		
		
		design.setDesignURL(drawFilePath);		
		DesignUtils.getDesignProject().saveContents();		
		eventBroker.send(DesignsView.DESIGNPROJECTSAVED_MODELEVENT, "Model saved");	
		
		return null;
	}
	
	/*
	 * Erzeugt ein DrawDocument im Temporaeren Verzeichnis und vergibt automatisch einen Namen.
	 * 
	 */
	public static String autoCreateDrawFile()
	{
		IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(IPreferenceAdapter.ROOT_APPLICATION_PREFERENCES_NODE);
		String drawFile = instancePreferenceNode.get(IPreferenceAdapter.PREFERENCE_APPLICATION_TEMPDIR_KEY,null);
		String drawFilePath = getAutoFileName(new File(drawFile),DEFAULT_DESIGNNAME);		
		drawFilePath = drawFile + File.separator+drawFilePath;
		createDesignFile(drawFilePath);		
		return drawFilePath;
	}
	
	public static String getDesignFilePath(Design design)
	{
		if((design != null) && (StringUtils.isNotEmpty(design.getDesignURL())))
		{
			// ist Zeichnung ein Projektdesign
			DesignGroup group = findDesignGroup(design);
			
			if(group != null)
			{
				if (StringUtils.isNotEmpty(group.getIProjectID()))
				{
					IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(group.getIProjectID());
					if (iProject.exists())
					{
						// die DesignURL ist relativ zum NtProjekt
						IFolder folder = iProject.getFolder(IProjectData.PROJECTDATA_FOLDER);
						IPath path = folder.getLocation();
						String designFileName = FilenameUtils.getName(design.getDesignURL());
						path = path.append(designFileName);
						return path.toPortableString();
					}
					else
					{
						log.error(Messages.DesignUtils_NoNtProjektFound); //$NON-NLS-N$
					}
				}
				else
				{
					// die DesignURL ist absolut im FileSystem
					return design.getDesignURL();
				}
			}
		}
								
		return null;
	}
	
	public static void createDrawFile(File destDrawFile)
	{
		Bundle bundle = FrameworkUtil.getBundle(DesignUtils.class);
		BundleContext bundleContext = bundle.getBundleContext();
		URL urlTemplate = FileLocator.find(bundleContext.getBundle(),new Path(DESIGN_TEMPLATE), null);
		try
		{
			urlTemplate = FileLocator.resolve(urlTemplate);
			try
			{
				// DesingTamplate in den Projekt-Databereich 'projectData' kopieren
				FileUtils.copyURLToFile(urlTemplate, destDrawFile);				
			} catch (IOException e)
			{							
				log.error(Messages.DesignUtils_ErrorCreateDrawFile);
				e.printStackTrace();
			}
			
		} catch (IOException e1)
		{						
			log.error(Messages.DesignUtils_ErrorCreateDrawFile);
			e1.printStackTrace();
		}

	}

	/*
	 * URL auf die Mastervorlage (ist im PlugIn gespeichert) zurueck
	 */
	private static URL getDesingTemplateURL()
	{
		try
		{
			// im 'templates' Pfad dieses PlugIns ist das Drawtemplate gespeichert
			//Bundle bundle = FrameworkUtil.getBundle(DesignUtils.class.getClass());
			//BundleContext bundleContext = bundle.getBundleContext();
			URL urlTemplate = FileLocator.find(Activator.getContext().getBundle(),new Path(DESIGN_TEMPLATE), null);
			return(FileLocator.resolve(urlTemplate));
		}
		catch (IOException e1)
		{
			log.error(Messages.DesignUtils_NoTemplateFounded); 
		}	
		
		return null;
	}

	/*
	 * Gibt es bereits eine Datei mit diesem Name wird ein zaehlerbasierende Erweiterung hinzugefuegt.
	 */
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
			File res = new File(dir, autoFileName);
			if (!res.exists())
				return autoFileName;

			counter++;
		}
	}

	/*
	 * Gibt es bereits eine Datei mit diesem Name wird ein zaehlerbasierende Erweiterung hinzugefuegt.
	 */
	public static String getAutoPageName(EList<Page>pages)
	{
		String autoPageName;
		String baseName = "Seite";
		
		List<String>pageNames = new ArrayList<String>();
		for(Page page : pages)
		{
			String name = page.getName();
			if(StringUtils.isNotEmpty(name))
				pageNames.add(name);
		}
		
		int counter = 1;
		while (true)
		{
			if (counter > 1)
				autoPageName = baseName + new Integer(counter);
			else
				autoPageName = baseName;
			
			if(!pageNames.contains(autoPageName))
				return autoPageName;
			
			counter++;
		}
	}
	
	
}
