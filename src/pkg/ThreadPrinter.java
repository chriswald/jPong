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
 *         ->Thread Printer
 *         ->Developed By Christopher J. Wald
 *         ->Copyright 2011 (c) All Rights Reserved
 * 
 * 
 * @author		Christopher J. Wald
 * @date		Sep 13, 2011 7:57:14 AM
 * @file		ThreadPrinter.java
 * @description	A class for tracking checkpoints in multithreaded applications.
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
package pkg;

public class ThreadPrinter {

	public static void print(final String msg){
		System.out.println(msg + ": " + Thread.currentThread().getName());
	}

	public static void print(final int msg){
		System.out.println(msg + ": " + Thread.currentThread().getName());
	}

	public static void print(final double msg){
		System.out.println(msg + ": " + Thread.currentThread().getName());
	}
	public static void print_ntd(final String msg) {
		System.out.println(msg);
	}
}
