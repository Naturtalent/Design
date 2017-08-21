package it.naturtalent.design.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import it.naturtalent.application.ChooseWorkspaceData;



public class Activator implements BundleActivator
{
	
	private static BundleContext context;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void start(BundleContext context) throws Exception
	{
		Activator.context = context;		
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		Activator.context = null;
	}
	
	static BundleContext getContext()
	{		
		return context;
	}
	
	/**
	 * Ein neues Projekt erzeugen.
	 * 
	 * @param projectName
	 * @return
	 */
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
			//log.error("kein Provider installiert"); //$NON-NLS-N$
			return null;
		}

		try
		{
			project = ECPUtil.getECPProjectManager()
					.createProject(providers.get(0), projectName, ECPUtil.createProperties());
		} catch (ECPProjectWithNameExistsException e)
		{
			//log.error("es wurde kein Project erzeugt"); //$NON-NLS-N$
		}
		
		return project;
	}
	
	/**
	 * Prueft, ob im Konfigurationsinifile ein LibPath definiert ist, der ein lib-Datei mit dem Namen 'libName'
	 * enthaelt.
	 * 
	 * @param libName
	 * @return
	 */
	public static boolean checkLibraryPath(String libName)
	{		
		ChooseWorkspaceData wd = new ChooseWorkspaceData();		
		String [] vmargs = wd.getConfigLibraryPaths();
		if(vmargs != null)
		{	
			for(String vmarg : vmargs)
			{
				File libDir = new File(vmarg);
				if(libDir.isDirectory())
				{					
					Iterator<File>itFiles = FileUtils.iterateFiles(libDir, null, false);
					for ( Iterator<File> iterator = itFiles; iterator.hasNext(); )
					{
						String baseName = FilenameUtils.getBaseName(iterator.next().getName());
						if(StringUtils.contains(baseName, libName))
							return true;
					}						  
				}
			}
		}
		return false;
	}

	/**
	 * Traegt den LibraryPath 'libPath' in die Konfigurationsinifile ein
	 * 
	 * @param libName
	 * @return
	 */
	public static void setLibraryPath(String libPath)
	{		
		ChooseWorkspaceData wd = new ChooseWorkspaceData();		
		String contentConfig = wd.getConfigFileContentAsString();
		if(contentConfig != null)
		{
			StringBuilder sb = new StringBuilder(contentConfig);
			
			if(!StringUtils.endsWith(contentConfig, "\n"))
				contentConfig = contentConfig+"\n"; 
			
			sb.append("-vmargs\n");
			sb.append("-Djava.library.path=");
			sb.append(libPath+"\n");
			
			wd.setConfigContent(sb.toString());
		}
	}
	
}
