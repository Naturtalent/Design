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
import it.naturtalent.design.ui.parts.DesignsView;
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
public class OpenDesignAction extends MasterDetailAction
{
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{		
		return null;
	}


	@Override
	public void execute(EObject eObject)
	{		
		new OpenAction((Design) eObject).run();
	}
	
	@Override
	public void dispose()
	{
	}
	
	@Override
	public boolean shouldShow(EObject eObject)
	{
		if (eObject instanceof Design)
		{
			Design design = (Design) eObject;
			
			// nicht sichtbar, wenn Design bereits geoffnet
			if(DesignsView.openDrawDocumentMap.containsKey(design))
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
	
}
