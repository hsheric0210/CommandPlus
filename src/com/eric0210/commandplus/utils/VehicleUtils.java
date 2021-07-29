package com.eric0210.commandplus.utils;

import java.util.*;
import java.util.Map.Entry;

import com.eric0210.commandplus.CommandPlus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.metadata.Metadatable;

public final class VehicleUtils
{
	private static final Map<UUID, Set<Integer>> dismountDenied = new HashMap<>();

	private static final String MOUNT_VEHICLE_SPECIAL_META = "GENERATED_BY_COMMAND+";

	private VehicleUtils()
	{

	}

	public static void addDismountDeniedVehicle(final UUID playerUUID, final int vehicleEntityID)
	{
		dismountDenied.computeIfAbsent(playerUUID, uuid -> new HashSet<>()).add(vehicleEntityID);
	}

	public static boolean isDismountDeniedVehicle(final UUID playerUUID, final int vehicleEntityID)
	{
		return dismountDenied.containsKey(playerUUID) && dismountDenied.get(playerUUID).contains(vehicleEntityID);
	}

	public static void removeDismountDeniedVehicle(final UUID playerUUID, final int vehicleEntityID)
	{
		if (!dismountDenied.containsKey(playerUUID))
			return;

		dismountDenied.get(playerUUID).remove(vehicleEntityID);

		if (dismountDenied.get(playerUUID).isEmpty())
			dismountDenied.remove(playerUUID);
	}

	public static void markAsSpecial(final Metadatable vehicle)
	{
		MetadataUtils.setMetadata(vehicle, MOUNT_VEHICLE_SPECIAL_META, true);
	}

	public static boolean isMarkedAsSpecial(final Metadatable vehicle)
	{
		return MetadataUtils.hasMetadata(vehicle, MOUNT_VEHICLE_SPECIAL_META) && (boolean) MetadataUtils.getMetadata(vehicle, MOUNT_VEHICLE_SPECIAL_META);
	}

	public static void loadFromDB(final ConfigurationSection database)
	{
		if (database.isConfigurationSection("denyDismount"))
		{
			final ConfigurationSection denyDismountSection = database.getConfigurationSection("denyDismount");
			for (final String key : denyDismountSection.getKeys(false))
			{
				if (!denyDismountSection.isList(key))
					continue;

				final UUID uuid = UUID.fromString(key);
				final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				if (player.getName() != null)
					dismountDenied.put(uuid, new HashSet<>(denyDismountSection.getIntegerList(key)));
			}
		}
	}

	public static void saveToDB(final ConfigurationSection database)
	{
		if (!dismountDenied.isEmpty())
		{
			final ConfigurationSection denyDismountSection = database.createSection("denyDismount");
			for (final Entry<UUID, Set<Integer>> entry : dismountDenied.entrySet())
				denyDismountSection.set(entry.getKey().toString(), new ArrayList<>(entry.getValue()));
		}
	}
}
