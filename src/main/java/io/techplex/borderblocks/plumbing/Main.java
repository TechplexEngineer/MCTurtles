/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks.plumbing;

import io.techplex.borderblocks.State;
import io.techplex.turtles.Turtle;
import io.techplex.turtles.TurtleMgr;
import io.techplex.turtles.plumbing.TurtleCMD;
import io.techplex.turtles.plumbing.TurtlePlayerListener;
import io.techplex.turtles.web.WebApi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author techplex
 */
public class Main extends JavaPlugin {
    private Logger log;
    public FileConfiguration config;
    
    private static Main inst;
    public static Main getInstance() {
        if (inst != null) {
            return inst;
        }
        //This should never happen because to call Main.getInstance the plugin has to have been initialized.
        throw new NullPointerException("Plugin not yet Initialized!");
    }

    
    
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        inst = this;
        log = getLogger();
        log.info("Starting BorderBlocks Plugin");
        PluginManager pm = Bukkit.getPluginManager();
        
		pm.registerEvents(new BlockPlayerListener(), this);
        Location loc1 = new Location(getServer().getWorld("world"), -8, 62, -65);
        Location loc2 = new Location(getServer().getWorld("world"), -4, 62, -69);
        State.getInstance().addRestrictedArea(loc1, loc2);
        
        //Tell the serializer/deserializer about new serializeable classes
		ConfigurationSerialization.registerClass(Turtle.class);

		inst = this;
		configs();
		pm.registerEvents(new TurtlePlayerListener(), this);
        TurtleCMD tcmd = new TurtleCMD();
		getCommand("turtle").setExecutor(tcmd);

		TurtleMgr.getInstance().restoreTurtles();
		
		WebApi.getInstance().start();

    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
        State.getInstance().cleanup();
        
        TurtleMgr.getInstance().persistTurtles();
		TurtleMgr.getInstance().cleanup();
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
