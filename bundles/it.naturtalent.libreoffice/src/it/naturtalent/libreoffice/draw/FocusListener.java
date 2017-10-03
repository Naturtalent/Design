package it.naturtalent.libreoffice.draw;

import com.sun.star.awt.FocusEvent;
import com.sun.star.awt.XFocusListener;
import com.sun.star.lang.EventObject;

public class FocusListener implements XFocusListener
{

	@Override
	public void disposing(EventObject arg0)
	{
		System.out.println("XFocusListener - disposing");

	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
		System.out.println("XFocusListener - Focus gained arg: "+arg0);
		
		Object obj = arg0.Source;
		System.out.println(obj);
		

	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		System.out.println("XFocusListener - Focus lost");

	}

}
