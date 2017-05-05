package it.naturtalent.planung;



import it.naturtalent.libreoffice.draw.Shape;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Composite;
import org.jdom2.Element;

public class LayerContent
{

	public enum DefaultContentNames
	{
		ShapeTypes, LineLength, PolyLineLength, RectangleArea, ShapeCounter;
	}
	
	protected String contentName;  // Name des Layers (Ebene der DrawSeite)	
	protected String contentText;	// der Inhalt als Text	
	protected Element rootContent;	// der Inhalt als xml 	
	protected Composite contentComposite;  // visuelle Darstellung des Inhalts
	
	protected BigDecimal scaleDenominator;
	protected IEventBroker eventBroker;
	
	
	public LayerContent()
	{
		
	}
	
	public LayerContent(String contentName)
	{
		super();
		this.contentName = contentName;
	}

	public void setContentName(String contentName)
	{
		this.contentName = contentName;
	}

	public String getContentName()
	{
		return contentName;
	}

	public String getContentText()
	{
		return contentText;
	}

	public Composite getContentComposite(Composite parent)
	{
		return contentComposite;
	}
	
	public void setShapes(List<Shape>shapes)	
	{
		
	}
	
	public void setScaleDenominator (Integer scaleDenominator)
	{
		this.scaleDenominator = new BigDecimal(scaleDenominator);
	}

	public IEventBroker getEventBroker()
	{
		return eventBroker;
	}

	public void setEventBroker(IEventBroker eventBroker)
	{
		this.eventBroker = eventBroker;
	}

}
