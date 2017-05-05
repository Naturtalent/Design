package it.naturtalent.planung;


import org.apache.commons.lang3.ArrayUtils;

public class DefaultAlternative  extends Alternative
{

	public static final String DEFAULT_ALTERNATIVE_NAME = "DefaultAlternative"; 
	
	private ILayerContentRepository layerContentRepository;
	
	private PlanningItem lineItem;

	public DefaultAlternative()
	{
		super();
		setName(DEFAULT_ALTERNATIVE_NAME);		
	}
	
	public void init()
	{
		PlanningItem [] items = null;
		
		if(layerContentRepository != null)
		{			
			// ein Design mit den Items erzeugen 
			Design design = new Design();

			// Items des Desgin
			lineItem = new PlanningItem();
			lineItem.setKey(design.getId());
			lineItem.setName("Linien messen");
			lineItem.setLayer("default");
			lineItem.setContentClass(LayerContent.DefaultContentNames.LineLength.name());
			items = ArrayUtils.add(items, lineItem);
						
			design.setKey(getId());
			lineItem.setKey(design.getId());
			design.setItems(items);
			design.setName("Zeichnung");
			
			Page page = new Page();
			page.setName("Seite 1");
			Page [] pages = {page};
			design.setPages(pages);

			// Design der Alternative zuordnen
			Design[]designs = {design};
			setDesigns(designs);			
		}		
	}

	public void setLayerContentRepository(
			ILayerContentRepository layerContentRepository)
	{
		this.layerContentRepository = layerContentRepository;
	}
	
	
	
}
