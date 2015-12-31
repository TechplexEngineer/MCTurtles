package com.tpl.turtles.web;

import java.util.concurrent.Executor;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import com.tpl.turtles.plumbing.Main;

/**
 * https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/Executor.html
 * @todo probably want to limit the number of threads this thing spawns
 * @author techplex
 */
public class ThreadPerTaskExecutor implements Executor {
	@Override
	public void execute(Runnable r) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.runTaskLater(Main.inst, r, 0);
	}
}