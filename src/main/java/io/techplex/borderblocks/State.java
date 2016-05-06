/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author techplex
 */
public class State {
    //set to true to allow students to build in restricted areas not over build allow blocks.
    private boolean studentBuildingEnabled = false;
    
    //list of restricted areas
    private List<RestrictedArea> areas = new ArrayList<RestrictedArea>(){};
    
    private static State inst = null;
    protected State() {
        
    }
    
    public static State getInstance() {
      if(inst == null) {
         inst = new State();
      }
      return inst;
	}
    
    /**
	 * Static variables are not cleared when bukkit reloads the plugin.
	 */
	public void cleanup() {
        areas.clear();
        
		State.inst = null;
	}
    
    
    public boolean addRestrictedArea(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return false;
        }
        RestrictedArea ra = new RestrictedArea(loc1, loc2);
        return areas.add(ra);
    }
    
    public boolean isInRestrictedArea(Location loc) {
        boolean isRestricted = false;
        for (RestrictedArea area : areas) {
            isRestricted = area.containsLocation(loc);
            if (isRestricted) break;
        }
        return isRestricted;
    }
    
    

    public boolean isStudentBuildingEnabled() {
        return studentBuildingEnabled;
    }

    public void setStudentBuildingEnabled(boolean enabled) {
        this.studentBuildingEnabled = enabled;
    }

   
    
    
}
