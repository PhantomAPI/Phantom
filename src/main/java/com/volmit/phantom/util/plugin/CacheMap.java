package com.volmit.phantom.util.plugin;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GMap;

public class CacheMap<K, V>
{
	private int limit;
	private GList<K> order;
	private GMap<K, V> map;

	public CacheMap(int limit)
	{
		this.limit = limit;
		order = new GList<K>();
		map = new GMap<K, V>();
	}

	public int size()
	{
		return map.size();
	}

	public void clear()
	{
		map.clear();
		order.clear();
	}

	public void invalidate(K k)
	{
		order.remove(k);
		map.remove(k);
	}

	public void put(K k, V v)
	{
		if(!order.contains(k))
		{
			order.add(k);
		}

		map.put(k, v);

		while(order.size() > limit)
		{
			K kf = order.pop();
			map.remove(kf);
		}
	}

	public V get(K k)
	{
		order.remove(k);
		order.add(0, k);
		return map.get(k);
	}

	public boolean has(K k)
	{
		return map.containsKey(k);
	}
}
