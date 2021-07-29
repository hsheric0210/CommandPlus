package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.Utils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CompassTargetCommand extends AbstractCommand
{
	protected CompassTargetCommand()
	{
		super("compasstarget");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 4)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
			{
				final Location execPos;

				if (sender instanceof Entity)
					execPos = ((Entity) sender).getLocation();
				else if (sender instanceof BlockCommandSender)
					execPos = ((BlockCommandSender) sender).getBlock().getLocation();
				else
				{
					sender.sendMessage(StringPool.E_EXECUTION_POSITION_NOT_RECOGNIZED);
					return false;
				}

				try
				{
					final Vector pos = Parser.parsePosition(execPos.toVector(), args[1], args[2], args[3]);
					if (pos == null)
					{
						sender.sendMessage(StringPool.E_FAILED_TO_PARSE_POSITION);
						return false;
					}

					final Location loc = pos.toLocation(execPos.getWorld());
					for (final Player player : players)
						player.setCompassTarget(loc);

					sender.sendMessage(ChatColor.GREEN + Utils.serializePlayerArray(players) + "(들)의 나침반 목표 위치를 " + pos + " (으)로 바꾸었습니다.");
				}
				catch (final NumberFormatException ignored)
				{
					return false;
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
