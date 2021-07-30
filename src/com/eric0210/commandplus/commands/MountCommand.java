package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.VehicleUtils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;

public class MountCommand extends AbstractCommand
{
	protected MountCommand()
	{
		super("mount");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (argCount >= 6)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
			{
				final String vehicleType = args[1].toLowerCase(Locale.ENGLISH);

				final boolean invisible = Parser.parseBoolean(args[2]);

				final boolean canDismount = argCount >= 7 && Parser.parseBoolean(args[6]);

				final double maxSpeed = argCount >= 8 ? Parser.parseDouble(args[7], 0.4D) : 0.4D;

				for (final Player player : players)
				{
					if (player.isInsideVehicle())
						continue;

					final Vector pos = Parser.parsePosition(player.getLocation().toVector(), args[3], args[4], args[5]);
					if (pos == null)
						continue;

					final Location loc = pos.toLocation(player.getWorld());

					final Vehicle vehicle;
					final String vehicleName;
					switch (vehicleType)
					{
						case "boat":
						{
							vehicle = player.getWorld().spawn(loc, Boat.class);
							vehicleName = "보트";
							((Boat) vehicle).setMaxSpeed(maxSpeed);
							break;
						}

						case "minecart":
						{
							vehicle = player.getWorld().spawn(loc, Minecart.class);
							vehicleName = "마인카트";
							((Minecart) vehicle).setMaxSpeed(maxSpeed);
							break;
						}

						// TODO: More vehicle types

						default:
						{
							sender.sendMessage(StringPool.E_UNKNOWN_VEHICLE_TYPE);
							return false;
						}
					}

					VehicleUtils.markAsSpecial(vehicle);

					vehicle.setPassenger(player);

					if (invisible)
						((CraftEntity) vehicle).getHandle().setInvisible(true);

					if (!canDismount)
						VehicleUtils.addDismountDeniedVehicle(player.getUniqueId(), vehicle.getEntityId());

					sender.sendMessage(String.format(StringPool.MOUNT_MOUNTED, player.getName(), vehicleName));
				}

				CommandPlus.saveToDB();

				return true;
			}

			sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
			return false;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		switch (args.length)
		{
			case 1:
				return CommandUtils.tabCompleteTargetSelector(args);
			case 2:
				return CommandUtils.TABCOMPLETE_VEHICLE_TYPE;
			case 3:
			case 7:
				return CommandUtils.TABCOMPLETE_YESNO;
		}

		return null;
	}

}
