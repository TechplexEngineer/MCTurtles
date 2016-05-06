package io.techplex.turtles.plumbing;

import io.techplex.turtles.Turtle;
import io.techplex.turtles.TurtleMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

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
			for (Map.Entry<String, Turtle> t : TurtleMgr.getInstance().getTurtles().entrySet()) {
				sender.sendMessage(t.getValue().getName());
			}
			return true;
		}
		
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		
//		if (args[0].equalsIgnoreCase("book")) {
//			
//			Book.give2player(p, DocBook.getDocBook());
//			return true;
//		}
		
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
			TurtleMgr.getInstance().persistTurtles();
			sender.sendMessage("Persisting turtles");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("restore")) {
			int num = TurtleMgr.getInstance().restoreTurtles();
			sender.sendMessage("Restored "+num+" turtles");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			TurtleMgr.getInstance().persistTurtles();
			TurtleMgr.getInstance().restoreTurtles();
			return true;
		}
		
//		if (args[0].equalsIgnoreCase("jstest")) {
//			String code = "var tm = com.tpl.turtles.TurtleMgr.getInstance();\n" +
//						"var turtles = tm.getTurtles();\n" +
//						"var turtle = turtles[0];\n" +
//						"turtle.move(org.bukkit.block.BlockFace.North);";
//			Turtle fred = TurtleMgr.getInstance().getByName("fred");
//			fred.js(code);
//			
//			return true;
//		}
		
//		if(args[0].equalsIgnoreCase("js")) {
//			if (!(sender instanceof Player)) {
//				sender.sendMessage(ChatColor.RED + "You must be a player!");
//				return false;
//			}
//			if (args.length >= 1) {
//				StringBuilder sb = new StringBuilder();
//				for (int i = 1; i < args.length; i++) {
//					sb.append(args[i]);
//					sb.append(" ");
//				}
//				ScriptEngine eng = Scripting.getInstance().getEngineFor(p.getUniqueId());
//				try {
//					sender.sendMessage("Eval:"+sb.toString());
//					Object res = eng.eval(sb.toString());
//					if (res != null) {
//						sender.sendMessage(res.toString());
//					} else {
//						sender.sendMessage("null");
//					}
//					
////					System.out.println(str);
//				} catch (ScriptException ex) {
//					ex.printStackTrace();
//				}
//				return true;
//			} else {
//				sender.sendMessage("/t <turtle> js <javascript>");
//				return false;
//			}
//		}
		
		String name = args[0];
		Turtle t = TurtleMgr.getInstance().getByName(name);
		if (t == null) {
			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
			return false;
		}

		if (p != null && t.getOwner() != p.getUniqueId()) {
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
		
		if(act.equalsIgnoreCase("book")) {
			//@todo cleanup or remove
			//Editing code in books is very much less than idea as one can't move
			//theire cursor around.
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player!");
				return false;
			}
			
			ItemStack book;
			if (p.getUniqueId() != t.getOwner()) {
				sender.sendMessage("Sending non-editable MCTurtles-CommandBook as you are not the owner.");
				book = new ItemStack(Material.WRITTEN_BOOK);
			} else {
				book = new ItemStack(Material.BOOK_AND_QUILL);
			}
			
			BookMeta bookMeta = (BookMeta) book.getItemMeta();
			
			//get the pages
			ArrayList<String> pages = new ArrayList<>();
			//bookMeta.getPages().go;

			//add the page to the list of pages
			StringBuilder page = new StringBuilder();
			page.append("Welcome to MCTurtles. \n");
			page.append("This book contains the command script for '").append(t.getName()).append("' the turtle.");
			page.append("Turtles understand the following commands: \n");
			page.append("move {").append(DIR_STRING).append("} [num_spaces] \n");
			page.append("delete \n");
			page.append("rotate {").append(DIR_STRING).append("}\n");
			page.append("place {").append(DIR_STRING).append("} <material>\n");
			page.append("penDown <material>\n");
			page.append("penUp \n");
			page.append("items in square brackets are optional[...] \n");
			page.append("items in curly braces {a|b} pick one\n");
			pages.add(page.toString());
			
			bookMeta.setPages(pages);

			//set the title and author of this book
			bookMeta.setTitle("Interactive Book");
			bookMeta.setAuthor(t.getName()+" Commands");

			//update the ItemStack with this new meta
			book.setItemMeta(bookMeta);

			p.getInventory().addItem(book);

			return true;
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
		
//		if(act.equalsIgnoreCase("js")) {
//			if (args.length >= 3) {
//				StringBuilder sb = new StringBuilder();
//				for (int i = 2; i < args.length; i++) {
//					sb.append(args[i]);
//					sb.append(" ");
//				}
//				t.js(sb.toString());
//				return true;
//			} else {
//				sender.sendMessage("/t <turtle> js <javascript>");
//				return false;
//			}
//		}

		//else send them the usage message
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
			for (Map.Entry<String, Turtle> t : TurtleMgr.getInstance().getTurtles().entrySet()) {
				Player p = (Player)sender;
				if (p.getUniqueId() != t.getValue().getOwner())
					continue;
				String name = t.getValue().getName();
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
