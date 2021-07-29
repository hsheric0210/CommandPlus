package com.eric0210.commandplus;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class PlayerWrapper
{
	public static final Map<UUID, PlayerWrapper> playerWrappers = new HashMap<>();

	public final OfflinePlayer player;
	public double damageDealt;
	public double damageTook;
	public double maxHealth;
	public int foodLevel;

	public PlayerWrapper(final OfflinePlayer player)
	{
		this.player = player;
		damageDealt = 1;
		damageTook = 1;
		maxHealth = 20;
		foodLevel = 20;
	}

	public void internalSave(final ConfigurationSection section)
	{
		section.set("damageDealt", damageDealt);
		section.set("damageTook", damageTook);
		section.set("health", maxHealth);
		section.set("food_level", foodLevel);
	}

	private void internalLoad(final ConfigurationSection section)
	{
		damageDealt = section.getDouble("damageDealt", damageDealt);
		damageTook = section.getDouble("damageTook", damageTook);
		maxHealth = section.getDouble("health", maxHealth);
		foodLevel = section.getInt("food_level", foodLevel);
	}

	@Nullable
	public static PlayerWrapper getPlayer(final UUID uuidPlayer)
	{
		return playerWrappers.get(uuidPlayer);
	}

	@Nonnull
	public static PlayerWrapper getPlayer(final Player player)
	{
		return playerWrappers.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerWrapper(player));
	}

	public static Map<UUID, PlayerWrapper> getPlayerWrappers()
	{
		return playerWrappers;
	}

	public static void loadFromDB(final ConfigurationSection database)
	{
		if (database.isConfigurationSection("players"))
		{
			final ConfigurationSection playersSection = database.getConfigurationSection("players");
			for (final String key : playersSection.getKeys(false))
			{
				if (!playersSection.isConfigurationSection(key))
					continue;

				final UUID uuid = UUID.fromString(key);
				final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				if (player.getName() != null)
				{
					final PlayerWrapper wrapper = new PlayerWrapper(player);
					wrapper.internalLoad(playersSection.getConfigurationSection(key));
					playerWrappers.put(uuid, wrapper);
				}
			}
		}
	}

	public static void saveToDB(final ConfigurationSection database)
	{
		if (!playerWrappers.isEmpty())
		{
			final ConfigurationSection playersSection = database.createSection("players");
			for (final Entry<UUID, PlayerWrapper> entry : playerWrappers.entrySet())
				entry.getValue().internalSave(playersSection.createSection(entry.getKey().toString()));
		}
	}
}
