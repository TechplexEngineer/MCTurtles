/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web.routes;

import com.tpl.turtles.plumbing.TurtleCodePlugin;
import io.techplex.turtles.web.HttpAction;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 *
 * @author techplex
 */
public class TurtleManagerAction extends HttpAction {

	@Override
	public void run(Optional<Pattern> route, Request req, Response res) {
		PrintStream body;
		try {
			body = res.getPrintStream();
			body.println("Turtle Manager!");
			body.close();
		} catch (IOException ex) {
			TurtleCodePlugin.getInstance().getLogger().log(Level.SEVERE, "Home Action", ex);
		}
	}
	
}
