package com.tpl.turtles;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.tpl.turtles.commands.ReloadCMD;
import com.tpl.turtles.commands.TurtleCMD;
import com.tpl.turtles.web.WebServer;
import java.util.List;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Main extends JavaPlugin {

	public static Main inst;
	public FileConfiguration config;
	public static final Material TURTLE_MATERIAL = Material.DISPENSER; //@note use a directioal block
	public static final Material TURTLEWAND_MATERIAL = Material.BLAZE_ROD;
	
	//@note continued: use one of: Banner, Bed, Button, Chest, CocoaPlant, Diode, DirectionalContainer, Dispenser, Door, EnderChest, Furnace, FurnaceAndDispenser, Gate, Ladder, Lever, PistonBaseMaterial, PistonExtensionMaterial, Pumpkin, RedstoneTorch, Sign, SimpleAttachableMaterialData, Skull, Stairs, Torch, TrapDoor, TripwireHook
	
	@Override
	public void onEnable() {
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

	@Override
	public void onDisable() {
		persistTurtles();
		WebServer.getInstance().stop();
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
			if (!f.exists())
				f.createNewFile();
			if (f.length() > 10) {
				c.load(f);
				List<Turtle> t = (List<Turtle>)c.get("Turtles"); //@note in the process of deseralizing TurtleMgr all of the turtles are added to the singleton
				TurtleMgr.getInstance().addEach(t);
				return t.size();
			} else {
				System.out.println("------ The file turtles.yml is too short"+f.length());
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
}