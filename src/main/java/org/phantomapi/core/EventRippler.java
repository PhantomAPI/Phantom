package org.phantomapi.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.InventoryDropItemOnItemEvent;
import org.phantomapi.event.PlayerArrowDamagePlayerEvent;
import org.phantomapi.event.PlayerDamagePlayerEvent;
import org.phantomapi.event.PlayerJumpEvent;
import org.phantomapi.event.PlayerKillPlayerEvent;
import org.phantomapi.event.PlayerMoveBlockEvent;
import org.phantomapi.event.PlayerMoveChunkEvent;
import org.phantomapi.event.PlayerMoveLookEvent;
import org.phantomapi.event.PlayerMovePositionEvent;
import org.phantomapi.event.PlayerProjectileDamagePlayerEvent;
import org.phantomapi.event.TNTPrimeEvent;
import org.phantomapi.event.WraithClickEvent;
import org.phantomapi.event.WraithCollisionEvent;
import org.phantomapi.event.WraithCombustByBlockEvent;
import org.phantomapi.event.WraithCombustByEntityEvent;
import org.phantomapi.event.WraithDamageByBlockEvent;
import org.phantomapi.event.WraithDamageByEntityEvent;
import org.phantomapi.event.WraithDeathEvent;
import org.phantomapi.event.WraithDespawnEvent;
import org.phantomapi.event.WraithRemoveEvent;
import org.phantomapi.event.WraithSpawnEvent;
import org.phantomapi.event.WraithTargetEntityEvent;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.world.Area;
import org.phantomapi.wraith.PhantomWraith;
import net.citizensnpcs.api.event.EntityTargetNPCEvent;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCCollisionEvent;
import net.citizensnpcs.api.event.NPCCombustByBlockEvent;
import net.citizensnpcs.api.event.NPCCombustByEntityEvent;
import net.citizensnpcs.api.event.NPCDamageByBlockEvent;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;

/**
 * Ripple fire events for more specific events
 * 
 * @author cyberpwn
 */
