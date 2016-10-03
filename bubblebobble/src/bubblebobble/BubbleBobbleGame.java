package bubblebobble;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;

public class BubbleBobbleGame extends StateBasedGame
{
	public static final int PLAYINGSTATE = 0;
	
	// Sprites
	public static final String BUB_STANDING = "resource/Bub_Standing.png";
		
	private final int ScreenWidth;
	private final int ScreenHeight;
	
	Bub bub;

	public BubbleBobbleGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayingState());
		
		ResourceManager.loadImage(BUB_STANDING);
		
		bub = new Bub(ScreenWidth / 2, ScreenHeight / 2, .1f, .2f);
	}

	 
	public static void main(String[] args) 
	{
		 AppGameContainer app;
		 try {
			 app = new AppGameContainer(new BubbleBobbleGame("Bubble Bobble", 256, 254));
			 app.setDisplayMode(256, 254, false);
			 app.setVSync(true);
			 app.start();
		 } catch (SlickException e) {
			 e.printStackTrace();
		 }
	}

}
