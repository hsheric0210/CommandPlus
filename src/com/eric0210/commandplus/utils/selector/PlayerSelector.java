package com.eric0210.commandplus.utils.selector;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eric0210.commandplus.utils.Parser;
import com.eric0210.commandplus.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

// Re-coded NMS Class PlayerSelector, PlayerDistanceComparator, PlayerList(partially)

public final class PlayerSelector
{
	private static class PlayerDistanceComparator implements Comparator<Player>, Serializable
	{
		private final Location coordinates;

		public PlayerDistanceComparator(final Location chunkCoordinates)
		{
			coordinates = chunkCoordinates;
		}

		@Override
		public int compare(final Player next, final Player prev)
		{
			final double prevDist;
			final double nextDist = next.getLocation().distanceSquared(coordinates);

			if (nextDist < (prevDist = prev.getLocation().distanceSquared(coordinates)))
				return -1;

			if (nextDist > prevDist)
				return 1;

			return 0;
		}
	}

	private static final Pattern playerSelectorPattern = Pattern.compile("^@([parf])(?:\\[([\\w=,!-]*)])?$");
	private static final Pattern optionsPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
	private static final Pattern optionsPattern2 = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");

	@SuppressWarnings("UnnecessaryCodeBlock")
	public static Player[] getPlayers(final CommandSender sender, final String targetSelectorExpression)
	{
		final Location executionPosition;
		if (sender instanceof Entity)
			executionPosition = ((Entity) sender).getLocation().clone();
		else if (sender instanceof BlockCommandSender)
			executionPosition = ((BlockCommandSender) sender).getBlock().getLocation().clone();
		else
			return EMPTY_PLAYER_ARRAY;

		final Matcher targetSelectorMatcher = playerSelectorPattern.matcher(targetSelectorExpression);

		if (targetSelectorMatcher.matches())
		{
			final Map<String, String> options = getOptions(targetSelectorMatcher.group(2));
			final String targetSelectorMode = targetSelectorMatcher.group(1);
			int distanceMin = getDistanceMin(targetSelectorMode);
			int distanceMax = getDistanceMax(targetSelectorMode);
			int levelMin = getLevelMin(targetSelectorMode);
			int levelMax = getLevelRequired(targetSelectorMode);
			int countlimit = getCountLimit(targetSelectorMode);
			int yawMin = getYawMin(targetSelectorMode);
			int yawMax = getYawMax(targetSelectorMode);
			int pitchMin = getPitchMin(targetSelectorMode);
			int pitchMax = getPitchMax(targetSelectorMode);
			int gamemode = -1;

			final Map<String, Integer> scoreMap = createScoreMap(options);

			String name = null;
			String team = null;
			boolean setworld = false;

			{
				// Minimum Distance
				if (options.containsKey("dm"))
				{
					distanceMin = Parser.parseInt(options.get("dm"), distanceMin);
					setworld = true;
				}
				if (options.containsKey("distancemin"))
				{
					distanceMin = Parser.parseInt(options.get("distancemin"), distanceMin);
					setworld = true;
				}
				if (options.containsKey("rm"))
				{
					distanceMin = Parser.parseInt(options.get("rm"), distanceMin);
					setworld = true;
				}
				if (options.containsKey("rmin"))
				{
					distanceMin = Parser.parseInt(options.get("rmin"), distanceMin);
					setworld = true;
				}
				if (options.containsKey("radiusmin"))
				{
					distanceMin = Parser.parseInt(options.get("radiusmin"), distanceMin);
					setworld = true;
				}

				// Maximum Distance
				if (options.containsKey("d"))
				{
					distanceMax = Parser.parseInt(options.get("d"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("distancemax"))
				{
					distanceMax = Parser.parseInt(options.get("distancemax"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("distance"))
				{
					distanceMax = Parser.parseInt(options.get("distance"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("rmax"))
				{
					distanceMax = Parser.parseInt(options.get("rmax"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("r"))
				{
					distanceMax = Parser.parseInt(options.get("r"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("radiusmax"))
				{
					distanceMax = Parser.parseInt(options.get("radiusmax"), distanceMax);
					setworld = true;
				}
				if (options.containsKey("radius"))
				{
					distanceMax = Parser.parseInt(options.get("radius"), distanceMax);
					setworld = true;
				}
			}

			{
				// Experience level
				if (options.containsKey("lm"))
					levelMin = Parser.parseInt(options.get("lm"), levelMin);

				if (options.containsKey("l"))
					levelMax = Parser.parseInt(options.get("l"), levelMax);

				if (options.containsKey("levelmin"))
					levelMin = Parser.parseInt(options.get("levelmin"), levelMin);

				if (options.containsKey("levelmax"))
					levelMax = Parser.parseInt(options.get("levelmax"), levelMax);

				if (options.containsKey("level"))
					levelMax = Parser.parseInt(options.get("level"), levelMax);
			}

			{
				// Search center position

				if (options.containsKey("x"))
				{
					executionPosition.setX(Parser.parseSinglePosition(options.get("x"), executionPosition.getBlockX()));
//					executionPosition.setX(Parser.parseInt(options.get("x"), executionPosition.getBlockX()));
					setworld = true;
				}
				if (options.containsKey("y"))
				{
					executionPosition.setY(Parser.parseSinglePosition(options.get("y"), executionPosition.getBlockY()));
//					executionPosition.setY(Parser.parseInt(options.get("y"), executionPosition.getBlockY()));
					setworld = true;
				}
				if (options.containsKey("z"))
				{
					executionPosition.setZ(Parser.parseSinglePosition(options.get("z"), executionPosition.getBlockZ()));
//					executionPosition.setZ(Parser.parseInt(options.get("z"), executionPosition.getBlockZ()));
					setworld = true;
				}

				if (options.containsKey("centerx"))
				{
					executionPosition.setX(Parser.parseSinglePosition(options.get("bx"), executionPosition.getBlockX()));
//					executionPosition.setX(Parser.parseInt(options.get("x"), executionPosition.getBlockX()));
					setworld = true;
				}
				if (options.containsKey("centery"))
				{
					executionPosition.setY(Parser.parseSinglePosition(options.get("by"), executionPosition.getBlockY()));
//					executionPosition.setY(Parser.parseInt(options.get("y"), executionPosition.getBlockY()));
					setworld = true;
				}
				if (options.containsKey("centerz"))
				{
					executionPosition.setZ(Parser.parseSinglePosition(options.get("bz"), executionPosition.getBlockZ()));
//					executionPosition.setZ(Parser.parseInt(options.get("z"), executionPosition.getBlockZ()));
					setworld = true;
				}
			}

			{
				// View angle

				{
					// Yaw
					if (options.containsKey("ryawmin"))
						yawMin = Parser.parseInt(options.get("ryawmin"), yawMin);

					if (options.containsKey("ryawmax"))
						yawMax = Parser.parseInt(options.get("ryawmax"), yawMax);

					if (options.containsKey("ryawm"))
						yawMin = Parser.parseInt(options.get("ryawm"), yawMin);

					if (options.containsKey("ryaw"))
						yawMax = Parser.parseInt(options.get("ryaw"), yawMax);

					if (options.containsKey("yawmin"))
						yawMin = Parser.parseInt(options.get("yawmin"), yawMin);

					if (options.containsKey("yawmax"))
						yawMax = Parser.parseInt(options.get("yawmax"), yawMax);

					if (options.containsKey("yawm"))
						yawMin = Parser.parseInt(options.get("yawm"), yawMin);

					if (options.containsKey("yaw"))
						yawMax = Parser.parseInt(options.get("yaw"), yawMax);

					if (options.containsKey("rxmin"))
						yawMin = Parser.parseInt(options.get("rxmin"), yawMin);

					if (options.containsKey("rxmax"))
						yawMax = Parser.parseInt(options.get("rxmax"), yawMax);

					if (options.containsKey("rxm"))
						yawMin = Parser.parseInt(options.get("rxm"), yawMin);

					if (options.containsKey("rx"))
						yawMax = Parser.parseInt(options.get("rx"), yawMax);
				}

				{
					// Pitch
					if (options.containsKey("rpitchmin"))
						pitchMin = Parser.parseInt(options.get("rpitchmin"), pitchMin);

					if (options.containsKey("rpitchmax"))
						pitchMax = Parser.parseInt(options.get("rpitchmax"), pitchMax);

					if (options.containsKey("rpitchm"))
						pitchMin = Parser.parseInt(options.get("rpitchm"), pitchMin);

					if (options.containsKey("rpitch"))
						pitchMax = Parser.parseInt(options.get("rpitch"), pitchMax);

					if (options.containsKey("pitchmin"))
						pitchMin = Parser.parseInt(options.get("pitchmin"), pitchMin);

					if (options.containsKey("pitchmax"))
						pitchMax = Parser.parseInt(options.get("pitchmax"), pitchMax);

					if (options.containsKey("pitchm"))
						pitchMin = Parser.parseInt(options.get("pitchm"), pitchMin);

					if (options.containsKey("pitch"))
						pitchMax = Parser.parseInt(options.get("pitch"), pitchMax);

					if (options.containsKey("rymin"))
						pitchMin = Parser.parseInt(options.get("rymin"), pitchMin);

					if (options.containsKey("rymax"))
						pitchMax = Parser.parseInt(options.get("rymax"), pitchMax);

					if (options.containsKey("rym"))
						pitchMin = Parser.parseInt(options.get("rym"), pitchMin);

					if (options.containsKey("ry"))
						pitchMax = Parser.parseInt(options.get("ry"), pitchMax);
				}
			}

			{
				// Player gamemode

				if (options.containsKey("gamemode"))
					gamemode = Parser.parseInt(options.get("gamemode"), gamemode);

				if (options.containsKey("mode"))
					gamemode = Parser.parseInt(options.get("mode"), gamemode);

				if (options.containsKey("gm"))
					gamemode = Parser.parseInt(options.get("gm"), gamemode);

				if (options.containsKey("m"))
					gamemode = Parser.parseInt(options.get("m"), gamemode);
			}

			{
				// Select count limit
				if (options.containsKey("countlimit"))
					countlimit = Parser.parseInt(options.get("countlimit"), countlimit);

				if (options.containsKey("limit")) // 1.13+ support
					countlimit = Parser.parseInt(options.get("limit"), countlimit);

				if (options.containsKey("c"))
					countlimit = Parser.parseInt(options.get("c"), countlimit);
			}

			{
				if (options.containsKey("t"))
					team = options.get("t");

				if (options.containsKey("team"))
					team = options.get("team");
			}

			{
				if (options.containsKey("n"))
					name = options.get("n");

				if (options.containsKey("name"))
					name = options.get("name");
			}

			if (!setworld)
				executionPosition.setWorld(null);

			if (!"p".equals(targetSelectorMode) && !"a".equals(targetSelectorMode))
			{
				if ("r".equals(targetSelectorMode))
				{
					List<Player> list = filter(executionPosition, distanceMin, distanceMax, 0, gamemode, levelMin, levelMax, yawMin, yawMax, pitchMin, pitchMax, scoreMap, name, team);
					Collections.shuffle(list);
					list = list.subList(0, Math.min(countlimit, list.size()));
					return list.isEmpty() ? EMPTY_PLAYER_ARRAY : list.toArray(EMPTY_PLAYER_ARRAY);
				}
				return EMPTY_PLAYER_ARRAY;
			}

			final List<Player> list = filter(executionPosition, distanceMin, distanceMax, countlimit, gamemode, levelMin, levelMax, yawMin, yawMax, pitchMin, pitchMax, scoreMap, name, team);
			return list.isEmpty() ? EMPTY_PLAYER_ARRAY : list.toArray(EMPTY_PLAYER_ARRAY);
		}

		// By UUID
		Player getplayerResult = null;
		try
		{
			getplayerResult = Bukkit.getPlayer(UUID.fromString(targetSelectorExpression));
		}
		catch (final IllegalArgumentException e)
		{
		}

		// By Name
		if (getplayerResult == null)
			getplayerResult = Bukkit.getPlayer(targetSelectorExpression);

		if (getplayerResult != null)
			return new Player[]
			{
					getplayerResult
			};

		return EMPTY_PLAYER_ARRAY; // Not Found
	}

	public static Map<String, Integer> createScoreMap(final Map<String, String> map)
	{
		final HashMap<String, Integer> hashmap = new HashMap<>();
		for (final String s : map.keySet())
		{
			if (!s.startsWith("score_") || s.length() <= "score_".length())
				continue;
			final String s1 = s.substring("score_".length());
			hashmap.put(s1, Parser.parseInt(map.get(s), 1));
		}
		return hashmap;
	}

	private static int getDistanceMin(final String targetSelectorMode)
	{
		return 0;
	}

	private static int getDistanceMax(final String targetSelectorMode)
	{
		return 0;
	}

	private static int getLevelRequired(final String targetSelectorMode)
	{
		return Integer.MAX_VALUE;
	}

	private static int getLevelMin(final String targetSelectorMode)
	{
		return 0;
	}

	private static int getCountLimit(final String targetSelectorMode)
	{
		return "a".equals(targetSelectorMode) ? 0 : 1;
	}

	private static int getYawMin(final String targetSelectorMode)
	{
		return -180;
	}

	private static int getYawMax(final String targetSelectorMode)
	{
		return 180;
	}

	private static int getPitchMin(final String targetSelectorMode)
	{
		return -90;
	}

	private static int getPitchMax(final String targetSelectorMode)
	{
		return 90;
	}

	private static Map<String, String> getOptions(final String s)
	{
		final Map<String, String> map = new HashMap<>();
		if (s == null)
			return map;
		Matcher matcher = optionsPattern.matcher(s);
		int matchCount = 0;
		int matcherEnd = -1;
		while (matcher.find())
		{
			String s1 = null;
			switch (matchCount++)
			{
				case 0:
					s1 = "x";
					break;
				case 1:
					s1 = "y";
					break;
				case 2:
					s1 = "z";
					break;
				case 3:
					s1 = "r";
			}
			if (s1 != null && !matcher.group(1).isEmpty())
				map.put(s1, matcher.group(1));
			matcherEnd = matcher.end();
		}

		if (matcherEnd < s.length())
		{
			matcher = optionsPattern2.matcher(matcherEnd == -1 ? s : s.substring(matcherEnd));
			while (matcher.find())
				map.put(matcher.group(1), matcher.group(2));
		}

		return map;
	}

	public static List<Player> filter(final Location executionLocation, final int radiusMin, final int radiusMax, int countLimit, final int gamemode, final int levelMin, final int levelMax, final int yawMin, final int yawMax, final int pitchMin, final int pitchMax, final Map<String, Integer> scoreMap, String name, String team)
	{
		final List<Player> players = Utils.getOnlinePlayers();

		if (players.size() <= 0)
			return Collections.emptyList();

		List<Player> selected = new ArrayList<>(players.size());

		final boolean reverseOrderSort = countLimit < 0;

		final boolean nameNEQ = name != null && !name.isEmpty() && name.charAt(0) == '!';
		final boolean teamNEQ = team != null && !team.isEmpty() && team.charAt(0) == '!';

		final int radiusMinSqr = radiusMin * radiusMin;
		final int radiusMaxSqr = radiusMax * radiusMax;

		countLimit = Math.abs(countLimit);

		if (nameNEQ)
			name = name.substring(1);
		if (teamNEQ)
			team = team.substring(1);

		for (final Player player : players)
		{
			// World, Name check
			if (executionLocation.getWorld() != null && player.getWorld() != executionLocation.getWorld() || name != null && nameNEQ == name.equalsIgnoreCase(player.getName()))
				continue;

			// Team check
			if (team != null)
			{
				final Team scoreboardTeam = player.getScoreboard().getPlayerTeam(player);
				final String scoreboardTeamName = scoreboardTeam == null ? "" : scoreboardTeam.getName();

				if (teamNEQ == team.equalsIgnoreCase(scoreboardTeamName))
					continue;
			}

			// Distance check
			if (radiusMin > 0 || radiusMax > 0)
			{
				final float distance = (float) executionLocation.distanceSquared(player.getLocation());
				if (radiusMin > 0 && distance < radiusMinSqr || radiusMax > 0 && distance > radiusMaxSqr)
					continue;
			}

			// Yaw, Pitch check
			if (yawMin > -180 && player.getLocation().getYaw() < yawMin || yawMax < 180 && player.getLocation().getYaw() > yawMax)
				continue;

			if (pitchMin > -90 && player.getLocation().getYaw() < pitchMin || pitchMax < 90 && player.getLocation().getYaw() > pitchMax)
				continue;

			// Score Check
			if (!checkScoreMap(player, scoreMap) || gamemode != -1 && gamemode != player.getGameMode().getValue() || levelMin > 0 && player.getLevel() < levelMin || player.getLevel() > levelMax)
				continue;

			selected.add(player);
		}

		selected.sort(new PlayerDistanceComparator(executionLocation)); // Sort by distance

		if (reverseOrderSort)
			Collections.reverse(selected);

		if (countLimit > 0)
			selected = selected.subList(0, Math.min(countLimit, selected.size()));

		return selected;
	}

	private static boolean checkScoreMap(final Player player, final Map<String, Integer> scoreMap)
	{
		if (scoreMap != null && !scoreMap.isEmpty())
		{
			boolean flag;
			int i;
			Entry<String, Integer> entry;
			final Iterator<Entry<String, Integer>> iterator = scoreMap.entrySet().iterator();
			do
			{
				final Objective objective;
				if (!iterator.hasNext())
					return true;
				entry = iterator.next();
				String s = entry.getKey();
				flag = false;

				if (s.endsWith("_min") && s.length() > 4)
				{
					flag = true;
					s = s.substring(0, s.length() - 4);
				}

				if ((objective = player.getScoreboard().getObjective(s)) == null)
					return false;

				final Score scoreboardscore = objective.getScore(player.getName());
				i = scoreboardscore.getScore();
				if (i >= entry.getValue() || !flag)
					continue;

				return false;
			}
			while (i <= entry.getValue() || flag);

			return false;
		}
		return true;
	}

	private PlayerSelector()
	{
	}

	private static final Player[] EMPTY_PLAYER_ARRAY = new Player[0];
}
