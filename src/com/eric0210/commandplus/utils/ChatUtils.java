package com.eric0210.commandplus.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils
{
	public static String translateColor(final String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void tell(final CommandSender player, final String message)
	{
		if (player != null && message != null)
			player.sendMessage(translateColor(message));
	}
}
