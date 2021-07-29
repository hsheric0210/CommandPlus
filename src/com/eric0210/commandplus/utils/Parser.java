package com.eric0210.commandplus.utils;

import org.bukkit.util.Vector;

public final class Parser
{
	private Parser()
	{
	}

	public static boolean parseBoolean(final String str)
	{
		return "true".equalsIgnoreCase(str) || parseInt(str, 0) > 0 || "y".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str);
	}

	public static int parseInt(final String string, final int _default)
	{
		try
		{
			return Integer.parseInt(string);
		}
		catch (final NumberFormatException ignored)
		{
		}
		return _default;
	}

	public static double parseDouble(final String string, final double _default)
	{
		try
		{
			final double val = Double.parseDouble(string);
			if (Double.isFinite(val))
				return val;
		}
		catch (final NumberFormatException ignored)
		{
		}
		return _default;
	}

	public static double parseSinglePosition(String paramString, final double executionPosition)
	{
		final boolean relative = !paramString.isEmpty() && paramString.charAt(0) == '~';
		if (relative && Double.isNaN(executionPosition))
			return -1;

		double currentCoord = relative ? executionPosition : 0.0D;

		if (!relative || paramString.length() > 1)
		{
			final boolean absoluteValue = paramString.contains(".");

			if (relative)
				paramString = paramString.substring(1);

			final double val = parseDouble(paramString, 0);
			if (val == 0)
				return Math.min(Math.max(currentCoord, -30000000), 30000000);

			currentCoord += val;

			if (!absoluteValue && !relative)
				currentCoord += 0.5D;
		}

		return Math.min(Math.max(currentCoord, -30000000), 30000000);
	}

	public static double parseVelocity(String paramString, final double executionVelocity)
	{
		final boolean relative = !paramString.isEmpty() && paramString.charAt(0) == '~';
		if (relative && Double.isNaN(executionVelocity))
			return -1;

		double value = relative ? executionVelocity : 0.0D;

		final boolean multiply = relative && paramString.length() > 2 && (paramString.charAt(1) == 'x' || paramString.charAt(1) == '*');
		if (!relative || paramString.length() > 1)
		{
			if (relative)
			{
				paramString = paramString.substring(1);
				if (multiply)
					paramString = paramString.substring(1);
			}

			final double val = parseDouble(paramString, 0);
			if (val == 0)
				return value;

			if (multiply)
				value *= val;
			else
				value += val;
		}

		return value;
	}

	public static Vector parsePosition(final Vector execpos, final String x_str, final String y_str, final String z_str)
	{
		final double x = parseSinglePosition(x_str, execpos.getX());
		final double y = parseSinglePosition(y_str, execpos.getY());
		final double z = parseSinglePosition(z_str, execpos.getZ());

		if (x == -99999999 || y == -99999999 || z == -99999999) // ?
			return null;

		return new Vector(x, y, z);
	}
}
