/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import org.bukkit.Location;

/**
 *
 * @author techplex
 */
public class RestrictedArea {
    private Location loc1;
    private Location loc2;

    public RestrictedArea(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
    
    public boolean containsPoint(int x, int z) {
        
        if (Math.min(loc1.getBlockX(), loc2.getBlockX()) <= x && x <= Math.max(loc1.getBlockX(), loc2.getBlockX())) {
            if (Math.min(loc1.getBlockZ(), loc2.getBlockZ()) <= z && z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ())) {
                return true;
            }
        }
        return false;
    }
    public boolean containsLocation(Location loc) {
        
        if (Math.min(loc1.getBlockX(), loc2.getBlockX()) <= loc.getBlockX() && loc.getBlockX() <= Math.max(loc1.getBlockX(), loc2.getBlockX())) {
            if (Math.min(loc1.getBlockZ(), loc2.getBlockZ()) <= loc.getBlockZ() && loc.getBlockZ() <= Math.max(loc1.getBlockZ(), loc2.getBlockZ())) {
                return true;
            }
        }
        return false;
    }
    
    
}
