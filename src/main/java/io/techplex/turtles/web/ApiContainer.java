/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import com.tpl.turtles.Turtle;
import com.tpl.turtles.TurtleMgr;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
				int successCount = 0;
				int failCount = 0;
				String[] acts = req.getContent().split("\\n");
				for (String content : acts) {
					if (isValidAction(content)) {
						WebApi.getInstance().addApiAction(new ApiAction(content));
						successCount++;
					} else {
						failCount++;
					}
				}
				body.println("Success: "+successCount);
				body.println("Failure: "+failCount);
				
			} else if(req.getPath().toString().equals("/js/Turtle0") && req.getMethod().equals("POST")) {
				String code = req.getContent();
				String turtleName = "Turtle0";
				
				body.println("Code Exec");
				WebApi.getInstance().addApiAction(new ApiAction(code, turtleName));
				
				
				
			}
			else {
				body.println("Hello World");
				body.println(req.getPath());
				body.println(req.getMethod());
			}
				
			
			body.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isValidAction(String content) {
		String pat = "([^\\s]+)\\s+(move|rotate|mine|place)\\s+(north|south|east|west|left|right|up|down|forward|back)\\s*(\\d+)?";
		Pattern r = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
		Matcher m = r.matcher(content);
		if (m.find()) {
			System.out.println("Turtle " + m.group(1) );
			System.out.println("Action " + m.group(2) );
			System.out.println("Direction " + m.group(3) );
			System.out.println("Amount " + m.group(4) );
			if (TurtleMgr.getInstance().getByName(m.group(1)) != null) {
				return true;
			} else {
				System.out.println("Turtle name not found: "+m.group(0));
				return false;
			}
		} else {
			System.out.println("NO MATCH");
			return false;
		}
	}
	
}
