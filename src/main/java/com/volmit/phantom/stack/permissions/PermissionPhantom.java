package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;
import com.volmit.phantom.plugin.Scaffold.Permission;

public class PermissionPhantom extends PhantomPermission
{
	@Permission
	public PermissionRift rift;

	@Permission
	public PermissionServices services;

	@Permission
	public PermissionModules modules;

	@Override
	protected String getNode()
	{
		return "phantom";
	}

	@Override
	public String getDescription()
	{
		return "The root permission node for all of phantom";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}
