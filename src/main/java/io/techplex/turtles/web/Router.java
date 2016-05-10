/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * @author techplex
 */
public class Router {
	
	private Map<Pattern, HttpAction> routes;
	
	public Router() {
		routes = new HashMap<>();
	}
	
	public Router addRoute(String route, HttpAction act) {
		Pattern p = Pattern.compile(route);
		
		routes.put(p, act);
		return this;
	}
	
	public Optional<HttpAction> resolve(String url) {
		
		for (Map.Entry<Pattern, HttpAction> pair : routes.entrySet()) {
			Pattern route = pair.getKey();
			if (Pattern.matches(route.pattern(), url)) {
				return Optional.of(pair.getValue());
			}
			
		}
		return Optional.empty();
	}
	
}
