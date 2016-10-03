package bubblebobble;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bub extends Entity
{
	private Vector velocity;
	
	public Bub(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BubbleBobbleGame.BUB_STANDING));
		velocity = new Vector(vx, vy);
	 }

	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
}
