package com.tpl.turtles;


import com.tpl.turtles.utils.KDebug;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

public class Turtle {

	//==========================================================================
    // Properties
    //==========================================================================
	private String name;
	private Location loc;
	private final Material mat;
//	private Script script;
	private final Inventory inv;
	private String owner;
	private int mined = 0, placed = 0;
	private boolean obeyCreative = true;

	//==========================================================================
    // Constructors & Destructors
    //==========================================================================

	public Turtle(String name, Material mat, Location loc, String owner) {
		
		if (!KDebug.isCalledFrom("TurtleMgr")) {
			System.err.println("Invalid Invocation. Turtles can only be created from the TurtleMgr");
		}
		this.name = name.replace(' ','_'); // @note names cannot contain spaces
		this.loc = loc;
		this.mat = mat;
		this.owner = owner;
		inv = Bukkit.createInventory(null, 9 * 4,  this.name + " the turtle");
	}
	
	/**
	 * Should be called when removing turtle from world
	 */
	public void destroy() {
//		if (isRunning())
//			stop();
		for (ItemStack is : getInventory().getContents())
			if (is != null)
				loc.getWorld().dropItem(loc.add(.5, .5, .5), is);
		inv.clear();
		loc.getBlock().breakNaturally();
		if (!KDebug.isCalledFrom("TurtleMgr")) {
			TurtleMgr.remove(this.getName());
		}
		
	}
	
	//==========================================================================
    // Getters
    //==========================================================================
	/**
	 * Get the owner as a player object
	 * @return Player
	 */
	public Player getOwner() {
		return Bukkit.getPlayer(owner);
	}
	
	/**
	 * Get the name of the owner
	 * @return owner's name
	 */
	public String getOwnerName() {
		return owner;
	}

	/**
	 * Get the player specified name of the turtle
	 * @return the turtle's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the turtle's location
	 * @return Location
	 */
	public Location getLocation() {
		return loc;
	}
	
	/**
	 * Get the turtle's inventory
	 * @return Inventory
	 */
	public Inventory getInventory() {
		return inv;
	}
	
	/**
	 * Get the type of material the turtle is made from
	 * @return Material
	 */
	public Material getMaterial() {
		return mat;
	}
	
	/**
	 * Get the direction the turtle is facing
	 * @return Cardinal direction BlockFace.{NORTH, EAST, SOUTH, WEST}
	 */
	public BlockFace getDir() {
		
		try {
			//@todo what happens if the block is not directional?
			//@note throws an exception which we catch. Should be able to use reflection...
			Block b = this.loc.getBlock();
			BlockState state = b.getState();
			Directional d = ((Directional) state.getData());
			return d.getFacing();

		} catch (ClassCastException e) {
			System.out.println("Is not Instance");
			return null;
		}
	}
	
	//==========================================================================
    // Setters
    //==========================================================================

	/**
	 * Change the turtle's name
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Change the turtle's owner
	 * @param owner 
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * When true the turtle does not need to have the item in its inventory to place it
	 * @param obeyCreative 
	 */
	public void setObeyCreative(boolean obeyCreative) {
		this.obeyCreative = obeyCreative;
	}
	
	/**
	 * Get the material type of the item in the inventory slot
	 * @param slot the slot number to check
	 * @return item type, else return Material.AIR
	 */
	private Material getType(int slot) {
		ItemStack is = getInventory().getItem(slot);
		return is == null ? Material.AIR : is.getType();
	}
	
	//==========================================================================
    // Utils
    //==========================================================================
	
