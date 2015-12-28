package com.tpl.turtles;

import com.google.common.reflect.ClassPath;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.tpl.turtles.commands.ReloadCMD;
import com.tpl.turtles.commands.TurtleCMD;

public class Main extends JavaPlugin {

	public static Main inst;
	public FileConfiguration config;
	public static final Material TURTLE_MATERIAL = Material.DISPENSER; //@note use a directioal block
	public static final Material TURTLEWAND_MATERIAL = Material.BLAZE_ROD;
	
	//@note continued: use one of: Banner, Bed, Button, Chest, CocoaPlant, Diode, DirectionalContainer, Dispenser, Door, EnderChest, Furnace, FurnaceAndDispenser, Gate, Ladder, Lever, PistonBaseMaterial, PistonExtensionMaterial, Pumpkin, RedstoneTorch, Sign, SimpleAttachableMaterialData, Skull, Stairs, Torch, TrapDoor, TripwireHook

	@Override
	public void onEnable() {
		inst = this;
		configs();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);

		getCommand("turtle").setExecutor(new TurtleCMD());
		getCommand("reloadscripts").setExecutor(new ReloadCMD());

//		YamlConfiguration c = new YamlConfiguration();
//		try {
//			File f = new File(getDataFolder() + File.separator + "turtles.yml");
//			if (!f.exists())
//				f.createNewFile();
//			c.load(f);
//		} catch (IOException | InvalidConfigurationException e) {
//			e.printStackTrace();
//		}
//		for (String s : c.getKeys(false)) {
//			@SuppressWarnings("deprecation")
//			Turtle t = TurtleMgr.getNewTurtle(s,
//					Material.getMaterial(c.getInt(s + ".material")),
//					new Location(Bukkit.getWorld(c.getString(s + ".location.world")), c.getInt(s + ".location.x"), c.getInt(s + ".location.y"), c.getInt(s + ".location.z")),
//					c.getString(s + ".owner"));
//			@SuppressWarnings("unchecked")
//			ItemStack[] content = ((List<ItemStack>) c.get(s + ".inv")).toArray(new ItemStack[0]);
//			t.getInventory().setContents(content);
//		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
			return false;
		}
		if (label.equalsIgnoreCase("turtlerod")) {
			ItemStack rod = new ItemStack(TURTLEWAND_MATERIAL);
			ItemMeta im = rod.getItemMeta();
			im.setDisplayName("Create a Turtle");
			rod.setItemMeta(im);
			((Player) sender).getInventory().addItem(rod);
			((Player) sender).getInventory().addItem(new ItemStack(TURTLE_MATERIAL));
			sender.sendMessage(ChatColor.GREEN + "Click with this rod on a "+TURTLE_MATERIAL.toString()+" to create a turtle.");
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		YamlConfiguration c = new YamlConfiguration();
		c.set("Turtles", TurtleMgr.getInstance());
		try {
			c.save(new File(getDataFolder() + File.separator + "turtles.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if (config.getBoolean("rewriteconfig"))
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