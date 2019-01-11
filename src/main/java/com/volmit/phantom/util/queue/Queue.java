package com.volmit.phantom.util.queue;

import com.volmit.phantom.api.lang.GList;

public interface Queue<T>
{
	public void queue(@SuppressWarnings("unchecked") T... t);

	public void queue(GList<T> t);

	public boolean hasNext(int amt);

	public boolean hasNext();

	public T next();

	public GList<T> next(int amt);

	public void clear();

	public int size();
}
