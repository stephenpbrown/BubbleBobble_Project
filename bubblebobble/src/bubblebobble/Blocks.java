package bubblebobble;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Blocks extends Entity
{

	private Vector velocity;
	private int level = 1;
	
	public Blocks(final float x, final float y, final int l, final int size) {
		super(x, y);
		level = l;
		
		if (level == 1 && size == 1)
			addImageWithBoundingBox(ResourceManager.getImage(BubbleBobbleGame.LEVEL_1_SINGLE_TILE));
		if (level == 1 && size == 4)
			addImageWithBoundingBox(ResourceManager.getImage(BubbleBobbleGame.LEVEL_1_4x4_TILE));
		
		velocity = new Vector(0, 0);
	 }

	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
}
