package com.eric0210.commandplus.commands;

import java.util.HashSet;
import java.util.Set;

import com.eric0210.commandplus.CommandPlus;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter
{
	public static final Set<AbstractCommand> loadedCommands = new HashSet<>();

	private final String command;

	protected AbstractCommand(final String command)
	{
		this.command = command;
	}

	public void register(final CommandPlus pluginInstance)
	{
		pluginInstance.getCommand(command).setExecutor(this);
		pluginInstance.getCommand(command).setTabCompleter(this);
	}

	public static void registerAll(final CommandPlus pluginInstance)
	{
		loadedCommands.add(new BanCommandCommand());
		loadedCommands.add(new UnbanCommandCommand());

		loadedCommands.add(new ChunkLoadCommand());
		loadedCommands.add(new CollectSignalCommand());
		loadedCommands.add(new CompassTargetCommand());

		loadedCommands.add(new DamageDealtCommand());
		loadedCommands.add(new DamageTookCommand());

		loadedCommands.add(new MountCommand());
		loadedCommands.add(new DismountCommand());

		loadedCommands.add(new FlightAllowedCommand());
		loadedCommands.add(new FlyingCommand());

		loadedCommands.add(new TargetCommand());
		loadedCommands.add(new GetTargetCommand());

		loadedCommands.add(new TeleportPlusCommand());

		loadedCommands.add(new TestForHeldItemCommand());
		loadedCommands.add(new TestForItemCommand());
		loadedCommands.add(new HoldItemCommand());

		loadedCommands.add(new VelocityCommand());

		loadedCommands.add(new MaxHealthCommand());
		loadedCommands.add(new FoodLevelCommand());

		loadedCommands.add(new OpenInventoryCommand());

		for (final AbstractCommand command : loadedCommands)
			command.register(pluginInstance);
	}
}
