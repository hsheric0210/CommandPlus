package com.eric0210.commandplus.utils;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;

public final class ChunkLoader
{
	private static final Map<UUID, LongHashSet> protectedChunks = new HashMap<>();

	private ChunkLoader()
	{

	}

	public static void loadChunkAt(final World world, final int chunkX, final int chunkZ, final boolean generate)
	{
		TaskManager.runTaskSync(() ->
		{
			world.loadChunk(chunkX, chunkZ, generate);
			world.getChunkAt(chunkX, chunkZ);
		});
	}

	public static void unloadChunkAt(final World world, final int chunkX, final int chunkZ, final boolean save, final boolean safe)
	{
		if (!world.isChunkLoaded(chunkX, chunkZ))
			return;

		TaskManager.runTaskSync(() -> world.unloadChunk(chunkX, chunkZ, save, safe));
	}

	public static void refreshChunkAt(final World world, final int chunkX, final int chunkZ)
	{
		TaskManager.runTaskSync(() -> world.refreshChunk(chunkX, chunkZ));
	}

	public static void applyProtection(final World world, final int chunkX, final int chunkZ)
	{
		final UUID worldUID = world.getUID();
		if (!protectedChunks.containsKey(worldUID))
			protectedChunks.put(worldUID, new LongHashSet());

		protectedChunks.get(worldUID).add(chunkX, chunkZ);
	}

	public static void removeProtection(final World world, final int chunkX, final int chunkZ)
	{
		final UUID worldUID = world.getUID();
		if (!protectedChunks.containsKey(worldUID))
			return;

		protectedChunks.get(worldUID).remove(LongHash.toLong(chunkX, chunkZ));

		if (protectedChunks.get(worldUID).isEmpty())
			protectedChunks.remove(worldUID);
	}

	public static boolean isProtected(final World world, final Chunk chunk)
	{
		return isProtected(world) && protectedChunks.get(world.getUID()).contains(chunk.getX(), chunk.getZ());
	}

	public static boolean isProtected(final World world)
	{
		return protectedChunks.containsKey(world.getUID());
	}

	public static Map<UUID, LongHashSet> getProtectedChunks()
	{
		return protectedChunks;
	}

	public static void loadFromDB(final ConfigurationSection database)
	{
		if (database.isConfigurationSection("protectedChunks"))
		{
			final ConfigurationSection protectedChunksSection = database.getConfigurationSection("protectedChunks");
			for (final String key : protectedChunksSection.getKeys(false))
			{
				if (!protectedChunksSection.isList(key))
					continue;

				final UUID worldUID = UUID.fromString(key);
				for (final String serialized : protectedChunksSection.getStringList(key))
				{
					final String[] pieces = serialized.split(",");
					if (pieces.length < 2)
						continue;

					try
					{
						final int chunkX = Integer.parseInt(pieces[0]);
						final int chunkZ = Integer.parseInt(pieces[1]);
						protectedChunks.computeIfAbsent(worldUID, uid -> new LongHashSet()).add(chunkX, chunkZ);
					}
					catch (final NumberFormatException ignored)
					{
					}
				}
			}
		}
	}

	public static void saveToDB(final ConfigurationSection database)
	{
		final ConfigurationSection protectedChunksSection = database.createSection("protectedChunks");
		for (final Entry<UUID, LongHashSet> entry : protectedChunks.entrySet())
		{
			final LongHashSet hashed = entry.getValue();
			final Collection<String> serialized = new ArrayList<>(hashed.size());
			final Iterator<Long> itr = hashed.iterator();
			while (itr.hasNext())
			{
				final long hash = itr.next();
				serialized.add(LongHash.msw(hash) + "," + LongHash.lsw(hash));
			}
			protectedChunksSection.set(entry.getKey().toString(), serialized);
		}
	}
}
