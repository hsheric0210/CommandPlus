package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.StringPool;
import com.eric0210.commandplus.utils.Testfor;
import com.eric0210.commandplus.utils.Utils;
import com.eric0210.commandplus.utils.selector.PlayerSelector;

import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestForHeldItemCommand extends AbstractCommand
{
	protected TestForHeldItemCommand()
	{
		super("testforhelditem");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		BlockCommandSender cmdBlockSender = null;
		if (sender instanceof BlockCommandSender)
			cmdBlockSender = (BlockCommandSender) sender;

		final int argCount = args.length;
		if (argCount >= 2)
		{

			final Player[] players = PlayerSelector.getPlayers(sender, args[0]);
			if (players.length <= 0)
			{
				sender.sendMessage(StringPool.E_PLAYER_NOT_FOUND);
				return true;
			}

			final String andor = args[1].toLowerCase(Locale.ENGLISH);

			final Material mat = Material.matchMaterial(args[2].toUpperCase(Locale.ENGLISH));
			if (mat == null)
			{
				sender.sendMessage(String.format(StringPool.E_UNKNOWN_ITEM_TYPE, args[2]));
				return false;
			}

			String amount = "";
			if (argCount >= 4)
				amount = args[3];

			short data = -1;
			if (argCount >= 5)
				data = Short.parseShort(args[4]);

			List<String> datatags = null;
			if (argCount >= 6)
				datatags = Utils.subList(args, 5, argCount);

			int count = 0;
			for (final Player player : players)
			{
				final ItemStack helditem = player.getItemInHand();
				if (Testfor.singleItem(helditem, mat, amount, data, datatags))
					count++;
			}

			if ("and".equalsIgnoreCase(andor) ? count == players.length : count > 0)
			{
				if (cmdBlockSender != null)
					CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, count);

				sender.sendMessage(String.format(StringPool.TESTFORHELDITEM_FOUND, Utils.serializePlayerArray(players), count));
			}
			else
			{
				if (cmdBlockSender != null)
					CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, 0);

				sender.sendMessage(String.format(StringPool.TESTFORHELDITEM_NOT_FOUND, Utils.serializePlayerArray(players)));
			}
			return true;
		}

		if (cmdBlockSender != null)
			CommandUtils.setCommandBlockAnalogOutput(cmdBlockSender, 0);
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
				return CommandUtils.TABCOMPLETE_AND_OR;
			case 3:
				return CommandUtils.TABCOMPLETE_MATERIAL_NAMES;
		}

		return null;
	}
}
