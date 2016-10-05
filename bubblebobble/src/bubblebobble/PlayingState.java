package bubblebobble;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;
import jig.Vector;

public class PlayingState extends BasicGameState {

	boolean onPlatform = false;
	float gravity = 0;
	boolean jumping = false;
	float jumpDistance = 0;
	int time = 0;
	float overlap = 0;
	private Animation walking;
	
	Toolkit toolkit;
	Timer timer;

	public void ReminderBeep(int seconds) {
		toolkit = Toolkit.getDefaultToolkit();
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
	}

	class RemindTask extends TimerTask {
		public void run() {
			System.out.println("Time's up!");
			toolkit.beep();
			//timer.cancel(); //Not necessary because we call System.exit
			System.exit(0); //Stops the AWT thread (and everything else)
		}
	}
	  
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Lower left brick
		bbg.brick.add(new Bricks(12, bbg.ScreenHeight-12, 1, 1));
		
		// Add bottom bricks
		for (int i = 3; i < 33; i++)
		{
			bbg.brick.add(new Bricks(i*24-12, bbg.ScreenHeight-12, 1, 1));
		}	
		
		// Add top bricks (left side of level indicator)
		for (int i = 3; i < 16; i++)
		{
			bbg.brick.add(new Bricks(i*24-12, 84, 1, 1));
		}
		
		// Add top bricks (right side of level indicator)
		for (int i = 18; i < 32; i++)
		{
			bbg.brick.add(new Bricks(i*24-12, 84, 1, 1));
		}	
		
		// Left side bricks
		for (int i = 2; i < 14; i++)
		{
			bbg.brick.add(new Bricks(24, 48*i, 1, 4));
		}	
		
		// Right side bricks
		for (int i = 2; i < 14; i++)
		{
			bbg.brick.add(new Bricks(bbg.ScreenWidth-24, 48*i, 1, 4));
		}
		
		// Add top platform
		for (int i = 1; i < 7; i++)
		{
			bbg.brick.add(new Bricks(12*24 + 24*i+12, 8*24, 1, 1));
		}	
		
		// Add second to top platform
		for (int i = 1; i < 13; i++)
		{
			if (i != 6 && i != 7)
				bbg.brick.add(new Bricks(9*24 + 24*i+12, 13*24, 1, 1));
		}
		
		// Add third to top platform
		for (int i = 1; i < 19; i++)
		{
			bbg.brick.add(new Bricks(6*24 + 24*i+12, 18*24, 1, 1));
		}
		
		// Add bottom top platform
		for (int i = 1; i < 25; i++)
		{
			if (i != 8 && i != 9 && i != 16 && i != 17)
				bbg.brick.add(new Bricks(3*24 + 24*i+12, 23*24, 1, 1));
		}
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for (Bricks b : bbg.brick)
		{
			b.render(g);
		}
		
		bbg.bub.render(g);
		
	}
	
//	public void jump()
//	{
//		if(gameTime.getTimeMS() - jumpTime > jumpDelayInMilliseconds) {
//		       jumpTime = gameTime.getTimeMS();
//		       character.jump();
//	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		if(onPlatform == false 
			&& jumping == false
			//&& bbg.bub.getCoarseGrainedMaxY() > jumpDistance
			&& bbg.bub.getCoarseGrainedMaxY() < bbg.ScreenHeight 
			&& bbg.bub.getCoarseGrainedMinX() > 48)
		{
			jumpDistance = 0;
			gravity = 0.15f;
		}
		
		for (Bricks b : bbg.brick)
		{
			if (bbg.bub.collides(b) != null 
				&& bbg.bub.getCoarseGrainedMaxY() < b.getCoarseGrainedMinY()+5
				&& bbg.bub.getCoarseGrainedMinX() > 48)
//				&& bbg.bub.getCoarseGrainedMaxX() < bbg.ScreenWidth - 48)
			{
				gravity = 0;
				onPlatform = true;
				jumping = false;
				overlap = Math.abs(b.getCoarseGrainedMinY() - bbg.bub.getCoarseGrainedMaxY()) - 1;
				bbg.bub.setPosition(bbg.bub.getX(), bbg.bub.getY() - overlap);
				//System.out.println(overlap);
				//System.out.println("Touching platform! gravity = " + gravity);
			}
			else
			{
				onPlatform = false;
			}
		}
		
		//System.out.println("x = " + bbg.bub.getCoarseGrainedMinX());
		//System.out.println("Velocity = " + bbg.bub.getVelocity());	
		
		float yVelocity = bbg.bub.getVelocity().getY();
		// Move the paddle left
		if (input.isKeyDown(Input.KEY_LEFT) 
			&& bbg.bub.getCoarseGrainedMinX() > 48
			&& bbg.bub.getCoarseGrainedMaxY() < bbg.ScreenHeight)
		{
			bbg.bub.setVelocity(new Vector(-0.2f, yVelocity));
//			bbg.bub.removeImage(ResourceManager.getImage(bbg.BUB_STANDING));
//			walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING, 42, 42), 
//					0, 0, 7, 0, true, 100, true);
//			bbg.bub.addAnimation(walking);
			if (input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL) && onPlatform == true)
			{
				jumping = true;
				onPlatform = false;
				jumpDistance = bbg.bub.getCoarseGrainedMaxY() - 48;
				gravity = -0.15f;
				//bbg.bub.setVelocity(new Vector(xVelocity, -0.1f));
			}
		}
		// Move the paddle right
		else if (input.isKeyDown(Input.KEY_RIGHT) && bbg.bub.getCoarseGrainedMaxX() < bbg.ScreenWidth - 48) 
		{
			bbg.bub.setVelocity(new Vector(0.2f, yVelocity));
			
			if (input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL) && onPlatform == true)
			{
				jumping = true;
				onPlatform = false;
				jumpDistance = bbg.bub.getCoarseGrainedMaxY() - 48;
				gravity = -0.15f;
				//bbg.bub.setVelocity(new Vector(xVelocity, -0.1f));
			}
		}
		else if (input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL) && onPlatform == true)
		{
			jumping = true;
			onPlatform = false;
			jumpDistance = bbg.bub.getCoarseGrainedMaxY() - 48;
			gravity = -0.15f;
			//bbg.bub.setVelocity(new Vector(xVelocity, -0.1f));
		}
		else
		{
			bbg.bub.setVelocity(new Vector(0,yVelocity));
		}
		float xVelocity = bbg.bub.getVelocity().getX();
		bbg.bub.setVelocity(new Vector(xVelocity, gravity));
//		if (onPlatform == false)
//		{
//			float xVelocity = bbg.bub.getVelocity().getX();
//			bbg.bub.setVelocity(new Vector(xVelocity, 0.1f));
//		}
//		float yVelocity = gravity;
//		float xVelocity = bbg.bub.getVelocity().getX();
		//System.out.println("yVelocity = " + yVelocity);
		bbg.bub.update(delta);
	}
	 
	@Override
	public int getID() {
		return BubbleBobbleGame.PLAYINGSTATE;
	}
}
