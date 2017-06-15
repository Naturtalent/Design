package it.naturtalent.design.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import it.naturtalent.design.model.design.DesignGroup;
import it.naturtalent.design.model.design.Designs;
import it.naturtalent.design.model.design.DesignsPackage;
import it.naturtalent.design.ui.parts.DesignsView;
import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.emf.model.EMFModelUtils;

public class DesignUtils
{
	
	// 'Quelle' der fuer eine Zeichnung benutze Vorlage 
	private static final String DESIGN_TEMPLATE = "/templates/draw.odg";
	
	// 'Ziel' im Projeckdatabereich wird die Vorlage unter diesem Namen abeglegt (ggf. erweitert mit counter)
	private static final String DEFAULT_DESIGNNAME = "zeichnung.odg";

	
	private static Log log = LogFactory.getLog(DesignUtils.class);

	/*
	 * Das Modell, indem die Designdaten gespeichert werden. wird zurueckgegeben (ggf. neu erzeugt) 
	 */
	public static ECPProject getDesignProject()
	{
		ECPProject designsProject = ECPUtil.getECPProjectManager().getProject(DesignsView.DESIGNPROJECTNAME);	
		
		// ggf. Projekt 'DESIGNPROJECT' erzeugen
		if(designsProject == null)
			designsProject = new EMFModelUtils().createProject(DesignsView.DESIGNPROJECTNAME);

		return designsProject;
	}
	
	/**
	 * Designs ist Root aller Designdaten (Container aller DesignGroup's).
	 * Die Funktion generiert 'Designs' falls noch kein Root existiert.
	 * 
	 * @return
	 */
	public static Designs getDesigns()
	{
		Designs designs = null;
		
		ECPProject designsProject = ECPUtil.getECPProjectManager().getProject(DesignsView.DESIGNPROJECTNAME);	
		
		// ggf. Projekt 'DESIGNPROJECT' erzeugen
		if(designsProject == null)
			designsProject = new EMFModelUtils().createProject(DesignsView.DESIGNPROJECTNAME);
		
		// im ECPProject das Modell Archives suchen 
		EList<Object>projectContents = designsProject.getContents();
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
				FileUtils.copyURLToFile(urlTemplate, designFile);
				
				// Pfad zum neuen Design im Modell speichern
				path = path.removeFirstSegments(path.segmentCount() - 3);
				return(path.toPortableString());				
			} catch (IOException e)
			{
				log.error("Error create ProjectDesignFile");
			}
		}
		return null;
	}
	
	/**
	 * Generiert eine neue Designdatei durch kopieren des DesingTemplates.
	 *  
	 * Erfolgsfall den relativen Pfad zurueck.
	 * 
	 * @param iProject
	 * @return
	 */
	public static void createDesignFile(String drawFile)
	{
		File designFile = new File(drawFile);		
		if (!designFile.exists())
		{
			// URL des im PlugIn gespeicherten DesingTemplates
			URL urlTemplate = getDesingTemplateURL();
			if (urlTemplate != null)
			{
				try
				{
					FileUtils.copyURLToFile(urlTemplate, designFile);
				} catch (IOException e)
				{
					log.error("Error create DesignFile");
				}
			}
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
			log.error("Error access DesignTemplate");
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
	
}
