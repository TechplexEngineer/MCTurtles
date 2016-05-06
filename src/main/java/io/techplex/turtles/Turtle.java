package io.techplex.turtles;

import io.techplex.borderblocks.plumbing.Main;
import io.techplex.utils.KDebug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.OfflinePlayer;


public class Turtle implements ConfigurationSerializable {

	//==========================================================================
    // Properties
    //==========================================================================
	private String name;
	private Location loc; //the current turtle location
//	private Script script
	private final Inventory inv;
	private UUID owner;
	private int mined = 0, placed = 0;
	private boolean obeyCreative = true;
	private Material penDown = Material.AIR;
	private final Map<String, Location> bookmarks;
	private BlockFace pointing;
	private BlinkTask blinkTask = null;
	
	public static final Material TURTLE_BLINK_MATERIAL = Material.GLOWSTONE;
	public static final Material TURTLE_MATERIAL = Material.DISPENSER; //@note use a directioal block Material.DISPENSER
	public static final Material TURTLEWAND_MATERIAL = Material.BLAZE_ROD;
	
	//@note continued (directional blocks): Banner, Bed, Button, Chest, CocoaPlant, Diode, DirectionalContainer, Dispenser, Door, EnderChest, Furnace, FurnaceAndDispenser, Gate, Ladder, Lever, PistonBaseMaterial, PistonExtensionMaterial, Pumpkin, RedstoneTorch, Sign, SimpleAttachableMaterialData, Skull, Stairs, Torch, TrapDoor, TripwireHook

	//==========================================================================
    // Constructors & Destructors
    //==========================================================================

	public Turtle(String name, Location loc, UUID owner) {
		
		if (!KDebug.isCalledFrom("TurtleMgr")) {
			System.err.println("Invalid Invocation. Turtles SHOULD only be created from the TurtleMgr");
		}
		this.name = name.replace(' ','_'); // @note names cannot contain spaces
		this.loc = loc;
		this.owner = owner;
		inv = Bukkit.createInventory(null, 9 * 4,  this.name + " the turtle");
		bookmarks = new HashMap<>();
	}
	/**
	 * This constructor is used exclusively for serialization
	 * 
	 * @param name
	 * @param loc
	 * @param inv
	 * @param owner
	 * @param mined
	 * @param placed
	 * @param obeyCreative
	 * @param penDown
	 * @param bookmarks 
	 * @param pointing
	 */
	private Turtle(String name, Location loc, ItemStack[] inv, UUID owner, int mined, int placed, boolean obeyCreative, Material penDown, Map<String, Location> bookmarks, BlockFace pointing) {
		this.name = name.replace(' ','_');
		this.loc = loc;
		if (loc.getBlock().getType() != TURTLE_MATERIAL) {
			loc.getBlock().setType(TURTLE_MATERIAL);
		}
		
		this.inv = Bukkit.createInventory(null, 9 * 4,  this.name + " the turtle");
		this.inv.setContents(inv);
		this.owner = owner;
		this.mined = mined;
		this.placed = placed;
		this.obeyCreative = obeyCreative;
		this.penDown = penDown;
		this.bookmarks = bookmarks;
		this.pointing = pointing;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", getName());
		map.put("loc", getLocation());
		map.put("inv", getInventory().getContents());
		map.put("owner", owner.toString()); //@todo probably should be a uuid
		map.put("mined", getMined());
		map.put("placed", getPlaced());
		map.put("obeyCreative", obeyCreative);
		map.put("penDown", penDown.name());
		map.put("bookmarks", bookmarks);
		map.put("pointing", getFacing().name());
		
		return map;
	}
	
	public static Turtle deserialize(Map<String, Object> map) {
		
		String name = (String)map.get("name");
		Location loc = (Location)map.get("loc"); //the current turtle location
		
		ItemStack[] inv = ((List<ItemStack>) map.get("inv")).toArray(new ItemStack[0]);
		UUID owner = UUID.fromString((String)map.get("owner"));
		int mined = (int)map.get("mined");
		int placed = (int)map.get("placed");
		boolean obeyCreative = (boolean)map.get("obeyCreative");
		Material penDown = Material.matchMaterial((String)map.get("penDown"));
		Map<String, Location> bookmarks = (HashMap<String, Location>)map.get("bookmarks");
		String p = (String)map.get("pointing");
		BlockFace pointing = Turtle.getBlockFaceByString(p);
		
		return new Turtle(name, loc, inv, owner, mined, placed, obeyCreative, penDown, bookmarks, pointing);
	}
	public static final Map<String, BlockFace> BLOCKFACE_BY_NAME = new HashMap<>();
	
