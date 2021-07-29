package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public class TeleportPlusCommand extends AbstractCommand
{
	protected TeleportPlusCommand()
	{
		super("tp+");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (argCount >= 4)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);

			for (final Player player : players)
			{
				final Vector pos;
				float yaw = player.getLocation().getYaw(), pitch = player.getLocation().getPitch();

				if (argCount >= 5)
					yaw = (float) Parser.parseSinglePosition(args[4], player.getLocation().getYaw());

				if (argCount >= 6)
					pitch = (float) Parser.parseSinglePosition(args[5], player.getLocation().getPitch());

				if ((pos = Parser.parsePosition(player.getLocation().toVector(), args[1], args[2], args[3])) == null)
				{
					sender.sendMessage(StringPool.E_FAILED_TO_PARSE_POSITION);
					return false;
				}

				player.teleport(new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ(), yaw, pitch), TeleportCause.COMMAND);

				sender.sendMessage(ChatColor.GREEN + player.getName() + "을(를) 성공적으로 " + pos + "으로 순간이동시켰습니다.");
			}
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
