package com.volmit.phantom.plugin;

import com.volmit.phantom.lang.D;

public abstract class SimpleService implements IService
{
	private D d;

	public void l(Object... o)
	{
		d().l(o);
	}

	public void w(Object... o)
	{
		d().l(o);
	}

	public void f(Object... o)
	{
		d().l(o);
	}

	private D d()
	{
		if(d == null)
		{
			d = D.as(getClass().getSimpleName().replaceAll("SVC", "").replaceAll("Component", "") + " Service");
		}

		return d;
	}
}
