package bubblebobble;

import java.util.ArrayList;

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
	public static final String LEVEL_1_SINGLE_TILE = "resource/Level_1_Single_Tile.png";
	public static final String LEVEL_1_4x4_TILE = "resource/Level_1_4x4_Tile.png";
		
	final int ScreenWidth;
	final int ScreenHeight;
	
	Bub bub;
	ArrayList<Bricks> brick;

	public BubbleBobbleGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		
		brick = new ArrayList<Bricks>(200);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayingState());
		
		ResourceManager.loadImage(BUB_STANDING);
		ResourceManager.loadImage(LEVEL_1_SINGLE_TILE);
		ResourceManager.loadImage(LEVEL_1_4x4_TILE);
		
		bub = new Bub(ScreenWidth / 4, ScreenHeight / 4);
	}

	 
	public static void main(String[] args) 
	{
		 AppGameContainer app;
		 try {
			 app = new AppGameContainer(new BubbleBobbleGame("Bubble Bobble", 768, 672));
			 app.setDisplayMode(768, 672, false);
			 app.setVSync(true);
			 app.start();
		 } catch (SlickException e) {
			 e.printStackTrace();
		 }
	}

}
