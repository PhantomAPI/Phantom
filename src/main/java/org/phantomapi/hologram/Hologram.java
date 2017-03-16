package org.phantomapi.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Hologram interface
 * 
 * @author cyberpwn
 */
public interface Hologram
{
	/**
	 * Get the handling entity
	 * 
	 * @return the entity
	 */
	public Entity getHandle();
	
	/**
	 * Get the display text
	 * 
	 * @return the display text
	 */
	public String getDisplay();
	
	/**
	 * Change the location of this hologram
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(Location location);
	
	/**
	 * Change the location of this hologram based on the text
	 * 
	 * @param location
	 *            the new location
	 */
	public void setTextLocation(Location location);
	
	/**
	 * Set the display text of this hologram
	 * 
	 * @param display
	 *            the display text
	 */
	public void setDisplay(String display);
	
	/**
	 * Destroy this hologram and all entities associated with it
	 */
	public void destroy();
	
	/**
	 * Get this hologram's location
	 * 
	 * @return the location
	 */
	public Location getLocation();
}