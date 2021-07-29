package com.eric0210.commandplus.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public final class Utils
{
	private Utils()
	{
	}

	public static <T> List<T> subList(final T[] strarr, final int start, final int end)
	{
		return new ArrayList<>(Arrays.asList(strarr).subList(start, end));
	}

	public static List<Player> getOnlinePlayers()
	{
		return Stream.of(CraftServer.class.getMethods()).filter(m -> "getOnlinePlayers".equals(m.getName()) && m.getReturnType() == List.class).map(m ->
		{
			try
			{
				return Collections.unmodifiableList((List<Player>) m.invoke(Bukkit.getServer(), new Object[0]));
			}
			catch (final IllegalAccessException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			return null;
		}).findAny().orElse(null);
	}

	public static List<String> getOnlinePlayerNames()
	{
		return getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
	}

	public static String serializePlayerArray(Player... players)
	{
		if (players == null)
			return "";

		boolean shouldAppendDots = false;
		if (players.length > 6)
		{
			final Player[] prevPlayers = players;
			players = new Player[6];
			System.arraycopy(prevPlayers, 0, players, 0, 6);
			shouldAppendDots = true;
		}

		final StringBuilder builder = new StringBuilder(players.length * 18/* 16(Nickname length limit) + 2(', ' string) */);
		for (final Player p : players)
			builder.append(", ").append(p.getName());

		if (shouldAppendDots)
			builder.append("...");

		return builder.substring(2);
	}
}
