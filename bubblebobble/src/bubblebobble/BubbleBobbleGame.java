package bubblebobble;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class BubbleBobbleGame extends BasicGame
{

	public BubbleBobbleGame(String title, int width, int height) {
		super(title);
	}
	
	@Override
	 public void render(GameContainer arg0, Graphics arg1) throws SlickException {
	 // TODO Auto-generated method stub
	 }
	 @Override
	 public void init(GameContainer arg0) throws SlickException {
	 // TODO Auto-generated method stub
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
