/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import com.tpl.turtles.plumbing.Main;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

/**
 *
 * @author techplex
 */
public class WebApi {
	private static WebApi inst = null;
	private Connection connection;
	
	ConcurrentLinkedQueue<ApiAction> queue = new ConcurrentLinkedQueue<>();

	
	public static WebApi getInstance() {
		if (inst == null) {
			inst = new WebApi();
		}
		return inst;
	}
	
	public ApiAction getNextApiAction() {
		return queue.poll();
	}
	
	/**
	 * Start the HTTP server thread
	 */
	public void start() {
		
		try {
			Container container = new ApiContainer();
			Server server = new ContainerServer(container);
			connection = new SocketConnection(server);
			SocketAddress address = new InetSocketAddress(8080);

			connection.connect(address);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		ProcessApiQueueTask processor = new ProcessApiQueueTask();
		
		long delay = 20; //ticks to wait before scheduling (usually 20 ticks /sec)
		long period = 20; //ticks to wait between runs
//		processor.runTaskTimer(Main.getInstance(), delay, period);
		
//		if (apiserver == null) {
//			apiserver = new Server();
//			apiserver.GET("/", (req, res) -> {
//				res.respondText("Hello world");
//				
//			})
//			.GET("/hello/:name", (req, res) -> {
//				String name = req.param("name");
//				queue.add(new ApiAction(name));
//				res.respondText("Hello " + name);
//			})
//			.GET("/turtle/:name/:action/:direction", (req, res) -> {
//				String name = req.param("name");
//				String action = req.param("action");
//				String direction = req.param("direction");
////				String name = req.param("name"); /:count
//				
//				queue.add(new ApiAction(name));
//				res.respondText("Hello " + name);
//			})
//			.POST("/t/:command", (req, res) -> {
//				Main.getInstance().getLogger().warning(req.param("command"));
//			})
//			.start(8000);
//			
//		} 
//		else {
//			Main.getInstance().getLogger().warning("apiserver already started");
//		}
	}
	public void stop() {
		try {
			connection.close();
		} catch (IOException ex) {
			Logger.getLogger(WebApi.class.getName()).log(Level.SEVERE, null, ex);
//			Main.getInstance().getLogger().warning("apiserver already started");
		}
	}
	
}
