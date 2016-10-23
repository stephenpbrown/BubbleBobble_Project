package bubblebobble;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;

import bubblebobble.BubbleBobbleGame;
import jig.ResourceManager;

public class GoState extends BasicGameState
{
	private int timer;
	private int lastKnownScore; // the user's score, to be displayed, but not updated.
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 500;
	}

	public void setUserScore(int score) {
		lastKnownScore = score;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
//		g.drawString("Score: " + lastKnownScore, 10, 30);
//		g.drawString("Highscore: " + ((GameWonState)game.getState(BounceGame.GAMEWONSTATE)).getUserHighScore(), 620, 10);
		
		g.drawImage(ResourceManager.getImage(BubbleBobbleGame.GO_RSC), 185, 210);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		
		timer -= delta;
		if (timer <= 0)
		{
			game.enterState(BubbleBobbleGame.PLAYINGSTATE, new EmptyTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return BubbleBobbleGame.GOSTATE;
	}
}
