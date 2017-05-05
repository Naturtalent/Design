package it.naturtalent.planning.ui.parts;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.planung.Alternative;
import it.naturtalent.planung.Design;
import it.naturtalent.planung.PlanningItem;
import it.naturtalent.planung.PlanningMaster;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class PlanungLabelProvider extends LabelProvider implements IFontProvider
{

	private PlanungMasterComposite planungMasterComposite;
	
	@Override
	public Image getImage(Object element)
	{
		if(element instanceof PlanningMaster)
			return Icon.ICON_PROJECT_OPEN.getImage(IconSize._16x16_DefaultIconSize);

		if(element instanceof Alternative)
			return Icon.ICON_FOLDER.getImage(IconSize._16x16_DefaultIconSize);
		
		if(element instanceof Design)
			return Icon.ICON_FILE.getImage(IconSize._16x16_DefaultIconSize);
		
		if(element instanceof PlanningItem)
			return Icon.ICON_PAINTBRUSH.getImage(IconSize._16x16_DefaultIconSize);
		
		return super.getImage(element);
	}

	@Override
	public String getText(Object element)
	{
		if(element instanceof PlanningMaster)
		{
			PlanningMaster master = (PlanningMaster) element;
			return master.getName();
		}

		if(element instanceof Alternative)
		{
			Alternative alternative = (Alternative) element;
			return alternative.getName();
		}

		if(element instanceof Design)
		{
			Design design = (Design) element;
			return design.getName();
		}

		if(element instanceof PlanningItem)
		{
			PlanningItem item = (PlanningItem) element;
			return item.getName();
		}

		return super.getText(element);
	}

	@Override
	public Font getFont(Object element)
	{
		if(element instanceof Design)
		{
			Design curDesign = planungMasterComposite.getCurDesign();
			if (curDesign != null)
			{
				if (curDesign.equals(element))
				{
					FontRegistry registry = new FontRegistry();
					return registry.getBold(Display.getCurrent()
							.getSystemFont().getFontData()[0].getName());
				}
			}
		}

		return null;
	}

	public void setPlanungMasterComposite(
			PlanungMasterComposite planungMasterComposite)
	{
		this.planungMasterComposite = planungMasterComposite;
	}
	
	

}
