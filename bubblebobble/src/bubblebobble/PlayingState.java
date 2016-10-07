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
	float gravity = 0.15f;
	boolean jumping = false;
	float jumpDistance = 0;
	int time = 0;
	float overlap = 0;
	private Animation walking;
	private Animation standing;
	private Animation jump;
	private boolean walk = false;
	private boolean stand = true;
	private boolean facingLeft = false;
	private boolean walkingLeft = false;
	private boolean walkingRight = false;
	private boolean standStill = false;
	private boolean idle = false;
	float velocityY = 0;
	float velocityX = 0;
	float positionX = 0;
	float positionY = 0;
	float bottomToMiddle = 0;
	
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
	
	public void StartJump()
	{
		velocityY = -0.45f;
		onPlatform = false;
		jumping = true;
	}
	
	public void EndJump()
	{
		if(velocityY < -0.125)
			velocityY = -0.125f;
	}
	
	public void UpdateJump(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		velocityY += 0.013;
		
		if(velocityY > 0)
			jumping = false;
	}
	
	public void WalkLeft(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		//float xVelocity = bbg.bub.getVelocity().getX();
		
//		System.out.println(bbg.bub.getNumAnimations());
		
//		if(onPlatform){
			if(bbg.bub.getVelocity().getX() > 0){
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_LEFT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
			else if(bbg.bub.getVelocity().getX() == 0){
				bbg.bub.removeAnimation();
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_LEFT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
//		}
		
		facingLeft = true;
//		bbg.bub.setVelocity(new Vector(-0.2f, velocityY));
		velocityX = -0.2f;
		walkingLeft = true;
		walkingRight = false;
		stand = true;
		//standStill = false;
	}
	
	public void WalkRight(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		float yVelocity = bbg.bub.getVelocity().getY();
		
//		if(onPlatform){
			if(bbg.bub.getVelocity().getX() < 0){
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_RIGHT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
			else if(bbg.bub.getVelocity().getX() == 0){
				bbg.bub.removeAnimation();
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_RIGHT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
//		}
		
		facingLeft = false;
		
		//bbg.bub.setVelocity(new Vector(0.2f, velocityY));
		velocityX = 0.2f;
		walkingLeft = false;
		walkingRight = true;
		stand = true;
		//standStill = false;
	}
	
	public void StandStill(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
//		float yVelocity = bbg.bub.getVelocity().getY();
		
		//System.out.println(yVelocity);
		
		if (stand == true)
		{
			bbg.bub.removeAnimation(walking);
			
			bbg.bub.removeImage(ResourceManager.getImage(bbg.BUB_STANDING));
			
			if(facingLeft == true)
			{
				bbg.bub.AnimateLeft();
			}
			else
			{
				bbg.bub.AnimateRight();
			}
				
			stand = false;
			//bbg.bub.addImageWithBoundingBox(ResourceManager.getImage(bbg.BUB_STANDING));
		}
		walkingLeft = false;
		walkingRight = false;
		standStill = true;
		velocityX = 0;
		//bbg.bub.setVelocity(new Vector(0,velocityY));
	}
	
	public void Controls(StateBasedGame game, GameContainer container)
	{
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Move the character left
		if (input.isKeyDown(Input.KEY_LEFT) 
			&& input.isKeyPressed(Input.KEY_LEFT)
			)//&& !(input.isKeyDown(Input.KEY_RIGHT)))
		{
			WalkLeft(bbg);
			
			if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
			{
				StartJump();
				
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_JUMPING_LEFT, 45, 40), 
						0, 0, 1, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
		}
		
		// Move the character right
		else if (input.isKeyDown(Input.KEY_RIGHT)
				&& input.isKeyPressed(Input.KEY_RIGHT)
				)//&& !(input.isKeyDown(Input.KEY_LEFT))) 
		{
			WalkRight(bbg);
			
			if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
				StartJump();
		}
		
		// Jumping while idle
		else if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
		{
			StartJump();
			
//			if(velocityY < 0)
//			{
//				System.out.println("Jumping right");
//				bbg.bub.removeAnimation(walking);
//				jump = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_JUMPING_RIGHT, 45, 40), 
//						0, 0, 1, 0, true, 120, true);
//				bbg.bub.addAnimation(jump);
//			}
//			else if(velocityY > 0)
//			{
//				System.out.println("Jumping left");
//				bbg.bub.removeAnimation(walking);
//				jump = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_JUMPING_LEFT, 45, 40), 
//						0, 0, 1, 0, true, 120, true);
//				bbg.bub.addAnimation(jump);
//			}
			
//			bbg.bub.removeAnimation(walking);
//			walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_JUMPING_RIGHT, 45, 40), 
//					0, 0, 1, 0, true, 120, true);
//			bbg.bub.addAnimation(walking);
		}
		
		// Not moving
		else if(!(input.isKeyDown(Input.KEY_LEFT)) 
				&& !(input.isKeyDown(Input.KEY_RIGHT)))
		{
			StandStill(bbg);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for (Bricks b : bbg.brick)
		{
			if (bbg.bub.collides(b) != null)
			{
				if (bbg.bub.getCoarseGrainedMaxY() < b.getCoarseGrainedMinY()+5
					&& jumping == false)
				{
					onPlatform = true;
//					bbg.bub.removeAnimation(jump);
					//velocityY = gravity;
					bottomToMiddle = b.getCoarseGrainedMinY() - (bbg.bub.getCoarseGrainedMaxY() - bbg.bub.getPosition().getY());
					bbg.bub.setPosition(bbg.bub.getX(), bottomToMiddle-1);
				}
				else if (bbg.bub.getCoarseGrainedMinX() < 48)
				{
					bbg.bub.setPosition(68, bbg.bub.getY());
				}
				else if (bbg.bub.getCoarseGrainedMaxX() > bbg.ScreenWidth - 48)
				{
					bbg.bub.setPosition(bbg.ScreenWidth - 68, bbg.bub.getY());
				}
			}
		}
		
		if(bbg.bub.getCoarseGrainedMinX() < 0)
		{
			bbg.bub.setPosition(20, bbg.bub.getY());
			if(bbg.bub.getCoarseGrainedMinY() < 10)
				EndJump();
		}
		else if(bbg.bub.getCoarseGrainedMaxX() > bbg.ScreenWidth)
		{
			bbg.bub.setPosition(bbg.ScreenWidth - 20, bbg.bub.getY());
			if(bbg.bub.getCoarseGrainedMinY() < 10)
				EndJump();
		}
		else if(bbg.bub.getCoarseGrainedMinY() < 12)
			EndJump();
		
		Controls(bbg, container);
		
		if(jumping)
			UpdateJump(bbg);

		bbg.bub.setVelocity(new Vector(velocityX, velocityY));
		
		velocityX = bbg.bub.getVelocity().getX();
		
		if(jumping == false)
		{
			bbg.bub.setVelocity(new Vector(velocityX, gravity));
			onPlatform = false;
		}
		else
		{
			bbg.bub.setVelocity(new Vector(velocityX, velocityY));
			//bbg.bub.setPosition(new Vector(positionX, positionY));
		}
		
		//bbg.bub.setVelocity(new Vector(velocityX, velocityY));
		bbg.bub.update(delta);		
		
	}
	 
	@Override
	public int getID() {
		return BubbleBobbleGame.PLAYINGSTATE;
	}
}
