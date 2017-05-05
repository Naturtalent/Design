package it.naturtalent.planung;

import java.util.ArrayList;
import java.util.List;

public class AlternativeRegistry
{
	private static List<Alternative>alternativeRegistry = new ArrayList<Alternative>();
	
	public static void registerAlternative(Alternative alternative)
	{
		alternativeRegistry.add(alternative);
	}
	
	public static List<Alternative> getRegisteredAlternatives()
	{
		List<Alternative>clones = new ArrayList<Alternative>();
		for (Alternative alternative : alternativeRegistry)
			try
			{
				clones.add(alternative.clone());
			} catch (CloneNotSupportedException e)
			{
				return null;
			}

		return clones;
	}
	
	public static Alternative [] getRegisteredAlternativesArray()
	{
		List<Alternative>clones = getRegisteredAlternatives();
		return clones.toArray(new Alternative[clones.size()]);
	}

}
