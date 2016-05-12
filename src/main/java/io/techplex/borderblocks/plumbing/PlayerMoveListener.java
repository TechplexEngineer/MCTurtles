/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks.plumbing;

import com.google.common.collect.Sets;
import com.tpl.turtles.plumbing.TurtleCodePlugin;
import io.techplex.borderblocks.BorderBlocks;
import io.techplex.borderblocks.MoveType;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

/**
 *
 * @author techplex
 */
public class PlayerMoveListener implements Listener {
	
	private TurtleCodePlugin plugin;
	private Location lastValid;
	
    public PlayerMoveListener(TurtleCodePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(this, plugin);

    }
// We'll assume that if they spawn somewhere they are allowed to be there.
//    @EventHandler
//    public void onPlayerRespawn(PlayerRespawnEvent event) {
//        Player player = event.getPlayer();
//
//        testMoveTo(player, event.getRespawnLocation(), MoveType.RESPAWN, true);
//    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = event.getEntered();
        if (entity instanceof Player) {
            Player player = (Player) entity;

            if (null != testMoveTo(player, null, event.getVehicle().getLocation(), MoveType.EMBARK, true)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
		
        Optional<Location> overriding = testMoveTo(player, event.getFrom(), event.getTo(), MoveType.MOVE, false);

        if (overriding.isPresent()) {
			Location override = overriding.get();
            override.setX(override.getBlockX());// + 0.5
            override.setY(override.getBlockY());
//            override.setZ(override.getBlockZ()); / + 0.5
            override.setPitch(event.getTo().getPitch());
            override.setYaw(event.getTo().getYaw());

            event.setTo(override.clone());

            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                vehicle.eject();

                Entity current = vehicle;
                while (current != null) {
                    current.eject();
                    vehicle.setVelocity(new Vector());
                    if (vehicle instanceof LivingEntity) {
                        vehicle.teleport(override.clone());
                    } else {
                        vehicle.teleport(override.clone().add(0, 1, 0));
                    }
                    current = current.getVehicle();
                }

                player.teleport(override.clone().add(0, 1, 0));

                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.teleport(override.clone().add(0, 1, 0));
                    }
                }, 1);
            }
        }
    }
	
	/**
     * Test movement to the given location.
     *
     * <p>If a non-null {@link Location} is returned, the player should be
     * at that location instead of where the player has tried to move to.</p>
     *
     * <p>If the {@code moveType} is cancellable
     * ({@link MoveType#isCancellable()}, then the last valid location will
     * be set to the given one.</p>
     *
     * @param player The player
     * @param to The new location
	 * @param from the previous location
     * @param moveType The type of move
     * @param forced Whether to force a check
     * @return The overridden location, if the location is being overridden
     */
    public Optional<Location> testMoveTo(Player player, Location to, Location from, MoveType moveType, boolean forced) {
		
//		TurtleCodePlugin.getInstance().getLogger().info("From: "+from+" To: "+to);
		
		if (from.getWorld() == to.getWorld()) {
			int x1 = Math.min(from.getBlockX(), to.getBlockX());
			int x2 = Math.min(to.getBlockX(), from.getBlockX());
//			TurtleCodePlugin.getInstance().getLogger().info("x1:"+x1+" x2:"+x2);
			for(; x1 <= x2; x1++) {
				for(int y=0; y<255; y++) {
					Block b = from.getWorld().getBlockAt(x1, y, from.getBlockZ());
					if (BorderBlocks.isBorderBlock(b)) {
						TurtleCodePlugin.getInstance().getLogger().info("Is Border Block! "+"<"+from.getX()+" "+from.getY()+" "+from.getZ()+">, to: <"+to.getX()+" "+to.getY()+" "+to.getZ()+">");
						
						double vx = from.getX() - to.getX();
						double vy = from.getY() - to.getY();
						double vz = from.getZ() - to.getZ();
						
						Vector v = new Vector(vx, vy, vz);
						v.multiply(2);
						TurtleCodePlugin.getInstance().getLogger().info(""+v);
						Location reject = from.clone();
						
						reject.subtract(v);
						
						
						
						return Optional.of(reject);
					} 
//					else {
//						TurtleCodePlugin.getInstance().getLogger().info("Found: " + b.getType());
//					}
				}
			}
			
		} else {
			TurtleCodePlugin.getInstance().getLogger().info("Player changed worlds! From: "+from.getWorld().getName()+" To: "+to.getWorld().getName());
		}
		
		return Optional.empty();
    }
	

}
