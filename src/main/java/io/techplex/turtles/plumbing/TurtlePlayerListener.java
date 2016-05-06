package io.techplex.turtles.plumbing;

import io.techplex.turtles.Turtle;
import io.techplex.turtles.TurtleMgr;

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
import org.bukkit.Material;



public class TurtlePlayerListener implements Listener {
	

	/**
	 * Handle the removal of turtles when blocks are broken
	 * @param event 
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Material m = event.getBlock().getType();
		if (m != Turtle.TURTLE_MATERIAL && m != Turtle.TURTLE_BLINK_MATERIAL)
			return;
		Turtle t = TurtleMgr.getInstance().getByLoc(event.getBlock().getLocation());
		if (t == null)
			return;
		if (!event.getPlayer().getName().equalsIgnoreCase(t.getOwnerName()) && !event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You do not own this turtle.");
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Destroyed turtle " + t.getName());
			t.destroy(true);
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
				|| event.getClickedBlock().getType() != Turtle.TURTLE_MATERIAL
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		Player p = event.getPlayer();
		Block blk = event.getClickedBlock();
		Turtle t = TurtleMgr.getInstance().getByLoc(blk.getLocation());
		if (t == null) {
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Turtle.TURTLEWAND_MATERIAL) {
				createTurtle(p, blk.getLocation());
				event.setCancelled(true);
			}
			return;
		}
		if (!t.getOwnerName().equalsIgnoreCase(p.getName())) {
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
//		String name = lines[0];
//        if (name.length() == 0) {
//            player.sendMessage(ChatColor.RED + "Please enter a valid name.");
//            return;
//        }
        String turtleName = "Turtle"+TurtleMgr.getInstance().getNumTurtles();
        System.out.println("Creating new turtle named:"+turtleName);
        Turtle t = TurtleMgr.getInstance().getByName(turtleName);
        while(t!= null) {
            turtleName += "_";
        }
        if (t == null) {
            //@todo Get direction
            t = TurtleMgr.getInstance().getNewTurtle(turtleName, l, player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Created turtle: " + t.getName());
            player.sendMessage(ChatColor.GOLD + "Go to the turtle manager with your web browser to rename and control your Turtle: http://plexon.local:8000/newturtle/"+turtleName);
            
            //@note this is slightly insecure as there is no check to ensure that the person who created the turtle is the one opening the web interface.
            //We could generate a random key and add that to the URL but for now thats overkill
        } else {
            //This should NEVER happen!
            player.sendMessage(ChatColor.RED + "A turtle with that name already exists.");
        }
	}
}