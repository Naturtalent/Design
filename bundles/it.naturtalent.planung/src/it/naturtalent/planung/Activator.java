package it.naturtalent.planung;

import java.net.URL;
import java.util.Date;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

	public final static String TEMPLATES_DIR = "templates";

	private static BundleContext context;

	static BundleContext getContext()
	{
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception
	{
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception
	{
		Activator.context = null;
	}
	
	public static URL getPluginPath(String path)
	{
		try
		{
			URL url = FileLocator.find(Activator.context.getBundle(), new Path(
					path), null);
			url = FileLocator.resolve(url);
			return url;

		} catch (Exception e)
		{
		}
		return null;
	}
	
	/**
	 * einen datumsbasierenden Key erzeugen
	 */

	private static String date;

	private static long identifierCounter;

	/**
	 * Einen eindeutigen, datumsbasierenden Schluessel erzeugen
	 * 
	 * @return
	 */
	public static String makeIdentifier()
	{
		if (date == null)
			date = Long.toString((new Date().getTime())) + "-";
		return date + Long.toString(++identifierCounter);
	}

}
