package it.naturtalent.planung;


import java.beans.PropertyChangeEvent;
import java.util.Arrays;

public class Design extends BaseBean implements Cloneable
{

	// Properties
	public static final String PROP_DESIGN_ID = "designid"; //$NON-NLS-N$
	public static final String PROP_DESIGN_KEY = "designkey"; //$NON-NLS-N$

	public static final String PROP_NAME = "designname"; //$NON-NLS-N$
	public static final String PROP_DESIGNSCALEDENOMINATOR = "designscaledenominator"; //$NON-NLS-N$
	public static final String PROP_DESIGNURL = "designurl"; //$NON-NLS-N$
	public static final String PROP_PAGES = "pages"; //$NON-NLS-N$
	public static final String PROP_PLANNINGITEMS = "planningitems"; //$NON-NLS-N$
	
	private String id;
	private String key;					// key auf Alternative
	private String name;
	private Integer scaleDenominator;	// Massstab
	private String designURL;			// Zeichnung
	private Page [] pages;
	private PlanningItem [] items;		// Inhalt der Zeichnung
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_DESIGN_ID, this.id, this.id = id));
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_DESIGN_KEY, this.key, this.key = key));		
	}
	
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
		firePropertyChange(new PropertyChangeEvent(this, PROP_DESIGNSCALEDENOMINATOR, this.scaleDenominator, this.scaleDenominator = scaleDenominator));		
	}
	
	public String getDesignURL()
	{
		return designURL;
	}
	public void setDesignURL(String designURL)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_DESIGNURL, this.designURL, this.designURL = designURL));		
	}
	
	public void setPages(Page[] pages)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PAGES, this.designURL, this.pages = pages));
	}
	
	public Page[] getPages()
	{
		return pages;
	}
	
	public PlanningItem[] getItems()
	{
		return items;
	}
	public void setItems(PlanningItem[] items)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PLANNINGITEMS, this.items, this.items = items));		
	}
	
	
	/**
	 * 
	 */
	public Design()
	{
		id = Activator.makeIdentifier();
	}


	@Override
	public Design clone()
	{
		try
		{
			return (Design) super.clone();
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
		result = prime * result
				+ ((designURL == null) ? 0 : designURL.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(items);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(pages);
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
		Design other = (Design) obj;
		if (designURL == null)
		{
			if (other.designURL != null)
				return false;
		}
		else if (!designURL.equals(other.designURL))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(items, other.items))
			return false;
		if (key == null)
		{
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(pages, other.pages))
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
