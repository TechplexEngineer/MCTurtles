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
import java.util.regex.Matcher;
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
	
	/**
	 * Add a route to the route map.
	 * @param route a regular expression string to act as the path. Some routes may use provided capture groups.
	 * @param act the action to invoke if the route is matched
	 * @return this router object to allow chaining.
	 */
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
	 * @return 
	 */
	public Optional<Map.Entry<Pattern, HttpAction>> resolve(String url) {
		
		for (Map.Entry<Pattern, HttpAction> pair : routes.entrySet()) {
			Pattern route = pair.getKey();
			if (Pattern.matches(route.pattern(), url)) {
				return Optional.of(pair);
			}
			
		}
		return Optional.empty();
	}
	/**
	 * Resolve the route and execute the run method of the action.
	 * Fail over to default route if not other route matches. If no default fail
	 * to notFoundRoute, and if no NotFoundRoute fail to printing a simple 404 message.
	 * @param url the path of the route to match
	 * @param req the request object for the current HTTP request
	 * @param res the response object for the current HTTP response
	 * @return True if routed to a route or default route, false if 404 sent.
	 */
	public boolean resolveRun(String url, Request req, Response res) {
		Optional<Map.Entry<Pattern, HttpAction>> entry = resolve(url);
		if (entry.isPresent()) {
			entry.get().getValue().run(Optional.of(entry.get().getKey()), req, res);
			return true;
		} else if (defaultRoute != null) {
			defaultRoute.run(Optional.empty(), req, res);
			return true;
		} else if (notFoundRoute != null) {
			notFoundRoute.run(Optional.empty(), req, res);
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
