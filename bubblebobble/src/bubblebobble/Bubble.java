package bubblebobble;

import org.newdawn.slick.Animation;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bubble extends Entity
{
	private Vector velocity;
	private Animation bubble;
	private boolean removeBubble;
	
	public Bubble(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BubbleBobbleGame.BUBBLE));
		removeImage(ResourceManager.getImage(BubbleBobbleGame.BUBBLE));
		velocity = new Vector(0, 0);
	 }

	public void removeAnimation()
	{
		removeAnimation(bubble);
	}
	
	public void AnimateBlowingBubble()
	{
		bubble = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.BUBBLE_SPRITE, 40, 40), 
				0, 0, 5, 0, true, 40, true);
		addAnimation(bubble);
		bubble.setLooping(false);
	}
	
	public boolean getRemoveBubble()
	{
		return this.removeBubble;
	}
	
	public void setRemoveBubble(boolean removeBubble)
	{
		this.removeBubble = removeBubble;
	}
	
	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public void update(final int delta) {
//		System.out.println("updating");
		translate(velocity.scale(delta));
	}
}
