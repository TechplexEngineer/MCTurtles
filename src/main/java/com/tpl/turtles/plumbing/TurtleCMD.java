package com.tpl.turtles.plumbing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.tpl.turtles.Turtle;
import com.tpl.turtles.TurtleMgr;

public class TurtleCMD implements CommandExecutor, TabCompleter {
	
	private static final String[] DIR_STRINGS = {"NORTH", "SOUTH", "EAST", "WEST", "UP", "DOWN", "RIGHT", "LEFT", "FORWARD", "BACK"};
	private static final String DIR_STRING = StringUtils.join(DIR_STRINGS, ", ");
	
	private static final String[] CMDS_STRINGS = {"delete","move", "rotate", "mine", "place", "blink", "bookmark", "goBookmark", "firework"};
	private static final String CMD_STRING = StringUtils.join(CMDS_STRINGS,", ");
	
	private static final List<String> mats = new ArrayList<>();
	
	static {
		for (Material material : Material.values()) {
			mats.add(material.name());
		}
	}

	/**
     * Executes the given command, returning its success
     *
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
        if (args.length == 0) {
            //@todo print usage
            sender.sendMessage("You've found the Turtle Command");
            sender.sendMessage("try /t list for a list of turtles");
            return true;
        }
		
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage("There are "+TurtleMgr.getInstance().getTurtles().size()+" turtles:");
			for (Turtle tur : TurtleMgr.getInstance().getTurtles()) {
				sender.sendMessage(tur.getName());
			}
			return true;
		}
		
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		
		if (args[0].equalsIgnoreCase("wand")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player!");
				return false;
			}

			ItemStack rod = new ItemStack(Turtle.TURTLEWAND_MATERIAL);
			ItemMeta im = rod.getItemMeta();
			im.setDisplayName("Create a Turtle");
			rod.setItemMeta(im);
			p.getInventory().addItem(rod);
			p.getInventory().addItem(new ItemStack(Turtle.TURTLE_MATERIAL));
			sender.sendMessage(ChatColor.GREEN + "Click with this wand on a " + Turtle.TURTLE_MATERIAL.toString() + " to create a turtle.");

			return true;
		}
		
		if (args[0].equalsIgnoreCase("persist")) {
			TurtleCodePlugin.getInstance().persistTurtles();
			sender.sendMessage("Persisting turtles");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("restore")) {
			int num = TurtleCodePlugin.getInstance().restoreTurtles();
			sender.sendMessage("Restored "+num+" turtles");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			TurtleCodePlugin.getInstance().persistTurtles();
			TurtleCodePlugin.getInstance().restoreTurtles();
			return true;
		}
		
		String name = args[0];
		Turtle t = TurtleMgr.getInstance().getByName(name);
		if (t == null) {
			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
			return false;
		}

		if (p != null && !t.isPlayerOwner(p)) {
			sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
			return false;
		}
		
		String act = "";
		if (args.length >= 2) {
			act = args[1];
		}

		String dir = "";
		if (args.length >= 3) {
			dir = args[2];
		}
		
		String mat = "";
		if (args.length >= 4) {
			mat = args[3];
		}
		
		if(act.equalsIgnoreCase("delete")) {
			t.destroy(true);
			return true;
		}
		
		if(act.equalsIgnoreCase("fw") || act.equalsIgnoreCase("firework")) {
			t.makeFirework();
			return true;
		}
		
		if(act.equalsIgnoreCase("blink")) {
			t.toggleBlink();
			return true;
		}
		
		if(act.equalsIgnoreCase("move")) {
			if (args.length == 3) {
				t.move(dir);
				return true;
			} else {
				sender.sendMessage("/t <turtle> move "+DIR_STRING);
				return false;
			}
		}
		
		if(act.equalsIgnoreCase("rotate")) {
			if (args.length == 3) {
				t.rotate(dir);
				return true;
			} else {
				sender.sendMessage("/t <turtle> rotate "+DIR_STRING);
				return false;
			}
		}
		
		if(act.equalsIgnoreCase("mine")) {
			if (args.length == 3) {
				t.mine(dir);
				return true;
			} else {
				sender.sendMessage("/t <turtle> mine "+DIR_STRING);
				return false;
			}
		}
		
		if(act.equalsIgnoreCase("place")) {
			if (args.length == 4) {
				t.place(dir, mat);
			} else {
				sender.sendMessage("/t <turtle> place "+DIR_STRING+" <material>");
			}
		}
		
		if(act.equalsIgnoreCase("bookmark")) {
			if (args.length == 3) {
				t.bookmark(dir);
				return true;
			} else {
				sender.sendMessage("/t <turtle> bookmark <bookmark_name>");
				return false;
			}
		}
		
		if(act.equalsIgnoreCase("goBookmark")) {
			if (args.length == 3) {
				t.goBookmark(dir);
				return true;
			} else {
				sender.sendMessage("/t <turtle> goBookmark <bookmark_name>");
				return false;
			}
		}
		if (act.equalsIgnoreCase("penDown")) {
			t.setPenDown(args[2]);
		}
		
		//else send them the usage message
		sender.sendMessage("/t <turtle> "+CMD_STRING+" ...");
		
		return false;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		
		Turtle turtle = TurtleMgr.getInstance().getByName(args[0]);
		
		//turtle name and "list" tab complete
		if (args.length == 1) {
			List<String> possibles = new ArrayList<>();

			if ("list".startsWith(args[0].toLowerCase())) {
				possibles.add("list");
			}
			if ("wand".startsWith(args[0].toLowerCase())) {
				possibles.add("wand");
			}
			for (Turtle t : TurtleMgr.getInstance().getTurtles()) {
				Player p = (Player)sender;
				if (!t.isPlayerOwner(p))
					continue;
				String name = t.getName();
				if (args[0].length() == 0) {
					possibles.add(name);
					continue;
				}
				if (name.toLowerCase().startsWith(args[0].toLowerCase()))
					possibles.add(name);
			}
			
			return possibles;
		}
		if (! ("list|wand".contains(args[0].toLowerCase())) ) {
			// command tab complete
			if (args.length == 2) {
				List<String> pos = new ArrayList<>();
				for (String s : CMDS_STRINGS) {
					if (s.startsWith(args[1].toLowerCase())) {
						pos.add(s);
					}
				}
				return pos;
			}
			
			// direction tab complete
			if (args.length == 3 && ("move|rotate|mine|place".contains(args[1].toLowerCase())) ) {
				List<String> pos = new ArrayList<>();
				for (String s : DIR_STRINGS)
					if (s.toLowerCase().startsWith(args[2].toLowerCase()))
						pos.add(s);
				return pos;
			}
			// bookmark tab complete
			if (args.length == 3 && ("bookmark|gobookmark".contains(args[1].toLowerCase())) ) {
				System.out.println("three");
				List<String> pos = new ArrayList<>();
				for (String s : turtle.getBookmarks().keySet())
					if (s.toLowerCase().startsWith(args[2].toLowerCase()))
						pos.add(s);
				return pos;
			}
			// material tab complete
			if (args.length == 4 && ("place".contains(args[1].toLowerCase())) ) {
				List<String> pos = new ArrayList<>();
				for (String s : mats)
					if (s.toLowerCase().startsWith(args[3].toLowerCase()))
						pos.add(s);
				return pos;
			}
		}
		
		return null;
	}
}