public class EventRippler extends Controller
{
	public EventRippler(Controllable parentController)
	{
		super(parentController);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityDamageByEntityEvent e)
	{
		try
		{
			if(e.getDamager().getType().equals(EntityType.PLAYER))
			{
				if(e.getEntity().getType().equals(EntityType.PLAYER))
				{
					PlayerDamagePlayerEvent ev = new PlayerDamagePlayerEvent((Player) e.getDamager(), (Player) e.getEntity(), (Double) e.getDamage());
					callEvent(ev);
					e.setCancelled(ev.isCancelled() ? true : e.isCancelled());
				}
			}
			
			else if(e.getDamager() instanceof Projectile)
			{
				if(e.getEntity().getType().equals(EntityType.PLAYER))
				{
					Projectile projectile = (Projectile) e.getDamager();
					
					if(projectile.getShooter() instanceof Player)
					{
						Player shooter = (Player) projectile.getShooter();
						
						PlayerProjectileDamagePlayerEvent ev = new PlayerProjectileDamagePlayerEvent(shooter, (Player) e.getEntity(), projectile, (Double) e.getDamage());
						callEvent(ev);
						e.setCancelled(ev.isCancelled() ? true : e.isCancelled());
						
						if(projectile.getType().equals(EntityType.ARROW))
						{
							Arrow arrow = (Arrow) projectile;
							
							PlayerArrowDamagePlayerEvent evx = new PlayerArrowDamagePlayerEvent(shooter, (Player) e.getEntity(), arrow, (Double) e.getDamage());
							callEvent(evx);
							e.setCancelled(evx.isCancelled() ? true : e.isCancelled());
						}
					}
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityDeathEvent e)
	{
		try
		{
			if(e.getEntity().getType().equals(EntityType.PLAYER))
			{
				if(e.getEntity().getKiller().getType().equals(EntityType.PLAYER))
				{
					PlayerKillPlayerEvent ev = new PlayerKillPlayerEvent((Player) e.getEntity().getKiller(), (Player) e.getEntity());
					callEvent(ev);
				}
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerMoveEvent e)
	{
		try
		{
			if(!e.getPlayer().isFlying() && e.getTo().clone().subtract(e.getFrom().clone()).toVector().getY() > 0.419999986)
			{
				if(!e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType().equals(Material.AIR) || !e.getFrom().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType().equals(Material.AIR))
				{
					PlayerJumpEvent ev = new PlayerJumpEvent(e.getPlayer());
					callEvent(ev);
					e.setCancelled(ev.isCancelled() ? true : e.isCancelled());
				}
			}
			
			Location f = new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ());
			Location t = new Location(e.getTo().getWorld(), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
			
			if(!t.equals(f) && !e.isCancelled())
			{
				PlayerMovePositionEvent pmpe = new PlayerMovePositionEvent(e.getPlayer(), e.getFrom(), e.getTo());
				callEvent(pmpe);
				e.setCancelled(pmpe.isCancelled() ? true : e.isCancelled());
				
				if(!t.getBlock().equals(f.getBlock()) && !e.isCancelled())
				{
					PlayerMoveBlockEvent pmbe = new PlayerMoveBlockEvent(e.getPlayer(), e.getFrom(), e.getTo());
					callEvent(pmbe);
					e.setCancelled(pmbe.isCancelled() ? true : e.isCancelled());
					
					if(!t.getChunk().equals(f.getChunk()) && !e.isCancelled())
					{
						PlayerMoveChunkEvent pmce = new PlayerMoveChunkEvent(e.getPlayer(), e.getFrom(), e.getTo());
						callEvent(pmce);
						e.setCancelled(pmce.isCancelled() ? true : e.isCancelled());
					}
				}
			}
			
			else if((e.getFrom().getYaw() != e.getTo().getYaw() || e.getFrom().getPitch() != e.getTo().getPitch()) && !e.isCancelled())
			{
				PlayerMoveLookEvent pmle = new PlayerMoveLookEvent(e.getPlayer(), e.getFrom(), e.getTo());
				callEvent(pmle);
				e.setCancelled(pmle.isCancelled() ? true : e.isCancelled());
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityTargetNPCEvent e)
	{
		WraithTargetEntityEvent ex = new WraithTargetEntityEvent(new PhantomWraith(e.getNPC().getId()), e.getEntity());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCCollisionEvent e)
	{
		WraithCollisionEvent ex = new WraithCollisionEvent(new PhantomWraith(e.getNPC().getId()), e.getCollidedWith());
		callEvent(ex);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCCombustByBlockEvent e)
	{
		WraithCombustByBlockEvent ex = new WraithCombustByBlockEvent(new PhantomWraith(e.getNPC().getId()), e.getCombuster());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCCombustByEntityEvent e)
	{
		WraithCombustByEntityEvent ex = new WraithCombustByEntityEvent(new PhantomWraith(e.getNPC().getId()), e.getCombuster());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCDamageByBlockEvent e)
	{
		WraithDamageByBlockEvent ex = new WraithDamageByBlockEvent(new PhantomWraith(e.getNPC().getId()), e.getDamager(), e.getDamage());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCDeathEvent e)
	{
		WraithDeathEvent ex = new WraithDeathEvent(new PhantomWraith(e.getNPC().getId()));
		callEvent(ex);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCDespawnEvent e)
	{
		WraithDespawnEvent ex = new WraithDespawnEvent(new PhantomWraith(e.getNPC().getId()));
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCRemoveEvent e)
	{
		WraithRemoveEvent ex = new WraithRemoveEvent(new PhantomWraith(e.getNPC().getId()));
		callEvent(ex);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCSpawnEvent e)
	{
		WraithSpawnEvent ex = new WraithSpawnEvent(new PhantomWraith(e.getNPC().getId()));
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCDamageByEntityEvent e)
	{
		WraithDamageByEntityEvent ex = new WraithDamageByEntityEvent(new PhantomWraith(e.getNPC().getId()), e.getDamager(), e.getDamage());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(NPCClickEvent e)
	{
		WraithClickEvent ex = new WraithClickEvent(new PhantomWraith(e.getNPC().getId()), e.getClicker());
		callEvent(ex);
		
		if(ex.isCancelled())
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityExplodeEvent e)
	{
		if(e.getEntity() instanceof TNTPrimed)
		{
			for(Block b : e.blockList())
			{
				if(b.getType().equals(Material.TNT))
				{
					new TaskLater(1)
					{
						@Override
						public void run()
						{
							Area a = new Area(b.getLocation(), 1.149);
							
							for(Entity i : a.getNearbyEntities())
							{
								if(i.getType().equals(EntityType.PRIMED_TNT))
								{
									TNTPrimed tnt = (TNTPrimed) i;
									TNTPrimeEvent ex = new TNTPrimeEvent(tnt, b);
									callEvent(ex);
									
									if(ex.isCancelled())
									{
										tnt.remove();
										b.setType(Material.TNT);
									}
									
									break;
								}
							}
						}
					};
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerInteractEvent e)
	{
		try
		{
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getPlayer().getItemInHand().getType().equals(Material.FLINT_AND_STEEL))
			{
				new TaskLater(1)
				{
					@Override
					public void run()
					{
						Area a = new Area(e.getClickedBlock().getLocation(), 1.149);
						
						for(Entity i : a.getNearbyEntities())
						{
							if(i.getType().equals(EntityType.PRIMED_TNT))
							{
								TNTPrimed tnt = (TNTPrimed) i;
								Block b = e.getClickedBlock();
								TNTPrimeEvent ex = new TNTPrimeEvent(tnt, b);
								callEvent(ex);
								
								if(ex.isCancelled())
								{
									tnt.remove();
									b.setType(Material.TNT);
								}
								
								break;
							}
						}
					}
				};
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(InventoryClickEvent e)
	{
		try
		{
			ItemStack drop = e.getCursor();
			ItemStack targ = e.getCurrentItem();
			
			if(targ != null && drop != null)
			{
				InventoryDropItemOnItemEvent emx = new InventoryDropItemOnItemEvent(e.getInventory(), (Player) e.getWhoClicked(), targ, drop, e);
				callEvent(emx);
				e.setCancelled(emx.isCancelled() ? true : e.isCancelled());
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
