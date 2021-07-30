package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VelocityCommand extends AbstractCommand
{
	protected VelocityCommand()
	{
		super("velocity");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 4)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
			{
				for (final Player player : players)
				{
					final Vector prevVelocity = player.getVelocity();
					final Vector newVelocity = new Vector(Parser.parseVelocity(args[1], prevVelocity.getX()), Parser.parseVelocity(args[2], prevVelocity.getY()), Parser.parseVelocity(args[3], prevVelocity.getZ()));
					player.setVelocity(newVelocity);

					sender.sendMessage(String.format(StringPool.VELOCITY_APPLIED, player.getName(), newVelocity));
				}
				return true;
			}

			sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length == 1)
			return CommandUtils.tabCompleteTargetSelector(args);
		return null;
	}

	}
