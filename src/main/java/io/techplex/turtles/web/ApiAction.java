/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.turtles.web;

import java.util.Optional;

/**
 * Represents an API Action
 * I feel like this could be done better such that the optional isn't needed.
 * @author techplex
 */
public class ApiAction {
	private String act;
	private ApiActionType type; 
	private String turtleName;
	public ApiAction(String act, ApiActionType type){
		this.act = act;
		this.type = type;
	}
	public ApiAction(String act) {
		this(act, ApiActionType.txtcmd);
	}
	
	public ApiAction(String code, String turtleName) {
		this(code, ApiActionType.js);
		this.turtleName = turtleName;
	}
	
	public String getAction() {
		return act;
	}
	
	public Optional<String> getTurtleName() {
		return Optional.ofNullable(turtleName);
	}
	
	public ApiActionType getType() {
		return type;
	}
	
	public enum ApiActionType {
		txtcmd,
		js
	}
}
