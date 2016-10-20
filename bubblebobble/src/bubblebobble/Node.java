package bubblebobble;

import jig.Vector;

public class Node 
{
	private float x;
	private float y;
	private Node walkLeft;
	private Node walkRight;
	private Node jumpLeft;
	private Node jumpRight;
	private Node jumpUp;
	private Node parent;
	private Node fallLeft;
	private Node fallRight;
	private Blocks refBlock;
	
	public Node(Blocks refBlock, float x, float y, Node walkLeft, Node walkRight, Node jumpLeft, Node jumpRight, Node jumpUp, Node fallLeft, Node fallRight)
	{
		this.refBlock = refBlock;
		this.x = x;
		this.y = y;
		this.walkLeft = walkLeft;
		this.walkRight = walkRight;
		this.jumpLeft = jumpLeft;
		this.jumpRight = jumpRight;
		this.jumpUp = jumpUp;
		this.fallLeft = fallLeft;
		this.fallRight = fallRight;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public Node getWalkLeft()
	{
		return this.walkLeft;
	}
	
	public void setWalkLeft(Node node)
	{
		this.walkLeft = node;
	}
	
	public Node getWalkRight()
	{
		return this.walkRight;
	}
	
	public void setWalkRight(Node node)
	{
		this.walkRight = node;
	}
	
	public Node getJumpLeft()
	{
		return this.jumpLeft;
	}
	
	public void setJumpLeft(Node node)
	{
		this.jumpLeft = node;
	}
	
	public Node getJumpRight()
	{
		return this.jumpRight;
	}
	
	public void setJumpRight(Node node)
	{
		this.jumpRight = node;
	}
	
	public Node getJumpUp()
	{
		return this.jumpUp;
	}
	
	public void setJumpUp(Node node)
	{
		this.jumpUp = node;
	}
	
	public Node getFallLeft()
	{
		return this.fallLeft;
	}
	
	public void setFallLeft(Node node)
	{
		this.fallLeft = node;
	}
	
	public Node getFallRight()
	{
		return this.fallRight;
	}
	
	public void setFallRight(Node node)
	{
		this.fallRight = node;
	}
	
	public void setParent(Node n)
	{
		this.parent = n;
	}
	
	public Node getParent()
	{
		return this.parent;
	}
	
	public void setRefBlock(Blocks b)
	{
		this.refBlock = b;
	}
	
	public Blocks getRefBlock()
	{
		return this.refBlock;
	}
}
