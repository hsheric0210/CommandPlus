package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Locale;

import com.eric0210.commandplus.utils.CommandUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BanCommandCommand extends AbstractCommand
{
	protected BanCommandCommand()
	{
		super("bancommand");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length > 0)
		{
			final String cmd = args[0].toLowerCase(Locale.ENGLISH);

			if (CommandUtils.isBanned(cmd))
				sender.sendMessage(ChatColor.RED + "이 명령어는 이미 사용 금지 조치되어 있습니다!");
			else
			{
				CommandUtils.addBannedCommand(cmd);
				sender.sendMessage(ChatColor.GREEN + "명령어 '" + cmd + "'를 사용 금지 조치 시켰습니다.");
			}

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
