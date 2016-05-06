/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author techplex
 */
public class BorderBlocks {
    
    private static boolean isMaterialAndData(Block b, Material mat, byte data) {
        return (b.getType() == mat&& b.getState().getData().getData() == data);
    }
    
    public static boolean isSpecialBlock(Block b) {
        return isBuildAllowBlock(b) ||
        isBuildDisallowBlock(b) ||
        isBorderBlock(b) ||
        isTurtleBuildAllowBlock(b) ||
        isTurtleBuildDisallowBlock(b) ||
        isTurtleBorderBlock(b) ||
        isTurtleAntiBorderBlock(b);
    }
    public static boolean isBuildAllowBlock(Block b) {
        return isMaterialAndData(b, Material.STAINED_CLAY, DyeColor.LIME.getData());
    }
    public static boolean isBuildDisallowBlock(Block b) {
        return isMaterialAndData(b, Material.STAINED_CLAY, DyeColor.RED.getData());              
    }
    
    public static boolean isBorderBlock(Block b) {
        if (b.getType() == Material.FENCE) {
            return true; 
        }
       return false;               
    }
    
    public static boolean isTurtleBuildAllowBlock(Block b) {
        return isMaterialAndData(b, Material.WOOL, DyeColor.BLUE.getData());              
    }
    
    public static boolean isTurtleBuildDisallowBlock(Block b) {
        return isMaterialAndData(b, Material.WOOL, DyeColor.ORANGE.getData());              
    }
    
    public static boolean isTurtleBorderBlock(Block b) {
        return isMaterialAndData(b, Material.WOOL, DyeColor.RED.getData());              
    }
    
    public static boolean isTurtleAntiBorderBlock(Block b) {
        return isMaterialAndData(b, Material.WOOL, DyeColor.GREEN.getData());              
    }
}
