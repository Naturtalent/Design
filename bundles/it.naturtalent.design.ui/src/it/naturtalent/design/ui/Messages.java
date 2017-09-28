package it.naturtalent.design.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "it.naturtalent.design.ui.messages"; //$NON-NLS-1$

	public static String DesignUtils_ErrorCreateDrawFile;

	public static String DesignUtils_ErrorCreateProjectDrawFile;

	public static String DesignUtils_IvalidDrawFilePath;

	public static String DesignUtils_NoEĆPProjectinstalled;

	public static String DesignUtils_NoEMFProvider;

	public static String DesignUtils_NoNtProjektFound;

	public static String DesignUtils_NoTemplateFounded;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
