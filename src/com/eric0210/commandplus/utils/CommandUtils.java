package com.eric0210.commandplus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.configuration.ConfigurationSection;

public final class CommandUtils
{
	private static final Collection<String> bannedCommands = new HashSet<>();

	public static final List<String> TABCOMPLETE_TARGETSELECTOR_TYPES = Arrays.asList("p", "a", "r", "f");
	public static final List<String> TABCOMPLETE_TARGETSELECTOR_OPENING_BRACE = Collections.singletonList("[");
	public static final List<String> TABCOMPLETE_TARGETSELECTOR_OPTIONS = Arrays.asList("rm", "r", "lm", "l", "x", "y", "z", "m", "c");
	public static final List<String> TABCOMPLETE_TARGETSELECTOR_CLOSING_BRACE = Collections.singletonList("]");
	public static final List<String> TABCOMPLETE_MATERIAL_NAMES = Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());
	public static final List<String> TABCOMPLETE_BLOCK_SLOT_ID = Collections.singletonList("container.");
	public static final List<String> TABCOMPLETE_ENTITY_SLOT_ID = Arrays.asList("armor.", "enderchest.", "inventory.", "hotbar.");
	public static final List<String> TABCOMPLETE_ARMOR_SLOT_ID = Arrays.asList("head.", "chest.", "legs.", "feet.");
	public static final List<String> TABCOMPLETE_AND_OR = Arrays.asList("and", "or");

	private CommandUtils()
	{
	}

	public static void setCommandBlockAnalogOutput(final BlockCommandSender blockCommandSender, final int signal)
	{
		try
		{
			final Method getTileEntity = blockCommandSender.getClass().getMethod("getTileEntity");
			final Object tileEntity = getTileEntity.invoke(blockCommandSender);
			// net.minecraft.server.<version>.CommandBlockListenerAbstract
			if ("CommandBlockListenerAbstract".equalsIgnoreCase(tileEntity.getClass().getSuperclass().getSimpleName()))
			{
				final Field successCountField = tileEntity.getClass().getSuperclass().getDeclaredField("b"); // successCount
				successCountField.setAccessible(true);

				// Run task on the main thread
				TaskManager.runTaskSync(() ->
				{
					try
					{
						final int prevSuccessCount = (int) successCountField.get(tileEntity);
						successCountField.set(tileEntity, signal);

						if (prevSuccessCount != signal)
							blockCommandSender.getBlock().getState().update(); // Update command block
					}
					catch (final IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				});
			}
		}
		catch (final NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}

	public static void addBannedCommand(final String command)
	{
		bannedCommands.add(command.toLowerCase(Locale.ENGLISH));
	}

	public static boolean isBanned(final String command)
	{
		return bannedCommands.contains(command.toLowerCase(Locale.ENGLISH));
	}

	public static void removeBannedCommand(final String command)
	{
		bannedCommands.remove(command.toLowerCase(Locale.ENGLISH));
	}

	public static void loadFromDB(final ConfigurationSection database)
	{
		if (database.isList("bannedCommands"))
		{
			final List<String> bannedCommandsList = database.getStringList("bannedCommands");
			bannedCommands.addAll(bannedCommandsList.stream().map(cmd -> cmd.toLowerCase(Locale.ENGLISH)).collect(Collectors.toSet()));
		}
	}

	public static void saveToDB(final ConfigurationSection database)
	{
		database.set("bannedCommands", new ArrayList<>(bannedCommands));
	}

	public static List<String> tabCompleteTargetSelector(final String... args)
	{
		if (!args[0].isEmpty() && args[0].charAt(0) == '@') // Target Selector
		{
			final int targetSelectorLength = args[0].length();
			if (targetSelectorLength == 1)
				return TABCOMPLETE_TARGETSELECTOR_TYPES;

			if (targetSelectorLength == 2)
				return TABCOMPLETE_TARGETSELECTOR_OPENING_BRACE;

			if (targetSelectorLength == 3 || args[0].charAt(targetSelectorLength - 1) == ',')
				return TABCOMPLETE_TARGETSELECTOR_OPTIONS;

			return TABCOMPLETE_TARGETSELECTOR_CLOSING_BRACE;
		}

		return Utils.getOnlinePlayerNames();
	}

	public static final List<String> TABCOMPLETE_TARGET_TYPE = Arrays.asList("facingPos", "currentPos");
	public static final List<String> TABCOMPLETE_BLOCK_OR_ENTITY = Arrays.asList("block", "entity");
	public static final List<String> TABCOMPLETE_CHUNKLOAD_MODE = Arrays.asList("load", "unload", "start", "stop", "refresh");
	public static final List<String> TABCOMPLETE_YESNO = Arrays.asList("yes", "no");
	public static final List<String> TABCOMPLETE_VEHICLE_TYPE = Arrays.asList("minecart", "boat");
	public static final List<String> TABCOMPLETE_SLOT_OR_ITEM = Arrays.asList("slot", "item");
}
