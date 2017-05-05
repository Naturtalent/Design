package it.naturtalent.planung;

public interface ILayerContentRepository
{
	public void registerLayerContentFactory(String contentClassName, ILayerContentFactory contentFactory);
	public ILayerContentFactory getLayerContentFactory(String contentName);
	public String[] getLayerContentFactoryNames();
	
	/*
	public LayerContent getLayerContent(String contentName);
	public void addLayerContent(LayerContent layerContent);
	
	public void addLayerContentClass(String name, Class<? extends LayerContent> contentClass);
	public Class<? extends LayerContent> getLayerContentClass(String contentName);
	
	public String [] getAllNames ();
	*/
	
}