	static {
		for (BlockFace blockface : BlockFace.values()) {
			BLOCKFACE_BY_NAME.put(blockface.name(), blockface);
		}
	}
	public static BlockFace getBlockFaceByString(final String dir) {
        Validate.notNull(dir, "Name cannot be null");
		
		String filtered = dir.toUpperCase();
		filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
		return BLOCKFACE_BY_NAME.get(filtered);

    }

	
	/**
	 * Should be called when removing turtle from world or reloading the plugin
	 * @param removeFromWorld set to true to remove the turtle from the world
	 */
	public void destroy(boolean removeFromWorld) {
//		if (isRunning())
//			stop();
		for (ItemStack is : getInventory().getContents())
			if (is != null)
				loc.getWorld().dropItem(loc.add(.5, .5, .5), is);
		inv.clear();
		if (removeFromWorld) {
			loc.getBlock().breakNaturally();
		}
		if (!KDebug.isCalledFrom("TurtleMgr")) {
			TurtleMgr.getInstance().remove(this.getName());
		}
		shutdownTasks();
	}
	
	/**
	 * Its important to shutdown any tasks that are running when the bukkit server
	 * reloads to minimize ill effects of having old tasks running.
	 */
	public void shutdownTasks() {
		if (blinkTask != null) {
			blinkTask.cancel();
		}
	}
	
	//==========================================================================
    // Getters
    //==========================================================================
	/**
	 * Get the owner as a player object
	 * @return the owners uuid
	 */
	public UUID getOwner() {
		return owner;
	}
	
	/**
	 * When placing blocks we need to check if the owner is in creative mode.
	 * If we can't tell (because the owner is offline) then we'll assume creative.
	 * @return the owners game mode
	 */
	private GameMode getOwnerGameMode() {
		OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
		if(op.isOnline()) {
			return Bukkit.getPlayer(owner).getGameMode();
		}
		else {
			System.out.println("Player "+op.getName()+" is possibly offline. Assuming creative mode");
			return GameMode.CREATIVE;
		}
	}
	
