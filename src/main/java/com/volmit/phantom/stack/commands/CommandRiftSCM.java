package com.volmit.phantom.stack.commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.Profiler;
import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.rift.RiftException;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;
import com.volmit.phantom.world.WorldEditor;

public class CommandRiftSCM extends PhantomCommand
{
	public CommandRiftSCM()
	{
		super("scm", "schem");
		requiresPermission(PhantomModule.perm.rift.create);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		if(args.length == 1)
		{
			File f = new File(args[0]);

			if(f.exists() && f.isFile())
			{
				sender.sendMessage("Creating Rift");
				//@builder
				try
				{
					Profiler px = new Profiler();
					px.begin();
					Rift r = SVC.get(RiftSVC.class).createRift("scm/" + UUID.randomUUID())
							.setAllowBosses(false)
							.setEntityTickLimit(1)
							.setTileTickLimit(0.5)
							.setPhysicsThrottle(20)
							.setArrowDespawnRate(1)
							.setDifficulty(Difficulty.PEACEFUL)
							.setRule("doMobSpawning", "false")
							.setRule("doTileDrops", "false")
							.setForcedGameMode(GameMode.SPECTATOR)
							.setNerfSpawnerMobs(true)
							.setViewDistance(10);

					//@done
					sender.sendMessage("Loading Rift");
					r.load();
					WorldEditor.streamSchematicAnvil(f, r, sender);
					px.end();
					sender.sendMessage("Created SCM Rift in " + F.time(px.getMilliseconds(), 1));
					r.send(sender.player());
				}

				catch(RiftException e)
				{
					sender.sendMessage("Rift name already in use.");
				}

				catch(IOException e)
				{
					sender.sendMessage("Anvil Stream Failure. Check console.");
					e.printStackTrace();
				}
			}

			else
			{
				sender.sendMessage("File not found.");
			}
		}

		return true;
	}
}