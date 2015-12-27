/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;


/**
 * Manage a list of turtles
 * @author techplex
 */
public class TurtleMgr {
	private static final List<Turtle> TRUTLES = new ArrayList<>();
	
	public static Turtle getNewTurtle(String name, Material mat, Location loc, String owner) 
	{
		Turtle t = new Turtle(name, mat, loc, owner);
		add(t);
		return t;
	}
	
	/**
	 * Get a list of turtles
	 * @return a collection of turtles
	 */
	public static List<Turtle> getTurtles() {
		return TRUTLES;
	}
	
	/**
	 * Add a turtle to the list
	 * @param t turtle to add
	 * @return true on success, false otherwise
	 */
	private static boolean add(Turtle t) {
		if (TurtleMgr.getByName(t.getName()) == null) {
			TRUTLES.add(t);
			return true;
		}
		return false;
		
	}
	
	/**
	 * Remove a turtle from the list
	 * @param name turtle to remove
	 */
	public static void remove(String name) {
		Turtle t = TurtleMgr.getByName(name);
		t.destroy();
		TRUTLES.remove(t);
	}

	/**
	 * Get a turtle given its location
	 * @param l the location of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public static Turtle getByLoc(Location l) {
		for (Turtle t : TRUTLES)
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
	public static Turtle getByName(String name) {
		for (Turtle t : TRUTLES)
			if (t.getName().equals(name)) {
				return t;
			}
		return null;
	}
}
