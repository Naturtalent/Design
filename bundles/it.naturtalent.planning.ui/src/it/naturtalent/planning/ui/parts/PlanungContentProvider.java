package it.naturtalent.planning.ui.parts;

import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;
import it.naturtalent.planung.PlanningModel;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TreeItem;

public class PlanungContentProvider implements ITreeContentProvider
{

	private StructuredViewer treeViewer;
	
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		this.treeViewer = (StructuredViewer) viewer;
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		if(inputElement instanceof PlanningModel)
		{
			PlanningModel planningModel = (PlanningModel) inputElement;
			List<PlanningMaster>data = planningModel.getModelData();
			return data.toArray(new PlanningMaster[data.size()]);
		}
		
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement)
	{
		if(parentElement instanceof PlanningMaster)
		{
			PlanningMaster master = (PlanningMaster) parentElement;			
			Alternative [] alternatives = master.getAlternatives();
			return(ArrayUtils.isNotEmpty(alternatives) ? alternatives : new Object[0]);
		}
		
		if(parentElement instanceof Alternative)
		{
			Alternative alternative = (Alternative) parentElement;
			Design [] designs = alternative.getDesigns();
			return(ArrayUtils.isNotEmpty(designs) ? designs : new Object[0]);
		}
		
		if (parentElement instanceof Design)
		{
			Design design = (Design) parentElement;
			PlanningItem [] items = design.getItems();
			return(ArrayUtils.isNotEmpty(items) ? items : new Object[0]);
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object element)
	{
		if ((element instanceof PlanningItem)
				|| (element instanceof Design)
				|| (element instanceof Alternative)) 
		{
			TreeItem treeItem = (TreeItem)treeViewer.testFindItem(element);
			if(treeItem != null)
				return treeItem.getParentItem().getData();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element)
	{
		return getChildren(element).length > 0;
	}

}
