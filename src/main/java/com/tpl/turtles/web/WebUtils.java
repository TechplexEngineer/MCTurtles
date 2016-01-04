/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author techplex
 */
public class WebUtils {
	private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
//	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
	
	public static List <SimpleEntry<String,String>> parse (String str) {
		List <SimpleEntry<String,String>> parameters = Collections.emptyList();
		Scanner scanner = new Scanner(str);
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            final String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);
            if (nameValue.length == 0 || nameValue.length > 2) {
                throw new IllegalArgumentException("bad parameter");
			}

            final String name = decode(nameValue[0]);
            String value = null;
            if (nameValue.length == 2)
                value = decode(nameValue[1]);
            parameters.add(new SimpleEntry(name, value));
        }
		return parameters;
    }
	public static String decode (final String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}
