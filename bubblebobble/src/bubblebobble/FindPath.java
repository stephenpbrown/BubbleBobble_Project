package bubblebobble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;

import jig.Vector;

public class FindPath 
{
	private List<Node> node;
	private List<String> instructions;
	private List<Node> nodePath;
	private Node refNode;
	private float enemyX;
	private float enemyY;
	private float playerX;
	private float playerY;
	private boolean foundPlayer = false;
	private boolean checkLeft = true;
	private int playerNode;
	private int enemyNode;
	private int nodesChecked = 0;
	
	public FindPath(List<Node> node, float enemyX, float enemyY, float playerX, float playerY)
	{
		this.node = node;
		this.enemyX = enemyX;
		this.enemyY = enemyY;
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	public void CreatePath(Node n)
	{
//		System.out.println("Create Path");
		Node tempNode = n;
		
//		System.out.println(n);
		
		while(tempNode != refNode)
		{
			Node parent = tempNode.getParent();
			
			if(parent.getWalkLeft() == tempNode)
			{
				instructions.add("WL");
				nodePath.add(tempNode);
			}
				
			if(parent.getWalkRight() == tempNode)
			{
				instructions.add("WR");
				nodePath.add(tempNode);
			}
			if(parent.getJumpLeft() == tempNode)
			{
				instructions.add("JL");
				nodePath.add(tempNode);
			}
			if(parent.getJumpRight() == tempNode)
			{
				instructions.add("JR");
				nodePath.add(tempNode);
			}
			if(parent.getJumpUp() == tempNode)
			{
				instructions.add("JU");
				nodePath.add(tempNode);
			}
			if(parent.getFallLeft() == tempNode)
			{
//				instructions.add("WL");
				instructions.add("FL");
				nodePath.add(tempNode);
			}
			if(parent.getFallRight() == tempNode)
			{
//				instructions.add("WR");
				instructions.add("FR");
				nodePath.add(tempNode);
			}
			tempNode = tempNode.getParent();
		}
	}
	
	public void Search(Node n, Node player)
	{
		
//		System.out.println(n.getX() + ", " + n.getY());
//		if(n.getX() == x && n.getY() == y)
		nodesChecked++;
		if(n.equals(player) && foundPlayer == false)
		{
			foundPlayer = true;
			System.out.println("Found player"); // DEBUG
			CreatePath(n);
			return;
		}
		
		if(nodesChecked <= 81)
		{
		
//			System.out.println(n + ", " + n.getWalkLeft() + ", " + n.getWalkRight()
//			+ ", " + n.getJumpLeft() + ", " + n.getJumpRight() + ", " + n.getJumpUp()
//			+ ", " + n.getFallLeft() + ", " + n.getFallRight());
//			if(enemyX <= playerX)
//			{
			if(n.getWalkRight() != null && n.getWalkRight().getParent() == null && enemyX <= playerX)
			{
	//			System.out.println("Searching WR");
				n.getWalkRight().setParent(n);
				
				Search(n.getWalkRight(), player);
			}
			if(n.getJumpRight() != null && n.getJumpRight().getParent() == null && playerX >= enemyX && playerY == enemyY) //n.getX())
			{
	//			System.out.println("Searching JR");
				n.getJumpRight().setParent(n);
			
				Search(n.getJumpRight(), player);
			}
			if(n.getFallRight() != null && n.getFallRight().getParent() == null && enemyX <= playerX && enemyY <= playerY)
			{
				n.getFallRight().setParent(n);
				
				Search(n.getFallRight(), player);
			}
//			}
//			else
//			{
			if(n.getFallLeft() != null && n.getFallLeft().getParent() == null && enemyX > playerX && playerY >= enemyY)
			{
				n.getFallLeft().setParent(n);
				
				Search(n.getFallLeft(), player);
			}
			if(n.getWalkLeft() != null && n.getWalkLeft().getParent() == null && enemyX > playerX)
			{
				n.getWalkLeft().setParent(n);
				
				Search(n.getWalkLeft(), player);
			}
			if(n.getJumpLeft() != null && n.getJumpLeft().getParent() == null && playerX < enemyX && playerY == enemyY) //n.getX())
			{
	//			System.out.println("Searching JL");
				n.getJumpLeft().setParent(n);
				
				Search(n.getJumpLeft(), player);
			}
//			}
			if(n.getJumpUp() != null && n.getJumpUp().getParent() == null && playerY <= enemyY)
			{
	//			System.out.println("Searching JU");
				n.getJumpUp().setParent(n);
	
				Search(n.getJumpUp(), player);
			}
		}
	}
	
	public List<String> FindThePath()
	{
		instructions = new ArrayList<String>();
		nodePath = new ArrayList<Node>();
		
		for(int i = 0; i < node.size(); i++)
		{
			if(node.get(i).getX() == playerX && node.get(i).getY() == playerY)
			{
//				System.out.println("Player is standing on node " + i);
				playerNode = i;
			}
			if(node.get(i).getX() == enemyX && node.get(i).getY() == enemyY)
			{
//				System.out.println("Enemy is standing on node " + i);
				enemyNode = i;
			}
		}
		
		int node1 = 32;
		int node2 = 27;
		
		for(Node n : node)
			n.setParent(null);
		
		Node sn = node.get(enemyNode); // starting node
		Node en = node.get(playerNode); // Ending node
//		System.out.println(n.getWalkLeft() + ", " + n.getWalkRight() + ", " + n.getJumpLeft() + ", " + n.getJumpRight() + ", " + n.getJumpUp());

//		sn.setParent(null);
		
		refNode = sn;
		
		System.out.println("**CHECKING LIST**");
		
		Search(sn, en);
		
		nodePath.add(sn);
		
//		if(instructions.isEmpty())
//		{
//			checkLeft = false;
//			Search(sn, en);
//		}
		
//		System.out.println(open.get(65).getWalkLeft());
		System.out.println("playerNode = " + playerNode + ", enemyNode = " + enemyNode);
		System.out.println("Nodes checked: " + nodesChecked/2);

//		System.out.println(instructions);
		
		if (!instructions.isEmpty())
		{
//			for (String s : instructions)
//			{
//				System.out.println(s);
//			}
		}
		else
			System.out.println("Player not found");
		
		return instructions;
	}
	
	public List<Node> getNodePath()
	{
		return nodePath;
	}
}
