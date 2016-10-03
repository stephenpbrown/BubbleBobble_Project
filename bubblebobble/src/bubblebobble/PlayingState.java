package bubblebobble;

import java.util.Iterator;

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
	int time = 0;
	
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
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		if(onPlatform == false)
			gravity = 0.1f;
		for (Bricks b : bbg.brick)
		{
			if (bbg.bub.collides(b) != null)
			{
				gravity = 0;
				onPlatform = true;
				System.out.println("Touching platform! gravity = " + gravity);
			}
			else
			{
				onPlatform = false;
			}
		}
		
		float xVelocity = bbg.bub.getVelocity().getX();
		bbg.bub.setVelocity(new Vector(xVelocity, gravity));
		
		System.out.println("Velocity = " + bbg.bub.getVelocity());	
		
		float yVelocity = bbg.bub.getVelocity().getY();
		// Move the paddle left
		if (input.isKeyDown(Input.KEY_LEFT) && bbg.bub.getCoarseGrainedMinX() > 48) 
		{
			bbg.bub.setVelocity(new Vector(-0.25f, yVelocity));
		}
		// Move the paddle right
		else if (input.isKeyDown(Input.KEY_RIGHT) && bbg.bub.getCoarseGrainedMaxX() < bbg.ScreenWidth - 48) 
		{
			bbg.bub.setVelocity(new Vector(0.25f, yVelocity));
		}
		else if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL) && onPlatform == true)
		{
			bbg.bub.setVelocity(new Vector(xVelocity, -0.1f));
		}
		else
		{
			bbg.bub.setVelocity(new Vector(0,yVelocity));
		}	
		
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
