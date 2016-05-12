/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles;

import com.tpl.turtles.plumbing.TurtleCodePlugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author techplex
 */
public class TurtleCodeRunner {
	
	private final ScriptEngine engine;
	private final String code;
	private long delay = 10;//ticks. 20 ticks in one second

	public TurtleCodeRunner(ScriptEngine engine, String code) {
		this.engine = engine;
		this.code = code;
		
		code = code.replace("'", "\"");
		code = code.replace("\n", "");
		
		try {
			TurtleCodePlugin.getInstance().getLogger().info("Eval TurtleInit");
			engine.eval(new FileReader(new File(TurtleCodePlugin.getInstance().getDataFolder() + File.separator + "javascript" + File.separator + "turtleinit.js")));
			TurtleCodePlugin.getInstance().getLogger().info("Eval Done");
			engine.eval("var myCode = '"+code+"';\n");
		
			engine.eval("var myInterpreter = new Interpreter(myCode, initFunc);"); // This has the nasty side effect that only one turtle per person can be operating at a time.
		} catch (ScriptException ex) {
			TurtleCodePlugin.getInstance().getLogger().log(Level.SEVERE, "Turtle Code Runner Constructor", ex);
			return;
		} catch (FileNotFoundException ex) {
			TurtleCodePlugin.getInstance().getLogger().log(Level.SEVERE, "File not Found", ex);
			return;
		}

		new TurtleCodeStepper(delay).runTaskLater(TurtleCodePlugin.getInstance(), delay);

	}
	
	private class TurtleCodeStepper  extends BukkitRunnable {
		private final long delay;
		private TurtleCodeStepper(long delay) {
			this.delay = delay;
		}
		@Override
		public void run() {
			try {
				Object out = engine.eval("stepOneLine()");

				if (out != null && (boolean) out) {
					new TurtleCodeStepper(delay).runTaskLater(TurtleCodePlugin.getInstance(), delay);
				}

			} catch (ScriptException ex) {
				//@note this most likely is the users code throwing the exception
				TurtleCodePlugin.getInstance().getLogger().log(Level.SEVERE, "Turtle Code Runner Run()", ex);

			}
		}
	}

	
	
}
