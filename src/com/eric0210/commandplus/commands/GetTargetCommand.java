package com.eric0210.commandplus.commands;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.eric0210.commandplus.CommandPlus;
import com.eric0210.commandplus.utils.StringPool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class GetTargetCommand extends AbstractCommand
{
	protected GetTargetCommand()
	{
		super("gettarget");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (sender instanceof Entity)
		{
			final Entity player = (Entity) sender;
			final Set<Entry<String, Vector>> positions = CommandPlus.targetPositions.get(player.getUniqueId());

			if (positions != null)
				for (final Entry<String, Vector> entry : positions)
				{
					final Vector pos = entry.getValue();
					sender.sendMessage(ChatColor.GREEN + "저장된 위치" + ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + ChatColor.BOLD + entry.getKey() + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "= " + ChatColor.DARK_GRAY + "[(" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + pos + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + ", block=" + ChatColor.DARK_GRAY + "(" + ChatColor.AQUA + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + ", chunk=" + ChatColor.DARK_GRAY + "(" + ChatColor.BLUE + (pos.getBlockX() >> 4) + ", " + (pos.getBlockZ() >> 4) + ChatColor.DARK_GRAY + ")" + ChatColor.GREEN + "]");
				}
			else
				sender.sendMessage(StringPool.E_POSITION_NOT_STORED);
		}
		else
			sender.sendMessage(StringPool.E_NOT_PLAYER);
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		return null;
	}
}
