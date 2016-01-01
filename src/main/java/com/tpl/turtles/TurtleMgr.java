package com.tpl.turtles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

/**
 * Manage a list of turtles
 * @author techplex
 */
public class TurtleMgr  {
	
	private static TurtleMgr inst = null;
	private List<Turtle> TURTLES;
	protected TurtleMgr () {
		TURTLES = new ArrayList<>();
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
		for (Turtle t : TurtleMgr.getInstance().getTurtles()) {
			t.shutdownTasks();
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
	public List<Turtle> getTurtles() {
		return TURTLES;
	}
	
	/**
	 * Add a turtle to the list
	 * @param t turtle to add
	 * @return true on success, false otherwise
	 */
	private boolean add(Turtle t) {
		if (getByName(t.getName()) == null) {
			TURTLES.add(t);
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
		TURTLES.remove(t);
	}

	/**
	 * Get a turtle given its location
	 * @param l the location of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public Turtle getByLoc(Location l) {
		for (Turtle t : TURTLES)
			if (t.getLocation().equals(l)) {
				return t;
			}
		return null;
	}

	/**
	 * Get a turtle given its name
	 * @param name the name of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public Turtle getByName(String name) {
		for (Turtle t : TURTLES)
			if (t.getName().equals(name)) {
				return t;
			}
		return null;
	}
}
