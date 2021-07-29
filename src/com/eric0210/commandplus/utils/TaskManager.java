package com.eric0210.commandplus.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.eric0210.commandplus.CommandPlus;

import net.minecraft.util.io.netty.util.concurrent.DefaultThreadFactory;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHash;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;

public final class TaskManager
{
	private static final ExecutorService asyncThreadPool;

	static
	{
		asyncThreadPool = Executors.newCachedThreadPool(new DefaultThreadFactory("CommandPlus-TaskManager-AsyncWorker-", true));
	}

	private TaskManager()
	{
	}

	public static void runTaskSync(final Runnable task)
	{
		Bukkit.getScheduler().runTask(CommandPlus.INSTANCE, task);
	}

	public static void runTaskAsync(final Runnable task)
	{
		asyncThreadPool.execute(task);
	}

	public static <T> Future<T> runTask(final Callable<T> task)
	{
		return Bukkit.getScheduler().callSyncMethod(CommandPlus.INSTANCE, task);
	}

	public static <T> Future<T> runTaskAsync(final Callable<T> task)
	{
		return asyncThreadPool.submit(task);
	}

	public static void shutdown()
	{
		asyncThreadPool.shutdown();
	}
}
