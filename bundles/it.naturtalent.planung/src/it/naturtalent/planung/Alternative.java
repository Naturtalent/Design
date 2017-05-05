package it.naturtalent.planung;



import java.beans.PropertyChangeEvent;
import java.util.Arrays;

public class Alternative extends BaseBean implements Cloneable
{
	/**
	 * Planungsalternative (Kindelement einer Planung)
	 * 
	 * @author dieter
	 *
	 */

		// Properties
		public static final String PROP_ID = "alternativid"; //$NON-NLS-N$
		public static final String PROP_KEY = "alternativkey"; //$NON-NLS-N$
		public static final String PROP_NAME = "alternativname"; //$NON-NLS-N$
		public static final String PROP_DESIGNS = "designs"; //$NON-NLS-N$
			
		
		private String id;
		private String key;			// primary key (zeigt auf die Planung)	
		private String name;		// Name der Alternative
		private Design [] designs;	// die zugeordneten Zeichungen 
		
		/**
		 * Konstruktion
		 */
		public Alternative()
		{
			id = Activator.makeIdentifier();
		}
		
		public String getId()
		{
			return id;
		}	
		public void setId(String id)
		{
			firePropertyChange(new PropertyChangeEvent(this, PROP_ID, this.id, this.id = id));
		}
		
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
		
		public Design[] getDesigns()
		{
			return designs;
		}

		public void setDesigns(Design[] designs)
		{
			firePropertyChange(new PropertyChangeEvent(this, PROP_DESIGNS, this.designs, this.designs = designs));		
		}
		
		public void copyIn (Alternative alternative)
		{
			id = alternative.getId();
			key = alternative.getKey();
			name = alternative.getName();
			designs = alternative.getDesigns();
		}

		@Override
		protected Alternative clone() throws CloneNotSupportedException
		{
			return (Alternative) super.clone();
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(designs);
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Alternative other = (Alternative) obj;
			if (!Arrays.equals(designs, other.designs))
				return false;
			if (id == null)
			{
				if (other.id != null)
					return false;
			}
			else if (!id.equals(other.id))
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
			return true;
		}
		

}
