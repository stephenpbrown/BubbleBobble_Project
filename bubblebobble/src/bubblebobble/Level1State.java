package bubblebobble;

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

public class Level1State extends BasicGameState
{
	private int timer;
	private int lastKnownBounces; // the user's score, to be displayed, but not updated.
	private int lastKnownScore;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 1000;
		
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		//sResourceManager.getSound(bg.LEVEL_SONG_RSC).play();
	}

	public void setUserScore(int score) {
		lastKnownScore = score;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		g.drawImage(ResourceManager.getImage(BubbleBobbleGame.LEVEL_1_RSC), 185, 210);
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
			Iterator<Enemy> removeEnemy = bbg.enemy.iterator();
			
			while (removeEnemy.hasNext())
			{
				Enemy e = removeEnemy.next();
				removeEnemy.remove();
			}
			
			bbg.bub.setPosition(new Vector(bbg.ScreenWidth/2, 2*bbg.ScreenHeight/3+60));
			game.enterState(BubbleBobbleGame.PLAYINGSTATE, new FadeOutTransition(), new FadeInTransition());
		}

	}

	@Override
	public int getID() {
		return BubbleBobbleGame.LEVEL1STATE;
	}
}
