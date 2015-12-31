package com.tpl.turtles.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import com.tpl.turtles.plumbing.Main;



/**
 * http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
 * @author techplex
 */
public class WebServer {
	
	private static WebServer inst = null;
	HttpServer server = null;
	
	public static WebServer getInstance() {
		if (inst == null) {
			inst = new WebServer();
		}
		return inst;
	}
	
	protected WebServer() {
		//prevent instantiation
		System.out.println("----- Extracting");
		extractResources("web", Main.inst.getDataFolder());
	}
	
	/**
	 * Start the HTTP server thread
	 */
	public void start() {
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext("/", new index());
			server.createContext("/update", new update());
			server.setExecutor(new ThreadPerTaskExecutor());
			server.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
	
	public void stop() {
		server.stop(2);
		server = null;
	}

    static class index implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

			sendFile(t, "index.html");
        }
    }
	
	private static void sendFile(HttpExchange http, String path) throws IOException {
		File file = new File(Main.inst.getDataFolder() + File.separator + "web" + File.separator + path);
			if (!file.isFile()) {
				// File does not exist or is not a file: reject with 404 error.
				String response = "404 (Not Found)\n";
				http.sendResponseHeaders(404, response.length());
				OutputStream os = http.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} else {
				InputStream stream = new FileInputStream(file);
				http.sendResponseHeaders(200, file.length());
				try (OutputStream os = http.getResponseBody()) {
					IOUtils.copy(stream, os);
				}
			}
	}
	
	static class update implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
			InputStream bodyStream = t.getRequestBody();
			StringWriter body = new StringWriter();
			IOUtils.copy(bodyStream, body, "UTF-8");
			String response = body.toString();
			t.sendResponseHeaders(200, response.length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(response.getBytes());
			}
        }
    }
	
	/**
	 * Extract the files with the prefix 'path' to 'out'
	 * Used to extract the web/ files to the plugin data directory
	 * Based on code from http://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file
	 * @param path
	 * @param out 
	 */
	private void extractResources(String path, File out) {
//		final String path = "sample/folder";
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
