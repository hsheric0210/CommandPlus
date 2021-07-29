package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.CommandUtils;

import org.bukkit.ChatColor;
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
				sender.sendMessage(ChatColor.GREEN + "명령어 '" + cmd + "'의 사용 금지 조치를 해제하였습니다.");
			}
			else
				sender.sendMessage(ChatColor.RED + "이 명령어는 사용 금지 조치되어 있지 않습니다!");

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
