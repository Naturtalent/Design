package it.naturtalent.planning.ui;

import it.naturtalent.libreoffice.draw.DrawDocument;
import it.naturtalent.planung.Design;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DesignDrawDocumentRegistry
{	
	private static Map<Design,DrawDocument>drawDocumentRegistry = new HashMap<Design,DrawDocument>();

	public static void addDrawDocument(Design design, DrawDocument drawDocument)
	{
		drawDocumentRegistry.put(design, drawDocument);
	}

	public static DrawDocument getDrawDocument(Design design)
	{
		return drawDocumentRegistry.get(design);
	}

	public static void removeDrawDocument(Design design)
	{
		drawDocumentRegistry.remove(design);
	}
	
	public static DrawDocument [] getAllDocuments()
	{
		DrawDocument [] documents = null;
		
		Collection<DrawDocument>collection = drawDocumentRegistry.values();
		return collection.toArray(new DrawDocument[collection.size()]);
	}
	
	public static void clearRegistry()
	{
		drawDocumentRegistry.clear();
	}

}
