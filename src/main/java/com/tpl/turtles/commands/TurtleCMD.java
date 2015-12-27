package com.tpl.turtles.commands;

import com.google.common.reflect.ClassPath;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

//import me.Mark.MT.Script;
import com.tpl.turtles.Turtle;
import com.tpl.turtles.TurtleMgr;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

public class TurtleCMD implements CommandExecutor, TabCompleter {
	
	private static final String[] DIR_STRINGS = {"NORTH", "SOUTH", "EAST", "WEST", "UP", "DOWN", "RIGHT", "LEFT", "FORWARD", "BACK"};
	private static final String DIR_STRING = StringUtils.join(DIR_STRINGS, " ");
	
	private static final String[] CMDS_STRINGS = {"delete","move", "rotate", "mine", "place"};
	private static final String CMD_STRING = StringUtils.join(CMDS_STRINGS," ");
	
	private static final List<String> mats = new ArrayList<>();
	
	static {
		for (Material material : Material.values()) {
			mats.add(material.name());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//0 1 2 3 4
		//1 2 3 4 5
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage("There are "+TurtleMgr.getTurtles().size()+" turtles:");
			for (Turtle tur : TurtleMgr.getTurtles()) {
				sender.sendMessage(tur.getName());
			}
			return false;
		}
		if(args[0].equalsIgnoreCase("all")) {
			

			return false;
		}
		
		String name = args[0];
		Turtle t = TurtleMgr.getByName(name);
		if (t == null) {
			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
			return false;
		}
		Player owner = t.getOwner();
		if (owner != sender) {
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
			t.destroy();

			return false;
		}
		
		if(act.equalsIgnoreCase("move")) {
			if (args.length == 3) {
				t.move(dir);
			} else {
				sender.sendMessage("/t <turtle> move "+DIR_STRING);
			}
			return false;
		}
		
		if(act.equalsIgnoreCase("rotate")) {
			if (args.length == 3) {
				t.rotate(dir);
			} else {
				sender.sendMessage("/t <turtle> rotate "+DIR_STRING);
			}
			return false;
		}
		
		if(act.equalsIgnoreCase("mine")) {
			if (args.length == 3) {
				t.mine(dir);
			} else {
				sender.sendMessage("/t <turtle> mine "+DIR_STRING);
			}
			return false;
		}
		
		if(act.equalsIgnoreCase("place")) {
			if (args.length == 4) {
				t.place(dir, mat);
			} else {
				sender.sendMessage("/t <turtle> place "+DIR_STRING+" <Material>");
			}
			return false;
		}
		sender.sendMessage("/t <turtle> "+CMD_STRING+" ...");
		
		return false;

//		if (args[1].equalsIgnoreCase("list")) {
//			System.out.println(TurtleMgr.getTurtles().size());
//			for(Turtle tur : TurtleMgr.getTurtles()) {
//				System.out.println(tur.getName());
//			}
//			return false;
//		}
//		
//		if (!(args.length > 1)) {
//			sender.sendMessage(help);
//			return false;
//		}
//		String name = args[0];
//		Turtle t = TurtleMgr.getByName(name);
//		if (t == null) {
//			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
//			return false;
//		}
//		Player owner = t.getOwner();
//		if (owner != sender) {
//			sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
//			return false;
//		}
//		
//		if (args[1].equalsIgnoreCase("start")) {
////			if (t.isRunning()) {
////				sender.sendMessage(ChatColor.RED + "Turtle is already running a script. \"/t " + t.getName()
////						+ " stop\" to stop it.");
////				return false;
////			}
//			if (args.length != 5) {
//				sender.sendMessage(help);
//				return false;
//			}
////			t.setScript(Script.getFromConfig(args[3]));
//			BlockFace bf;
//			try {
//				bf = BlockFace.valueOf(args[2].toUpperCase());
//			} catch (Exception e) {
//				sender.sendMessage(ChatColor.RED + "Use the names for blockfaces. eg NORTH, EAST, SOUTH or WEST");
//				return false;
//			}
//			if (bf != BlockFace.NORTH && bf != BlockFace.EAST && bf != BlockFace.SOUTH && bf != BlockFace.WEST) {
//				sender.sendMessage(ChatColor.RED + "You can only use directions; NORTH, EAST, SOUTH or WEST");
//				return false;
//			}
//			t.setDir(bf);
////			t.start(Integer.parseInt(args[4]));
//			sender.sendMessage(ChatColor.GREEN + "Started " + t.getName());
//		} else if (args[1].equalsIgnoreCase("stop")) {
//			if (owner != sender && !sender.hasPermission("turtles.stop")) {
//				sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
//				return false;
//			}
////			t.stop();
//			if (owner != null && t.getOwner() != sender)
//				owner.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + sender.getName() + " stopped "
//						+ t.getName() + "'s current task.");
//			sender.sendMessage(ChatColor.GREEN + "You stopped " + t.getName() + "'s current task.");
//		} else if (args[1].equalsIgnoreCase("destroy")) {
//			if (t.getOwner() == sender || sender.hasPermission("turtles.destroy")) {
//				t.destroy();
//				sender.sendMessage(ChatColor.GREEN + "Destroyed turtle " + t.getName());
//				if (!(t.getOwner() == null))
//					if (sender != t.getOwner())
//						t.getOwner().sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Your turtle" + t.getName()
//								+ " was destroyed by " + sender.getName());
//			} else
//				sender.sendMessage(ChatColor.RED + "You do not own that turtle.");
//		}
//		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			List<String> possibles = new ArrayList<>();

			if ("list".toLowerCase().startsWith(args[0].toLowerCase()))
				possibles.add("list");
			for (Turtle t : TurtleMgr.getTurtles()) {
				if (sender != t.getOwner())
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
		if (!args[0].toLowerCase().equalsIgnoreCase("list")) {
			if (args.length == 2) {
				List<String> pos = new ArrayList<>();
				for (String s : CMDS_STRINGS) {
					if (s.startsWith(args[1].toLowerCase())) {
						pos.add(s);
					}
				}
				return pos;
			}
			if (args.length == 3) {
				System.out.println("three");
				List<String> pos = new ArrayList<>();
				for (String s : DIR_STRINGS)
					if (s.toLowerCase().startsWith(args[2].toLowerCase()))
						pos.add(s);
				return pos;
			}
			if (args.length == 4 && args[1].equalsIgnoreCase("place")) {

				List<String> pos = new ArrayList<>();
				for (String s : mats)
					if (s.toLowerCase().startsWith(args[3].toLowerCase()))
						pos.add(s);
				System.out.println("four "+mats.size()+" "+pos.size()+" "+args[3]);
				return pos;
			}
		}
		
		return null;
	}
}
