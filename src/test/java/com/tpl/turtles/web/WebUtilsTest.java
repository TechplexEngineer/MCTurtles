/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.web;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author techplex
 */
public class WebUtilsTest {
	
	public WebUtilsTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of parse method, of class WebUtils.
	 */
	@Test
	public void testParse() {
		System.out.println("parse");
		String str = "name=thisIsTheName&script=thisIstheScript";
		List<AbstractMap.SimpleEntry<String, String>> expResult = new ArrayList<>();
		expResult.add(new AbstractMap.SimpleEntry<>("name", "thisIsTheName"));
		expResult.add(new AbstractMap.SimpleEntry<>("script", "thisIstheScript"));
		List<AbstractMap.SimpleEntry<String, String>> result = WebUtils.parse(str);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getEncodedValue method, of class WebUtils.
	 */
	@Test
	public void testGetEncodedValue() {
		System.out.println("getEncodedValue");
		String str = "name=thisIsTheName&script=thisIstheScript";
		String key = "name";
		String expResult = "thisIsTheName";
		String result = WebUtils.getEncodedValue(str, key);
		assertEquals(expResult, result);
	}

	/**
	 * Test of decode method, of class WebUtils.
	 */
	@Test
	public void testDecode() {
		System.out.println("decode");
		String content = "this%20is%20a%20url%20%26";
		String expResult = "this is a url &";
		String result = WebUtils.decode(content);
		assertEquals(expResult, result);
	}
	
}
