package com.tpl.turtles.scripting;

import java.util.Set;
import jdk.nashorn.api.scripting.ClassFilter;
/**
 * Only allow certain packages for use in the script engine
 * @author techplex
 */
public class SandboxClassFilter implements ClassFilter {
	private final Set<String> allowed;
  
	public SandboxClassFilter(final Set<String> allowed) {
		this.allowed = allowed;
	}
  
	@Override
	public boolean exposeToScripts(final String className) {
		return this.allowed.contains(className);
	}
}
