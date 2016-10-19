package org.phantomapi.abstraction;

import java.util.UUID;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;

/**
 * Represents an instance
 * 
 * @author cyberpwn
 */
public class Instance implements MetaInstance
{
	private final DataCluster data;
	private final String type;
	private final MetaInstance parent;
	private final UUID id;
	
	/**
	 * Create an instance
	 * 
	 * @param type
	 *            the type
	 * @param parent
	 *            the parent instance or null
	 */
	public Instance(String type, MetaInstance parent)
	{
		this.type = type;
		this.parent = parent;
		id = UUID.randomUUID();
		
		if(parent != null)
		{
			data = parent.getData();
			data.set(parent.getRoot() + "." + parent.getType() + "-type", type);
		}
		
		else
		{
			data = new DataCluster();
		}
		
		data.set(getRoot() + ".hash", id.toString());
	}
	
	@Override
	public DataCluster getData()
	{
		return data;
	}
	
	@Override
	public String getType()
	{
		return type;
	}
	
	@Override
	public MetaInstance getParent()
	{
		return parent;
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public String getRoot()
	{
		MetaInstance m = this;
		GList<String> ma = new GList<String>();
		
		while(m != null)
		{
			ma.add(m.getType());
			m = m.getParent();
		}
		
		return ma.reverse().toString(".");
	}
}
