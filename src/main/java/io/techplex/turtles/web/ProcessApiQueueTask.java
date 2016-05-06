/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;


import com.tpl.turtles.Turtle;
import com.tpl.turtles.TurtleMgr;
import com.tpl.turtles.plumbing.Main;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author techplex
 */
public class ProcessApiQueueTask extends BukkitRunnable {

	@Override
	public void run() {
		ApiAction action = WebApi.getInstance().getNextApiAction();
		if (action != null) {
			Main.getInstance().getLogger().warning("--------Runnable: "+action.getAction());
			String[] args = action.getAction().split(" ");
			Turtle t = TurtleMgr.getInstance().getByName(args[0]);
			
			String act = "";
			if (args.length >= 2) {
				act = args[1];
			}

			String dir = "";
			if (args.length >= 3) {
				dir = args[2];
			}

			String mat = "";
			if (args.length >= 4) {
				mat = args[3];
			}
			
			if (act.equalsIgnoreCase("move"))if (act.equalsIgnoreCase("delete")) {
				t.destroy(true);

			}

			if (act.equalsIgnoreCase("fw") || act.equalsIgnoreCase("firework")) {
				t.makeFirework();
			}

			if (act.equalsIgnoreCase("blink")) {
				t.toggleBlink();
			}

			if (act.equalsIgnoreCase("move")) {
				if (args.length == 3) {
					t.move(dir);
				}
			}

			if (act.equalsIgnoreCase("rotate")) {
				if (args.length == 3) {
					t.rotate(dir);
				}
			}

			if (act.equalsIgnoreCase("mine")) {
				if (args.length == 3) {
					t.mine(dir);
				}
			}

			if (act.equalsIgnoreCase("place")) {
				if (args.length == 4) {
					t.place(dir, mat);
				}
			}
			
			
		}
	}
	
}
