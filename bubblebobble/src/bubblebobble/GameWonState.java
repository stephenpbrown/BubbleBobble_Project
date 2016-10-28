package bubblebobble;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import jig.ResourceManager;
import jig.Vector;

public class GameWonState extends BasicGameState
{
	private int timer;
	private int lastKnownScore;
	private int lastKnownHighScore;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 4000;
	}

	public void setUserScore(int score) {
		lastKnownScore = score;
	}
	
	public int getUserScore()
	{
		return lastKnownScore;
	}
	
	public void setUserHighScore(int highScore)
	{
		lastKnownHighScore = highScore;
	}
	
	public int getUserHighScore()
	{
		return lastKnownHighScore;
	}
	
	public void saveHighScore(int highScore) throws IOException
	{
		//System.out.println("Saving highScore: " + highScore);
		Writer wr = new FileWriter("highScore.txt");
		wr.write(String.valueOf(highScore));
		wr.close();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
//		
//		g.drawString("Score: " + lastKnownScore, 10, 30);
//		g.drawString("Highscore: " + ((StartUpState)game.getState(BounceGame.STARTUPSTATE)).getUserHighScore(), 620, 10);
//		//g.drawString("Lives Remaining: 0", 10, 50);
//		
//		for (Bang b : bg.explosions)
//			b.render(g);
		
		g.drawImage(ResourceManager.getImage(BubbleBobbleGame.GAMEWON_RSC), 185,
				210);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		timer -= delta;
		if (timer <= 0)
		{
			// Remove remaining bricks
			for (Iterator<Blocks> b = bbg.block.iterator(); b.hasNext();)
			{
				if(b.next() != null)
					b.remove();
			}
			
			// Remove enemies that have died
			for (Iterator<Enemy> removeEnemy = bbg.enemy.iterator(); removeEnemy.hasNext();)
			{
				Enemy e = removeEnemy.next();
				removeEnemy.remove();
			}
			
			// Remove enemies that have died
			for (Iterator<Bubble> removeBubbles = bbg.bubble.iterator(); removeBubbles.hasNext();)
			{
				Bubble b = removeBubbles.next();
				removeBubbles.remove();
			}
			
//			bbg.bub.AnimateRight();
			bbg.bub.setPosition(new Vector(bbg.ScreenWidth/2, 2*bbg.ScreenHeight/3+60));
			game.enterState(BubbleBobbleGame.SPLASHSCREENSTATE, new FadeOutTransition(), new FadeInTransition() );
		}

	}

	@Override
	public int getID() {
		return BubbleBobbleGame.GAMEWONSTATE;
	}
}
