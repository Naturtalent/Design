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
	public static String LayerDialog_this_title;
	public static String LayerDialog_lblNewLabel_text;
	public static String LayerDialog_text_text;
	public static String LayerDialog_lblNewLabel_text_1;
	public static String MasslineLengthDialog_lblLength_text;
	public static String MasslineLengthDialog_text_text;
	public static String MasslineLengthDialog_comboUnit_text;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
