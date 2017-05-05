package it.naturtalent.planning.ui;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class DefaultLayerContentLabelProvider extends ColumnLabelProvider
{

	private int idx = 0;
	
	public String getText(Object obj)
	{
		String [] data = (String[]) obj;		
		idx = (idx >= data.length) ? 0 : idx;
		return getColumnText(obj, idx++);
	}
	
	public String getColumnText(Object element, int columnIndex)
	{
		String [] data = (String[]) element;
		return data[columnIndex];
	}
}
