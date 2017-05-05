package it.naturtalent.planung;

public interface IAlternativeFactoryRegistry
{
	public void registerAlternativeFatory(String factoryName, IAlternativeFactory alterativeFactory);
	public IAlternativeFactory getAlternativeFactory(String factoryName);
	public String [] getAlternativeFatoryNames();
}
