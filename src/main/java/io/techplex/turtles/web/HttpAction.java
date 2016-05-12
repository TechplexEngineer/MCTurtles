/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 *
 * @author techplex
 */
public abstract class HttpAction {
	
	public abstract void run(Request req, Response res);
	
}
