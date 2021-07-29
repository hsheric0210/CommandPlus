package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.*;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TestForItemCommand extends AbstractCommand
{
	protected TestForItemCommand()
	{
		super("testforitem");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		final int argCount = args.length;
		if (argCount >= 1)
			switch (args[0])
			{
				case "block":
					if (argCount >= 6)
						try
						{
							return handleTestForBlock(sender, args);
						}
						catch (final NumberFormatException ignored)
						{
						}
					break;
				case "entity":
					if (argCount >= 5)
						try
						{
							return handleTestForEntity(sender, args);
						}
						catch (final NumberFormatException ignored)
						{
						}
					break;
			}
		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length == 1)
			return CommandUtils.TABCOMPLETE_BLOCK_OR_ENTITY;

		if ("entity".equalsIgnoreCase(args[0]))
			switch (args.length)
			{
				case 2:
					return CommandUtils.tabCompleteTargetSelector(args);
				case 3:
					return CommandUtils.TABCOMPLETE_AND_OR;
				case 4:
					return CommandUtils.TABCOMPLETE_SLOT_ID;
				case 5:
					return CommandUtils.TABCOMPLETE_MATERIAL_NAMES;
			}
		else
			switch (args.length)
			{
				case 5:
					return CommandUtils.TABCOMPLETE_SLOT_ID;
				case 6:
					return CommandUtils.TABCOMPLETE_MATERIAL_NAMES;
			}

		return null;
	}

	private static boolean handleTestForEntity(final CommandSender sender, final String... args)
	{
		final Player[] targets = PlayerSelector.getPlayers(sender, args[1]);
		if (targets.length <= 0)
		{
			sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
			return false;
		}

		final String andor = args[2].toLowerCase(Locale.ENGLISH);

		final String rawFullSlotName = args[3].toLowerCase(Locale.ENGLISH);

		final String itemName = args[4];

		String amount = "";
		if (args.length >= 6)
			amount = args[5];

		short data = -1;
		if (args.length >= 7)
			data = Short.parseShort(args[6]);

		List<String> datatags = null;
		if (args.length >= 8)
			datatags = Utils.subList(args, 7, args.length);

		BlockCommandSender cmdBlockSender = null;

		if (sender instanceof BlockCommandSender)
			cmdBlockSender = (BlockCommandSender) sender;

		final Material itemType = Material.matchMaterial(itemName);
		if (itemType == null)
		{
			sender.sendMessage(String.format(StringPool.E_ITEM_TYPE_NOT_RECOGNIZED, itemName));
			return false;
		}

		final String fullSlotName;
		if (rawFullSlotName.startsWith("slot."))
			fullSlotName = rawFullSlotName.substring(5 /* "slot.".length() */);
		else
			fullSlotName = rawFullSlotName;
		final String[] slotPieces = fullSlotName.split("\\.");

		int count = 0;
		for (final Player target : targets)
			if (slotPieces.length >= 2)
			{
				final int slot = Parser.parseInt(slotPieces[1], -1);
				final boolean searchAll = slot == -1 || "all".equalsIgnoreCase(slotPieces[1]);
				switch (slotPieces[0])
				{
					case "armor":
						final ItemStack slotItem;
						switch (slotPieces[1])
						{
							case "head":
								slotItem = target.getInventory().getHelmet();
								break;
							case "chest":
								slotItem = target.getInventory().getChestplate();
								break;
							case "legs":
								slotItem = target.getInventory().getLeggings();
								break;
							case "feet":
								slotItem = target.getInventory().getBoots();
								break;
							default:
								sender.sendMessage(String.format(StringPool.E_ARMOR_SLOT_NOT_RECOGNIZED, rawFullSlotName));
								return false;
						}

						count = Testfor.singleItem(slotItem, itemType, amount, data, datatags) ? 1 : 0;
						break;
					case "enderchest":
						count = Testfor.multipleItems(target.getEnderChest(), slot, itemType, amount, data, datatags, searchAll);
						break;
					case "inventory":
						count = Testfor.multipleItems(target.getInventory(), slot, itemType, amount, data, datatags, searchAll);
						break;
					case "hotbar":
						if (searchAll) // (slot.)hotbar.all support
							for (int i = 0, j = 9 /* PlayerInventory.getHotbarSize() */; i < j; i++)
							{
								if (Testfor.singleItem(target.getInventory().getItem(i), itemType, amount, data, datatags))
								{
									count++;
									break;
								}
							}
						else
							count = Testfor.singleItem(target.getInventory().getItem(slot), itemType, amount, data, datatags) ? 1 : 0;
						break;
				}
			}

		if ("and".equalsIgnoreCase(andor) ? count == targets.length /* AND */ : count > 0 /* OR */)
		{
			if (cmdBlockSender != null)
				CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, count);

			sender.sendMessage(ChatColor.GREEN + Utils.serializePlayerArray(targets) + "(들)에게서 입력된 아이템을 총 " + count + "개 찾았습니다.");
		}
		else
		{
			if (cmdBlockSender != null)
				CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, 0);

			sender.sendMessage(ChatColor.RED + Utils.serializePlayerArray(targets) + "(들)에게서 입력된 아이템을 찾지 못했습니다.");
		}

		return true;
	}

	private static boolean handleTestForBlock(final CommandSender sender, final String... args)
	{
		Location execPos = null;
		BlockCommandSender cmdBlockSender = null;
		if (sender instanceof Entity)
			execPos = ((Entity) sender).getLocation();
		else if (sender instanceof BlockCommandSender)
		{
			cmdBlockSender = (BlockCommandSender) sender;
			execPos = ((BlockCommandSender) sender).getBlock().getLocation();
		}

		if (execPos == null)
		{
			sender.sendMessage(StringPool.E_ANALOG_OUTPUT_REQUIRED);
			return false;
		}

		final World world = execPos.getWorld();

		final Vector pos = Parser.parsePosition(execPos.toVector(), args[1], args[2], args[3]);
		if (pos == null)
		{
			sender.sendMessage(StringPool.E_FAILED_TO_PARSE_POSITION);
			return false;
		}

		final Block block = pos.toLocation(world).getBlock();
		if (block == null)
		{
			sender.sendMessage(StringPool.E_BLOCK_NOT_FOUND);
			return false;
		}

		final BlockState state = block.getState();
		if (!(state instanceof InventoryHolder))
		{
			sender.sendMessage(StringPool.E_INVENTORY_NOT_FOUND);
			return false;
		}
		final Inventory blockinv = ((InventoryHolder) state).getInventory();

		final String rawFullSlotName = args[4].toLowerCase(Locale.ENGLISH);

		final String itemName = args[5];
		final Material itemType = Material.matchMaterial(itemName);
		if (itemType == null)
		{
			sender.sendMessage(String.format(StringPool.E_ITEM_TYPE_NOT_RECOGNIZED, itemName));
			return false;
		}

		String amount = "";
		if (args.length >= 7)
			amount = args[6];

		short data = -1;
		if (args.length >= 8)
			data = Short.parseShort(args[7]);

		List<String> datatags = null;
		if (args.length >= 9)
			datatags = Utils.subList(args, 8, args.length);

		final String fullSlotName;
		if (rawFullSlotName.startsWith("slot."))
			fullSlotName = rawFullSlotName.substring(5);
		else
			fullSlotName = rawFullSlotName;
		final String[] slotPieces = fullSlotName.split("\\.");

		if (slotPieces.length >= 2)
			if ("container".equalsIgnoreCase(slotPieces[0]))
				if ("all".equalsIgnoreCase(slotPieces[1])) // (slot.)container.all support
				{
					final int count = Testfor.multipleItems(blockinv, 0, itemType, amount, data, datatags, true);

					if (cmdBlockSender != null)
						CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, count);

					if (count > 0)
						sender.sendMessage(ChatColor.GREEN + pos.toString() + "(" + block.getType() + ")의 인벤토리에서 입력된 아이템을 총 " + count + "개 찾았습니다.");
					else
						sender.sendMessage(ChatColor.RED + pos.toString() + "(" + block.getType() + ")의 인벤토리에서 입력된 아이템을 찾지 못했습니다.");
				}
				else if (Testfor.singleItem(blockinv.getItem(Parser.parseInt(slotPieces[1], -1)), itemType, amount, data, datatags))
				{
					if (cmdBlockSender != null)
						CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, 1);

					sender.sendMessage(ChatColor.GREEN + pos.toString() + "(" + block.getType() + ")의 인벤토리에서 입력된 아이템을 찾았습니다.");
				}
				else
				{
					if (cmdBlockSender != null)
						CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, 0);

					sender.sendMessage(ChatColor.RED + pos.toString() + "(" + block.getType() + ")의 인벤토리에서 입력된 아이템을 찾지 못했습니다.");
				}
		return true;
	}

}
