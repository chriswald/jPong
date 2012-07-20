/**
 * @author  Chris Wald
 * @date    Sep 29, 2010 8:02:14 PM
 * @project jPong
 * @file    PongBall.java
 */

package pkg;


import home.jPong;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreBoard extends JFrame implements Runnable, KeyListener{

	private static final long			serialVersionUID	= 1L;
	private JLabel						score0l				=null;
	private JLabel						score1l				=null;
	private JLabel						score_spacer		=null;
	private JPanel						panel				=null;
	private int							score0				=0;
	private int							score1				=0;
	private double						posx				=0.0;
	private double						posy				=0.0;
	private double						velx				=0.0;
	private double						vely				=0.0;
	private static boolean				done				=false;
	private static boolean				MOVEABLE			=false;

	public ScoreBoard(final int width, final int height, final boolean move){
		super("Ruck");

		this.panel=new JPanel(new GridLayout(1,3));

		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setScore(0,0);
		this.setScore(1,0);

		this.score0l=new JLabel("0");
		this.score0l.setFont(new Font("Calibri", Font.BOLD, 48));
		this.score0l.setHorizontalTextPosition(JLabel.LEFT);

		this.score1l=new JLabel("0");
		this.score1l.setFont(new Font("Calibri", Font.BOLD, 48));
		this.score1l.setHorizontalTextPosition(JLabel.LEFT);

		this.score_spacer=new JLabel(" ");
		this.score_spacer.setFont(new Font("Calibri", Font.BOLD, 40));
		this.score_spacer.setHorizontalTextPosition(JLabel.CENTER);

		this.panel.add(this.score0l);
		this.panel.add(this.score_spacer);
		this.panel.add(this.score1l);

		this.setPosx(jPong.swidth() / 2 - this.getWidth() / 2);
		this.setPosy(0);
		this.setLocation((int) this.getPosx(), (int) this.getPosy());

		this.addKeyListener(this);

		this.add(this.panel);
		this.setVisible(true);

		/*
		 * Set velocity for the Score board
		 */
		ScoreBoard.MOVEABLE=move;
		if (move) {
			do {
				final int vr=40;
				this.velx=Math.random() * vr - (vr / 2);
				this.vely=Math.random() * vr - (vr / 2);
			} while (this.velx == 0  ||  this.vely == 0);
		} else {
			this.velx=0.0;
			this.vely=0.0;
		}

		if (home.jPong.VERBOSE) {
			ThreadPrinter.print_ntd(this.toString());
		}
	}

	@Override
	public synchronized void run(){
		ThreadPrinter.print("In Thread");

		while (!ScoreBoard.done){

			this.getScores();
			this.update();

			try{
				Thread.sleep(25);
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
		ScoreBoard.done=true;
	}

	@Override
	public void keyTyped(final KeyEvent e) {}
	@Override
	public void keyReleased(final KeyEvent e) {}
	@Override
	public void keyPressed(final KeyEvent e) {
		int key=e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_Q:
				ThreadPrinter.print("Quit");
				ScoreBoard.sigDone();
				break;
			default:	break;
		}
	}

	public void getScores(){
		this.setScore(0, PongBall.points[0]);
		this.setScore(1, PongBall.points[1]);
		this.score0l.setText("" + this.getScore(0));
		this.score1l.setText("" + this.getScore(1));
		this.pack();
		this.reposition();
	}

	private void reposition() {
		if (!ScoreBoard.MOVEABLE) {
			this.setPosx((jPong.swidth() / 2) - (this.getWidth() / 2));
			this.setPosy(0);
			this.setLocation((int) this.getPosx(), (int) this.getPosy());
		}
	}

	private void update() {
		/*
		 * Update Positions
		 */
		this.setPosx(this.getPosx() + this.velx);
		this.setPosy(this.getPosy() + this.vely);

		/*
		 * Check for side collisions
		 */
		if (this.getPosx() < 0){  //Hit left side
			this.setPosx(0);
			this.velx=-this.velx;
		}
		if (this.getPosx() + this.getWidth() > jPong.swidth()){  //Hit right side
			this.setPosx(jPong.swidth() - this.getWidth());
			this.velx=-this.velx;
		}

		if (this.getPosy() < 0){  //Hit top of screen
			this.setPosy(0);
			this.vely=-this.vely;
		}
		if (this.getPosy() + this.getHeight()  >  jPong.sheight()){  //Hit bottom of screen
			this.setPosy(jPong.sheight() - this.getHeight());
			this.vely=-this.vely;
		}

		//Move Window
		this.setLocation((int) this.getPosx(), (int) this.getPosy());
	}

	public void checkCollide(final Paddle pad){
		/*
		 * If the mouse is enable, set proper
		 * velocities to simulate keyboard based
		 * motion
		 */
		if (pad.isMouseEnabled()) {
			if (pad.getPsudoVely() > 40) {
				pad.setVely(40);
			} else if (pad.getPsudoVely() <= 40  &&  pad.getPsudoVely() >= 5) {
				pad.setVely(pad.getPsudoVely());
			} else if (pad.getPsudoVely() < 5) {
				pad.setVely(pad.getVely());
			}
		}

		/*
		 * Do actual collision checking
		 */
		if (this.isInside(pad)) {
			this.collided(pad);
		}

		/*
		 * Return actual velocities to their
		 * proper values
		 */
		if (pad.isMouseEnabled()){
			pad.setVely(0);
			pad.setPsudoVely(0);
		}
	}

	protected void collided(final Paddle pad){
		/*
		 * Move the ball back outside of the paddle
		 */
		if (this.velx < 0){
			this.setPosx(pad.getPosx() + pad.getWidth() + 2);
		}else{
			this.setPosx(pad.getPosx() - this.getWidth());
		}

		/*
		 * Switch direction
		 */
		this.velx=-this.velx;

		/*
		 * Set velocities of the paddle if
		 * is to act fluidly (enable_extras
		 * is true)
		 */
		if (Paddle.ENABLE_EXTRAS){	//If the paddle is to act fluidly
			pad.setVelx(-this.velx);
			pad.setVely(pad.getVely() + this.vely);
		}
	}

	protected boolean isInside(final Paddle pad){
		/*
		 * Check to see if something is inside of another
		 */
		if (this.getPosx() + this.getWidth()	    > pad.getPosx() + 1						&&
				this.getPosx()						< pad.getPosx() + pad.getWidth() + 1	&&
				this.getPosy() + this.getHeight()	> pad.getPosy() + 1						&&
				this.getPosy()						< pad.getPosy() + pad.getHeight() + 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.getWidth() + "x" + this.getHeight() + ", Moveable:" + ScoreBoard.MOVEABLE + "]";
	}





	/////////////
	//ACCESSORS//
	/////////////

	private void setPosx(final double d) {
		this.posx = d;
	}

	protected double getPosx() {
		return this.posx;
	}

	private void setPosy(final double d) {
		this.posy = d;
	}

	protected double getPosy() {
		return this.posy;
	}

	private void setScore(final int score_num, final int score) {
		switch(score_num) {
			case 0:	this.score0=score;	break;
			case 1: this.score1=score;	break;
			default:	break;
		}
	}

	private int getScore(final int score_num) {
		switch(score_num) {
			case 0:		return this.score0;
			case 1: 	return this.score1;
			default:	return -1;
		}
	}
}
