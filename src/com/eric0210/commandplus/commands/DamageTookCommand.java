package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.PlayerWrapper;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.Utils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DamageTookCommand extends AbstractCommand
{
	protected DamageTookCommand()
	{
		super("damagetook");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 2)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
			{
				final String percentString = args[1];
				try
				{
					final double percent = Double.parseDouble(percentString);

					for (final Player player : players)
						PlayerWrapper.getPlayer(player).damageTook = percent / 100;

					sender.sendMessage(String.format(StringPool.DAMAGETOOK_CHANGED, Utils.serializePlayerArray(players), percent));
					CommandPlus.saveToDB();
					return true;
				}
				catch (final NumberFormatException ignored)
				{
					sender.sendMessage(String.format(StringPool.E_NUMBER_FORMAT_EXCEPTION, percentString));
					return false;
				}
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
