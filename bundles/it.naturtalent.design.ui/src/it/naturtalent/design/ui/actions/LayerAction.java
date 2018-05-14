package it.naturtalent.design.ui.actions;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.action.Action;

import it.naturtalent.design.model.design.Layer;
import it.naturtalent.design.ui.dialogs.LayerDialog;

public class LayerAction extends Action
{

	private LayerDialog layerDialog;
	
	public LayerAction(MPart contextPart, Layer layer)
	{		
		layerDialog = ContextInjectionFactory.make(LayerDialog.class,
				contextPart.getContext());		
		layerDialog.setLayer(layer);
	}

	@Override
	public void run()
	{	
		layerDialog.open();
	}

}
