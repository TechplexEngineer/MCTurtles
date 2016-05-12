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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 *
 * @author techplex
 */
public class HomeAction extends HttpAction {

	@Override
	public void run(Request req, Response res) {
		PrintStream body;
		try {
			body = res.getPrintStream();
			body.println("Hooray!");
			body.close();
		} catch (IOException ex) {
			TurtleCodePlugin.getInstance().getLogger().log(Level.SEVERE, "Home Action", ex);
		}
	}
	
}
