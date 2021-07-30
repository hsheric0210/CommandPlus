package com.eric0210.commandplus.commands;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.List;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.StringPool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class TargetCommand extends AbstractCommand
{
	protected TargetCommand()
	{
		super("target");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (sender instanceof LivingEntity)
		{
			final LivingEntity player = (LivingEntity) sender;
			final Vector pos;

			if (argCount >= 1 && "currentPos".equalsIgnoreCase(args[0]))
				pos = player.getLocation().toVector();
			else
				pos = player.getTargetBlock(null, 10).getLocation().toVector();

			String key = "default";
			if (argCount >= 2)
				key = args[1];

			if (pos != null)
			{
				final String finalKey = key;
				CommandPlus.targetPositions.computeIfAbsent(player.getUniqueId(), uuid -> new HashSet<>(1)).removeIf(v -> finalKey.equalsIgnoreCase(v.getKey()));
				CommandPlus.targetPositions.get(player.getUniqueId()).add(new SimpleImmutableEntry<>(key, pos));
				sender.sendMessage(String.format(StringPool.TARGET, pos, key));
			}
			else
				sender.sendMessage(StringPool.E_POSITION_NOT_SPECIFIED);
		}
		else
		{
			sender.sendMessage(StringPool.E_NOT_PLAYER);
			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length == 1)
			return CommandUtils.TABCOMPLETE_TARGET_TYPE;
		return null;
	}

	}
