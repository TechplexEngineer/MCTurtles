package com.tpl.turtles.scripting;

import com.tpl.turtles.plumbing.Main;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.bukkit.Bukkit;

/**
 * Manage the script engines
 * @author techplex
 */
public class Scripting {
	private final Map<UUID, ScriptEngine> engines;
	private static Scripting inst = null;
	
	public static Scripting getInstance() {
		if (inst == null) {
			inst = new Scripting();
		}
		return inst;
	}

	/**
	 * Static variables are not cleared when bukkit reloads the plugin.
	 */
	public void cleanup() {
		for (Map.Entry<UUID, ScriptEngine> entry : engines.entrySet()) {
			engines.remove(entry.getKey());
		}
		Scripting.inst = null;
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
	public ScriptEngine getEngineFor(UUID p) {
		ScriptEngine se = engines.get(p);
		if (se == null) {
			System.out.println("Creating ScriptEngine for: "+Bukkit.getOfflinePlayer(p).getName());
			se = factory();
			engines.put(p, se);
		}
		return se;
	}
	
	/**
	 * How to build a script engine.
	 * This is where we add the allowed and disallowed 
	 * @return 
	 */
	private ScriptEngine factory() {
		final String NO_JAVASCRIPT_MESSAGE = "No JavaScript Engine available. ScriptCraft will not work without Javascript.";
		
		Thread currentThread = Thread.currentThread();
		ClassLoader previousClassLoader = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(Main.getInstance().getClsLdr());
		try {
//			ScriptEngineManager factory = new ScriptEngineManager();
//			engine = factory.getEngineByName("JavaScript");

			NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
			Set<String> allowed = new HashSet<>();
			allowed.add("com.tpl");
			allowed.add("com.tpl.turtles");
			allowed.add("com.tpl.turtles.TurtleMgr");
			allowed.add("com.tpl.turtles.TurtleMgr.getInstance");
//stop

			ScriptEngine engine = factory.getScriptEngine(new SandboxClassFilter(allowed));
			if (engine == null) {
				System.out.println(NO_JAVASCRIPT_MESSAGE);
			} else {
//				Invocable inv = (Invocable) engine;
//				this.engine.eval(new InputStreamReader(this.getResource("boot.js")));
//				inv.invokeFunction("__scboot", this, engine);
				return engine;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			this.getLogger().severe(e.getMessage());
		} finally {
			currentThread.setContextClassLoader(previousClassLoader);
		}
		return null;
	}
//	private static void putJavaVariablesIntoEngine(ScriptEngine engine, List variables) {
//
//		Bindings bindings = new SimpleBindings();
//
//		for (Object variable : variables) {
//			putJavaVariableIntoEcmaScope(bindings, variable);
//		}
//
//		engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
//	}
//
//	private static void putJavaVariableIntoEcmaScope(Bindings bindings, EcmaVariable variable) {
//
//		String variableName = variable.getName();
//		EcmaValue ecmaValue = variable.getValue();
//		Object javaValue = ecmaValue.getValue();
//
//		bindings.put(variableName, javaValue);
//	}
}