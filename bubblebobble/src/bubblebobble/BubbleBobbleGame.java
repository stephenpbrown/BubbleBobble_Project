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
	public static final int SPLASHSCREENSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int LEVEL2STATE = 3;
	public static final int GOSTATE = 4;
	public static final int GAMEOVERSTATE = 5;
	public static final int GAMEWONSTATE = 6;
	
	public static final String MAIN_THEME = "resource/Main_Theme.wav";
	public static final String JUMP = "resource/Sound_Effects/Jump.wav";
	public static final String BLOW_BUBBLE = "resource/Sound_Effects/Blow_Bubble.wav";
	public static final String ENEMY_DIES = "resource/Sound_Effects/Enemy_Dies.wav";
	
	// Sprites
	public static final String BUB_STANDING = "resource/Bub_Standing.png";
	public static final String BUB_STANDING_LEFT = "resource/Bub_Standing_Left.png";
	public static final String BUB_STANDING_RIGHT = "resource/Bub_Standing_Right.png";
	public static final String BUB_WALKING_LEFT = "resource/Bub_Walking_Left.png";
	public static final String BUB_WALKING_RIGHT = "resource/Bub_Walking_Right.png";
	public static final String BUB_BLOWING_BUBBLE_LEFT = "resource/Bub_Blowing_Bubble_Left2.png";
	public static final String BUB_BLOWING_BUBBLE_RIGHT = "resource/Bub_Blowing_Bubble_Right2.png";
	public static final String BUB_DYING = "resource/Bub_Dying2.png";
	public static final String ENEMY_IN_BUBBLE = "resource/Enemy_In_Bubble2.png";
	public static final String ANGRY_ENEMY_IN_BUBBLE = "resource/Angry_Enemy_In_Bubble2.png";
	public static final String ENEMY_DYING = "resource/Enemy_Dying2.png";
	public static final String BUBBLE_SPRITE = "resource/Bubble_Sprite2.png";
	public static final String BUBBLE = "resource/Bubble2.png";
	public static final String LEVEL_1_ENEMY_WALKING_LEFT = "resource/Level_1_Enemy_Walking_Left2.png";
	public static final String LEVEL_1_ENEMY_WALKING_RIGHT = "resource/Level_1_Enemy_Walking_Right2.png";
	public static final String LEVEL_1_ENEMY_ANGRY_LEFT = "resource/Angry_Enemy_Left.png";
	public static final String LEVEL_1_ENEMY_ANGRY_RIGHT = "resource/Angry_Enemy_Right.png";
	public static final String BUB_FALLING_LEFT = "resource/Bub_Falling_Left.png";
	public static final String BUB_FALLING_RIGHT = "resource/Bub_Falling_Right.png";
	public static final String BUB_JUMPING_LEFT = "resource/Bub_Jumping_Left.png";
	public static final String BUB_JUMPING_RIGHT = "resource/Bub_Jumping_Right.png";
	public static final String LEVEL_1_SINGLE_TILE = "resource/Level_1_Single_Tile.png";
	public static final String LEVEL_1_4x4_TILE = "resource/Level_1_4x4_Tile.png";
	public static final String LEVEL_2_SINGLE_TILE = "resource/Level_2_Single_Tile.png";
	public static final String LEVEL_2_4x4_TILE = "resource/Level_2_4x4_Tile.png";	
	public static final String LEVEL_1_RSC = "resource/Level_1.png";
	public static final String LEVEL_2_RSC = "resource/Level_2.png";
	public static final String GO_RSC = "resource/Go.png";
	public static final String SPLASH_SCREEN_RSC = "resource/SplashScreen2.png";
	public static final String GAMEWON_RSC = "resource/GameWon.png";
	public static final String GAMEOVER_BANNER_RSC = "resource/Gameover2.png";
	
	final int ScreenWidth;
	final int ScreenHeight;
	
	Bub bub;
	ArrayList<Blocks> block;
	ArrayList<Enemy> enemy;
	ArrayList<Bubble> bubble;

	public BubbleBobbleGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		
		block = new ArrayList<Blocks>(200);
		enemy = new ArrayList<Enemy>(6);
		bubble = new ArrayList<Bubble>();
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new SplashScreenState());
		addState(new PlayingState());
		addState(new Level1State());
		addState(new Level2State());
		addState(new GoState());
		addState(new GameWonState());
		addState(new GameOverState());
		
		ResourceManager.loadSound(MAIN_THEME);
		ResourceManager.loadSound(JUMP);
		ResourceManager.loadSound(BLOW_BUBBLE);
		ResourceManager.loadSound(ENEMY_DIES);

		
		ResourceManager.loadImage(BUB_STANDING);
		ResourceManager.loadImage(BUB_STANDING_RIGHT);
		ResourceManager.loadImage(BUB_STANDING_LEFT);
		ResourceManager.loadImage(BUB_WALKING_RIGHT);
		ResourceManager.loadImage(BUB_WALKING_LEFT);
		ResourceManager.loadImage(BUB_BLOWING_BUBBLE_RIGHT);
		ResourceManager.loadImage(BUB_BLOWING_BUBBLE_LEFT);
		ResourceManager.loadImage(BUB_DYING);
		ResourceManager.loadImage(BUBBLE_SPRITE);
		ResourceManager.loadImage(BUBBLE);
		ResourceManager.loadImage(ENEMY_IN_BUBBLE);
		ResourceManager.loadImage(ANGRY_ENEMY_IN_BUBBLE);
		ResourceManager.loadImage(ENEMY_DYING);
		ResourceManager.loadImage(LEVEL_1_ENEMY_WALKING_RIGHT);
		ResourceManager.loadImage(LEVEL_1_ENEMY_WALKING_LEFT);
		ResourceManager.loadImage(LEVEL_1_ENEMY_ANGRY_RIGHT);
		ResourceManager.loadImage(LEVEL_1_ENEMY_ANGRY_LEFT);
		ResourceManager.loadImage(BUB_FALLING_RIGHT);
		ResourceManager.loadImage(BUB_FALLING_LEFT);
		ResourceManager.loadImage(BUB_JUMPING_RIGHT);
		ResourceManager.loadImage(BUB_JUMPING_LEFT);
		ResourceManager.loadImage(LEVEL_1_SINGLE_TILE);
		ResourceManager.loadImage(LEVEL_1_4x4_TILE);
		ResourceManager.loadImage(LEVEL_2_SINGLE_TILE);
		ResourceManager.loadImage(LEVEL_2_4x4_TILE);
		ResourceManager.loadImage(LEVEL_1_RSC);
		ResourceManager.loadImage(LEVEL_2_RSC);
		ResourceManager.loadImage(GO_RSC);
		ResourceManager.loadImage(SPLASH_SCREEN_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(GAMEWON_RSC);
		
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
