package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.*;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HoldItemCommand extends AbstractCommand
{
	protected HoldItemCommand()
	{
		super("holditem");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (argCount >= 3)
		{
			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);

			switch (args[1].toLowerCase(Locale.ENGLISH))
			{
				case "slot":
					final int slot = Parser.parseInt(args[2], -1);
					if (slot >= 0 && slot <= 8)
						for (final Player player : players)
							player.getInventory().setHeldItemSlot(slot);
					sender.sendMessage(ChatColor.GREEN + Utils.serializePlayerArray(players) + "(들)의 핫바 슬롯을 " + slot + "로 바꾸었습니다.");
					break;
				case "item":
					final String itemName = args[2];
					final Material mat = Material.matchMaterial(itemName);
					if (mat == null)
						sender.sendMessage(String.format(StringPool.E_ITEM_TYPE_NOT_RECOGNIZED, itemName));

					String amount = "";
					if (argCount >= 4)
						amount = args[3];

					short data = -1;
					if (argCount >= 5)
						data = Short.parseShort(args[4]);

					List<String> datatags = null;
					if (argCount >= 6)
						datatags = Utils.subList(args, 5, argCount);

					for (final Player player : players)
					{
						int itemslot = -1;
						for (int i = 0; i < 9; i++)
						{
							final ItemStack hotbar = player.getInventory().getItem(i);
							if (Testfor.singleItem(hotbar, mat, amount, data, datatags))
							{
								itemslot = i;
								break;
							}
						}

						if (itemslot != -1)
							player.getInventory().setHeldItemSlot(itemslot);
					}
			}

			return true;
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
				return CommandUtils.TABCOMPLETE_SLOT_OR_ITEM;
			case 3:
				if ("item".equalsIgnoreCase(args[2]))
					return CommandUtils.TABCOMPLETE_MATERIAL_NAMES;
		}

		return null;
	}

}
