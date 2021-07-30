package com.eric0210.commandplus.commands;

import java.util.List;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.StringPool;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class CollectSignalCommand extends AbstractCommand
{
	protected CollectSignalCommand()
	{
		super("collectsignal");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (sender instanceof BlockCommandSender)
		{
			final BlockCommandSender bcs = (BlockCommandSender) sender;
			if (args.length >= 3)
			{
				final Vector pos = Parser.parsePosition(bcs.getBlock().getLocation().toVector(), args[0], args[1], args[2]);
				if (pos == null)
				{
					sender.sendMessage(StringPool.E_POSITION_FORMAT_EXCEPTION);
					return false;
				}

				CommandUtils.setCommandBlockAnalogOutput(bcs, pos.toBlockVector().toLocation(bcs.getBlock().getWorld()).getBlock().getBlockPower());
				return true;
			}
		}
		else
			sender.sendMessage(StringPool.E_ANALOG_OUTPUT_REQUIRED);

		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		return null;
	}
}
