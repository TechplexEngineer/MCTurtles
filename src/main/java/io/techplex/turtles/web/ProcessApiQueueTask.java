/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import io.techplex.borderblocks.plumbing.Main;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author techplex
 */
public class ProcessApiQueueTask extends BukkitRunnable {

	@Override
	public void run() {
		ApiAction act = WebApi.getInstance().getNextApiAction();
		Main.getInstance().getLogger().warning("Runnable: "+act.getAction());
	}
	
}
