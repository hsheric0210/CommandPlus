package com.eric0210.commandplus;

import java.util.Iterator;
import java.util.Locale;

import com.eric0210.commandplus.utils.ChunkLoader;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.TaskManager;
import com.eric0210.commandplus.utils.VehicleUtils;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class EventListener implements Listener
{
	@EventHandler
	public void onDamage(final EntityDamageEvent e)
	{
		final Entity entity = e.getEntity();
		if (entity instanceof Player)
		{
			final Entity player = entity;
			final PlayerWrapper wrapper = PlayerWrapper.getPlayer(player.getUniqueId());
			if (wrapper != null)
				e.setDamage(e.getDamage(DamageModifier.BASE) * wrapper.damageTook);
		}

		if (e instanceof EntityDamageByEntityEvent)
		{
			final Entity damager = ((EntityDamageByEntityEvent) e).getDamager();
			if (damager instanceof Player)
			{
				final Entity player = damager;
				final PlayerWrapper wrapper = PlayerWrapper.getPlayer(player.getUniqueId());
				if (wrapper != null)
					e.setDamage(e.getDamage(DamageModifier.BASE) * wrapper.damageDealt);
			}

//			if (entity instanceof Vehicle && VehicleUtils.isMarkedAsSpecial(entity))
//				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCommandPreProcess(final PlayerCommandPreprocessEvent e)
	{
		if (!e.isCancelled() && CommandUtils.isBanned(e.getMessage().toLowerCase(Locale.ENGLISH)))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "이 명령어는 사용 금지 조치되어 있습니다!");
		}
	}

	@EventHandler
	public void onVehicleExit(final VehicleExitEvent e)
	{
		final Vehicle vehicle = e.getVehicle();
		if (VehicleUtils.isMarkedAsSpecial(vehicle))
			if (VehicleUtils.isDismountDeniedVehicle(e.getExited().getUniqueId(), vehicle.getEntityId()))
				e.setCancelled(true);
			else
				vehicle.remove();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkUnload(final ChunkUnloadEvent event)
	{
		if (ChunkLoader.isProtected(event.getWorld(), event.getChunk()))
			event.setCancelled(true); // System.err.println("[Command+ - ChunkLoader] onChunkUnload(): Chunk (" + event.getChunk().getX() + ", " + event.getChunk().getZ() + ") unload attempt cancelled.");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldUnload(final WorldUnloadEvent event)
	{
		if (ChunkLoader.isProtected(event.getWorld()))
			event.setCancelled(true); // System.err.println("[Command+ - ChunkLoader] onWorldUnload(): World " + event.getWorld().getName() + " (UID: " + event.getWorld().getUID() + ") unload attempt cancelled.");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldLoad(final WorldLoadEvent event)
	{
		if (ChunkLoader.isProtected(event.getWorld()))
		{
			final Iterator<Long> itr = ChunkLoader.getProtectedChunks().get(event.getWorld().getUID()).iterator();
			while (itr.hasNext())
			{
				final long hash = itr.next();
				final int chunkX = LongHash.msw(hash);
				final int chunkZ = LongHash.lsw(hash);

//				final long time = System.nanoTime();
//				System.err.println("[Command+ - ChunkLoader] onWorldLoad(): Attempted to load chunk at (" + chunkX + ", " + chunkZ + ").");
				TaskManager.runTaskSync(() ->
				{
					event.getWorld().loadChunk(chunkX, chunkZ);
					event.getWorld().refreshChunk(chunkX, chunkZ); // This is required because server doesn't send the new chunk bulk packet when the chunk is already loaded into memory and it hadn't updated before.
					event.getWorld().getChunkAt(chunkX, chunkZ);
//					System.err.println("[Command+ - ChunkLoader] onWorldLoad(): Successfully loaded chunk at (" + chunkX + ", " + chunkZ + "). [Took " + (System.nanoTime() - time) + " nanoseconds]");
				});
			}
		}
	}
}
