package it.naturtalent.planning.ui;

import it.naturtalent.e4.project.IProjectData;
import it.naturtalent.e4.project.NtProject;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningMaster;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.part.WorkbenchPart;

public class PlanningUtils
{
	
	/**
	 * Ein Zeichnungsdokument im Projektdataverzeichnis erzeugen.
	 * 
	 * @param alternative
	 * @param ntProject
	 * @return
	 */
	public static File getProjectDesignFile(IProject project, Design design)
	{
		File designFile = null;
		
		if (project != null)
		{
			IFolder folder = project.getFolder(IProjectData.PROJECTDATA_FOLDER);
			IPath path = folder.getLocation();
			
			// Filname von Projectname
			String fileName = design.getId()+".odg";
						
			// Extension 'odg' hinzufuegen			
			path = path.append(fileName);
			designFile = path.toFile();			
		}
		
		return designFile;		
	}


	/**
	 * Ein Zeichnungsdokument im Projektdataverzeichnis erzeugen.
	 * 
	 * @param alternative
	 * @param ntProject
	 * @return
	 */
	public static File getProjectDesignFile(Design design, NtProject ntProject)
	{
		File designFile = null;
		
		if ((design != null) && (StringUtils.isNotEmpty(design.getName())) && (ntProject != null))
		{
			IFolder folder = ntProject.getIProject().getFolder(IProjectData.PROJECTDATA_FOLDER);
			IPath path = folder.getLocation();
			
			// Filname von Designname ableiten
			String fileName = design.getName()+".odg";
						
			// Extension 'odg' hinzufuegen			
			path = path.append(fileName);
			designFile = path.toFile();			
		}
		
		return designFile;		
	}

	/**
	 * Ein Zeichnungsdokument im Projektdataverzeichnis erzeugen.
	 * 
	 * @param alternative
	 * @param ntProject
	 * @return
	 */
	public static File getProjectDesignFile(Alternative alternative, NtProject ntProject)
	{
		File designFile = null;
		
		if ((alternative != null) && (ntProject != null) && (StringUtils.isNotEmpty(alternative.getId())))
		{
			IFolder folder = ntProject.getIProject().getFolder(IProjectData.PROJECTDATA_FOLDER);
			IPath path = folder.getLocation();
			
			// Filname von AlternativeID ableiten
			String fileName = alternative.getId()+".odg";
			fileName = getAutoFileName(path.toFile(), fileName);
			
			// Extension 'odg' hinzufuegen			
			path = path.append(fileName);
			designFile = path.toFile();			
		}
		
		return designFile;		
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

	private static String getAutoFileNameOLD(IContainer dir, String originalFileName)
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

			IResource res = dir.findMember(autoFileName);
			if (res == null)
				return autoFileName;

			counter++;
		}
	}

	/**
	 * Kopiert ein DesignTemplateDokument in das Verzeichnis 'destDir'.
	 * 
	 * @param destPath
	 * @return
	 */
	public static File copyDesignTemplate(String destPath)
	{
		if (StringUtils.isNotEmpty(destPath))
		{
			File destFile = new File(destPath);
			URL urlTemplate = Activator.getPluginPath("/templates/draw.odg");
			try
			{
				// DesignTemplate in Zielverzeichnis kopieren
				FileUtils.copyURLToFile(urlTemplate, destFile);
				return destFile;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void updateAlternativeProperties(PlanningMaster planningMaster, Alternative alternative)
	{
		// die Alternative erhaelt einen Key zur 'Parent'-Planung 
		alternative.setKey(planningMaster.getId());
		
		// DesignURL (Pfad zum Zeichendokument) aktualisieren
		String projectID = planningMaster.getProjektKey(); 
		if(StringUtils.isNotEmpty(projectID))
		{
			// update 'DesignURL' nur wenn ein Projekt zugordnet ist
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectID);
			if(project != null)
			{
				// die Zeichendokumente werden im Projektdataverzeichnis abgelegt
				Design [] designs = alternative.getDesigns();
				if(ArrayUtils.isNotEmpty(designs))
				{
					for (Design design : designs)
					{		
						// bereits definierte Pfade bleiben unveraendert
						String designURL = design.getDesignURL();
						if (StringUtils.isEmpty(designURL))
						{
							// Zeichnungsdokument anlegen
							File designFile = getProjectDesignFile(alternative,new NtProject(project));
							if (designFile != null)
							{
								// Template in die Zeichnungsdokument kopieren
								designFile = copyDesignTemplate(designFile.getPath());
								if (designFile != null)
									design.setDesignURL(designFile.getPath());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Ueberprueft Verzeichnis auf gueltiges DesignDokument,
	 * 
	 * @param designPath
	 * @return
	 */
	public static File checkAndCreateDesignTemplate(String designPath)
	{
		File destFile = null;
		if(StringUtils.isNotEmpty(designPath))
		{
			destFile = new File(designPath);
			if(destFile.exists() && destFile.isFile())
			{
				// existierende Datei mit 'odg'-Extension wird als gueltiges Dokument akzeptiert
				if(StringUtils.equals(FilenameUtils.getExtension(designPath), "odg"))
					return destFile;
				else
					return null;
			}
			
			// Verzeichnis ggf. neu erzeugen
			if (destFile.isDirectory())
			{
				if (!destFile.exists())
				{
					if (!destFile.mkdir())
						return null;
				}
			}
		}
		
		return destFile;
	}
	
}
