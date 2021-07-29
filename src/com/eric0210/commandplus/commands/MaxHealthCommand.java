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

public class MaxHealthCommand extends AbstractCommand
{
	protected MaxHealthCommand()
	{
		super("maxhealth");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 2)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
			{
				final String healthString = args[1];
				try
				{
					final double health = Double.parseDouble(healthString);
					if (health < 0.0)
					{
						sender.sendMessage(StringPool.E_NEGATIVE_HEALTH);
						return true;
					}

					for (final Player player : players)
					{
						player.setMaxHealth(health);
						player.setHealth(health);
						PlayerWrapper.getPlayer(player).maxHealth = health;
					}
					CommandPlus.saveToDB();
					sender.sendMessage(ChatColor.GREEN + Utils.serializePlayerArray(players) + "(들)의 최대 체력을 " + health + "로 조정했습니다.");
					return true;
				}
				catch (final NumberFormatException ignored)
				{
					sender.sendMessage(String.format(StringPool.E_FAILED_TO_PARSE_NUMBER, healthString));
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
