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
import io.techplex.borderblocks.State;
import io.techplex.borderblocks.plumbing.BlockPlayerListener;
import io.techplex.borderblocks.plumbing.PlayerMoveListener;
import io.techplex.turtles.web.WebApi;
import org.bukkit.Location;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class TurtleCodePlugin extends JavaPlugin {

	private static TurtleCodePlugin inst;
	public FileConfiguration config;
	private PlayerMoveListener playerMoveListener;
	
	public static TurtleCodePlugin getInstance() {
		if (inst != null) {
			return inst;
		}
		//This should never happen because to call Main.getInstance the plugin has to have been initialized.
		throw new NullPointerException("Plugin not yet Initialized!");
	}
	
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
		
		//modules
		(playerMoveListener = new PlayerMoveListener(this)).registerEvents();
		
		pm.registerEvents(new BlockPlayerListener(), this);
        Location loc1 = new Location(getServer().getWorld("world"), -8, 62, -65);
        Location loc2 = new Location(getServer().getWorld("world"), -4, 62, -69);
        State.getInstance().addRestrictedArea(loc1, loc2);
		
		WebApi.getInstance().start();
	}

	/**
	 * Cleanup any static resources and stop any running tasks
	 */
	@Override
	public void onDisable() {
		persistTurtles();
		WebApi.getInstance().stop();
		TurtleMgr.getInstance().cleanup();
		//playerMoveListener = null; // not sure if I should do this or not?
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