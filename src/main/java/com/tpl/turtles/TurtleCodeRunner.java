/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles;

import com.tpl.turtles.plumbing.Main;
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
public class TurtleCodeRunner extends BukkitRunnable {
	
	private final ScriptEngine engine;
	private final String code;
	public TurtleCodeRunner(ScriptEngine engine, String code) {
		this.engine = engine;
		this.code = code;
		
		code = code.replace("'", "\"");
		code = code.replace("\n", "");
		
		try {
			Main.getInstance().getLogger().info("Eval TurtleInit");
			engine.eval(new FileReader(new File(Main.getInstance().getDataFolder() + File.separator + "javascript" + File.separator + "turtleinit.js")));
			Main.getInstance().getLogger().info("Eval Done");
			engine.eval("var myCode = '"+code+"';\n");
		
			engine.eval("var myInterpreter = new Interpreter(myCode, initFunc);"); // This has the nasty side effect that only one turtle per person can be operating at a time.
//			engine.eval("myInterpreter.step()");
		} catch (ScriptException ex) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Turtle Code Runner Constructor", ex);
			return;
		} catch (FileNotFoundException ex) {
			Main.getInstance().getLogger().log(Level.SEVERE, "File not Found", ex);
			return;
		}
		long delay = 0;
		long period = 20; //ticks. 20 ticks in one second
		runTaskTimer(Main.getInstance(), delay, period);

	}

	@Override
	public void run() {
		try {
//			System.out.println("Starting Run");
//			Object out = engine.eval("myInterpreter.step()");
			Object out = engine.eval("stepOneLine()");


			
			System.out.println("++++++"+out);
			

			if (out == null || !(boolean)out)
				this.cancel();
		} catch (ScriptException ex) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Turtle Code Runner Run()", ex);
			this.cancel();
		}
	}
	
}
