/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles;

import com.tpl.turtles.utils.Book;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author techplex
 */
public class DocBook {
	
	public static final String TAB = "  ";
	public static final String DIR = "Where <dir> is one of:\n"+TAB+"NORTH SOUTH EAST\n"+TAB+"WEST LEFT RIGHT UP\n"+TAB+"DOWN FORWARD BACK\n\n";
	
	public static ItemStack getDocBook() {
		
		ArrayList<String> pages = new ArrayList<>();

		StringBuilder page1 = new StringBuilder();
		page1.append(ChatColor.BOLD).append("MCTurtles Docs").append(ChatColor.RESET).append("\n\n");
		page1.append("MCTurtles understand many commands but require a player to direct them.\n\n");
		page1.append("In this book the commands MCTurtles understand are documented.\n\n");
		page1.append("Type \"/t book\" to get this book (no quotes)"); //@note it would be cool of /t book worked
		pages.add(page1.toString());
		
		
		StringBuilder page2 = new StringBuilder();
		page2.append("Each command has its own page.\n\n");
		page2.append("A parameter in angle brackets <dir> is required.\n\n");
		page2.append("A parameter in square brackets [num] is optional.\n\n");
		page2.append("More info:\ntechplex.io/mcturtles"); //@todo this really should be a link
		pages.add(page2.toString());

		StringBuilder page3 = new StringBuilder();
		page3.append(ChatColor.BOLD).append("Move <dir> [num]").append(ChatColor.RESET).append("\n\n");
		page3.append("Move the turtle in <dir> direction [num] blocks.\n\n");
		page3.append(DIR);
		page3.append("And [num] is optional, when not provided 1 is used.\n\n");
		pages.add(page3.toString());

		StringBuilder page4 = new StringBuilder();
		page4.append(ChatColor.BOLD).append("Rotate <dir>").append(ChatColor.RESET).append("\n\n");
		page4.append("Rotate the turtle 90° in <dir> direction.\n\n");
		page4.append(DIR);
		pages.add(page4.toString());

		StringBuilder page5 = new StringBuilder();
		page5.append(ChatColor.BOLD).append("Mine <dir>").append(ChatColor.RESET).append("\n\n");
		page5.append("Mine 1 block in <dir> direction.\n\n");
		page5.append(DIR);
		pages.add(page5.toString());

		StringBuilder page6 = new StringBuilder();
		page6.append(ChatColor.BOLD).append("Place <dir> <mat>").append(ChatColor.RESET).append("\n\n");
		page6.append("Place block of type <mat> in <dir> direction.\n\n");
		page6.append("Where <mat> is a valid material. Page 11 lists some common materials.\n\n");
		page6.append(DIR);
		pages.add(page6.toString());
		//@todo obey creative...

		StringBuilder page7 = new StringBuilder();
		page7.append(ChatColor.BOLD).append("PenDown <mat>").append(ChatColor.RESET).append("\n\n");
		page7.append("Place a block each time the turtle moves leaving a trail of <mat> blocks behind.\n\n");
		page7.append("Where <mat> is a valid material. Page 11 lists some common materials.\n\n");
		pages.add(page7.toString());

		StringBuilder page8 = new StringBuilder();
		page8.append(ChatColor.BOLD).append("PenUp").append(ChatColor.RESET).append("\n\n");
		page8.append("Stop leaving a trail of blocks.\n\n");
		pages.add(page8.toString());

		StringBuilder page9 = new StringBuilder();
		page9.append(ChatColor.BOLD).append("Bookmark <name>").append(ChatColor.RESET).append("\n\n");
		page9.append("Save the current location with the name <name>.\n\n");
		pages.add(page9.toString());

		StringBuilder page10 = new StringBuilder();
		page10.append(ChatColor.BOLD).append("GoBookmark <name>").append(ChatColor.RESET).append("\n\n");
		page10.append("Move the turtle to the saved bookmark named <name>.\n\n");
		pages.add(page10.toString());

		StringBuilder page11 = new StringBuilder();
		page11.append(ChatColor.BOLD).append("Common Materials").append(ChatColor.RESET).append("\n\n");
		page11.append("Move the turtle to the saved bookmark named <name>.\n\n");
		page11.append("Dirt").append("\n\n");
		page11.append("Grass").append("\n\n");
		page11.append("Cobblestone").append("\n\n");
		page11.append("Wood").append("\n\n");
		page11.append("Sand").append("\n\n");
		pages.add(page11.toString());
		
		return Book.createBook(pages, "MCTurtles DocBook", "TechplexEngineer", false);
		
	}
	
}
