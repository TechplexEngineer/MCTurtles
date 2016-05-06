/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

/**
 * Represents an API Action
 * @author techplex
 */
public class ApiAction {
	private String act;
	public ApiAction(String act){
		this.act = act;
	}
	
	public String getAction() {
		return act;
	}
}
