/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author techplex
 */
public class Serializer {
	
	public static JSONArray toJson(List items) {
		JSONArray out = new JSONArray();
		for (Object item : items) {
			if (item instanceof ConfigurationSerializable) {
				out.put(toJson((ConfigurationSerializable)item));
			} else if (item == null) {
				out.put("null");
			}
			else
			{
				out.put(item.toString());
			}
		}
		return out;
	}
	public static JSONObject toJson(ConfigurationSerializable item) {
		JSONObject json = new JSONObject();
		for (Map.Entry<String, Object> e : item.serialize().entrySet())
		{
			if (e.getValue() instanceof ConfigurationSerializable)
			{
				json.put(e.getKey(), Serializer.toJson((ConfigurationSerializable)e.getValue()));
			}
			else if (e.getValue() instanceof ItemStack[])
			{
				Object o = e.getValue();
				ItemStack[] is = (ItemStack[])o;
				List<ItemStack> lis = Arrays.asList(is);
				ArrayList<ItemStack> items = new ArrayList<>(lis);
				json.put(e.getKey(), Serializer.toJson(items));
			}
			else
			{
				json.put(e.getKey(), e.getValue().toString());
			}
		}
		return json;
	}
}
