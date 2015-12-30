/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.web;

import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tpl.turtles.Main;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.IOUtils;



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
            String response = "<!DOCTYPE html>\n<html><head><title>fred</title></head><body><h1>test</h1><form action=\"\" method=\"post\"><textarea name=\"fred\"></textarea></form></body></html>";
			Headers h = t.getResponseHeaders();
			h.add("Content-Type", "text/html");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
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
