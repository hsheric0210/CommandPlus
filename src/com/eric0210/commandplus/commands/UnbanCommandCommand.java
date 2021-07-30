package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.CommandUtils;
import com.eric0210.commandplus.utils.StringPool;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UnbanCommandCommand extends AbstractCommand
{
	protected UnbanCommandCommand()
	{
		super("unbancommand");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length > 0)
		{
			final String cmd = args[0].toLowerCase(Locale.ENGLISH);

			if (CommandUtils.isBanned(cmd))
			{
				CommandUtils.removeBannedCommand(cmd);
				sender.sendMessage(String.format(StringPool.UNBANCOMMAND_RESPONCE, cmd));
			}
			else
				sender.sendMessage(StringPool.E_COMMAND_NOT_BANNED);

			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		return null;
	}
}
