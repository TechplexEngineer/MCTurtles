/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.web;

import java.util.AbstractMap;
import java.util.List;

/**
 *
 * @author techplex
 */
public class WebUtils_Test {
	public static void main(String[] args) {
		final String post = "name=thisIsTheName&script=thisIstheScript";
		
		List<AbstractMap.SimpleEntry<String, String>> parse = WebUtils.parse(post);
		for(AbstractMap.SimpleEntry<String, String> entry : parse) {
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
		
//		String script = WebUtils.getEncodedValue(post, "script");
		
	}
}
