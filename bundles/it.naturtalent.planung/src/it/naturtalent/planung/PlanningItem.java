package it.naturtalent.planung;

import java.beans.PropertyChangeEvent;

/**
 * Ein Planungselement
 * 
 * @author dieter
 *
 */
public class PlanningItem extends BaseBean implements Cloneable
{	
	// Properties
	public static final String PROP_KEY = "key"; //$NON-NLS-N$	
	public static final String PROP_NAME = "name"; //$NON-NLS-N$
	public static final String PROP_LAYER = "layer"; //$NON-NLS-N$
	public static final String PROP_CONTENTCLASS = "contentclass"; //$NON-NLS-N$
	public static final String PROP_STYLENAME = "stylename"; //$NON-NLS-N$
	
		
	private java.lang.String key; // primary key (zeigt auf die Zeichnung)
	private String name;
	private String layer;
	private String contentClass;
	private String styleName;	
	
	public java.lang.String getKey()
	{
		return key;
	}
	public void setKey(java.lang.String key)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_KEY, this.key, this.key = key));		
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_NAME, this.name, this.name = name));
	}
	public String getContentClass()
	{
		return contentClass;
	}
	public void setContentClass(String contentClass)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_CONTENTCLASS, this.contentClass, this.contentClass = contentClass));		
	}
	
	public String getLayer()
	{
		return layer;
	}
	public void setLayer(String layer)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_LAYER, this.layer, this.layer = layer));		
	}
	
	public String getStyleName()
	{
		return styleName;
	}
	public void setStyleName(String styleName)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_STYLENAME, this.styleName, this.styleName = styleName));
	}

	public void copyIn(PlanningItem srcItem)
	{
		name = srcItem.getName();
		layer = srcItem.getLayer();
		key = srcItem.getKey();
		contentClass = srcItem.getContentClass();
		styleName = srcItem.getStyleName();
	}
	
	@Override	
	public PlanningItem clone()
	{		
		try
		{
			return (PlanningItem) super.clone();
		} catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentClass == null) ? 0 : contentClass.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((styleName == null) ? 0 : styleName.hashCode());
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
		PlanningItem other = (PlanningItem) obj;
		if (contentClass == null)
		{
			if (other.contentClass != null)
				return false;
		}
		else if (!contentClass.equals(other.contentClass))
			return false;
		if (key == null)
		{
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (layer == null)
		{
			if (other.layer != null)
				return false;
		}
		else if (!layer.equals(other.layer))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (styleName == null)
		{
			if (other.styleName != null)
				return false;
		}
		else if (!styleName.equals(other.styleName))
			return false;
		return true;
	}
	
}
