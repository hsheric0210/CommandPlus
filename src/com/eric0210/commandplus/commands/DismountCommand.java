package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.UUID;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.VehicleUtils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DismountCommand extends AbstractCommand
{
	protected DismountCommand()
	{
		super("dismount");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length >= 1)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length <= 0)
			{
				sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
				return true;
			}

			for (final Player player : players)
			{
				if (!player.isInsideVehicle())
					continue;

				final UUID uuid = player.getUniqueId();
				final Entity vehicle = player.getVehicle();

				VehicleUtils.removeDismountDeniedVehicle(uuid, vehicle.getEntityId());

				vehicle.eject();

				sender.sendMessage(ChatColor.GREEN + player.getName() + "를 해당 플레이어가 타고 있던 이동 수단에게서 강제로 내쫒았습니다.");

				if (VehicleUtils.isMarkedAsSpecial(vehicle))
				{
					vehicle.remove();
					sender.sendMessage(ChatColor.GREEN + player.getName() + "가 타고 있던 이동 수단은 제거되었습니다.");
				}
			}

			CommandPlus.saveToDB();

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
