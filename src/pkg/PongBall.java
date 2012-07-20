/**
 * @author  Chris Wald
 * @date    Sep 29, 2010 8:02:14 PM
 * @project jPong
 * @file    PongBall.java
 */

package pkg;

import home.jPong;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import collision.CollisionDetection;
import collision.Side;

public class PongBall extends JFrame implements Runnable{
	private static final long	serialVersionUID	= 1L;
	private double				velx				= 0.0;
	private double				vely				= 0.0;
	private double				posx				= 0.0;
	private double				posy				= 0.0;
	private double				sposx				= 0.0;
	private double				sposy				= 0.0;
	private Force				force				= new Force(0,0);
	private boolean				active				= false;		//States whether the ball is in play and moving or not

	public static int[]			points				= {0,0};
	private static final int	SPEED_INCREASE		= 1;
	public static final int		REST_MILLIS			= 25;
	public static final int		NEW_POINT_MILLIS	= 1500;
	public static int[]			MAX_VEL				= {(int) (jPong.swidth() * .06), (int) (jPong.sheight() * .06)};
	private static boolean		DONE				= false;
	private static final int	XVEL				= 0;
	private static final int	YVEL				= 1;




	/**
	 * Constructor
	 */
	public PongBall(){
		super("O");
		this.setSize(90, 90);
		this.setPreferredSize(new Dimension(90, 90));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);

