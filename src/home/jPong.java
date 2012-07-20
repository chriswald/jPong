/**
 *     _______    __    __   ________   __________    _______
 *    /\   __ \  /\ \  /\ \ /\  ____ \ /\____  ___\  /\  ____\
 *    \ \ \_/\_\ \ \ \_\_\ \\ \ \___\ \\/___/\ \__/  \ \ \___/
 *     \ \ \\/_/  \ \  ____ \\ \  ___ <     \ \ \     \ \____`\
 *      \ \ \   __ \ \ \__/\ \\ \ \ /\ \     \ \ \     \/___/\ \
 *       \ \ \__\ \ \ \ \ \ \ \\ \ \\ \ \    _\_\ \____   __\_\ \
 *        \ \______\ \ \_\ \ \_\\ \_\\ \_\  /\_________\ /\______\
 *         \/______/  \/_/  \/_/ \/_/ \/_/  \/_________/ \/______/
 *                __      __    ________    __        ______
 *               /\ \    /\ \  /\  ____ \  /\ \      /\  ___`,
 *               \ \ \   \ \ \ \ \ \__/\ \ \ \ \     \ \ \_/\ \
 *                \ \ \   \ \ \ \ \ \_\_\ \ \ \ \     \ \ \\ \ \
 *                 \ \ \  _\ \ \ \ \  ____ \ \ \ \     \ \ \\ \ \
 *                  \ \ \_\ \_\ \ \ \ \__/\ \ \ \ \_____\ \ \\_\ \
 *                   \ \_________\ \ \_\ \ \_\ \ \______\\ \_____/
 *                    \/_________/  \/_/  \/_/  \/______/ \/____/
 *
 *         ->jPong
 *         ->Developed By Christopher J. Wald
 *         ->Copyright 2011 (c) All Rights Reserved
 * 
 * 
 * @author  	Chris Wald
 * @date    	Sep 28, 2010 5:50:51 PM
 * @project 	jPong
 * @file    	jPong.java
 * @description A Windowed Pong Game
 * @license:
 *
 * 	Redistribution and use in source and binary forms, with or without
 * 	modification, are permitted provided that the following conditions
 * 	are met:
 *
 *	- Redistributions of source code must retain the above copyright
 *	  notice, this list of conditions and the following disclaimer.
 *
 *	- Redistributions in binary form must reproduce the above copyright
 *	  notice, this list of conditions and the following disclaimer in the
 *	  documentation and/or other materials provided with the distribution.
 *
 *	- The name of Christopher J. Wald may not be used to endorse or promote
 *	  products derived from this software without specific prior written
 *	  permission.
 *
 * 	THIS SOFTWARE IS PRIVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY
 * 	EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * 	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * 	ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
 * 	DIRECT, INDIRECT, INCIDENTAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING
 * 	BUT NOT LIMITED TO UNDESIRED ACTION, LOSS OF SECURITY, LOSS OF DATA, LOSS OF
 * 	SLEEP,  LOSS OF HAIR, OR EXPLOSIONS). USE AT YOUR OWN RISK.
 */

package home;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.util.Vector;

import pkg.Force;
import pkg.Paddle;
import pkg.PongBall;
import pkg.ScoreBoard;
import pkg.ThreadPrinter;
import exception.IllegalSizeException;

public class jPong{

	private static final double		G					=6.67 * Math.pow(10, -11);
	public  static boolean			VERBOSE				=true;
	private static boolean			collide_lockout		=false;
	//public  static DisplayConfigs	display_configs		=null;
	private static final int		REST_MILLIS			=25;

