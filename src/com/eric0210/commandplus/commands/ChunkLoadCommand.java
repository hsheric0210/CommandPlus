package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.utils.ChunkLoader;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;

import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class ChunkLoadCommand extends AbstractCommand
{
	protected ChunkLoadCommand()
	{
		super("chunkload");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		World world = null;
		if (sender instanceof Entity)
			world = ((Entity) sender).getWorld();
		else if (sender instanceof BlockCommandSender)
			world = ((BlockCommandSender) sender).getBlock().getWorld();

		if (world == null)
		{
			sender.sendMessage(StringPool.E_WORLD_NOT_FOUND);
			return true;
		}

		if (argCount >= 5)
		{
			try
			{
				final int blockX1 = Integer.parseInt(args[1]);
				final int blockZ1 = Integer.parseInt(args[2]);
				final int blockX2 = Integer.parseInt(args[3]);
				final int blockZ2 = Integer.parseInt(args[4]);

				final int chunkXMin = Math.min(blockX1, blockX2);
				final int chunkXMax = Math.max(blockX1, blockX2);
				final int chunkZMin = Math.min(blockZ1, blockZ2);
				final int chunkZMax = Math.max(blockZ1, blockZ2);

				switch (args[0].toLowerCase(Locale.ENGLISH))
				{
					case "load":
					{
						final boolean generate = argCount >= 6 && Parser.parseBoolean(args[5]);

						for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
							for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
								ChunkLoader.loadChunkAt(world, chunkX, chunkZ, generate);

						sender.sendMessage(String.format(StringPool.CHUNKLOAD_LOADED_CHUNK, chunkXMin, chunkXMax, chunkZMin, chunkZMax));
						break;
					}

					case "unload":
						final boolean safe = argCount >= 6 && Parser.parseBoolean(args[5]);
						for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
							for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
								ChunkLoader.unloadChunkAt(world, chunkX, chunkZ, true, safe);

						sender.sendMessage(String.format(StringPool.CHUNKLOAD_UNLOADED_CHUNK, chunkXMin, chunkXMax, chunkZMin, chunkZMax));
						break;

					case "start":
					{
						final boolean generate = argCount >= 6 && Parser.parseBoolean(args[5]);

						for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
							for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
							{
								ChunkLoader.applyProtection(world, chunkX, chunkZ);
								ChunkLoader.loadChunkAt(world, chunkX, chunkZ, generate);
							}

						CommandPlus.saveToDB();

						sender.sendMessage(String.format(StringPool.CHUNKLOAD_PROTECTION_STARTED, chunkXMin, chunkXMax, chunkZMin, chunkZMax));
						break;
					}

					case "stop":
					{
						for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
							for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
								ChunkLoader.removeProtection(world, chunkX, chunkZ);

						CommandPlus.saveToDB();

						sender.sendMessage(String.format(StringPool.CHUNKLOAD_PROTECTION_STOPPED, chunkXMin, chunkXMax, chunkZMin, chunkZMax));
						break;
					}

					case "refresh":
					{
						for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
							for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
								ChunkLoader.refreshChunkAt(world, chunkX, chunkZ);

						sender.sendMessage(String.format(StringPool.CHUNKLOAD_REFRESHED_CHUNK, chunkXMin, chunkXMax, chunkZMin, chunkZMax));
						break;
					}

					default:
						return false;
				}

				return true;
			}
			catch (final NumberFormatException ignored)
			{
				sender.sendMessage(StringPool.E_CHUNK_COORDINATE_FORMAT_EXCEPTION);
			}

			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		switch (args.length)
		{
			case 1:
				return CommandUtils.TABCOMPLETE_CHUNKLOAD_MODE;
			case 6:
				if (Stream.of("load", "start", "unload").anyMatch(s -> s.equalsIgnoreCase(args[0])))
					return CommandUtils.TABCOMPLETE_YESNO;
		}

		return null;
	}

}
