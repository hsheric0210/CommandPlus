package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

public class OpenInventoryCommand extends AbstractCommand
{
	protected OpenInventoryCommand()
	{
		super("openinventory");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (argCount >= 4)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length > 0)
				try
				{
					for (final Player player : players)
					{
						final Vector containerPosition = Parser.parsePosition(player.getLocation().toVector(), args[1], args[2], args[3]);
						if (containerPosition == null)
						{
							sender.sendMessage(StringPool.E_POSITION_FORMAT_EXCEPTION);
							return false;
						}

						final Block containerBlock = containerPosition.toLocation(player.getWorld()).getBlock();
						final BlockState containerState = containerBlock.getState();
						if (containerState instanceof InventoryHolder)
						{
							player.openInventory(((InventoryHolder) containerState).getInventory());
							player.updateInventory();

							sender.sendMessage(String.format(StringPool.OPENINV_OPENED, player.getName(), containerPosition));
						}
						else
							sender.sendMessage(String.format(StringPool.E_INVENTORY_NOT_FOUND, containerPosition.getBlockX(), containerPosition.getBlockY(), containerPosition.getBlockZ(), containerBlock.getType().name()));
					}
					return true;
				}
				catch (final NumberFormatException ignored)
				{

				}
			else
			{
				sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
				return true;
			}
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