	/**
	 * Main Driver Method
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InterruptedException {

		//Set the options for a standard game in
		//case there aren't any command line arguments
		boolean 		isPC0			=true;
		boolean 		isPC1			=false;
		boolean 		enable_extras	=false;
		int 			NUM_PONG_BALLS	=1;
		final int 		MAX_PONG_BALLS	=25;
		final int		EDGE_OFFSET		=40;
		final Dimension	DIMENSION		=new Dimension(130,150);

		//New Paddles
		Paddle pad0=null;
		Paddle pad1=null;

		String[] prefix={"--","-","/"};		//Commonly used command line prefixes

		/*
		 * Check command line arguments
		 * to set number of players or
		 * display help message
		 */
		if (args.length>0) {
			for (int i=0; i<args.length; i++){

				//Get rid of prefixes
				for (String element : prefix) {
					while (args[i].startsWith(element)) {
						args[i]=args[i].substring(element.length());
					}
				}
				if (args[i].equalsIgnoreCase("pcbrawl")){	//Two Computer
					isPC0 = true;
					isPC1 = true;
				}
				else if (args[i].equalsIgnoreCase("oneplayer")){	//One computer, One Human (default)
					isPC0 = true;
					isPC1 = false;
				}
				else if (args[i].equalsIgnoreCase("enable-fluid")){
					enable_extras = true;
				}
				else if (args[i].equalsIgnoreCase("help")  ||  args[i].equalsIgnoreCase("h")){
					System.out.println("-- jPong --");
					System.out.println("Christopher J. Wald\t2010\n");
					jPong.print_help();
					return;
				} else {
					try{
						NUM_PONG_BALLS=Integer.parseInt(args[i].trim());

						if (NUM_PONG_BALLS > MAX_PONG_BALLS){
							NUM_PONG_BALLS=MAX_PONG_BALLS;
							ThreadPrinter.print("WARNING: Clipping NUM_PONG_BALLS to " + MAX_PONG_BALLS + " to protect system performance.");
						}
						if (NUM_PONG_BALLS < 0){
							NUM_PONG_BALLS=0;
							ThreadPrinter.print("WARNING: Clipping NUM_PONG_BALLS to 0");
						}
					} catch (NumberFormatException e) {
						//Register the exception, print help, and continue with a default game
						ThreadPrinter.print(e.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
						jPong.print_help();
						isPC0=true;
						isPC1=false;
						NUM_PONG_BALLS=1;
					}
				}
			}
		} else {		//No args
			isPC0=true;
			isPC1=false;
		}

		//Now set up the new paddles
		try {
			pad0=new Paddle(isPC0, DIMENSION, EDGE_OFFSET, enable_extras);
			pad1=new Paddle(isPC1, DIMENSION, EDGE_OFFSET, enable_extras);
		} catch (IllegalSizeException e) {
			//Game ending exception if the paddles are the wrong size.
			ThreadPrinter.print(e.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			e.printStackTrace();
		}

		//New ball vector
		Vector<PongBall> ball=new Vector<PongBall>();
		for (int i=0; i<NUM_PONG_BALLS; i++) {
			ball.add(new PongBall());
		}

		//New ScoreBoard
		ScoreBoard board=new ScoreBoard(300, 100, enable_extras);

		/*
		 * Make threads for each of the windows
		 * -1st Paddle
		 * -2nd Paddle
		 * -ScoreBoard
		 * -Vector of PongBalls
		 */
		Vector<Thread> threads=new Vector<Thread>();
		threads.add(new Thread(pad0));
		threads.add(new Thread(pad1));
		threads.add(new Thread(board));
		for (int i=0; i<ball.size(); i++){
			threads.add(new Thread(ball.get(i)));
			threads.get(threads.size()-1).setName("PongBall-" + i);
		}
		threads.get(0).setName("Paddle-0");
		threads.get(1).setName("Paddle-1");
		threads.get(2).setName("ScoreBoard");

		//Start all
		for (int i=0; i<threads.size(); i++) {
			threads.get(i).start();
		}

		jPong.threadReport();

		//Main game loop
		//    -Make sure all threads are alive
		while (jPong.checkAllThreadsAlive(threads)){

			if (!jPong.collide_lockout) {
				//Allows paddles to check if collided with ball
				for (int i=0; i<ball.size(); i++){
					ball.get(i).checkCollide(pad0);
					ball.get(i).checkCollide(pad1);
				}
			}

			//Run features for an "Extras Enabled"
			//game of pong
			if (enable_extras) {
				jPong.applyForce(pad0, pad1, ball);
				board.checkCollide(pad0);
				board.checkCollide(pad1);
			}

			//Moves paddles based on projected position of the ball
			pad0.setInfo(ball);
			pad1.setInfo(ball);

			//Slow down the game
			try {
				Thread.sleep(jPong.REST_MILLIS);
			} catch (InterruptedException e) {
				ThreadPrinter.print(e.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			}

		}

		//Signal all threads to exit
		jPong.sigDone();

	}

	/**
	 * Passes Terminate signal to all threads
	 */
	private static void sigDone() {
		PongBall.sigDone();
		Paddle.sigDone();
		ScoreBoard.sigDone();
		ThreadPrinter.print("Done...Waiting for all threads to exit");
	}

	/**
	 * Displays the total number of threads lists the name of each
	 */
	private static void threadReport() {
		ThreadPrinter.print(Thread.activeCount() + " Thread(s)");
		Thread[] thread_list=new Thread[Thread.activeCount()];
		Thread.enumerate(thread_list);
		for (int i=0; i<thread_list.length; i++) {
			System.out.print(thread_list[i].getName());
			if (i%2!=0) {
				System.out.println();
			} else {
				System.out.print("\t");
			}
		}
		System.out.println();
	}

	public static int swidth(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds().width;
	}

	public static int sheight(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds().height;
	}


	/**
	 * Checks to see if all the threads in the vector are still alive
	 * 
	 * @param threads:	Vector of all threads
	 * @return True if all are alive; False otherwise
	 */
	private static boolean checkAllThreadsAlive(final Vector<Thread> threads) {
		for(int i=0; i<threads.size(); i++) {
			if (!threads.get(i).isAlive()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Applies appropriate force on all parameter objects
	 * 
	 * @param pad0
	 * @param pad1
	 * @param ball
	 */
	private static void applyForce(final Paddle pad0, final Paddle pad1, final Vector<PongBall> ball) {

		/*
		 * The forces the paddles apply on
		 * each other
		 */
		final double MIN_OFFSET=0;

		double xoff   =pad1.getPosx() - pad1.getPosx();
		double yoff   =pad1.getPosy() - pad1.getPosy();
		double dist   =Common.pythag(xoff, yoff);
		double force  =0.0;
		double scaler =1.0;
		double xforce =0.0;
		double yforce =0.0;
		if (dist > MIN_OFFSET) {
			force  =(jPong.G * pad1.getMass() * pad1.getMass()) / Math.pow(dist, 2);
			scaler =dist / force;
			xforce =xoff / scaler;
			yforce =yoff / scaler;
		}

		pad0.applyForce(new Force(xforce, yforce));
		pad1.applyForce(new Force(-xforce, -yforce));

		/*
		 * The force the ball applies on the paddles
		 */
		if (ball.size() > 0) {
			for (int i=0; i<ball.size(); i++) {
				double b1xoff   =ball.get(i).getPosx() - pad0.getPosx();
				double b1yoff   =ball.get(i).getPosy() - pad0.getPosy();
				double b1dist   =Common.pythag(b1xoff, b1yoff);
				double b1force  =0.0;
				double b1scaler =1.0;
				double b1xforce =0.0;
				double b1yforce =0.0;

				if (b1dist > MIN_OFFSET) {
					b1force  =(jPong.G * ball.get(i).getMass() * pad0.getMass()) / Math.pow(b1dist, 2);
					b1scaler =b1dist / b1force;
					b1xforce =b1xoff / b1scaler;
					b1yforce =b1yoff / b1scaler;
				}

				pad0.applyForce(new Force(b1xforce, b1yforce));
				ball.get(i).applyForce(new Force(-b1xforce, -b1yforce));



				double b2xoff   =ball.get(i).getPosx() - pad1.getPosx();
				double b2yoff   =ball.get(i).getPosy() - pad1.getPosy();
				double b2dist   =Common.pythag(b2xoff, b2yoff);
				double b2force  =0.0;
				double b2scaler =1.0;
				double b2xforce =0.0;
				double b2yforce =0.0;

				if (b2dist > MIN_OFFSET) {
					b2force  =(jPong.G * ball.get(i).getMass() * pad1.getMass()) / Math.pow(b2dist, 2);
					b2scaler =b2dist / b2force;
					b2xforce =b2xoff / b2scaler;
					b2yforce =b2yoff / b2scaler;
				}

				pad1.applyForce(new Force(b2xforce, b2yforce));
				ball.get(i).applyForce(new Force(-b2xforce, -b2yforce));
			}
		}
	}

	/**
	 * Prints CLI help
	 */
	private static void print_help(){
		System.out.println("Usage:");
		System.out.println("\tjPong.jar [options...]");
		System.out.println("");
		System.out.println("Accepted arguments are:");
		System.out.println("\tpcbrawl");
		System.out.println("\toneplayer (default)");
		System.out.println("\tenable-fluid");
		System.out.println("\t<number of pong balls>");
	}
}