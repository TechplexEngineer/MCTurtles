package com.tpl.turtles.plumbing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.tpl.turtles.Turtle;
import com.tpl.turtles.TurtleMgr;
import com.tpl.turtles.scripting.Scripting;
import com.tpl.turtles.web.WebServer;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Main extends JavaPlugin {

	public static Main inst;
	public FileConfiguration config;
	
	@Override
	public void onEnable() {
		//Tell the serializer/deserializer about new serializeable classes
		ConfigurationSerialization.registerClass(Turtle.class);

		inst = this;
		configs();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);

		getCommand("turtle").setExecutor(new TurtleCMD());
		getCommand("reloadscripts").setExecutor(new ReloadCMD());

		restoreTurtles();
		
		WebServer.getInstance().start();
	}

	/**
	 * Cleanup any static resources and stop any running tasks
	 */
	@Override
	public void onDisable() {
		persistTurtles();
		WebServer.getInstance().stop();
		TurtleMgr.getInstance().cleanup();
		Scripting.getInstance().cleanup();
	}
	
	public void persistTurtles() {
		YamlConfiguration c = new YamlConfiguration();
		c.set("Turtles", TurtleMgr.getInstance().getTurtles());
		try {
			c.save(new File(getDataFolder() + File.separator + "turtles.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int restoreTurtles() {
		YamlConfiguration c = new YamlConfiguration();
		try {
			File f = new File(getDataFolder() + File.separator + "turtles.yml");
			if (!f.exists()) {
				f.createNewFile();
			} else {
				c.load(f);
				List<Turtle> t = (List<Turtle>)c.get("Turtles"); //@note in the process of deseralizing TurtleMgr all of the turtles are added to the singleton
				if (t != null) {
					TurtleMgr.getInstance().addEach(t);
					return t.size();
				} else {
					return 0;
				}
			}

		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void configs() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		File file = new File(getDataFolder() + File.separator + "config.yml");
		config = getConfig();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			config.options().copyDefaults(true);
			saveConfig();
		}
		if (config.getBoolean("rewriteconfig")) {
			try {
				file.delete();
				file.createNewFile();
				config.options().copyDefaults(true);
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @note I wonder if there was a reason getClassLoader() had protected access
	 * @return 
	 */
	public ClassLoader getClsLdr() {
		return getClassLoader();
	}
}