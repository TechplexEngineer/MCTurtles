/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 *
 * @author techplex
 */
public class Router {
	
	private Map<Pattern, HttpAction> routes;
	private HttpAction defaultRoute;
	private HttpAction notFoundRoute;
	
	public Router() {
		routes = new HashMap<>();
	}
	
	public Router addRoute(String route, HttpAction act) {
		Pattern p = Pattern.compile(route);
		
		routes.put(p, act);
		return this;
	} 
	/**
	 * This sets the default route which is where traffic is routed if it 
	 * matches no other route.
	 * @param act the action to route to
	 * @return this router object to allow chaining.
	 */
	public Router addDefaultRoute(HttpAction act) {
		defaultRoute = act;
		return this;
	}
	/**
	 * This sets the 404 route which is where traffic is routed if it matches no 
	 * other route and there is no default route.
	 * @param act the action to route to
	 * @return this router object to allow chaining.
	 */
	public Router add404Route(HttpAction act) {
		notFoundRoute = act;
		return this;
	}
	
	/**
	 * Given a url path, return the matching route if found.
	 * @param url the url path to match against the route dictionary
	 * @return Optional HTTP action if found
	 */
	public Optional<HttpAction> resolve(String url) {
		
		for (Map.Entry<Pattern, HttpAction> pair : routes.entrySet()) {
			Pattern route = pair.getKey();
			if (Pattern.matches(route.pattern(), url)) {
				return Optional.of(pair.getValue());
			}
			
		}
		return Optional.empty();
	}
	/**
	 * 
	 * @param url
	 * @param req
	 * @param res
	 * @return True if routed to a route or default route, false if 404 sent.
	 */
	public boolean resolveRun(String url, Request req, Response res) {
		Optional<HttpAction> act = resolve(url);
		if (act.isPresent()) {
			act.get().run(req,res);
			return true;
		} else if (defaultRoute != null) {
			defaultRoute.run(req,res);
			return true;
		} else if (notFoundRoute != null) {
			notFoundRoute.run(req,res);
			return true;
		} else {
			try {
				PrintStream body = res.getPrintStream();
				body.println("Sorry! 404 Not Found");
				body.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
}
