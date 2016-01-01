package com.tpl.turtles.scripting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.bukkit.entity.Player;

/**
 * Manage the script engines
 * @author techplex
 */
public class Scripting {
	private final Map<Player, ScriptEngine> engines;
	private Scripting inst = null;
	
	public Scripting getInstance() {
		if (inst == null) {
			inst = new Scripting();
		}
		return inst;
	}
	
	/**
	 * Protected as to only be instantiated by getInstance
	 */
	protected Scripting() {
		engines = new HashMap<>();
	}
	
	/**
	 * Lookup or create a script engine for player p
	 * @param p Player to lookup engine for
	 * @return the script engine
	 */
	public ScriptEngine getEngineFor(Player p) {
		ScriptEngine se = engines.get(p);
		if (se == null) {
			se = factory();
		}
		return se;
	}
	
	/**
	 * How to build a script engine.
	 * This is where we add the allowed and disallowed 
	 * @return 
	 */
	private ScriptEngine factory() {
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
		Set<String> allowed = new HashSet<>();
		allowed.add("com.tpl.turtles");

		ScriptEngine engine = factory.getScriptEngine(new SandboxClassFilter(allowed));
		return engine;
	}
}
