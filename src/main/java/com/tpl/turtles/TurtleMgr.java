/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


/**
 * Manage a list of turtles
 * @author techplex
 */
public class TurtleMgr implements ConfigurationSerializable {
	
	private static TurtleMgr inst = null;
	protected TurtleMgr () {
		
	}
	public static TurtleMgr getInstance() {
      if(inst == null) {
         inst = new TurtleMgr();
      }
      return inst;
   }
	private final List<Turtle> TURTLES = new ArrayList<>();
	
	public Turtle getNewTurtle(String name, Material mat, Location loc, String owner) 
	{
		Turtle t = new Turtle(name, mat, loc, owner);
		add(t);
		return t;
	}
	
	/**
     * Creates a Map representation of this class.
     * @return Map containing the current state of this class
     */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> m = new HashMap<>();
		for (Turtle t : TURTLES) {
			m.put(t.getName(), t);
		}
		return m;
	}
	
	/**
	 * Deserialize a TurtleMgr by adding all turtles to the TurtleMgr singleton
	 * @param map
	 * @return 
	 */
	public TurtleMgr deserialize(Map<String, Object> map) {
		TurtleMgr tm = TurtleMgr.getInstance();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Turtle t = (Turtle)entry;
			tm.add(t);
		}
		return tm;
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
	 * Remove a turtle from the list
	 * @param name turtle to remove
	 */
	public void remove(String name) {
		Turtle t = getByName(name);
		t.destroy();
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
