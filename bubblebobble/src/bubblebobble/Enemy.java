package bubblebobble;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Enemy extends Entity {

	private Vector velocity;
	private Animation standing;
	private Vector currentLocation;
	private boolean jumping;
	private boolean falling;
	private boolean onPlatform;
	private boolean inBubble;
	private boolean enemyDying;
	private boolean removeEnemy;
	private float xVelocity;
	private float yVelocity;
	private float bottomToMiddle;
	private List<String> instructions;
	private List<Node> path;
	private List<Blocks> collidingWithBlocks;
	private Blocks lastBlock;
	private int ID;
	private int timeInBubble;
	
	public Enemy(final float x, final float y, int ID) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BubbleBobbleGame.BUB_STANDING));
		removeImage(ResourceManager.getImage(BubbleBobbleGame.BUB_STANDING));
		AnimateLeft();
//		AnimateRight();
		velocity = new Vector(0, 0);
		jumping = false;
		falling = false;
		instructions = new ArrayList<String>();
		path = new ArrayList<Node>();
		collidingWithBlocks = new ArrayList<Blocks>();
		this.ID = ID;
	 }

	public void removeAnimation()
	{
		removeAnimation(standing);
	}
	
	public void AnimateRight()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.LEVEL_1_ENEMY_WALKING_RIGHT, 45, 40), 
				0, 0, 3, 0, true, 300, true);
		addAnimation(standing);
	}
	
	public void AnimateLeft()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.LEVEL_1_ENEMY_WALKING_LEFT, 45, 40), 
				0, 0, 3, 0, true, 300, true);
		addAnimation(standing);
	}
	
	public void AnimateInBubble()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.ENEMY_IN_BUBBLE, 40, 40), 
				0, 0, 2, 0, true, 300, true);
		addAnimation(standing);
		inBubble = true;
	}
	
	public void AnimateEnemyDying()
	{
		standing = new Animation(ResourceManager.getSpriteSheet(BubbleBobbleGame.ENEMY_DYING, 30, 35), 
				0, 0, 3, 0, true, 200, true);
		addAnimation(standing);
		enemyDying = true;
	}
	
	public boolean getEnemyDying()
	{
		return this.enemyDying;
	}
	
	public void setEnemyDying(boolean enemyDying)
	{
		this.enemyDying = enemyDying;
	}
	
	public boolean getRemoveEnemy()
	{
		return this.removeEnemy;
	}
	
	public void setRemoveEnemy(boolean removeEnemy)
	{
		this.removeEnemy = removeEnemy;
	}
	
	public void setInBubble(boolean inBubble)
	{
		this.inBubble = inBubble;
	}
	
	public boolean getInBubble()
	{
		return this.inBubble;
	}
	
	public void setTimeInBubble(int timeInBubble) {
		this.timeInBubble = timeInBubble;
	}
	
	public int getTimeInBubble() {
		return this.timeInBubble;
	}
	
	public void decTimeInBubble() {
		this.timeInBubble--;
	}
	
	public void setVelocity(final Vector v) {
		this.velocity = v;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public void setCurrentLocation(Vector currentLocation)
	{
		this.currentLocation = currentLocation;
	}
	
	public Vector getCurrentLocation()
	{
		return this.currentLocation;
	}
	
	public void setJumping(boolean jumping)
	{
		this.jumping = jumping;
	}
	
	public boolean getJumping()
	{
		return this.jumping;
	}
	
	public void setFalling(boolean falling)
	{
		this.falling = falling;
	}
	
	public boolean getFalling()
	{
		return this.falling;
	}
	
	public void setOnPlatform(boolean onPlatform)
	{
		this.onPlatform = onPlatform;
	}
	
	public boolean getOnPlatform()
	{
		return this.onPlatform;
	}
	
	public void setInstructions(List<String> instructions)
	{
		this.instructions = instructions;
	}
	
	public List<String> getInstructions()
	{
		return this.instructions;
	}
	
	public void setCollidingWithBlocks(List<Blocks> collidingWithBlocks)
	{
		this.collidingWithBlocks = collidingWithBlocks;
	}
	
	public List<Blocks> getCollidingWithBlocks()
	{
		return this.collidingWithBlocks;
	}
	
	public void setPath(List<Node> path)
	{
		this.path = path;
	}
	
	public List<Node> getPath()
	{
		return this.path;
	}
	
	public void setXVelocity(float xVelocity)
	{
		this.xVelocity = xVelocity;
	}
	
	public float getXVelocity()
	{
		return this.xVelocity;
	}
	
	public void setYVelocity(float yVelocity)
	{
		this.yVelocity = yVelocity;
	}
	
	public float getYVelocity()
	{
		return this.yVelocity;
	}
	
	public void setBottomToMiddle(float bottomToMiddle)
	{
		this.bottomToMiddle = bottomToMiddle;
	}
	
	public float getBottomToMiddle()
	{
		return this.bottomToMiddle;
	}
	
	public void setLastBlock(Blocks lastBlock)
	{
		this.lastBlock = lastBlock;
	}
	
	public Blocks getLastBlock()
	{
		return this.lastBlock;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public void update(final int delta) {
//		System.out.println("updating");
		translate(velocity.scale(delta));
	}
}
