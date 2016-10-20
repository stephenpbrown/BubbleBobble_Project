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
	public static final String BUB_STANDING_LEFT = "resource/Bub_Standing_Left.png";
	public static final String BUB_STANDING_RIGHT = "resource/Bub_Standing_Right.png";
	public static final String BUB_WALKING_LEFT = "resource/Bub_Walking_Left.png";
	public static final String BUB_WALKING_RIGHT = "resource/Bub_Walking_Right.png";
	public static final String LEVEL_1_ENEMY_WALKING_LEFT = "resource/Level_1_Enemy_Walking_Left2.png";
	public static final String LEVEL_1_ENEMY_WALKING_RIGHT = "resource/Level_1_Enemy_Walking_Right2.png";
	public static final String BUB_FALLING_LEFT = "resource/Bub_Falling_Left.png";
	public static final String BUB_FALLING_RIGHT = "resource/Bub_Falling_Right.png";
	public static final String BUB_JUMPING_LEFT = "resource/Bub_Jumping_Left.png";
	public static final String BUB_JUMPING_RIGHT = "resource/Bub_Jumping_Right.png";
	public static final String LEVEL_1_SINGLE_TILE = "resource/Level_1_Single_Tile.png";
	public static final String LEVEL_1_4x4_TILE = "resource/Level_1_4x4_Tile.png";
		
	final int ScreenWidth;
	final int ScreenHeight;
	
	Bub bub;
	ArrayList<Blocks> block;
	ArrayList<Enemy> enemy;

	public BubbleBobbleGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		
		block = new ArrayList<Blocks>(200);
		enemy = new ArrayList<Enemy>(6);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayingState());
		
		ResourceManager.loadImage(BUB_STANDING);
		ResourceManager.loadImage(BUB_STANDING_RIGHT);
		ResourceManager.loadImage(BUB_STANDING_LEFT);
		ResourceManager.loadImage(BUB_WALKING_RIGHT);
		ResourceManager.loadImage(BUB_WALKING_LEFT);
		ResourceManager.loadImage(LEVEL_1_ENEMY_WALKING_RIGHT);
		ResourceManager.loadImage(LEVEL_1_ENEMY_WALKING_LEFT);
		ResourceManager.loadImage(BUB_FALLING_RIGHT);
		ResourceManager.loadImage(BUB_FALLING_LEFT);
		ResourceManager.loadImage(BUB_JUMPING_RIGHT);
		ResourceManager.loadImage(BUB_JUMPING_LEFT);
		ResourceManager.loadImage(LEVEL_1_SINGLE_TILE);
		ResourceManager.loadImage(LEVEL_1_4x4_TILE);
		
		bub = new Bub(ScreenWidth / 2, 2*ScreenHeight/3+60);
		
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
