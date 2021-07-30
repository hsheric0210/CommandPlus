package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.Utils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyingCommand extends AbstractCommand
{
	protected FlyingCommand()
	{
		super("flying");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 2)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);

			if (players.length <= 0)
			{
				sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
				return true;
			}

			final boolean state = Parser.parseBoolean(args[1]);
			for (final Player player : players)
			{
				if (!player.getAllowFlight())
					player.setAllowFlight(true);
				player.setFlying(state);
			}
			sender.sendMessage(String.format(state ? StringPool.FLYING_ENABLED : StringPool.FLYING_DISABLED, Utils.serializePlayerArray(players)));
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