	/**
	 * Resolve string {NORTH, EAST, SOUTH, WEST, Up, DOWN, LEFT, RIGHT} to BlockFace
	 * @param face
	 * @return BlockFace
	 */
	public BlockFace str2BlockFace(String face) {
		BlockFace out = null;
		if (face.equalsIgnoreCase("NORTH")) {
			out = BlockFace.NORTH;
		}
		if (face.equalsIgnoreCase("EAST")) {
			out = BlockFace.EAST;
		}
		if (face.equalsIgnoreCase("SOUTH")) {
			out = BlockFace.SOUTH;
		}
		if (face.equalsIgnoreCase("WEST")) {
			out = BlockFace.WEST;
		}
		if (face.equalsIgnoreCase("UP")) {
			out = BlockFace.UP;
		}
		if (face.equalsIgnoreCase("DOWN")) {
			out = BlockFace.DOWN;
		}
		if (face.equalsIgnoreCase("FORWARD")) {
			out = getDir();
		}
		if (face.equalsIgnoreCase("BACK")) {
			out = getDir().getOppositeFace();
		}
		if (face.equalsIgnoreCase("RIGHT") || face.equalsIgnoreCase("LEFT")) {
			BlockFace[] dirs = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
			int s = java.util.Arrays.asList(dirs).indexOf(getDir());
			if (face.equalsIgnoreCase("RIGHT")) {
				s ++;
			}
			if (face.equalsIgnoreCase("LEFT")) {
				s --;
			}
			out = dirs[s%4];
		}
		return out;
	}
	
	//==========================================================================
    // Actions
    //==========================================================================

	/**
	 * Break a block in the direction provided, get the dropped item and store in inventory
	 * @param face the direction to mine
	 * @return true on successful mine, false otherwise
	 */
	public boolean mine(BlockFace face) {
		if (getInventory().firstEmpty() == -1)
			return false;
		Block b = loc.getBlock().getRelative(face);
		if (b.getType() == Material.BEDROCK 
				|| b.getType() == Material.CHEST 
				|| b.getType() == Material.AIR
				|| b.getType() == Material.MOB_SPAWNER 
				|| b.getType() == Material.PORTAL
				|| b.getType() == Material.ENDER_PORTAL_FRAME 
				|| b.getType() == Material.ENDER_PORTAL)
			return false;
		for (ItemStack is : b.getDrops())
			getInventory().addItem(is);
		b.setType(Material.AIR);
		return true;
	}
	
	/**
	 * Break a block in the direction provided, get the dropped item and store in inventory
	 * Automatically resolve strings to the proper BlockFace
	 * @param face
	 * @return true on successful mine, false otherwise
	 */
	public boolean mine(String face) {
		BlockFace out = str2BlockFace(face);
		if (out == null) {
			return false;
		} else {
			return mine(out);
		}
	}
	
	/**
	 * Place a block. When in creative mode and obeyCreative==true inventory is not used
	 * @param face direction to place the block
	 * @param mat material to place
	 * @return true on successful place, false otherwise
	 */
	public boolean place(BlockFace face, Material mat) {
		if (face == null) {
			System.err.println("Error: while placing. BlockFace is null.");
			return false;
		}
		if (mat == null) {
			System.err.println("Error: while placing. Material is null.");
			return false;
		}
		Block b = this.loc.getBlock().getRelative(face);
		// only place block in empty slot
		if (b.getType() != Material.AIR)
			return false;
		if (obeyCreative && getOwner().getGameMode() == GameMode.CREATIVE) {
			b.setType(mat);
			placed++;
			return true;
		}
		if (getInventory().containsAtLeast(new ItemStack(mat), 1)) {
			for (int i = 0; i < getInventory().getSize(); i++) {
				ItemStack is = getInventory().getItem(i);
				if (is == null)
					continue;
				if (is.getType() == mat) {
					int am = is.getAmount() - 1;
					is.setAmount(am);
					if (am <= 0)
						getInventory().setItem(i, null);
					break;
				}
			}
			b.setType(mat);
			placed++;
			return true;
		}
		return false;
	}
	
	public boolean place(String face, String mat) {
		BlockFace bf = str2BlockFace(face);
		Material mt = Material.getMaterial(mat.toUpperCase());
		return place(bf, mt);
	}

	/**
	 * Move the turtle to the newloc
	 * Does not check if there is a block in the way
	 * @param newloc new location to move to 
	 * @param facing direction to face
	 * @return success
	 */
	public boolean setLocation(Location newloc, BlockFace facing) {
		this.loc.getBlock().setType(Material.AIR);
		this.loc = newloc;
		Block b = loc.getBlock();
		b.setType(mat);
		try {
			//@todo what happens if the block is not directional?
			//@note throws an exception which we catch. Should be able to use reflection...
			BlockState state = b.getState();
			Directional d = ((Directional)state.getData());
			d.setFacingDirection(facing);
			state.update();
			System.out.println("Is Instance");
			return true;
		} catch (ClassCastException e) {
			System.err.println("Is not Instance");
		}
		return false;
	}

