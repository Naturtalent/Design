package it.naturtalent.design.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;



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


}
