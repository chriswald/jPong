/**
 *  _______    __    __   ________   __________    _______
 * /\   __ \  /\ \  /\ \ /\  ____ \ /\____  ___\  /\  ____\
 * \ \ \_/\_\ \ \ \_\_\ \\ \ \___\ \\/___/\ \__/  \ \ \___/
 *  \ \ \\/_/  \ \  ____ \\ \  ___ <     \ \ \     \ \____`\
 *   \ \ \   __ \ \ \__/\ \\ \ \ /\ \     \ \ \     \/___/\ \
 *    \ \ \__\ \ \ \ \ \ \ \\ \ \\ \ \    _\_\ \____   __\_\ \
 *     \ \______\ \ \_\ \ \_\\ \_\\ \_\  /\_________\ /\______\
 *      \/______/  \/_/  \/_/ \/_/ \/_/  \/_________/ \/______/
 *             __      __    ________    __        ______
 *            /\ \    /\ \  /\  ____ \  /\ \      /\  ___`,
 *            \ \ \   \ \ \ \ \ \__/\ \ \ \ \     \ \ \_/\ \
 *             \ \ \   \ \ \ \ \ \_\_\ \ \ \ \     \ \ \\ \ \
 *              \ \ \  _\ \ \ \ \  ____ \ \ \ \     \ \ \\ \ \
 *               \ \ \_\ \_\ \ \ \ \__/\ \ \ \ \_____\ \ \\_\ \
 *                \ \_________\ \ \_\ \ \_\ \ \______\\ \_____/
 *                 \/_________/  \/_/  \/_/  \/______/ \/____/
 *
 *         ->jPong
 *         ->Developed By Christopher J. Wald
 *         ->Copyright *Year* (c) All Rights Reserved
 *
 *
 *
 *
 * @author  	Christopher J. Wald
 * @date    	Sep 18, 2011 - 3:45:12 PM
 * @project 	jPong
 * @file    	Common.java
 * @description *Description*
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

import java.awt.Point;

public class Common {
	public static double distance(final Point p0, final Point p1) {
		return Common.distance(p0.x, p0.y, p1.x, p1.y);
	}

	public static double distance(final int x1, final int y1, final int x2, final int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static double distance(final double x1, final double y1, final double x2, final double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * Determines length C based on Pythagrian Theorem
	 * 
	 * @param a:	Side length a
	 * @param b:	Side length b
	 * @return Length C based on Pythagrian Theorem
	 */
	public static double pythag(final double a, final double b) {
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
}
