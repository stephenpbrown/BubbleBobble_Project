package bubblebobble;

import org.newdawn.slick.Animation;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bub extends Entity
{
	private Vector velocity;
	private Animation standing;
	private Vector currentLocation;
	private boolean bubDead;
	
	public Bub(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BubbleBobbleGame.BUB_STANDING));
		removeImage(ResourceManager.getImage(BubbleBobbleGame.BUB_STANDING));
		//Animate();
		velocity = new Vector(0, 0);
	 }

	public void removeAnimation()
	{
		removeAnimation(standing);
	}
	
	public void AnimateRight()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.BUB_STANDING_RIGHT, 45, 40), 
				0, 0, 1, 0, true, 300, true);
		addAnimation(standing);
	}
	
	public void AnimateLeft()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.BUB_STANDING_LEFT, 45, 40), 
				0, 0, 1, 0, true, 300, true);
		addAnimation(standing);
	}
	
	public void AnimateBlowingBubbleRight()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.BUB_STANDING_RIGHT, 45, 40), 
				0, 0, 1, 0, true, 300, true);
		addAnimation(standing);
	}
	
	public void AnimateBubDying()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.BUB_DYING, 42, 79), 
				0, 0, 10, 0, true, 170, true);
		addAnimation(standing);
		standing.setLooping(false);
	}
	
	public void setBubDead(boolean bubDead) {
		this.bubDead = bubDead;
	}
	
	public boolean getBubDead() {
		return this.bubDead;
	}
	
	public void setVelocity(final Vector v) {
		this.velocity = v;
	}
	
	public Vector getVelocity() {
		return this.velocity;
	}
	
	public void update(final int delta) {
//		System.out.println("updating");
		translate(velocity.scale(delta));
	}
	
	public void setCurrentLocation(Vector currentLocation)
	{
		this.currentLocation = currentLocation;
	}
	
	public Vector getCurrentLocation()
	{
		return this.currentLocation;
	}
}
