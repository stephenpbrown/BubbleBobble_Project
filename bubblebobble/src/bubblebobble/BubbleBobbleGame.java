package bubblebobble;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import jig.Entity;

public class BubbleBobbleGame extends BasicGame
{
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
	public void render(GameContainer container, Graphics g)
			 throws SlickException {
			 bub.render(g);
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		bub = new Bub(ScreenWidth / 2, ScreenHeight / 2, .1f, .2f);
	}

	 @Override
	 public void update(GameContainer arg0, int arg1) throws SlickException {
	 // TODO Auto-generated method stub
	 }

	 
	public static void main(String[] args) 
	{
		 AppGameContainer app;
		 try {
			 app = new AppGameContainer(new BubbleBobbleGame("Bubble Bobble", 800, 600));
			 app.setDisplayMode(800, 600, false);
			 app.start();
		 } catch (SlickException e) {
			 e.printStackTrace();
		 }
	}

}