		if (home.jPong.VERBOSE) {
			ThreadPrinter.print_ntd(this.toString());
		}
	}

	/**
	 * Main driver method of each PongBall
	 */
	@Override
	public synchronized void run(){
		ThreadPrinter.print("In Thread");

		this.setVisible(true);
		this.start_new((int) Math.random() * Paddle.numPaddles());

		while (!PongBall.DONE){
			this.update();

			try{
				Thread.sleep(PongBall.REST_MILLIS);
			}catch (InterruptedException e){
				System.out.println(e.getMessage() + ":" + Thread.currentThread().getStackTrace()[1].getFileName() + " in "
						+ Thread.currentThread().getStackTrace()[1].getMethodName() + "() at "
						+ Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		}

		this.dispose();
		ThreadPrinter.print("Exiting Thread");
	}

	public static void sigDone(){
		PongBall.DONE=true;
	}

	public void sigPoint(final int player){
		this.active=false;
		this.augPoints(player);
	}

	private void start_new(final int player){
		int[] speed_mod={83, 233};	//Lower Values Move Faster {x speed mod, y speed mod}

		if (player == 0){
			this.sposx=jPong.swidth() * .85 - this.getWidth() - 10;
			this.sposy=(int) (Math.random() * (jPong.sheight() - this.getFrame().getHeight()));

			this.posx=this.sposx;
			this.posy=this.sposy;

			this.velx = -((Math.random() * 3) + (jPong.swidth()  / speed_mod[0]));
			this.vely =  ((Math.random() * 3) - 1) * ((Math.random() * 4) + (jPong.sheight() / speed_mod[1]));

			this.setLocation((int) this.posx, (int) this.posy);
			this.getFrame().getContentPane().setBackground(this.pickNewColor());
		} else {
			this.sposx=jPong.swidth() * .15;
			this.sposy=(int) (Math.random()*(jPong.sheight() - this.getFrame().getHeight()));

			this.posx=this.sposx;
			this.posy=this.sposy;

			this.velx = ((Math.random() * 3) + (jPong.swidth()  / speed_mod[0]));
			this.vely = ((Math.random() * 3) - 1) * ((Math.random() * 4) + (jPong.sheight() / speed_mod[1]));

			this.setLocation((int) this.posx, (int) this.posy);
			this.getFrame().getContentPane().setBackground(this.pickNewColor());
		}
		try{
			//Give the player some time to get ready
			Thread.sleep(PongBall.NEW_POINT_MILLIS);
		}
		catch(InterruptedException e){
			System.out.println(e.getMessage() + ":" + Thread.currentThread().getStackTrace()[1].getFileName() + " in "
					+ Thread.currentThread().getStackTrace()[1].getMethodName() + "() at "
					+ Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		this.active=true;
	}

	public void checkCollide(final Paddle pad){
		if (pad.isMouseEnabled()) {
			if (pad.getPsudoVely() > 40) {
				pad.setVely(40);
			} else if (pad.getPsudoVely() <= 40  &&  pad.getPsudoVely() >= 5) {
				pad.setVely(pad.getPsudoVely());
			} else if (pad.getPsudoVely() < 5) {
				pad.setVely(pad.getVely());
			}
		}

		Side side=CollisionDetection.detectCollisionArea(pad.getFrame(), this.getFrame());
		if (side!=Side.NONE) {
			this.collided(pad);
		}

		if (pad.isMouseEnabled()){
			pad.setVely(0);
			pad.setPsudoVely(0);
		}

	}

	protected void collided(final Paddle pad){
		//Move the ball back outside of the paddle
		if (this.getVelx() < 0){
			this.setPosx(pad.getPosx() + pad.getWidth() + 2);
		}else{
			this.setPosx(pad.getPosx() - this.getFrame().getWidth());
		}

		this.setNewVely(pad);
		this.setVelx(-this.getVelx());
		this.update();

		if (this.getAbsVelx() < PongBall.MAX_VEL[PongBall.XVEL]) {	//Add or subtract the velocity depending
			if (this.getVelx() < 0) {						//on which direction the ball is going
				this.setVelx(this.getVelx() - PongBall.SPEED_INCREASE);
			} else {
				this.setVelx(this.getVelx() + PongBall.SPEED_INCREASE);
			}
		}

		if (Paddle.ENABLE_EXTRAS){	//If the paddle is to act fluidly
			pad.applyForce(new Force(-this.getVelx(), this.getVely()));
		}
	}


	private void setNewVely(final Paddle pad){
		int    ball_mid     = this.getHeight() / 2;
		int    padd_mid     = pad.getHeight()  / 2;
		int    mid_height   = ball_mid + padd_mid - this.getHeight();
		double mid_height_y = pad.getPosy() + mid_height;

		double diff    = this.getPosy() - mid_height_y;
		double new_vel = (diff / 5) + (pad.getVely() / 4);	//Higher Divisor Values move slower

		this.setVely(new_vel);
	}

	@SuppressWarnings("unused")
	private void setNewVelx(final Paddle pad){
		int    ball_mid    =this.getWidth() / 2;
		int    padd_mid    =pad.getWidth()  / 2;
		int    mid_width   =ball_mid + padd_mid - this.getWidth();
		double mid_width_x =pad.getPosy() + mid_width;

		double diff    =this.getPosx() - mid_width_x;
		double new_vel =(diff / 5) + (pad.getVelx() / 4);	//Higher Divisor Values move slower

		this.setVelx(new_vel);
	}


	@SuppressWarnings("unused")
	private boolean isInside(final Paddle pad){
		if (this.getPosx() + this.getFrame().getWidth()	    	> pad.getPosx() + 1						&&
				this.getPosx()									< pad.getPosx() + pad.getWidth() + 1	&&
				this.getPosy() + this.getFrame().getHeight()	> pad.getPosy() + 1						&&
				this.getPosy()									< pad.getPosy()+pad.getHeight() + 1) {
			return true;
		} else {
			return false;
		}
	}

	private void update(){
		/*
		 * Make Sure The Velocities are in bounds
		 */
		if (Math.abs(this.getVelx()) > PongBall.MAX_VEL[PongBall.XVEL]) {
			if (this.getVelx() > 0) {
				this.setVelx(PongBall.MAX_VEL[PongBall.XVEL]);
			} else {
				this.setVelx(-PongBall.MAX_VEL[PongBall.XVEL]);
			}
		}
		if (Math.abs(this.getVely()) > PongBall.MAX_VEL[PongBall.YVEL]) {
			if (this.getVely() > 0) {
				this.setVely(PongBall.MAX_VEL[PongBall.YVEL]);
			} else {
				this.setVely(-PongBall.MAX_VEL[PongBall.YVEL]);
			}
		}

		this.updatePosx();
		this.updatePosy();

		if (this.getPosx() < 0){  //Hit left side
			this.setPosx(0);
			this.flipVelx();
			if (Paddle.numPaddles() > 0) {
				this.sigPoint(1);
				this.start_new(0);
			}
		}
		if (this.getPosx() + this.getWidth() > jPong.swidth()){  //Hit right side
			this.setPosx(jPong.swidth() - this.getWidth());
			this.flipVelx();
			if (Paddle.numPaddles() > 0) {
				this.sigPoint(0);
				this.start_new(1);
			}
		}

		if (this.getPosy() < 0){  //Hit top of screen
			this.setPosy(0);
			this.flipVely();
		}
		if (this.getPosy() + this.getHeight() > jPong.sheight()){  //Hit bottom of screen
			this.setPosy(jPong.sheight() - this.getHeight());
			this.flipVely();
		}

		this.setLocation((int) this.getPosx(), (int) this.getPosy());

	}

	private Color pickNewColor(){
		int red   =(int) (Math.random() * 256);
		int green =(int) (Math.random() * 256);
		int blue  =(int) (Math.random() * 256);

		return new Color(red,green,blue);
	}

	public void applyForce(final Force force) {
		this.force=force;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.getWidth() + "x" + this.getHeight() + "]";
	}

	private void updatePosx(){
		this.setPosx(this.getPosx() + this.getVelx() + this.force.getMagnitudeX());
	}

	private void updatePosy(){
		this.setPosy(this.getPosy() + this.getVely() + this.force.getMagnitudeY());
	}

	private void flipVelx(){
		this.setVelx(-this.getVelx());
	}

	private void flipVely(){
		this.setVely(-this.getVely());
	}

	private void setPosx(final double d){
		this.posx=d;
	}

	public double getPosx(){
		return this.posx;
	}

	private void setPosy(final double d){
		this.posy=d;
	}

	public double getPosy(){
		return this.posy;
	}

	private void setVelx(final double new_vel){
		if (new_vel <= PongBall.MAX_VEL[PongBall.XVEL]) {
			this.velx=new_vel;
		}
	}

	public double getVelx(){
		return this.velx;
	}

	private double getAbsVelx(){
		return Math.abs(this.getVelx());
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@SuppressWarnings("unused")
	private double getAbsVelx_n(){
		return -this.getAbsVelx();
	}

	private void setVely(final double new_vel){
		this.vely=new_vel;
	}

	public double getVely(){
		return this.vely;
	}

	private double getAbsVely(){
		return Math.abs(this.getVely());
	}

	@SuppressWarnings("unused")
	private double getAbsVely_n(){
		return -this.getAbsVely();
	}

	private void augPoints(final int playernum){
		PongBall.points[playernum]++;
	}

	public JFrame getFrame(){
		return this;
	}

	public long getMass() {
		return this.getWidth() * this.getHeight() * 100l;
	}
}
