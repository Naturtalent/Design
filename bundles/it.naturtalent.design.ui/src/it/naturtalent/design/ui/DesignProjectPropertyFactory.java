package it.naturtalent.design.ui;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;

import it.naturtalent.e4.project.INtProjectProperty;
import it.naturtalent.e4.project.INtProjectPropertyFactory;

public class DesignProjectPropertyFactory implements INtProjectPropertyFactory
{
	public final static String DESIGNPROJECTPROPERTYLABEL = "Zeichnungen";

	@Override
	public INtProjectProperty createNtProjektData()
	{
		// die ProjectProperty-Klasse wird vie ContextInjectionFactory erzeugt		
		final MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();
		IEclipseContext context = currentApplication.getContext();		
		return (context != null) ? ContextInjectionFactory.make(DesignProjectProperty.class, context) : null;
	}

	@Override
	public String getLabel()
	{
		return DESIGNPROJECTPROPERTYLABEL;
	}

}
