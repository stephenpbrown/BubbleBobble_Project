package bubblebobble;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import jig.ResourceManager;

public class SplashScreenState extends BasicGameState
{
	private int timer;
	private int lastKnownBounces; // the user's score, to be displayed, but not updated.
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		ResourceManager.getSound(BubbleBobbleGame.MAIN_THEME).loop();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 1500;
	}

	public void setUserScore(int bounces) {
		lastKnownBounces = bounces;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		g.drawImage(ResourceManager.getImage(BubbleBobbleGame.SPLASH_SCREEN_RSC), 260, 210);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		Input input = container.getInput();
		
		if(input.isKeyPressed(Input.KEY_SPACE))
			game.enterState(BubbleBobbleGame.LEVEL1STATE, new FadeOutTransition(), new FadeInTransition());

	}

	@Override
	public int getID() {
		return BubbleBobbleGame.SPLASHSCREENSTATE;
	}
}
