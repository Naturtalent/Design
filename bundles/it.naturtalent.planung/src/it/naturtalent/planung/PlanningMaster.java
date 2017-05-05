package it.naturtalent.planung;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Grundbestandteil einer Planung.
 * 
 * @author dieter
 *
 */
@XmlRootElement(name="planningMaster")
public class PlanningMaster extends BaseBean
{

	// Properties
	public static final String PROP_PLANNINGID = "planningID"; //$NON-NLS-N$
	public static final String PROP_PROJECTKEY = "projectKey"; //$NON-NLS-N$
	public static final String PROP_NAME = "planningName"; //$NON-NLS-N$
	public static final String PROP_PLANNINGALTERATIVES = "planningAlternatives"; //$NON-NLS-N$

	// eigene ID
	private java.lang.String id;
	private String projektKey;     			// Fremdkey auf das Projekt 
	private String name;         			// Name der Planung
	private Alternative [] alternatives;	// die PlanungsAlternativen
	
	public java.lang.String getId()
	{
		return id;
	}
	
	public void setId(java.lang.String id)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PLANNINGID, this.id, this.id = id));
	}

	public String getProjektKey()
	{
		return projektKey;
	}
	public void setProjektKey(String projektKey)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PROJECTKEY, this.projektKey, this.projektKey = projektKey));		
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_NAME, this.name, this.name = name));
	}
	
	public Alternative[] getAlternatives()
	{
		return alternatives;
	}
	public void setAlternatives(Alternative[] alternatives)
	{
		firePropertyChange(new PropertyChangeEvent(this, PROP_PLANNINGALTERATIVES, this.alternatives, this.alternatives = alternatives));
	}

	public PlanningMaster()
	{
		id = Activator.makeIdentifier();		
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(alternatives);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((projektKey == null) ? 0 : projektKey.hashCode());
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
		PlanningMaster other = (PlanningMaster) obj;
		if (!Arrays.equals(alternatives, other.alternatives))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (projektKey == null)
		{
			if (other.projektKey != null)
				return false;
		}
		else if (!projektKey.equals(other.projektKey))
			return false;
		return true;
	}
	

	
	
	
}
