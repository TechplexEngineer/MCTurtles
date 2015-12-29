package com.tpl.turtles.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.tpl.turtles.Main;

public class ReloadCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("turtles.reload")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission.");
			return false;
		}
		Main.inst.reloadConfig();
		Main.inst.config = Main.inst.getConfig();
		sender.sendMessage(ChatColor.GREEN + "Reloaded the scripts!");
		return true;
	}
}
