/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import com.tpl.turtles.plumbing.TurtleCodePlugin;
import io.techplex.turtles.web.routes.HomeAction;
import io.techplex.turtles.web.routes.TurtleManagerAction;
import io.techplex.turtles.web.routes.jsAction;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
	
	//A queue of actions to be processed on the main server thread. ex: turtle0 move forward
	private ConcurrentLinkedQueue<ApiAction> queue = new ConcurrentLinkedQueue<>();

	
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
	 * Add an ApiAction to the queue to be processed by the runnable on the main thread.
	 * We have to do this because the Bukkit/Spigot API's are not threadsafe, changes
	 * to the world can only be made on the main server thread.
	 * @param act 
	 */
	public void addApiAction(ApiAction act) {
		queue.add(act);
	}
	
	/**
	 * Start the HTTP server thread
	 */
	public void start() {
		
		if (!Files.exists(new File(TurtleCodePlugin.getInstance().getDataFolder() + File.separator + "web").toPath()))
			extractResources("web", TurtleCodePlugin.getInstance().getDataFolder());

		if (!Files.exists(new File(TurtleCodePlugin.getInstance().getDataFolder() + File.separator + "javascript").toPath()))
			extractResources("javascript", TurtleCodePlugin.getInstance().getDataFolder());
		
		Router router = new Router();
		router.addDefaultRoute(new HomeAction());
		router.addRoute("/manager", new TurtleManagerAction());
		router.addRoute("/js/([^/]+)", new jsAction());
		
		try {
			Container container = new ApiContainer(router);
			Server server = new ContainerServer(container);
			connection = new SocketConnection(server);
			SocketAddress address = new InetSocketAddress(8090);

			connection.connect(address);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		ProcessApiQueueTask processor = new ProcessApiQueueTask();
		
		long delay = 20; //ticks to wait before scheduling (usually 20 ticks /sec)
		long period = 20; //ticks to wait between runs
		processor.runTaskTimer(TurtleCodePlugin.getInstance(), delay, period);

	}
	public void stop() {
		try {
			connection.close();
		} catch (IOException ex) {
			Logger.getLogger(WebApi.class.getName()).log(Level.SEVERE, null, ex);
//			Main.getInstance().getLogger().warning("apiserver already started");
		}
	}
	/**
	 * Extract the files with the prefix 'path' to 'out' Used to extract the
	 * web/ files to the plugin data directory Based on code from
	 * http://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file
	 *
	 * @param path where to extract the files from
	 * @param out where to extract the files to
	 */
	private void extractResources(String path, File out) {

		final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

		if (jarFile.isFile()) {
			// Run with JAR file
			try (JarFile jar = new JarFile(jarFile)) {

				final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				while (entries.hasMoreElements()) {

					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.startsWith(path + "/")) { //filter according to the path
						System.out.println(name);
						File file = new File(out + File.separator + name);
						if (entry.isDirectory()) {
							file.mkdirs();
						} else {
							try (InputStream input = jar.getInputStream(entry)) {
								Files.copy(input, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
							}
						}

					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
