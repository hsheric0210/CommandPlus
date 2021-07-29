package com.eric0210.commandplus.utils;

import com.eric0210.commandplus.CommandPlus;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

public final class MetadataUtils
{
	private MetadataUtils()
	{
	}

	public static void setMetadata(final Metadatable metadatable, final String key, final Object data)
	{
		metadatable.setMetadata(key, new FixedMetadataValue(CommandPlus.INSTANCE, data));
	}

	public static void removeMetadata(final Metadatable metadatable, final String key)
	{
		metadatable.removeMetadata(key, CommandPlus.INSTANCE);
	}

	public static boolean hasMetadata(final Metadatable metadatable, final String key)
	{
		return metadatable.hasMetadata(key);
	}

	public static Object getMetadata(final Metadatable metadatable, final String key)
	{
		return metadatable.getMetadata(key).stream().filter(mv -> mv.getOwningPlugin().getName().equalsIgnoreCase(CommandPlus.INSTANCE.getName())).findFirst().map(MetadataValue::value).orElse(null);
	}
}
