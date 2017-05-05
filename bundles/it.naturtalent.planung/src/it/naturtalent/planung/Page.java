package it.naturtalent.planung;

import java.beans.PropertyChangeEvent;

public class Page extends BaseBean implements Cloneable
{

	// Properties
	public static final String PROP_NAME = "pageName"; //$NON-NLS-N$
	public static final String PROP_SCALEDENOMINATOR = "pagescaledenominator"; //$NON-NLS-N$

	private String name;
	private Integer scaleDenominator;	// Massstab
	
	public String getName()
	{
		return name;
	}	
	public void setName(String name)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_NAME, this.name, this.name = name));		
	}
	
	public Integer getScaleDenominator()
	{
		return scaleDenominator;
	}
	public void setScaleDenominator(Integer scaleDenominator)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_NAME, this.scaleDenominator, this.scaleDenominator = scaleDenominator));		
	}
	
	@Override
	public Page clone()
	{
		try
		{
			return (Page) super.clone();
		} catch (CloneNotSupportedException e)
		{			
		}
		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((scaleDenominator == null) ? 0 : scaleDenominator.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (scaleDenominator == null)
		{
			if (other.scaleDenominator != null)
				return false;
		}
		else if (!scaleDenominator.equals(other.scaleDenominator))
			return false;
		return true;
	}
	
	
}
