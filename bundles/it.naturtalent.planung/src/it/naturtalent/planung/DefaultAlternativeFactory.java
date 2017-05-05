package it.naturtalent.planung;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultAlternativeFactory implements IAlternativeFactory
{

	public static final String DEFAULT_ALTERNATIVE_NAME = "DefaultAlternative"; 
	
	private static final String ALTERNATIVE_TEMPLATE_PATH = Activator.TEMPLATES_DIR
			+ File.separator + "DefaultAlternative.xml";
	
	protected String alternativePath = ALTERNATIVE_TEMPLATE_PATH;
	
	private static ILayerContentRepository layerContentRepository;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public Alternative createAlternative()
	{
		Alternative alternative = readTemplateAlternative(ALTERNATIVE_TEMPLATE_PATH);
		init(alternative);		
		return alternative;		
	}

	protected Alternative readTemplateAlternative(String templatePath)
	{
		try
		{
			File templateFile = FileUtils.toFile(Activator.getPluginPath(ALTERNATIVE_TEMPLATE_PATH));
			FileInputStream in = new FileInputStream(templateFile);
			return JAXB.unmarshal(in, Alternative.class);
			
		} catch (Exception e)
		{
			log.error("Fehler beim Lesen der Templatedaten \'"+templatePath+"\'", e);
		}
		
		return null;
	}
	
	protected void init(Alternative alternative)
	{
		Design [] designs = alternative.getDesigns();
		for(Design design : designs)
		{		
			// ID und Key in den Designs definieren
			design.setId(it.naturtalent.planung.Activator.makeIdentifier());
			design.setKey(alternative.getId());
			
			// Key in den Items definieren
			PlanningItem [] items = design.getItems();
			for(PlanningItem item : items)
			{
				item.setKey(design.getKey());
			}
		}
	}

	
	public static void setLayerContentRepository(ILayerContentRepository layerContentRepository)
	{
		DefaultAlternativeFactory.layerContentRepository = layerContentRepository;
	}

	// Test
	private void writeTemplateAlternative(Alternative alternative)
	{
		File file = FileUtils.toFile(Activator.getPluginPath(ALTERNATIVE_TEMPLATE_PATH));
		FileOutputStream out;
		try
		{
			out = new FileOutputStream(file);
			JAXB.marshal(alternative, out);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
