package com.tpl.turtles;

import com.tpl.turtles.utils.SignGUI;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Location;

public class PlayerListener implements Listener {
	

	/**
	 * Handle the removal of turtles when blocks are broken
	 * @param event 
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Main.TURTLE_MATERIAL)
			return;
		Turtle t = TurtleMgr.getInstance().getByLoc(event.getBlock().getLocation());
		if (t == null)
			return;
		if (!event.getPlayer().getName().equalsIgnoreCase(t.getOwner().getName()) && !event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You do not own this turtle.");
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Destroyed turtle " + t.getName());
			t.destroy();
		}
	}
	
	/**
	 * Handle the creation of turtles and opening their inventory
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onOpeninv(PlayerInteractEvent event) {
		if (event.getPlayer().isSneaking() 
				|| event.getClickedBlock() == null 
				|| event.getClickedBlock().getType() != Main.TURTLE_MATERIAL
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		Player p = event.getPlayer();
		Block blk = event.getClickedBlock();
		Turtle t = TurtleMgr.getInstance().getByLoc(blk.getLocation());
		if (t == null) {
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Main.TURTLEWAND_MATERIAL) {
				createTurtle(p, blk.getLocation());
				event.setCancelled(true);
			}
			return;
		}
		if (!t.getOwner().getName().equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You do not own this turtle!");
			return;
		}
		p.openInventory(t.getInventory());
		event.setCancelled(true);
	}
	
	/**
	 * Ask the player for the name of a turtle and create one if name not exists
	 * @param player player who owns the turtle
	 * @param l Location to create the turtle
	 */
	public void createTurtle(Player player, final Location l) {
		//get the name of the turtle from the user
		SignGUI gui = new SignGUI(Main.inst);
		gui.open(player, new String[]{"", "Enter a turtle", "name on the first", "line of this sign."}, new SignGUI.SignGUIListener() {
			@Override
			public void onSignDone(Player player, String[] lines) {
				String name = lines[0];
				if (name.length() == 0) {
					player.sendMessage(ChatColor.RED + "Please enter a valid name.");
					return;
				}
				System.out.println("Creating new turtle named:"+name);
				Turtle t = TurtleMgr.getInstance().getByName(name);
				if (t == null) {
					t = TurtleMgr.getInstance().getNewTurtle(name, Main.TURTLE_MATERIAL, l, player.getName());
					player.sendMessage(ChatColor.GREEN + "Created turtle: " + t.getName());
				} else {
					player.sendMessage(ChatColor.RED + "A turtle with that name already exists.");
				}
			}

		});
	}
}