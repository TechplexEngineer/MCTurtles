/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import java.io.PrintStream;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

/**
 * Handles request to the web api
 * Request to /t are turned into ApiActions and enqueued for processing on the main server thread
 * @author techplex
 */
public class ApiContainer implements Container {

	@Override
	public void handle(Request req, Response res) {
		try {
			
			PrintStream body = res.getPrintStream();
			long time = System.currentTimeMillis();

			res.setValue("Content-Type", "text/plain");
			res.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
			res.setDate("Date", time);
			res.setDate("Last-Modified", time);

			
			if (req.getPath().toString().equals("/t") && req.getMethod().equals("POST")) {
				//@todo validate the action before adding it to the queue
				WebApi.getInstance().addApiAction(new ApiAction(req.getContent()));
				body.println("Success");
			} else {
				body.println("Hello World");
				body.println(req.getPath());
				body.println(req.getMethod());
			}
				
			
			body.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