	/**
	 * Move the turtle in the given absolute direction
	 * @param face
	 * @return success 
	 */
	public boolean move(BlockFace face) {
		Location l = this.loc.getBlock().getRelative(face).getLocation();
		if (l.getBlock().getType() != Material.AIR) {
			System.out.println("Can't move, "+l.getBlock().getType()+" block in the way.");
			return false;
		}
		setLocation(l, getDir());
		return true;
	}
	
	/**
	 * Move the turtle in the given absolute direction
	 * Automatically resolve strings to the proper BlockFace
	 * @param face
	 * @return success
	 */
	public boolean move(String face) {
		BlockFace out = str2BlockFace(face);
		if (out == null) {
			return false;
		} else {
			return move(out);
		}
	}
	
	/**
	 * Turn the turtle
	 * @param dir direction to head
	 * @return success
	 */
	public boolean rotate(BlockFace dir) {
		return setLocation(this.loc, dir);
	}
	
	/**
	 * Turn the turtle
	 * Automatically resolve strings to the proper BlockFace
	 * @param dir direction to head
	 * @return success
	 */
	public boolean rotate(String dir) {
		BlockFace out = str2BlockFace(dir);
		if (out == null) {
			return false;
		} else {
			return rotate(out);
		}
	}
	
	/**
	 * Check the type of a block immediately adjacent to the turtle
	 * @param face the direction to check
	 * @param mat the material to look for
	 * @return true if the block in face direction is the same material as mat
	 */
	private boolean checkType(BlockFace face, Material mat) {
		return this.loc.getBlock().getRelative(face).getType() == mat;
	}
	

//	public void setScript(Script script) {
//		this.script = script;
//	}

//	private int task;
//	private boolean running;
//	private int timees;

//	public boolean isRunning() {
//		return running;
//	}
//
//	public boolean start(final int timess) {
//		if (isRunning())
//			return false;
//		running = true;
//		timees = timess * script.getLength();
//		mined = 0;
//		placed = 0;
//		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.inst, new Runnable() {
//			@Override
//			public void run() {
//				if (timees > 0) {
//					Command cmd = script.getNextCommand();
//					try {
//						processCommand(cmd.getLabel(), cmd.getArgs());
//					} catch (Exception e) {
//						if (getOwner() != null)
//							getOwner().sendMessage(ChatColor.RED + "There is an error in: \"" + cmd.getLabel() + " "
//									+ cmd.getArgs()[0] + "...\"");
//						else
//							System.out.println(
//									"There is an error in: \"" + cmd.getLabel() + " " + cmd.getArgs()[0] + "...\"");
//					}
//					timees--;
//				} else {
//					running = false;
//					if (Bukkit.getPlayer(owner) != null)
//						Bukkit.getPlayer(owner).sendMessage(name + " is done with the script! It mined " + mined
//								+ " blocks and placed " + placed + " blocks.");
//					Bukkit.getScheduler().cancelTask(task);
//				}
//			}
//		}, 0, 20);
//		return true;
//	}

	

//	public void stop() {
//		Bukkit.getScheduler().cancelTask(task);
//		running = false;
//	}


	

	

//	public void processCommand(String label, String[] args) {
//		if (label.equalsIgnoreCase("move"))
//			move(Face.valueOf(args[0].toUpperCase()));
//		else if (label.equalsIgnoreCase("break")) {
//			if (breakBlock(Face.valueOf(args[0].toUpperCase())))
//				mined++;
//		} else if (label.equalsIgnoreCase("place")) {
//			place(Face.valueOf(args[0].toUpperCase()), getType(args[1]));
//		} else if (label.equalsIgnoreCase("if")) {
//			if (check(Face.valueOf(args[0].toUpperCase()), getType(args[1]))) {
//				String[] argss = new String[args.length - 3];
//				for (int i = 3; i < args.length; i++)
//					argss[i - 3] = args[i];
//				processCommand(args[2], argss);
//			}
//		}
//	}
	
}
