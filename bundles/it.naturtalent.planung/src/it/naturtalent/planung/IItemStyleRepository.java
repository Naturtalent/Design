package it.naturtalent.planung;

public interface IItemStyleRepository
{
	public void registerItemStyleFactory(String styleName, IItemStyleFactory itemStyleFactory);
	public IItemStyleFactory getItemStyleFactory(String styleName);
}
