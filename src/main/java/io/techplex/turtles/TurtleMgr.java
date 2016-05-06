package io.techplex.turtles;

import io.techplex.borderblocks.plumbing.Main;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manage a list of turtles
 * @author techplex
 */
public class TurtleMgr  {
	
	private static TurtleMgr inst = null;
        private ConcurrentHashMap<String, Turtle> TURTLES;
	protected TurtleMgr () {
		TURTLES = new ConcurrentHashMap<>();
	}
	public static TurtleMgr getInstance() {
            if(inst == null) {
                inst = new TurtleMgr();
            }
            return inst;
	}
	
	/**
	 * Static variables are not cleared when bukkit reloads the plugin.
	 */
	public void cleanup() {
		for (Map.Entry<String, Turtle> t : TURTLES.entrySet()) {
			t.getValue().shutdownTasks();
		}
		TurtleMgr.inst = null;
	}
	
	public Turtle getNewTurtle(String name,  Location loc, UUID owner) 
	{
		Turtle t = new Turtle(name, loc, owner);
		add(t);
		return t;
	}
	
	/**
	 * Get a list of turtles
	 * @return a collection of turtles
	 */
	public ConcurrentHashMap<String, Turtle> getTurtles() {
		return TURTLES;
	}
    
        /**
         * Get the number of Turtles managed by this TurtleMgr
         * @return Count of turtles on the server
         */
        public int getNumTurtles() {
            return TURTLES.size();
        }
	
	/**
	 * Add a turtle to the list
	 * @param t turtle to add
	 * @return true on success, false otherwise
	 */
	private boolean add(Turtle t) {
		if (getByName(t.getName()) == null) {
			TURTLES.put(t.getName(), t);
			return true;
		}
		return false;

	}
	
	/**
	 * Add each turtle to the TurtleMgr
	 * @todo I really would like to have the serialization in turtleMgr but
	 * the yaml serializer kept throwing errors.
	 * @param turtles list of turtles to add
	 */
	public void addEach(List<Turtle> turtles) {
		if (turtles != null) {
			for (Turtle t : turtles) {
				add(t);
			}
		}
		
	}
	
	/**
	 * Remove a turtle from the list
	 * @param name turtle to remove
	 */
	public void remove(String name) {
		Turtle t = getByName(name);
		t.destroy(true);
		TURTLES.remove(t.getName());
	}

	/**
	 * Get a turtle given its location
	 * @param l the location of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public Turtle getByLoc(Location l) {
		for (Map.Entry<String, Turtle> t : TURTLES.entrySet())
			if (t.getValue().getLocation().equals(l)) {
				return t.getValue();
			}
		return null;
	}

	/**
	 * Get a turtle given its name
	 * @param name the name of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public Turtle getByName(String name) {
		return TURTLES.get(name);
	}
    
	
	//@todo we need a perodic task to persist this info to protect againts a bad server shutdown
    public void persistTurtles() {
		YamlConfiguration c = new YamlConfiguration();
		c.set("Turtles", TurtleMgr.getInstance().getTurtles());
		try {
			c.save(new File(Main.getInstance().getDataFolder() + File.separator + "turtles.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int restoreTurtles() {
		YamlConfiguration c = new YamlConfiguration();
		try {
			File f = new File(Main.getInstance().getDataFolder() + File.separator + "turtles.yml");
			if (!f.exists()) {
				f.createNewFile();
			} else {
				c.load(f);
				Object ted = c.get("Turtles");
				List<Turtle> t = (List<Turtle>)ted; //@note in the process of deseralizing TurtleMgr all of the turtles are added to the singleton
				if (t != null) {
					TurtleMgr.getInstance().addEach(t);
					return t.size();
				} else {
					return 0;
				}
			}

		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
