package com.eric0210.commandplus;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eric0210.commandplus.commands.AbstractCommand;
import com.eric0210.commandplus.utils.*;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class CommandPlus extends JavaPlugin
{
	private static final Logger LOGGER = Logger.getLogger("Command+");
	public static CommandPlus INSTANCE;
	public static final Map<UUID, Set<Entry<String, Vector>>> targetPositions = new HashMap<>();

	private static final File DATABASE_FILE = new File(Bukkit.getWorldContainer(), "command+.yml");
	private static final YamlConfiguration database = new YamlConfiguration();

	@Override
	public void onEnable()
	{
		INSTANCE = this;

		LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Registering commands...");
		AbstractCommand.registerAll(this);
		LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Done registering commands.");

		LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Registering event listener...");
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Done registering event listener.");

		if (DATABASE_FILE.exists())
		{
			LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Parsing existing database file (command+.yml)...");
			try
			{
				database.load(DATABASE_FILE);
				loadFromDB();
			}
			catch (final InvalidConfigurationException | IOException ex)
			{
				LOGGER.log(Level.WARNING, StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Failed to parse existing database file! (" + ex.getMessage() + ")", ex);
			}
			LOGGER.info(StringPool.PLUGIN_PREFIX_WITHOUT_COLOR + "Done parsing existing database file (command+.yml).");
		}

		for (final PlayerWrapper wrapper : PlayerWrapper.getPlayerWrappers().values())
			if (wrapper.player.isOnline())
			{
				final Player p = (Player) wrapper.player;
				p.setHealth(wrapper.maxHealth);
				p.setMaxHealth(wrapper.maxHealth);
				p.setFoodLevel(wrapper.foodLevel);
			}

		for (final Entry<UUID, LongHashSet> entry : ChunkLoader.getProtectedChunks().entrySet())
		{
			final World world = Bukkit.getWorld(entry.getKey());

			final Iterator<Long> itr = entry.getValue().iterator();
			while (itr.hasNext())
			{
				final long hash = itr.next();
				final int chunkX = LongHash.msw(hash);
				final int chunkZ = LongHash.lsw(hash);
				ChunkLoader.loadChunkAt(world, chunkX, chunkZ, false);
				ChunkLoader.refreshChunkAt(world, chunkX, chunkZ);
			}
		}
	}

	@Override
	public void onDisable()
	{
		saveToDB();
		TaskManager.shutdown();
	}

	public static void saveToDB()
	{
		if (INSTANCE == null || !INSTANCE.isEnabled())
			return;

		PlayerWrapper.saveToDB(database);
		CommandUtils.saveToDB(database);
		VehicleUtils.saveToDB(database);
		ChunkLoader.saveToDB(database);

		TaskManager.runTaskAsync(() ->
		{
			try
			{
				if (DATABASE_FILE.exists() || DATABASE_FILE.createNewFile())
					database.save(DATABASE_FILE);
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		});
	}

	public static void loadFromDB()
	{
		PlayerWrapper.loadFromDB(database);
		CommandUtils.loadFromDB(database);
		VehicleUtils.loadFromDB(database);
		ChunkLoader.loadFromDB(database);
	}
}
