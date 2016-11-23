package org.phantomapi.ppa;

import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.util.Fingerprint;
import org.phantomapi.util.M;

public class PPA extends DataCluster
{
	private static final long serialVersionUID = 1L;
	
	public PPA(String type, String destination)
	{
		super();
		
		set("ppak", type);
		set("ppad", destination);
		set("ppas", Phantom.getPPAID());
		set("ppaf", Fingerprint.randomFingerprint("ph1"));
		set("ppat", M.ms());
	}
	
	public PPA(String type)
	{
		this(type, "all");
	}
	
	public void send()
	{
		Phantom.instance().getPpaController().send(this);
	}
	
	public PPA createResponse()
	{
		return new PPA(getType(), getSource());
	}
	
	public String getDestination()
	{
		return getString("ppad");
	}
	
	public String getSource()
	{
		return getString("ppas");
	}
	
	public String getFingerprint()
	{
		return getString("ppaf");
	}
	
	public String getType()
	{
		return getString("ppak");
	}
	
	public long getTime()
	{
		return getLong("ppat");
	}
}
