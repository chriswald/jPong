/**
 * @author  Chris Wald
 * @date    Sep 28, 2010 5:51:41 PM
 * @project jPong
 * @file    Paddle.java
 */

package pkg;

import home.Common;
import home.jPong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JFrame;

import exception.IllegalSizeException;

public class Paddle extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	//Globals
	private static final long serialVersionUID = 1L;

	protected double				posx			=0;
	protected double				posy			=0;
	protected double				velx			=0;
	protected double				vely			=0;
	private double					origy			=0.0;
	private int						psudo_vely		=0;
	private int						mpos			=0;

	private static final int		STD_VEL			=(int) (jPong.sheight() * .03);
	private static final int		MAX_VELX		=PongBall.MAX_VEL[0];
	private static final int		MAX_VELY		=Paddle.STD_VEL;
	private static final int		REST_MILLIS		=25;
	private static final double		VELX_DECAY		=1.10;	//Higher values decay faster
	private static final double		VELY_DECAY		=1.10;	//Higher values decay faster
	private static boolean			DONE			=false;
	public  static boolean			ENABLE_EXTRAS	=false;

	private int						player_num		=-1;
	private static int				TOTAL_PLAYERS	=0;
	private boolean					is_computer		=false;

	private final String			CTITLE			="C | " + (Paddle.TOTAL_PLAYERS + 1);
	private final String			HTITLE			="H | " + (Paddle.TOTAL_PLAYERS + 1);

	private boolean					done_move		=false;

	private boolean					mouse_enabled	=false;
	private PongBall				to_go_for		=null;

	private Force 					force			=new Force(0,0);


	/**
	 * Constructor
	 * @param is_computer:		Is this a computer player?
	 * @param edge_offset:		Spacing from back edge to screen edge
	 * @param width:			Width of paddle
	 * @param height:			Height of paddle
	 * @param enable_extras:	Should extra features be enabled?
	 */
	public Paddle(final boolean is_computer, final Dimension dimension, final int edge_offset, final boolean enable_extras) throws IllegalSizeException{

		super();

		//Set the paddles dimensions
		this.setSize(dimension.width, dimension.height);

		//Initialize rest of variables
		this.setVelx(0);
		this.setVely(0);
		this.is_computer=is_computer;
		this.done_move=true;

		/*
		 * Update the total number of Paddles
		 * and assign that # to the individual
		 * paddle (for placement purposes)
		 */
		this.player_num=Paddle.TOTAL_PLAYERS;
		Paddle.TOTAL_PLAYERS++;

		//Set Paddle's position on screen
		double x = 0;
		double y = 0;

		switch(this.player_num){
			case 0:
				x = edge_offset;
				y = (jPong.sheight() / 2) - (this.getHeight() / 2);
				this.setPosition(x, y);
				break;
			case 1:
				x = jPong.swidth() - (edge_offset + this.getWidth());
				y = (jPong.sheight() / 2) - (this.getHeight() / 2);
				this.setPosition(x, y);
				break;
			default:
				break;
		}

		Paddle.ENABLE_EXTRAS=enable_extras;

		ThreadPrinter.print_ntd(this.toString());
	}

	/**
	 * Main driver method of each Paddle
	 */
	@Override
	public void run(){
		ThreadPrinter.print("In Thread");

		//Make Window
		//this.frame=new JFrame(jPong.display_configs.base_gd.getDefaultConfiguration());

		if (this.is_computer) {
			super.setTitle(this.CTITLE);
		} else {
			super.setTitle(this.HTITLE);
		}
		super.setSize(this.getWidth(),this.getHeight());
		super.setLocation((int) this.getPosx(), (int) this.getPosy());
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setResizable(false);

		//Set Color based on player number
		if (this.getPlayernum() == 1) {
			super.getContentPane().setBackground(new Color(175, 0, 0));
		} else {
			super.getContentPane().setBackground(new Color(0, 0, 175));
		}

		//Add key and mouse listeners
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		super.setVisible(true);

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage() + ":" + Thread.currentThread().getStackTrace()[1].getFileName()
					+ " in " + Thread.currentThread().getStackTrace()[1].getMethodName() + "() at "
					+ Thread.currentThread().getStackTrace()[1].getLineNumber());
		}



		//Thread Loop
		while (!Paddle.DONE){

			//Move the computer controlled paddle to intercept
			//the pongball
			try{
				this.moveComputerPlayer(this.to_go_for);
			} catch (NullPointerException e) {
				System.out.println(e.getMessage() + ":" + Thread.currentThread().getStackTrace()[1].getFileName()
						+ " in " + Thread.currentThread().getStackTrace()[1].getMethodName() + "() at "
						+ Thread.currentThread().getStackTrace()[1].getLineNumber());
			}

			this.update();

			//Try to sleep
			try{
				Thread.sleep(Paddle.REST_MILLIS);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage() + ":" + Thread.currentThread().getStackTrace()[1].getFileName() + " in "
						+ Thread.currentThread().getStackTrace()[1].getMethodName() + "() at "
						+ Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		}
		super.dispose();
		ThreadPrinter.print("Exiting Thread");
	}

	/**
	 * Signals all Paddles to die
	 */
	public static void sigDone(){
		Paddle.DONE=true;
	}

	/**
	 * Sets new coords for paddle, and updates the position on the screen
	 */
	public void update(){
		double new_posx=this.getPosx() + this.getVelx() + this.force.getMagnitudeX();
		double new_posy=this.getPosy() + this.getVely() + this.force.getMagnitudeY();
		this.setPosition(new_posx, new_posy);

		this.keepInSpeedBounds();
		this.keepInScreenBounds();


		//Allow frame to slow down from its velocity
		this.setVelx(this.getVelx() / Paddle.VELX_DECAY);
		this.setVely(this.getVely() / Paddle.VELY_DECAY);
		if (Math.abs(this.getVely()) <= (Paddle.STD_VEL/1.25)  &&  this.is_computer == true) {	//Higher values refresh slower
			this.done_move=true;
		}

		this.updateLocation();
	}

	private void updateLocation() {
		super.setLocation((int) this.posx, (int) this.posy);
	}

	/**
	 * Makes sure the paddle stays on the screen
	 * 
	 * @param new_pos_x:	new x coordinate for the paddle
	 * @param new_pos_y:	new y coordinate for the paddle
	 */
	private void keepInScreenBounds(){
		if (this.getPosx() <= 0) {	//Left Side
			this.setPosx(0);
			this.setVelx(-this.getVelx());
		} else if (this.getPosx() >= jPong.swidth() - this.getWidth()) {	//Right Side
			this.setPosx(jPong.swidth() - this.getWidth());
			this.setVelx(-this.getVelx());
		}

		if (this.getPosy() <= 0) {	//Top
			this.setPosy(0);
			this.setVely(-this.getVely() / 1.75);
		} else if (this.getPosy() >= jPong.sheight() - this.getHeight()) {	//Bottom
			this.setPosy(jPong.sheight() - this.getHeight());
			this.setVely(-this.getVely() / 1.75);
		}
	}

	/**
	 * Makes sure the Paddle doesn't get moving too fast
	 */
	private void keepInSpeedBounds(){
		if (Math.abs(this.getVelx()) > Paddle.MAX_VELX) {
			if (this.getVelx() > 0) {
				this.setVelx(Paddle.MAX_VELX);
			} else {
				this.setVelx(-Paddle.MAX_VELX);
			}
		}
		if (Math.abs(this.getVely()) > Paddle.MAX_VELY) {
			if (this.getVely() > 0) {
				this.setVely(Paddle.MAX_VELY);
			} else {
				this.setVely(-Paddle.MAX_VELY);
			}
		}
	}

	/**
	 * Set the needed info from the ball
	 * 
	 * @param ball:	PongBall Vector to retrieve info from
	 */
	public void setInfo(final Vector<PongBall> ball){
		if (ball.size() == 0) {
			return;
		}

		PongBall to_go_for=this.calcBallToGoFor(ball);
		this.setToGoFor(to_go_for);
	}

	/**
	 * Returns which ball the paddle should go for
	 * @return PongBall to intercept
	 */
	private PongBall calcBallToGoFor(final Vector<PongBall> balls) {
		if (this.is_computer) {
			if (this.player_num==0) {
				return this.getToGoFor0(balls);
			} else {
				return this.getToGoFor1(balls);
			}
		}

		return null;
	}

	private PongBall getToGoFor0(final Vector<PongBall> balls) {
		int clossest_ball=0;
		double clossest_dist=0;
		for (int i=0; i<balls.size(); i++) {
			PongBall b=balls.get(i);
			if (b.getVelx() < 0 && b.isActive()) {	//Moving toward player 0
				double distance=Common.distance(this.getPosx(), this.getPosy(), balls.get(i).getPosx(), balls.get(i).getPosy());

				if (i==0 || distance < clossest_dist) {
					clossest_dist=distance;
					clossest_ball=i;
				}
			}
		}

		return balls.get(clossest_ball);
	}

	private PongBall getToGoFor1(final Vector<PongBall> balls) {
		int clossest_ball=0;
		double clossest_dist=0;
		for (int i=0; i<balls.size(); i++) {
			PongBall b=balls.get(i);
			if (b.getVelx() > 0 && b.isActive()) {	//Moving toward player 1
				double distance=Common.distance(this.getPosx(), this.getPosy(), balls.get(i).getPosx(), balls.get(i).getPosy());

				if (i==0 || distance < clossest_dist) {
					clossest_dist=distance;
					clossest_ball=i;
				}
			}
		}

		return balls.get(clossest_ball);
	}

	/**
	 * Moves a computer controlled Paddle to intercept a PongBall
	 * 
	 * @param ball:	PongBall to move the paddle toward
	 */
	private void moveComputerPlayer(final PongBall ball){
		if (this.done_move == true){
			this.done_move=false;

			double final_y=0;
			double slope_m=0;
			double intersept_b=0;

			switch (this.getPlayernum()){
				case 0:
					if (ball.getVelx() < 0){
						/*
						 * Set up equation that predicts the final position
						 * of the currently checked PongBall
						 */
						slope_m     =ball.getVely() / ball.getVelx();
						intersept_b =ball.getPosy() - (slope_m * ball.getPosx());
						final_y		=(int) Math.abs(slope_m * (this.getPosx() + this.getWidth()) + intersept_b);

						/*
						 * If the final position is out of bounds, try to
						 * bring it back into bounds with a reasonable prediction
						 */
						if (final_y > jPong.sheight()) {
							final_y = jPong.sheight() - (final_y - jPong.sheight());
						}

						/*
						 * Move the paddle based on what we actually predicted
						 * (or calculated) the final position of the PongBall to
						 * be
						 */
						if (this.getPosy() < final_y - this.getHeight()) {
							if (Math.random() * 10  ==  0) {
								this.setVely(-Paddle.STD_VEL);
							} else {
								this.setVely(Paddle.STD_VEL);
							}
						} else if (this.getPosy() > final_y + ball.getFrame().getHeight()) {
							if (Math.random() * 10  ==  0) {
								this.setVely(Paddle.STD_VEL);
							} else {
								this.setVely(-Paddle.STD_VEL);
							}
						}
					}
					break;
				case 1:
					if (ball.getVelx() > 0){
						/*
						 * Set up equation that predicts the final position
						 * of the currently checked PongBall
						 */
						slope_m     =ball.getVely() / ball.getVelx();
						intersept_b =ball.getPosy() - (slope_m * ball.getPosx());
						final_y     =(int) Math.abs(slope_m * this.getPosx() + intersept_b);

						/*
						 * If the final position is out of bounds, try to
						 * bring it back into bounds with a reasonable prediction
						 */
						if (final_y > jPong.sheight()) {
							final_y = jPong.sheight() - (final_y - jPong.sheight());
						}

						/*
						 * Move the paddle based on what we actually predicted
						 * (or calculated) the final position of the PongBall to
						 * be
						 */
						if (this.getPosy() < final_y - this.getHeight()) {
							if (Math.random() * 10  ==  0) {
								this.setVely(-Paddle.STD_VEL);
							} else {
								this.setVely(Paddle.STD_VEL);
							}
						} else if (this.getPosy() > final_y + ball.getFrame().getHeight()) {
							if (Math.random() * 10  ==  0) {
								this.setVely(Paddle.STD_VEL);
							} else {
								this.setVely(-Paddle.STD_VEL);
							}
						}
					}
					break;
				default:
					break;
			}
		}
	}

	//Mouse Listeners
	@Override
	public void mouseExited(final MouseEvent e) {}
	@Override
	public void mouseEntered(final MouseEvent e) {}
	@Override
	public void mouseClicked(final MouseEvent e) {}
	@Override
	public void mouseMoved(final MouseEvent e) {}

	@Override
	public void mouseDragged(final MouseEvent e) {
		this.mouse_enabled=true;
		int diff=e.getLocationOnScreen().y - this.getMpos();
		this.setPosition(this.posx, e.getLocationOnScreen().y - (this.getHeight() / 2));
		this.setPsudoVely(diff);
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		this.mouse_enabled=false;
		this.setVely(this.getPsudoVely());
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		this.mouse_enabled=true;
		this.setMpos(e.getLocationOnScreen().y);
		this.setOrigy(this.getPosy());
	}

	//Key Listeners
	@Override
	public void keyTyped(final KeyEvent e) {}
	@Override
	public void keyReleased(final KeyEvent e) {}

	@Override
	public void keyPressed(final KeyEvent e) {
		this.mouse_enabled=false;
		int key=e.getKeyCode();

		//Paddle Specific Keys
		if (this.player_num==0) {
			if (key==KeyEvent.VK_S) {
				this.setVely(Paddle.STD_VEL);
				ThreadPrinter.print("VK_S");
			}
			if (key==KeyEvent.VK_W) {
				this.setVely(-Paddle.STD_VEL);
				ThreadPrinter.print("VK_W");
			}
		} else {
			if (key==KeyEvent.VK_DOWN) {
				this.setVely(Paddle.STD_VEL);
				ThreadPrinter.print("VK_DOWN");
			}
			if (key==KeyEvent.VK_UP) {
				this.setVely(-Paddle.STD_VEL);
				ThreadPrinter.print("VK_UP");
			}
		}

		//Global keys
		switch (key){
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_Q:
				ThreadPrinter.print("Quit");
				Paddle.DONE=true;
				break;

			case KeyEvent.VK_T:
				this.is_computer=!this.is_computer;
				if (this.is_computer) {
					super.setTitle(this.CTITLE);
					ThreadPrinter.print("Paddle " + this.getPlayernum() + " is now COMPUTER");
				} else {
					super.setTitle(this.HTITLE);
					ThreadPrinter.print("Paddle " + this.getPlayernum() + " is now HUMAN");
				}
				break;

			default:
				break;
		}
	}

	/**
	 * Applies a Force to the Paddle in fluid mode
	 * 
	 * @param force:	force to apply to paddle
	 */
	public void applyForce(final Force force) {
		this.force=force;
	}

	/**
	 * Prints info about this instance
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.getPlayernum() + "; (" + (int) this.getPosx() + ", " + (int) this.getPosy() + "); " + this.getWidth() + "x" + this.getHeight() + "; "
				+ (this.is_computer ? "COMPUTER" : "HUMAN") + "; Extras:" + Paddle.ENABLE_EXTRAS + "; " + Paddle.STD_VEL + "]";
	}



	/////////////
	//ACCESSORS//
	/////////////

	protected void setVelx(final double x) {
		this.velx = x;
	}

	protected void setVely(final double y) {
		this.vely = y;
	}

	public double getVelx() {
		return this.velx;
	}

	public double getVely() {
		return this.vely;
	}

	protected void setPosx(final double x) {
		this.posx = x;
	}

	protected void setPosy(final double y) {
		this.posy = y;
	}
	public double getPosx() {
		return this.posx;
	}

	public double getPosy() {
		return this.posy;
	}

	protected void setPosition(final double x, final double y) {
		this.posx = x;
		this.posy = y;
	}

	//PLAYER_NUM
	public int getPlayernum(){
		return this.player_num;
	}

	//m_start_coords
	/**
	 * @param x:	Mouse's X position
	 */
	public void setMpos(final int x) {
		this.mpos=x;
	}
	public int getMpos(){
		return this.mpos;
	}

	//ORIGY
	/**
	 * @param y:	Paddle's origional y position
	 */
	public void setOrigy(final double y) {
		this.origy = y;
	}

	public double getOrigy() {
		return this.origy;
	}

	//PSUDO_VELY
	/**
	 * @param psudo_vely:	Paddle's y velocity based on mouse movement
	 */
	public void setPsudoVely(final int psudo_vely) {
		this.psudo_vely = psudo_vely;
	}

	public int getPsudoVely() {
		return this.psudo_vely;
	}

	/**
	 * @param to_go_for:	PongBall for Paddle to go for
	 */
	public void setToGoFor(final PongBall to_go_for) {
		this.to_go_for = to_go_for;
	}

	public PongBall getToGoFor() {
		return this.to_go_for;
	}

	//MOUSE_ENABLED
	public boolean isMouseEnabled(){
		return this.mouse_enabled;
	}

	//Number of Paddles
	public static int numPaddles() {
		return Paddle.TOTAL_PLAYERS;
	}

	//Mass
	public long getMass() {
		return this.getWidth() * this.getHeight() * 3000l;
	}

	public JFrame getFrame() {
		return this;
	}
}
