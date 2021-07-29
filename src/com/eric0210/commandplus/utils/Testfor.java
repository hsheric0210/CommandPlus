package com.eric0210.commandplus.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import com.google.common.base.Joiner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class Testfor
{
	private Testfor()
	{
	}

	public static int multipleItems(final Inventory inventory, final int slot, final Material itemType, final String amount, final short data, final List<String> datatags, final boolean searchAll)
	{
		if (slot < 0 && !searchAll)
			return 0;

		if (searchAll)
			return (int) IntStream.range(0, inventory.getSize()).filter(i -> singleItem(inventory.getItem(i), itemType, amount, data, datatags)).count();

		if (singleItem(inventory.getItem(slot), itemType, amount, data, datatags))
			return 1;

		return 0;
	}

	public static boolean singleItem(final ItemStack item, final Material itemType, final String amount, final short data, final Collection<String> datatags)
	{
		if (item == null)
			return itemType == Material.AIR;

		final char amountComparatorChar = amount.length() >= 2 && (amount.charAt(0) == '>' || amount.charAt(0) == '<') ? amount.charAt(0) : '\0';
		final boolean amountComparatorOrEq = amount.length() >= 3 && amount.charAt(1) == '=';

		final int _amount = Parser.parseInt(amount.substring(amountComparatorChar == '\0' ? 0 : amountComparatorOrEq ? 2 : 1), -1);
		final boolean amountCheck = _amount < 0 || amountComparatorChar == '\0' && item.getAmount() == _amount || amountComparatorChar == '>' && (amountComparatorOrEq ? item.getAmount() >= _amount : item.getAmount() > _amount) || amountComparatorChar == '<' && (amountComparatorOrEq ? item.getAmount() <= _amount : item.getAmount() < _amount);

		if (datatags != null && !datatags.isEmpty())
		{
			// NBT check
			ItemStack generatedItem = new ItemStack(itemType, _amount, data);
			try
			{
				generatedItem = Bukkit.getUnsafe().modifyItemStack(generatedItem, Joiner.on(' ').join(datatags));
				if (generatedItem.isSimilar(item) && amountCheck)
					return true;
			}
			catch (final Throwable t)
			{
				t.printStackTrace();
			}
		}
		else
			return item.getType() == itemType && amountCheck && (data < 0 || item.getDurability() == data); // Fuzzy (ignore NBT)

		return false;
	}
}
