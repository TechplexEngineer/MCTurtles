/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.web;

import java.util.concurrent.Executor;

/**
 * https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/Executor.html
 * @todo probably want to limit the number of threads this thing spawns
 * @author techplex
 */
public class ThreadPerTaskExecutor implements Executor {
	@Override
	public void execute(Runnable r) {
		new Thread(r).start();
	}
}