	/**
	 * Get the name of the owner
	 * @return owner's name
	 */
	public String getOwnerName() {
		OfflinePlayer op = Bukkit.getOfflinePlayer(owner);
		return op.getName();
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
	 * Get the direction the turtle is facing
	 * @return Cardinal direction BlockFace.{NORTH, EAST, SOUTH, WEST}
	 */
	public BlockFace getFacing() {
		if (pointing == null) {
			//read the blocks direction
			pointing = dir(null);
		}
		
		return pointing;
	}
	/**
	 * Block direction utility
	 * 
	 * @param facing the direction set the block to, or null to read block direction
	 * @return the direction the block is facing
	 */
	private BlockFace dir(BlockFace facing) {
		try {
			BlockState state = loc.getBlock().getState();
			MaterialData data = state.getData();

			if (data instanceof Directional) {
				Directional d = ((Directional)data);
				if (facing == null) {
					return d.getFacing();
				} else {
					d.setFacingDirection(facing);
					state.update();
				}
			}
			
		} catch (ClassCastException e) {
		}
		return facing;
	}
	
	/**
	 * When the turtle's pen is down it places the penDown material when it moves
	 * @return true if turtle's pen is down, false otherwise
	 */
	public boolean isPenDown() {
		return penDown != Material.AIR;
	}
	
	/**
	 * When the turtle's pen is up it does not place a material when it moves
	 * @return true if turtle's pen is up, false otherwise
	 */
	public boolean isPenUp() {
		return penDown == Material.AIR;
	}
	
	/**
	 * Get a map of all bookmarks
	 * @return map of bookmarks
	 */
	public Map<String, Location> getBookmarks() {
		return bookmarks;
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
	public void setOwner(UUID owner) {
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
	public Material getType(int slot) {
		ItemStack is = getInventory().getItem(slot);
		return is == null ? Material.AIR : is.getType();
	}
	
	/**
	 * When the turtle's pen is down it places the penDown material when it moves
	 * @param m material to leave behind
	 */
	public void setPenDown(Material m) {
		penDown = m;
	}
	
	/**
	 * Make the turtle stop placing blocks when it moves
	 */
	public void setPenUp() {
		penDown = Material.AIR;
	}
	
	/**
	 * Get the number of blocks mined:
	 * - Since last reset
	 * - Since the turtle was created
	 * @return number of blocks mined
	 */
	public int getMined() {
		return mined;
	}
	
	/**
	 * Get the number of blocks placed:
	 * - Since the last reset
	 * - Since the turtle was created
	 * @return 
	 */
	public int getPlaced() {
		return placed;
	}
	
	/**
	 * Set the mined and placed stats to 0;
	 */
	public void resetStats() {
		mined = 0;
		placed = 0;
	}
	
	/**
	 * Create a bookmark at the current turtle location for easy access
	 * @param name name of the bookmark
	 * @return 
	 */
	public boolean bookmark(String name) {
		if (bookmarks.containsKey(name)) {
			return false;
		}
		bookmarks.put(name, loc);
		return true;
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
			out = getFacing();
		}
		if (face.equalsIgnoreCase("BACK")) {
			out = getFacing().getOppositeFace();
		}
		if (face.equalsIgnoreCase("RIGHT") || face.equalsIgnoreCase("LEFT")) {
			BlockFace[] dirs = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
			int s = java.util.Arrays.asList(dirs).indexOf(getFacing());
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
		mined ++;
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
		if (obeyCreative && getOwnerGameMode() == GameMode.CREATIVE) {
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
	 */
	public void setLocation(Location newloc, BlockFace facing) {
		this.loc.getBlock().setType(penDown);
		if (penDown != Material.AIR) {
			placed ++;
		}
		this.loc = newloc;
		Block b = loc.getBlock();
		b.setType(TURTLE_MATERIAL);

		pointing = dir(facing);
	
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
		setLocation(l, getFacing());
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
	 */
	public void rotate(BlockFace dir) {
		setLocation(this.loc, dir);
	}
	
	/**
	 * Turn the turtle
	 * Automatically resolve strings to the proper BlockFace
	 * @param dir direction to head
	 */
	public void rotate(String dir) {
		BlockFace out = str2BlockFace(dir);
		if (out != null) {
			rotate(out);
		}
	}
	
	/**
	 * Check the type of a block immediately adjacent to the turtle
	 * @param face the direction to check
	 * @param mat the material to look for
	 * @return true if the block in face direction is the same material as mat
	 */
	public boolean checkType(BlockFace face, Material mat) {
		return this.loc.getBlock().getRelative(face).getType() == mat;
	}
	
	/**
	 * Move the turtle to the bookmark name
	 * WARNING! This does not check if the bookmark location is empty
	 * @param name the bookmark to go to
	 * @return true if bookmark exists, false otherwise
	 */
	public boolean goBookmark(String name) {
		
		if (bookmarks.containsKey(name)) {
			setLocation(bookmarks.get(name), getFacing());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get a random number between min and max, inclusive of both min and max
	 * @param min smallest value, inclusive
	 * @param max largest value, inclusive
	 * @return random number
	 */
	private int randInt (int min, int max ) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	/**
	 * Get one of the colors by idx
	 * @param idx
	 * @return Color
	 */
	private Color getColor(int idx) {
		Color[] colors = {Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, 
			Color.GRAY,	Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, 
			Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, 
			Color.TEAL, Color.WHITE, Color.YELLOW};
		if(idx <= colors.length-1) {
			return colors[idx];
		} else {
			return colors[0];
		}
	}
	
	/**
	 * Get one of the firework types by idx
	 * @param idx
	 * @return Firework Type
	 */
	private FireworkEffect.Type getFWType(int idx) {
		FireworkEffect.Type[] types = {FireworkEffect.Type.BALL, 
			FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BURST, 
			FireworkEffect.Type.CREEPER, FireworkEffect.Type.STAR};
		if(idx <= types.length-1) {
			return types[idx];
		} else {
			return types[0];
		}
	}
	
	/**
	 * Make a firework at the turtle's current location
	 * Great for identifying where the turtle is when you loose him.
	 * https://bukkit.org/threads/spawn-firework.118019/
	 */
    public void makeFirework() {              

		//Spawn the Firework, get the FireworkMeta.
		Firework fw = (Firework) loc.getWorld().spawnEntity(getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		//Create our effect with this
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(ThreadLocalRandom.current().nextBoolean())
				.withColor(getColor(randInt(0, 16)))
				.withFade(getColor(randInt(0, 16)))
				.with(getFWType(randInt(0, 5)))
				.trail(ThreadLocalRandom.current().nextBoolean())
				.build();

		//Then apply the effect to the meta
		fwm.addEffect(effect);

		//Generate some random power and set it
		fwm.setPower(randInt(1, 2));

		//Then apply this to our rocket
		fw.setFireworkMeta(fwm);                  
    }
	
	/**
	 * Make the turtle blink about once every 1/2 a second to make it easier for
	 * the programmer to keep track of it.
	 * Call toggleBlink once to start the blinking and call toggleBlink again to stop.
	 */
	public void toggleBlink() {
		long delay = 0;
		long period = 10; //is about 1/2 sec

		if (blinkTask == null) {
			blinkTask = new BlinkTask();
			blinkTask.runTaskTimer(Main.getInstance(), delay, period);
		} else {
			blinkTask.cancel();
			blinkTask = null;
		}
		
	}
	/**
	 * Used to make the turtle blink for the programmer's sanity to find their turtle.
	 */
	class BlinkTask extends BukkitRunnable {
		boolean isAltered = false;
		@Override
		public void run() {
			if (isAltered) {
				tempChangeMaterial(TURTLE_MATERIAL);
			} else {
				tempChangeMaterial(TURTLE_BLINK_MATERIAL);
			}
			isAltered = !isAltered;
		}
		
		@Override
		public synchronized void cancel() {
			super.cancel();
			if (isAltered) {
				run();
			}
			
		}
		
	}
	
	/**
	 * Temporarily change the material of the turtle.
	 * Great for making our blink effect.
	 * @param m 
	 */
	private void tempChangeMaterial(Material m) {
		this.loc.getBlock().setType(m);
		BlockFace f = getFacing();
		dir(f);
	}
}
