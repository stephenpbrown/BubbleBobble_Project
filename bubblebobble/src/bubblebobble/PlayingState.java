package bubblebobble;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import jig.ResourceManager;
import jig.Vector;

public class PlayingState extends BasicGameState {

	boolean onPlatform = false;
	boolean enemyOnPlatform = false;
	float gravity = 0.15f;
	boolean jumping = false;
	boolean enemyJumping = false;
	float jumpDistance = 0;
	int time = 0;
	float overlap = 0;
	private Animation walking;
	private Animation standing;
	private Animation jump;
//	private List<Animation> blowBubble;
	private Animation blowBubble;
	private Animation savedAnimation;
	private FindPath createPath;
	private List<FindPath> createPathList;
	private boolean walk = false;
	private boolean stand = true;
	private boolean facingLeft = false;
	private boolean walkingLeft = false;
	private boolean walkingRight = false;
	private boolean standStill = false;
	private boolean idle = false;
	private boolean fastFall = false;
	private boolean showEdges = false;
	private boolean pause = false;
	private boolean blowingBubble = false;
	private boolean bubDied = false;
	private boolean stopTimer = false;
	private int bubbleTimeCount = 60;
	private List<Blocks> collidingWithBlocks;
	private List<Blocks> collidingWithBlocksP;
	private Blocks lastBlock;
	public Vector playerLocation;
	private float velocityY = 0;
	private float velocityX = 0;
	private float positionX = 0;
	private float positionY = 0;
	private float bottomToMiddle = 0;
	private int livesRemaining;
	private int playerHitTimeout = 0;
	private int levelTimer = 1800; // Timer goes for 30 seconds
	private int timerDisplay = 0; 
	private int score = 0;
	private int level = 1;
	private int deadTimer = 0;
	

	private List<Node> node;
	private List<String> instructions;
	private char[][] mapGrid; // E = empty tile, B = block tile, F = falling, J = jumping, W = walking, C = climbing
	
	Toolkit toolkit;
	Timer timer;

	public void ReminderBeep(int seconds) {
		toolkit = Toolkit.getDefaultToolkit();
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
	}

	class RemindTask extends TimerTask {
		public void run() {
			System.out.println("Time's up!");
			toolkit.beep();
			//timer.cancel(); //Not necessary because we call System.exit
			System.exit(0); //Stops the AWT thread (and everything else)
		}
	}
	  
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;

		node = new ArrayList<Node>();
		instructions = new ArrayList<String>();
		collidingWithBlocks = new ArrayList<Blocks>();
		collidingWithBlocksP = new ArrayList<Blocks>();
//		blowBubble = new ArrayList<Animation>();
		livesRemaining = 3;
		mapGrid = new char[24][28]; // 0-23 width, 0-27 height
		
//		for(int i = 0; i < 2; i++)
//			collidingWithBlocks.add(i, null);
		
		for(int i = 0; i < 24; i++)
			for(int j = 0; j < 28; j++)
				mapGrid[i][j] = 'E';
		
		if(level == 1)
		{
			// Add enemies into the map
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4)-12, (2*bbg.ScreenHeight / 3)+20, 0));
//			bbg.enemy.get(0).removeAnimation();
//			bbg.enemy.get(0).AnimateRight();
//			bbg.enemy.get(0).setWalkingLeft(true);
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4)+12, (2*bbg.ScreenHeight / 3)+20, 1));
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4) - 36, (bbg.ScreenHeight - 80), 2));
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4) + 36, (bbg.ScreenHeight - 80), 3));
//			bbg.enemy.get(3).setWalkingLeft(true);
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4)-12, (bbg.ScreenHeight / 3)+100, 4));
//			bbg.enemy.get(4).setVelocity(new Vector(0, 0.15f));
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4)+12, (bbg.ScreenHeight / 3)+100, 5));
			
			for (Enemy e : bbg.enemy)
			{
				e.setOnPlatform(false);
				e.setVelocity(new Vector(0, 0.1f));
			}
			
			Level1blocks(bbg);
			
			for(int i = 0; i < node.size(); i++)
				node.get(i).setRefBlock(bbg.block.get(i));
			
			for(Node n : node)
			{
				for (Node n2 : node)
				{
					if(n != n2)
					{
						if((n.getWalkLeft() == null) && (n2.getX() == (n.getX() - 24)) && (n2.getY() == n.getY()))
							n.setWalkLeft(n2);
						
						else if((n.getWalkRight() == null) && (n2.getX() == (n.getX() + 24)) && (n2.getY() == n.getY()))
							n.setWalkRight(n2);
					}
				}
			}
			
			// Jump left and right on the platforms
			node.get(39).setJumpLeft(node.get(38));
			node.get(38).setJumpRight(node.get(39));
			node.get(69).setJumpLeft(node.get(68));
			node.get(68).setJumpRight(node.get(69));
			node.get(75).setJumpLeft(node.get(74));
			node.get(74).setJumpRight(node.get(75));
			
			// Fall left and right onto the platforms
			node.get(62).setFallLeft(node.get(1));
			node.get(44).setFallLeft(node.get(64));
			node.get(34).setFallLeft(node.get(46));
			node.get(28).setFallLeft(node.get(36));
			node.get(39).setFallLeft(node.get(53));
			node.get(69).setFallLeft(node.get(10));
			node.get(75).setFallLeft(node.get(18));
			
			node.get(33).setFallRight(node.get(41));
			node.get(43).setFallRight(node.get(59));
			node.get(61).setFallRight(node.get(79));
			node.get(81).setFallRight(node.get(26));
			node.get(38).setFallRight(node.get(52));
			node.get(68).setFallRight(node.get(9));
			node.get(74).setFallRight(node.get(17));
			
			// Jump up onto platforms
			node.get(5).setJumpUp(node.get(65));
			node.get(13).setJumpUp(node.get(71));
			node.get(22).setJumpUp(node.get(78));
			node.get(66).setJumpUp(node.get(45));
			node.get(72).setJumpUp(node.get(53));
			node.get(77).setJumpUp(node.get(60));
			node.get(48).setJumpUp(node.get(35));
			node.get(57).setJumpUp(node.get(42));
			node.get(37).setJumpUp(node.get(28));
			node.get(40).setJumpUp(node.get(33));
			
			bbg.bub.setCurrentLocation(new Vector(node.get(53).getX(), node.get(53).getY()));
			bbg.enemy.get(0).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(1).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(2).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(3).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			
			bbg.enemy.get(4).setCurrentLocation(new Vector(node.get(54).getX(), node.get(54).getY()));
//			bbg.enemy.get(4).setVelocity(new Vector(-0.1f, 0));
			bbg.enemy.get(5).setCurrentLocation(new Vector(node.get(54).getX(), node.get(54).getY()));
		}
		else if(level == 2)
		{
			// Add enemies into the map
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4) - 36, (bbg.ScreenHeight - 80), 0));
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4) + 36, (bbg.ScreenHeight - 80), 1));
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4)-12, (bbg.ScreenHeight / 3)+120, 2));
//			bbg.enemy.get(0).setWalkingLeft(true);
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4)+12, (bbg.ScreenHeight / 3)+120, 3));
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4)-12, (2*bbg.ScreenHeight / 3)+30, 4));
//			bbg.enemy.get(0).setWalkingLeft(true);
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4)+12, (2*bbg.ScreenHeight / 3)+30, 5));
//			bbg.enemy.get(3).setWalkingLeft(true);
			bbg.enemy.add(new Enemy((bbg.ScreenWidth / 4)-12, (bbg.ScreenHeight / 3)+10, 6));
			bbg.enemy.add(new Enemy((3*bbg.ScreenWidth / 4)+12, (bbg.ScreenHeight / 3)+10, 7));
						
			for (Enemy e : bbg.enemy)
			{
				e.setOnPlatform(false);
			}
			
			Level2blocks(bbg);
			
			for(int i = 0; i < node.size(); i++)
				node.get(i).setRefBlock(bbg.block.get(i));
			
			for(Node n : node)
			{
				for (Node n2 : node)
				{
					if(n != n2)
					{
						if((n.getWalkLeft() == null) && (n2.getX() == (n.getX() - 24)) && (n2.getY() == n.getY()))
							n.setWalkLeft(n2);
						
						else if((n.getWalkRight() == null) && (n2.getX() == (n.getX() + 24)) && (n2.getY() == n.getY()))
							n.setWalkRight(n2);
					}
				}
			}
			
			// Jump left and right on the platforms
			// Bottom platform
			node.get(74).setJumpLeft(node.get(73));
			node.get(73).setJumpRight(node.get(74));
			node.get(92).setJumpLeft(node.get(91));
			node.get(91).setJumpRight(node.get(92));
			// Middle platform
			node.get(52).setJumpLeft(node.get(51));
			node.get(51).setJumpRight(node.get(52));
			node.get(70).setJumpLeft(node.get(69));
			node.get(69).setJumpRight(node.get(70));
			// Top platform
			node.get(30).setJumpLeft(node.get(29));
			node.get(29).setJumpRight(node.get(30));
			node.get(48).setJumpLeft(node.get(47));
			node.get(47).setJumpRight(node.get(48));
			
			// Fall left onto the platforms
			node.get(74).setFallLeft(node.get(2));
			node.get(52).setFallLeft(node.get(73));
			node.get(30).setFallLeft(node.get(51));
			node.get(48).setFallLeft(node.get(69));
			node.get(70).setFallLeft(node.get(91));
			node.get(92).setFallLeft(node.get(23));
			
			// Fall right onto the platforms
			node.get(73).setFallRight(node.get(5));
			node.get(51).setFallRight(node.get(74));
			node.get(29).setFallRight(node.get(52));
			node.get(47).setFallRight(node.get(70));
			node.get(69).setFallRight(node.get(92));
			node.get(91).setFallRight(node.get(26));
			
			// Jump up onto platforms
			// Left side
			node.get(1).setJumpUp(node.get(73));
			node.get(72).setJumpUp(node.get(50));
			node.get(51).setJumpUp(node.get(29));
			// Right side
			node.get(26).setJumpUp(node.get(92));
			node.get(93).setJumpUp(node.get(71));
			node.get(70).setJumpUp(node.get(48));
			// Floor to bottom
			node.get(6).setJumpUp(node.get(75));
			node.get(14).setJumpUp(node.get(83));
			node.get(21).setJumpUp(node.get(90));
			// Bottom to middle
			node.get(79).setJumpUp(node.get(57));
			node.get(87).setJumpUp(node.get(65));
			// Middle to top
			node.get(53).setJumpUp(node.get(31));
			node.get(61).setJumpUp(node.get(39));
			node.get(68).setJumpUp(node.get(46));
			
			bbg.bub.setCurrentLocation(new Vector(node.get(15).getX(), node.get(15).getY()));
			bbg.enemy.get(0).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(1).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(2).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(3).setCurrentLocation(new Vector(node.get(59).getX(), node.get(59).getY()));
			bbg.enemy.get(4).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(5).setCurrentLocation(new Vector(node.get(59).getX(), node.get(59).getY()));
			bbg.enemy.get(6).setCurrentLocation(new Vector(node.get(46).getX(), node.get(46).getY()));
			bbg.enemy.get(7).setCurrentLocation(new Vector(node.get(59).getX(), node.get(59).getY()));
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for (Blocks b : bbg.block)
		{
			b.render(g);
		}
		
		for (Enemy e : bbg.enemy)
		{
			e.render(g);
		}
		
		for (Bubble b : bbg.bubble)
		{
			b.render(g);
		}
		
		bbg.bub.render(g);
		
//		if (bbg.enemy.isEmpty() && livesRemaining != -1)
//		{
//			g.drawString("You win!", bbg.ScreenWidth/2 - 48, bbg.ScreenHeight/2);
//		}
		
		g.drawString("0" + level, bbg.ScreenWidth/2-8, bbg.ScreenHeight/9);
		
		if(livesRemaining >= 0)
			g.drawString("" + livesRemaining, 30, bbg.ScreenHeight-22);
		else
			g.drawString("0", 30, bbg.ScreenHeight-22);
		
		if(levelTimer%60 == 0 && !stopTimer)
			timerDisplay = levelTimer/60;
		
		if(timerDisplay >= 0)
			g.drawString("Timer: " + timerDisplay, 5*bbg.ScreenWidth/6, bbg.ScreenHeight/10-12);
		else
			g.drawString("Timer: " + 0, 5*bbg.ScreenWidth/6, bbg.ScreenHeight/10-12);
		
		g.drawString("Score: " + score, 48, bbg.ScreenHeight/10-12);
		
		for(Enemy e : bbg.enemy)
		{
	//		if(findPath == true)
			if(!e.getJumping() && !jumping)
			{
				float playerX = bbg.bub.getCurrentLocation().getX();
				float playerY = bbg.bub.getCurrentLocation().getY();
				float enemyX = e.getCurrentLocation().getX();
				float enemyY = e.getCurrentLocation().getY();
				int playerNode = 0;
				int enemyNode = 0;
				
				
				for(int i = 0; i < node.size(); i++)
				{
					if(node.get(i).getX() == playerX && node.get(i).getY() == playerY)
					{
//						System.out.println("Player is standing on node " + i);
						playerNode = i;
					}
					if(node.get(i).getX() == enemyX && node.get(i).getY() == enemyY)
					{
//						System.out.println("Enemy is standing on node " + i);
						enemyNode = i;
					}
				}
				
				if(e.getID() == 1)
					playerNode += 6;
				else if(e.getID() == 3)
					playerNode += 15;
				else if(e.getID() == 4)
					playerNode -= 3;
				
				createPath = new FindPath(node, enemyX, enemyY, playerX, playerY);
	//			createPath = new FindPath(node, bbg.enemy.get(0).getCurrentLocation().getX(), bbg.enemy.get(0).getCurrentLocation().getY(), node.get(32).getX(), node.get(32).getY());
				instructions = createPath.FindThePath();
				
				e.setInstructions(instructions);
				e.setPath(createPath.getNodePath());
				
//				for (int i = instructions.size()-1; i >=0; i--)
//				{
//					System.out.println(instructions.get(i) + " on node " + createPath.getNodePath().get(i) + " which is block " + createPath.getNodePath().get(i).getRefBlock());
//				}
	//	    	Node n = nodeL.get(5).getWalkRight();
	//	    	System.out.println(n.getWalkLeft() + ", " + n.getWalkRight() + ", " + n.getJumpLeft() + ", " + n.getJumpRight() + ", " + n.getJumpUp());
			}
	//		findPath = false;
	        
	        if(showEdges)
	        {
	        	for(int i = 0; i < node.size(); i++)
	        	{
	        		g.setColor(new Color(255,246,0)); // Yellow
	        		if(node.get(i).getWalkLeft() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getWalkLeft().getX(), node.get(i).getWalkLeft().getY());
	        		if(node.get(i).getWalkRight() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getWalkRight().getX(), node.get(i).getWalkRight().getY());
	        		
	        		g.setColor(new Color(83,244,66)); // Green
	        		if(node.get(i).getJumpLeft() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getJumpLeft().getX(), node.get(i).getJumpLeft().getY());
	        		if(node.get(i).getJumpRight() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getJumpRight().getX(), node.get(i).getJumpRight().getY());
	        		
	        		g.setColor(new Color(229, 9, 9));
	        		if(node.get(i).getFallLeft() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getFallLeft().getX(), node.get(i).getFallLeft().getY());
	        		if(node.get(i).getFallRight() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getFallRight().getX(), node.get(i).getFallRight().getY());
	        		
	        		g.setColor(new Color(0,63,255));// Blue
	        		if(node.get(i).getJumpUp() != null)
	        			g.drawLine(node.get(i).getX(), node.get(i).getY(), node.get(i).getJumpUp().getX(), node.get(i).getJumpUp().getY());
	 
	        	}
	        	
	        		
	        	g.setColor(new Color(256,256,256));
	        	g.drawString("Yellow: Walk left/right", 64, bbg.ScreenHeight/6);
	        	g.drawString("Green: Jump left/right", 64, bbg.ScreenHeight/6+24);
	        	g.drawString("Red: Fall left/right", 64, bbg.ScreenHeight/6+48);
	        	g.drawString("Blue: Jump up", 64, bbg.ScreenHeight/6+72);
	        	//g.drawLine(bbg.enemy.get(0).getX(), bbg.enemy.get(0).getY(), bbg.bub.getX(), bbg.bub.getY());
	        	
	        	List<Node> path = e.getPath();
	        	
	        	if(path != null)
	        	{
	        		for(int i = 0; i < path.size()-1; i++)
	        		{
	        			g.drawLine(path.get(i).getX(), path.get(i).getY(), path.get(i+1).getX(), path.get(i+1).getY());
	        		}
	        	}
	        }
//        	if(bbg.bub.getCurrentLocation() != null)
//        		g.drawLine(bbg.bub.getCurrentLocation().getX(), bbg.bub.getCurrentLocation().getY(), 
//        			bbg.enemy.get(0).getCurrentLocation().getX(), bbg.enemy.get(0).getCurrentLocation().getY());
        }
        
		if(pause && livesRemaining != -1)
		{
			g.drawString("Game Paused", bbg.ScreenWidth/2 - 48, bbg.ScreenHeight/2);
		}
		
//		if(livesRemaining == -1)
//		{
//			g.drawString("Game Lost", bbg.ScreenWidth/2 - 48, bbg.ScreenHeight/2);
//		}
	}
	
	public void Level2blocks(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Add top blocks (left side of level indicator)
		for (int i = 1; i < 14; i++)
		{
			bbg.block.add(new Blocks(36+i*24, 84, 2, 1));
		}
		
		// Add top blocks (right side of level indicator)
		for (int i = 16; i < 30; i++)
		{
			bbg.block.add(new Blocks(36+i*24, 84, 2, 1));
		}	
		
		// Add floor blocks
		for (int i = 3; i < 33; i++)
		{
			Vector v = new Vector((i*24-12),(bbg.ScreenHeight-12));
			bbg.block.add(new Blocks(v.getX(), v.getY(), 2, 1));
			
			if(i < 31)
				node.add(new Node(null, v.getX(), v.getY(), null, null, null, null, null, null, null));
		}		
		
		// Add top platform
		for (int i = 1; i < 29; i++)
		{
			if (i != 3 && i != 4 && i != 5 &&
				i != 24 && i != 25 && i != 26)
			{
				Vector v = new Vector((2*24 + 24*i-12),(84+9*24));
				bbg.block.add(new Blocks(v.getX(),v.getY(), 2, 1));
				node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
			}
		}
		
		// Add middle platform
		for (int i = 1; i < 29; i++)
		{
			if (i != 3 && i != 4 && i != 5 &&
				i != 24 && i != 25 && i != 26)
			{
				Vector v = new Vector((2*24 + 24*i-12),(84+14*24));
				bbg.block.add(new Blocks(v.getX(),v.getY(), 2, 1));
				node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
			}
		}
		
		// Add bottom platform
		for (int i = 1; i < 29; i++)
		{
			if (i != 3 && i != 4 && i != 5 &&
				i != 24 && i != 25 && i != 26)
			{
				Vector v = new Vector((2*24 + 24*i-12),(84+19*24));
				bbg.block.add(new Blocks(v.getX(),v.getY(), 2, 1));
				node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
			}
		}
		
		// Lower left block
		bbg.block.add(new Blocks(12, bbg.ScreenHeight-12, 2, 1));
		
		// Left side blocks
		for (int i = 2; i < 14; i++)
		{
			bbg.block.add(new Blocks(24, 48*i, 2, 4));
		}	
		
		// Right side blocks
		for (int i = 2; i < 14; i++)
		{
			bbg.block.add(new Blocks(bbg.ScreenWidth-24, 48*i, 2, 4));
		}
	}
	
	public void Level1blocks(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Add top blocks (left side of level indicator)
		for (int i = 1; i < 14; i++)
		{
			bbg.block.add(new Blocks(36+i*24, 84, 1, 1));
		}
		
		// Add top blocks (right side of level indicator)
		for (int i = 16; i < 30; i++)
		{
			bbg.block.add(new Blocks(36+i*24, 84, 1, 1));
		}	
		
		// Add floor blocks
		for (int i = 3; i < 33; i++)
		{
			bbg.block.add(new Blocks(i*24-12, bbg.ScreenHeight-12, 1, 1));
			Vector v = new Vector((i*24-12),(bbg.ScreenHeight-12));
			
			if(i < 31)
				node.add(new Node(null, v.getX(), v.getY(), null, null, null, null, null, null, null));
		}	
		
		// Add top platform
		for (int i = 1; i < 7; i++)
		{
			bbg.block.add(new Blocks(24*12 + 24*i+12, 84+4*24, 1, 1));
			Vector v = new Vector((12*24 + 24*i+12),(84+4*24));
			node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
		}	
		
		// Add second to top platform
		for (int i = 1; i < 13; i++)
		{
			if (i != 6 && i != 7)
			{
				bbg.block.add(new Blocks(9*24 + 24*i+12, 84+9*24, 1, 1));
				Vector v = new Vector((9*24 + 24*i+12),(84+9*24));
				node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
			}
		}
		
		// Add third to top platform
		for (int i = 1; i < 19; i++)
		{
			bbg.block.add(new Blocks(6*24 + 24*i+12, 84+14*24, 1, 1));
			Vector v = new Vector((6*24 + 24*i+12),(84+14*24));
			node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
		}
		
		// Add bottom platform
		for (int i = 1; i < 25; i++)
		{
			if (i != 8 && i != 9 && i != 16 && i != 17)
			{
				bbg.block.add(new Blocks(3*24 + 24*i+12, 84+19*24, 1, 1));
				Vector v = new Vector((3*24 + 24*i+12),(84+19*24));
				node.add(new Node(null, v.getX(),v.getY(), null, null, null, null, null, null, null));
			}
		}
		
		// Lower left block
		bbg.block.add(new Blocks(12, bbg.ScreenHeight-12, 1, 1));
		
		// Left side blocks
		for (int i = 2; i < 14; i++)
		{
			bbg.block.add(new Blocks(24, 48*i, 1, 4));
		}	
		
		// Right side blocks
		for (int i = 2; i < 14; i++)
		{
			bbg.block.add(new Blocks(bbg.ScreenWidth-24, 48*i, 1, 4));
		}
	}
	
	public void StartJump()
	{
		velocityY = -0.45f;
		onPlatform = false;
		jumping = true;
	}
	
	public void EndJump()
	{
		if(velocityY < -0.125)
			velocityY = -0.125f;
	}
	
	public void UpdateJump(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		velocityY += 0.013;
		
		if(velocityY > 0)
			jumping = false;
	}
	
	public void WalkLeft(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		bbg.bub.removeAnimation(walking);
		bbg.bub.removeAnimation(blowBubble);
		
		//float xVelocity = bbg.bub.getVelocity().getX();
		
//		System.out.println(bbg.bub.getNumAnimations());
		
//		if(!blowingBubble)
//		{
//		if(onPlatform){
			if(bbg.bub.getVelocity().getX() > 0){
//				savedAnimation = walking;
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_LEFT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
			else if(bbg.bub.getVelocity().getX() == 0){
//				savedAnimation = walking;
				bbg.bub.removeAnimation();
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_LEFT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
//		}
		
		facingLeft = true;
//		bbg.bub.setVelocity(new Vector(-0.2f, velocityY));
		velocityX = -0.2f;
		walkingLeft = true;
		walkingRight = false;
		stand = true;
		//standStill = false;
	}
	
	public void WalkRight(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		float yVelocity = bbg.bub.getVelocity().getY();
		
		bbg.bub.removeAnimation(walking);
		bbg.bub.removeAnimation(blowBubble);
		
//		if(!blowingBubble)
//		{
//		if(onPlatform){
			if(bbg.bub.getVelocity().getX() < 0){
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_RIGHT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
			else if(bbg.bub.getVelocity().getX() == 0){
				bbg.bub.removeAnimation();
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_RIGHT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
//		}
		
		facingLeft = false;
		
		//bbg.bub.setVelocity(new Vector(0.2f, velocityY));
		velocityX = 0.2f;
		walkingLeft = false;
		walkingRight = true;
		stand = true;
		//standStill = false;
	}
	
	public void StandStill(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
//		float yVelocity = bbg.bub.getVelocity().getY();
		
		//System.out.println(yVelocity);
		
		if (stand == true)
		{
			bbg.bub.removeAnimation(walking);
			
			bbg.bub.removeImage(ResourceManager.getImage(bbg.BUB_STANDING));
			
//			if(!blowingBubble)
//			{
				bbg.bub.removeAnimation();
				bbg.bub.removeAnimation(blowBubble);
				
				if(facingLeft == true)
				{
					bbg.bub.AnimateLeft();
				}
				else
				{
					bbg.bub.AnimateRight();
				}
//			}
				
			stand = false;
			//bbg.bub.addImageWithBoundingBox(ResourceManager.getImage(bbg.BUB_STANDING));
		}
		walkingLeft = false;
		walkingRight = false;
		standStill = true;
		velocityX = 0;
		//bbg.bub.setVelocity(new Vector(0,velocityY));
	}
	
	public void Controls(StateBasedGame game, GameContainer container)
	{
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Move the character left
		if (input.isKeyDown(Input.KEY_LEFT) 
			&& input.isKeyPressed(Input.KEY_LEFT)
			)//&& !(input.isKeyDown(Input.KEY_RIGHT)))
		{			
			WalkLeft(bbg);
			
			if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
			{
				StartJump();
				ResourceManager.getSound(bbg.JUMP).play();;
				bbg.bub.removeAnimation(walking);
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_JUMPING_LEFT, 45, 40), 
						0, 0, 1, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
		}
		
		// Move the character right
		else if (input.isKeyDown(Input.KEY_RIGHT)
				&& input.isKeyPressed(Input.KEY_RIGHT)
				)//&& !(input.isKeyDown(Input.KEY_LEFT))) 
		{
			WalkRight(bbg);
			
			if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
			{
				ResourceManager.getSound(bbg.JUMP).play();
				StartJump();
			}
		}
		
		// Jumping while idle
		else if ((input.isKeyPressed(Input.KEY_LCONTROL) || input.isKeyPressed(Input.KEY_RCONTROL)) && onPlatform == true && jumping == false)
		{
			ResourceManager.getSound(bbg.JUMP).play();
			StartJump();
		}
		
		else if(input.isKeyDown(Input.KEY_DOWN))
		{
			fastFall = true;
		}
		
		// Not moving
		else if(!(input.isKeyDown(Input.KEY_LEFT)) 
				&& !(input.isKeyDown(Input.KEY_RIGHT)))
		{
			StandStill(bbg);
		}
		
		if(input.isKeyPressed(Input.KEY_E))
		{
			if(showEdges)
				showEdges = false;
			else
				showEdges = true;
		}
		if(input.isKeyPressed(Input.KEY_SPACE)) // Blow bubble
		{
//			for (Animation a : blowBubble)
//			{
			ResourceManager.getSound(bbg.BLOW_BUBBLE).play();
			bbg.bub.removeAnimation(blowBubble);
			
//			if(!blowingBubble)
//			{
				if(!facingLeft)
				{
					bbg.bub.removeAnimation();
					if(bbg.bub.getVelocity().getX() > 0){
						bbg.bub.removeAnimation(walking);
						blowBubble = (new Animation(ResourceManager.getSpriteSheet(bbg.BUB_BLOWING_BUBBLE_RIGHT, 45, 40), 
								0, 0, 4, 0, true, 120, true));
					}
					else if(bbg.bub.getVelocity().getX() == 0){
						bbg.bub.removeAnimation();
						blowBubble = (new Animation(ResourceManager.getSpriteSheet(bbg.BUB_BLOWING_BUBBLE_RIGHT, 45, 40), 
								0, 0, 4, 0, true, 120, true));
					}
					
					bbg.bub.addAnimation(blowBubble);
					blowBubble.setLooping(false);
//					}
					
					blowingBubble = true;
					bubbleTimeCount = 40;
					
					float bubX = bbg.bub.getX();
					float bubY = bbg.bub.getY();
					float bubXVelocity = bbg.bub.getVelocity().getX();
					
					bbg.bubble.add(new Bubble(bubX, bubY));
					
					int size = bbg.bubble.size();
					bbg.bubble.get(size-1).AnimateBlowingBubble();
					
					if(bubXVelocity > 0)
						bbg.bubble.get(size-1).setVelocity(new Vector(bubXVelocity + 0.08f, 0));
					else
						bbg.bubble.get(size-1).setVelocity(new Vector(0.18f, 0));
//					}
				}
				else
				{
					bbg.bub.removeAnimation();
					if(bbg.bub.getVelocity().getX() < 0){
						bbg.bub.removeAnimation(walking);
						blowBubble = (new Animation(ResourceManager.getSpriteSheet(bbg.BUB_BLOWING_BUBBLE_LEFT, 45, 40), 
								0, 0, 4, 0, true, 120, true));
					}
					else if(bbg.bub.getVelocity().getX() == 0){
						bbg.bub.removeAnimation();
						blowBubble = (new Animation(ResourceManager.getSpriteSheet(bbg.BUB_BLOWING_BUBBLE_LEFT, 45, 40), 
								0, 0, 4, 0, true, 120, true));
					}
					
					bbg.bub.addAnimation(blowBubble);
					blowBubble.setLooping(false);
//					}
					
					blowingBubble = true;
					bubbleTimeCount = 40;
					
					float bubX = bbg.bub.getX();
					float bubY = bbg.bub.getY();
					float bubXVelocity = bbg.bub.getVelocity().getX();
					
					bbg.bubble.add(new Bubble(bubX, bubY));
					
					int size = bbg.bubble.size();
					bbg.bubble.get(size-1).AnimateBlowingBubble();
					
					if(bubXVelocity < 0)
						bbg.bubble.get(size-1).setVelocity(new Vector(bubXVelocity + -0.08f, 0));
					else
						bbg.bubble.get(size-1).setVelocity(new Vector(-0.18f, 0));
				}
//			}
		}
	}
	
	public void PlayerCollisionDetection(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		for (Blocks b : bbg.block)
		{
			if (bbg.bub.collides(b) != null && bbg.bub.getCoarseGrainedMaxY() > 100)
			{				
				if (bbg.bub.getCoarseGrainedMaxY() < b.getCoarseGrainedMinY()+5
					&& jumping == false)
				{
					// Find player location
//					bbg.bub.setCurrentLocation(new Vector(b.getX(), b.getY()));
					collidingWithBlocksP.add(b);
//					findPath = true;
					onPlatform = true;
					fastFall = false;
//					bbg.bub.removeAnimation(jump);
					//velocityY = gravity;
					bottomToMiddle = b.getCoarseGrainedMinY() - (bbg.bub.getCoarseGrainedMaxY() - bbg.bub.getPosition().getY());
					bbg.bub.setPosition(bbg.bub.getX(), bottomToMiddle);
				}
				else if (bbg.bub.getCoarseGrainedMinX() < 48)
				{
					bbg.bub.setPosition(68, bbg.bub.getY());
				}
				else if (bbg.bub.getCoarseGrainedMaxX() > bbg.ScreenWidth - 48)
				{
					bbg.bub.setPosition(bbg.ScreenWidth - 68, bbg.bub.getY());
				}
			}
		}
		
		if(bbg.bub.getCoarseGrainedMinX() < 0)
		{
			bbg.bub.setPosition(20, bbg.bub.getY());
			if(bbg.bub.getCoarseGrainedMinY() < 10)
				EndJump();
		}
		else if(bbg.bub.getCoarseGrainedMaxX() > bbg.ScreenWidth)
		{
			bbg.bub.setPosition(bbg.ScreenWidth - 20, bbg.bub.getY());
			if(bbg.bub.getCoarseGrainedMinY() < 10)
				EndJump();
		}
		else if(bbg.bub.getCoarseGrainedMinY() < 12)
			EndJump();
	
		if(onPlatform && collidingWithBlocksP.size() > 2)
		{
			int size = collidingWithBlocksP.size();
			float block1X = collidingWithBlocksP.get(size-2).getX();
			float block1Y = collidingWithBlocksP.get(size-2).getY();
			float block2X = collidingWithBlocksP.get(size-1).getX();
			float block2Y = collidingWithBlocksP.get(size-1).getY();
			float distBlock1 = Math.abs(bbg.bub.getX() - block1X);
			float distBlock2 = Math.abs(bbg.bub.getX() - block2X);
			
//			System.out.println(distBlock1 + ", " + distBlock2);
			
			if(distBlock1 < distBlock2)
			{
//				e.setPosition(block1X, e.getY());
				bbg.bub.setCurrentLocation(new Vector(block1X, block1Y));
				
//					lastBlock = collidingWithBlocksP.get(size-2);
			}
			else
			{
//				e.setPosition(block2X, e.getY());
				bbg.bub.setCurrentLocation(new Vector(block2X, block2Y));
//					lastBlock = collidingWithBlocksP.get(size-1);
			}
		}
	}
	
	public void PlayerMovements(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		if(jumping)
			UpdateJump(bbg);

		bbg.bub.setVelocity(new Vector(velocityX, velocityY));
		
		velocityX = bbg.bub.getVelocity().getX();
		
		if(jumping == false)
		{
			if(fastFall && gravity < 0.265);
//				gravity += 0.005f;
			else if(!fastFall)
				gravity = 0.15f;
		
			bbg.bub.setVelocity(new Vector(velocityX, gravity));
			onPlatform = false;
		}
		else
		{
			bbg.bub.setVelocity(new Vector(velocityX, velocityY));
			//bbg.bub.setPosition(new Vector(positionX, positionY));
		}
	}
	
	public void StartEnemyJump(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for(Enemy e : bbg.enemy)
		{
			e.setYVelocity(-0.2f);
			e.setOnPlatform(false);
			e.setJumping(true);
		}
	}
	
	public void EndEnemyJump(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for(Enemy e : bbg.enemy)
		{
			if(e.getYVelocity() < -0.125)
				e.setYVelocity(-0.125f);
		}
	}
	
	public void UpdateEnemyJump(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for(Enemy e : bbg.enemy)
		{
//			enemyVelocityY += 0.02;
			e.setYVelocity(e.getYVelocity() + 0.02f);
			if(e.getYVelocity() > 0)
				e.setJumping(false);
		}
	}
	
	public void EnemyMovements(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
//		Blocks blockEnemyIsOn = null;
		
		for(Enemy e : bbg.enemy)
		{
		
			if(!e.getInBubble() && !e.getEnemyDying())
			{
				List<Node> path = e.getPath();
				instructions = e.getInstructions();
				
				if(e.getJumping())
					UpdateEnemyJump(bbg);
				
				e.setVelocity(new Vector(e.getXVelocity(), e.getYVelocity()));
				
				if(e.getOnPlatform()&& !e.getJumping() && !e.getFalling() && !e.getInstructions().isEmpty())
				{	
					
//					if(!instructions.isEmpty())
//					{
//						e.setPreviousInstructions(instructions);
//						e.setPreviousPath(path);
//					}
//					else
//					{
//						path = e.getPreviousPath();
//						instructions = e.getPreviousInstructions();
//					}
					
		//			e.setVelocity(new Vector(enemyVelocityX, enemyVelocityY));
					
					for(int i = instructions.size()-1; i >= 0; i--)
					{
						
		//					System.out.println(path.get(i+1) + ", " + e.getCurrentLocation());
						
						if(instructions.get(i) == "WL" 
								&& e.getCurrentLocation().getX() == path.get(i+1).getX() 
								&& e.getCurrentLocation().getY() == path.get(i+1).getY()
								)//&& enemyOnPlatform)
		//							&& !enemyJumpingSide)
						{
							
							if(e.getEnemyAngry())
							{
								if(!e.getWalkingLeft())
								{
									e.removeAnimation();
									e.AnimateAngryLeft();
								}
								e.setXVelocity(-0.2f);
							}
							else
							{
								if(!e.getWalkingLeft())
								{
									e.removeAnimation();
									e.AnimateLeft();
								}
								e.setXVelocity(-0.1f);
							}
							e.setWalkingLeft(true);
							e.setYVelocity(0);
	//						bottomToMiddle = lastBlock.getCoarseGrainedMinY() - (e.getCoarseGrainedMaxY() - e.getPosition().getY());
							e.setPosition(e.getX(), e.getBottomToMiddle()+1);
						}
						//ystem.out.println(e.getCurrentLocation().getY() + ", " + path.get(i+1).getY());
						else if(instructions.get(i) == "WR" 
								&& e.getCurrentLocation().getX() == path.get(i+1).getX() 
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{

							
							if(e.getEnemyAngry())
							{
								if(e.getWalkingLeft())
								{
									e.removeAnimation();
									e.AnimateAngryRight();
								}
								e.setXVelocity(0.2f);
							}
							else
							{
								if(e.getWalkingLeft())
								{
									e.removeAnimation();
									e.AnimateRight();
								}
								e.setXVelocity(0.1f);
							}
							e.setWalkingLeft(false);
							
							e.setYVelocity(0);
	//						bottomToMiddle() = lastBlock.getCoarseGrainedMinY() - (e.getCoarseGrainedMaxY() - e.getPosition().getY());
							e.setPosition(e.getX(), e.getBottomToMiddle()+1);
						}
						else if(instructions.get(i) == "JL"
								&& e.getCurrentLocation().getX() == path.get(i+1).getX() 
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{
							StartEnemyJump(bbg);
							
						}
						else if(instructions.get(i) == "JR"
								&& e.getCurrentLocation().getX() >= path.get(i+1).getX()
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{
							StartEnemyJump(bbg);
						}
						else if(instructions.get(i) == "JU" 
								&& e.getCurrentLocation().getX() == path.get(i+1).getX() 
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{
		//						e.setVelocity(new Vector(0, -0.15f));
							e.setXVelocity(0);
							e.setYVelocity(-0.15f);
	//						enemyVelocityX = 0;
	//						enemyVelocityY = -0.15f;
							
							e.setOnPlatform(false);
//							e.setJumping(true);
	//						enemyOnPlatform = false;
		//						enemyJumping = true;
						}
						else if(instructions.get(i) == "FL" 
								&& e.getCurrentLocation().getX() <= path.get(i+1).getX()-5
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{
							e.setPosition(e.getX()+7, e.getY());
							
							e.setOnPlatform(false);
							e.setFalling(true);
//							e.setYVelocity(0.15f);
						}
						else if(instructions.get(i) == "FR"
								&& e.getCurrentLocation().getX() >= path.get(i+1).getX()+5
								&& e.getCurrentLocation().getY() == path.get(i+1).getY())
						{
							e.setPosition(e.getX()-7, e.getY());
							
							e.setOnPlatform(false);
							e.setFalling(true);
//							e.setYVelocity(0.15f);
						}
					}
				}
				else if(instructions.isEmpty() && !e.getJumping())
				{
					e.setYVelocity(0.15f);
					e.setFalling(true);
				}
//				else if(instructions.isEmpty() && e.getOnPlatform())
//				{
					//			StartEnemyJump();
//					e.setYVelocity(0.15f);
					
//					if(e.getOnPlatform())
//					{
//						if(!e.getWalkingLeft())
//						{
//							if(e.getEnemyAngry())
//							{
//								if(e.getWalkingLeft())
//								{
//									e.removeAnimation();
//									e.AnimateAngryRight();
//								}
//								e.setXVelocity(0.2f);
//							}
//							else
//							{
//								if(e.getWalkingLeft())
//								{
//									e.removeAnimation();
//									e.AnimateRight();
//								}
//								e.setXVelocity(0.1f);
//							}
//							e.setWalkingLeft(false);
//							e.setYVelocity(0);
//							e.setPosition(e.getX(), e.getBottomToMiddle()+1);
//						}
//						else
//						{
//							if(e.getEnemyAngry())
//							{
//								if(!e.getWalkingLeft())
//								{
//									e.removeAnimation();
//									e.AnimateAngryLeft();
//								}
//								e.setXVelocity(-0.2f);
//							}
//							else
//							{
//								if(!e.getWalkingLeft())
//								{
//									e.removeAnimation();
//									e.AnimateLeft();
//								}
//								e.setXVelocity(-0.1f);
//							}
//							e.setWalkingLeft(true);
//							e.setYVelocity(0);
//							e.setPosition(e.getX(), e.getBottomToMiddle()+1);
//						}
//					}
////					e.setXVelocity(0.15f);
////					e.setFalling(false);
//				}
			}
		}
	}
	
	public void EnemyCollisionDetection(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		for (Blocks b : bbg.block)
		{
			for (Enemy e : bbg.enemy)
			{	
				if (e.collides(b) != null && !e.getInBubble() && e.getCoarseGrainedMaxY() > 100)
				{				
					// Collision detection
					if (e.getCoarseGrainedMaxY() < b.getCoarseGrainedMinY()+5
						)//&& jumping == false)
					{
						bottomToMiddle = b.getCoarseGrainedMinY() - (e.getCoarseGrainedMaxY() - e.getPosition().getY());
						e.setPosition(e.getX(), bottomToMiddle+1);
						
						e.setBottomToMiddle(bottomToMiddle);
						e.setJumping(false);
						e.setFalling(false);
						e.setOnPlatform(true);
						
						e.getCollidingWithBlocks().add(b);
						
						
						if(e.getEnemyDying() && e.getVelocity().getY() > 0)
						{
							e.removeAnimation();
							e.setRemoveEnemy(true);
						}
//						enemyOnPlatform = true;
//						enemyJumping = false;
//						enemyFalling = false;
//						enemyVelocityY = 0;
					}
					else if (e.getCoarseGrainedMinX() < 48)
					{
						e.removeAnimation();
						e.setPosition(68, e.getY());
//						e.setCurrentLocation(new Vector(node.get(0).getX(), node.get(0).getY()));
//						e.setVelocity(new Vector(0.15f, 0));
						if(!e.getEnemyAngry())
						{
							e.AnimateRight();
							if(e.getOnPlatform())
								e.setXVelocity(0.1f);
							e.setYVelocity(0.15f);
						}
						else
						{
							e.AnimateAngryRight();
							if(e.getOnPlatform())
								e.setXVelocity(0.2f);
							e.setYVelocity(0.2f);
						}
//						enemyVelocityX = 0;
//						enemyVelocityY = 0.15f;
					}
					else if (e.getCoarseGrainedMaxX() > bbg.ScreenWidth - 48)
					{
						e.removeAnimation();
						e.setPosition(bbg.ScreenWidth - 68, e.getY());
//						e.setCurrentLocation(b.getPosition());
						if(!e.getEnemyAngry())
						{
							e.AnimateLeft();
							if(e.getOnPlatform())
								e.setXVelocity(-0.1f);
							e.setYVelocity(0.15f);
						}
						else
						{
							e.AnimateAngryLeft();
							if(e.getOnPlatform())
								e.setXVelocity(-0.2f);
							e.setYVelocity(0.2f);
						}
//						enemyVelocityX = 0;
//						enemyVelocityY = 0.15f;
//						e.setVelocity(new Vector(-0.2f, 0));
					}
				}
//				else
//					e.setOnPlatform(false);
			}
		}
		
		for (Enemy e : bbg.enemy)
		{
			if(e.getOnPlatform() && e.getCollidingWithBlocks().size() > 2)
			{
				int size = e.getCollidingWithBlocks().size();
				float block1X = e.getCollidingWithBlocks().get(size-2).getX();
				float block1Y = e.getCollidingWithBlocks().get(size-2).getY();
				float block2X = e.getCollidingWithBlocks().get(size-1).getX();
				float block2Y = e.getCollidingWithBlocks().get(size-1).getY();
				float distBlock1 = Math.abs(e.getX() - block1X);
				float distBlock2 = Math.abs(e.getX() - block2X);
				
	//			System.out.println(distBlock1 + ", " + distBlock2);
				
				if(distBlock1 < distBlock2)
				{
	//				e.setPosition(block1X, e.getY());
					e.setCurrentLocation(new Vector(block1X, block1Y));
					e.setLastBlock(e.getCollidingWithBlocks().get(size-2));
				}
				else
				{
	//				e.setPosition(block2X, e.getY());
					e.setCurrentLocation(new Vector(block2X, block2Y));
					e.setLastBlock(e.getCollidingWithBlocks().get(size-1));
				}
	//			e.setCurrentLocation(e.);
			}
//			collidingWithBlocks.remove(0);
//			collidingWithBlocks.remove(1);
		}
	}
	
	public void Attacking(StateBasedGame game)
	{
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		// Animate Bub blowing a bubble
		bubbleTimeCount--;
		if(blowingBubble && bubbleTimeCount == 0)
		{
//			System.out.println("Yo");
			bbg.bub.removeAnimation(blowBubble);
			bbg.bub.removeAnimation(walking);
			bbg.bub.removeAnimation();
			blowingBubble = false;
			
			if(bbg.bub.getVelocity().getX() > 0)
			{
//				System.out.println("Yo again");
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_RIGHT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
//				WalkRight(bbg);
			}
			else if(bbg.bub.getVelocity().getX() < 0)
			{
				walking = new Animation(ResourceManager.getSpriteSheet(bbg.BUB_WALKING_LEFT, 45, 40), 
						0, 0, 6, 0, true, 120, true);
				bbg.bub.addAnimation(walking);
			}
			else if(!facingLeft && bbg.bub.getVelocity().getX() == 0)
				bbg.bub.AnimateRight();
			else if(facingLeft && bbg.bub.getVelocity().getX() == 0)
				bbg.bub.AnimateLeft();
		}
		
		
		// Hit enemy with bubble
		for (Enemy e : bbg.enemy)
		{
			for (Bubble b : bbg.bubble)
			{
				if(e.collides(b) != null && !e.getInBubble() && !e.getEnemyDying())
				{
//					System.out.println("Enemy hit");
					e.removeAnimation();
					
					if(e.getEnemyAngry())
						e.AnimateAngryInBubble();
					else
						e.AnimateInBubble();
					
					e.setVelocity(new Vector(0, -0.08f));
					e.setTimeInBubble(240);
					b.removeAnimation();
					b.setRemoveBubble(true);
					break;
				}
			}
		}
		
		// Remove any bubbles that have hit an enemy
		Iterator<Bubble> removeBubble = bbg.bubble.iterator();
		
		while (removeBubble.hasNext())
		{
			Bubble b = removeBubble.next();
			
			if(b.getRemoveBubble())
				removeBubble.remove();
		}
		
		// After 4 seconds, the enemy breaks out of the bubble and goes back to normal
		for (Enemy e : bbg.enemy)
		{
			if(e.getInBubble() && !e.getEnemyDying())
			{
//				System.out.println(e.getTimeInBubble());
				if(e.getTimeInBubble() <= 0)
				{
					e.removeAnimation();
					if(e.getWalkingLeft())
					{
						e.AnimateAngryLeft();
						e.setYVelocity(0.2f);
						e.setXVelocity(-0.2f);
					}
					else
					{
						e.AnimateAngryRight();
						e.setYVelocity(0.2f);
						e.setXVelocity(0.2f);
					}
//					e.AnimateLeft();
					e.setInBubble(false);
					e.setWalkingLeft(true);
					e.setEnemyAngry(true);
					e.setFalling(true);
				}
				else
					e.decTimeInBubble();
			}
		}
		
		// Kill the enemy when bub hits into its bubble
		for (Enemy e : bbg.enemy)
		{
			if(e.getInBubble())
			{
				if(e.collides(bbg.bub) != null)
				{
					if(ResourceManager.getSound(bbg.ENEMY_DIES).playing())
					{
						ResourceManager.getSound(bbg.ENEMY_DIES).stop();
						ResourceManager.getSound(bbg.ENEMY_DIES).play();
					}
					else
						ResourceManager.getSound(bbg.ENEMY_DIES).play();
					
					e.removeAnimation();
					e.AnimateEnemyDying();
					e.setInBubble(false);
					e.setEnemyDying(true);
					e.setVelocity(new Vector(0, -0.25f));
				}
			}
		}
		
//		System.out.println(bbg.enemy.size());
		
		for (Enemy e : bbg.enemy)
		{
			if(e.getEnemyDying())
			{
//				e.removeAnimation();
//				e.AnimateEnemyDying();
				e.setVelocity(new Vector(0, e.getVelocity().getY() + 0.003f));
			}
		}	
		
		// Remove enemies that have died
		Iterator<Enemy> removeEnemy = bbg.enemy.iterator();
		
		while (removeEnemy.hasNext())
		{
			Enemy e = removeEnemy.next();
			
			if(e.getRemoveEnemy())
			{
				removeEnemy.remove();
				
				score += 1000;
			}
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BubbleBobbleGame bbg = (BubbleBobbleGame)game;
		
		if(bubDied)
		{
			deadTimer--;
			if(deadTimer == 0)
			{
				game.enterState(BubbleBobbleGame.GAMEOVERSTATE, new FadeOutTransition(), new FadeInTransition());
				bubDied = false;
				score = 0;
				livesRemaining = 3;
				level = 1;
				levelTimer = 1800;
				stopTimer = false;
			}
		}
		else
		{
			if(!pause && !bbg.enemy.isEmpty())
				levelTimer--;
			
			if(bbg.enemy.isEmpty() && !stopTimer)
			{
				stopTimer = true;
				
				if(timerDisplay > 0)
					score += Math.round(timerDisplay * 13);
				
				if(level == 1)
				{
					game.enterState(BubbleBobbleGame.LEVEL2STATE, new FadeOutTransition(), new FadeInTransition());
					
					level = 2;
					levelTimer = 1800;
					livesRemaining = 3;
					stopTimer = false;
				}
				else if(level == 2)
				{
					game.enterState(BubbleBobbleGame.GAMEWONSTATE, new FadeOutTransition(), new FadeInTransition());
					level = 1;
					levelTimer = 1800;
					livesRemaining = 3;
					stopTimer = false;
					score = 0;
				}
			}
			
			PlayerCollisionDetection(bbg); // Detect player collisions			
			EnemyCollisionDetection(bbg); // Detect enemy collisions
			
			
			// Check for Bub touching an enemy and losing a life. A hit timeout of 3 seconds is activated.
			if(playerHitTimeout == 0)
			{
				for(Enemy e : bbg.enemy)
				{
					if(e.collides(bbg.bub) != null && !e.getInBubble() && !e.getEnemyDying())
					{
	//					System.out.println("Gotcha!");
						livesRemaining--; // KEEP GOING HERE
						playerHitTimeout = 120;
						break;
					}
				}
			}
			else
				playerHitTimeout--;
			
			if(timerDisplay == 0)
			{
				for (Enemy e : bbg.enemy)
				{
					e.removeAnimation();
					e.setEnemyAngry(true);
					if(e.getWalkingLeft())
					{
						e.AnimateAngryLeft();
						e.setYVelocity(0.2f);
						e.setXVelocity(-0.2f);
					}
					else
					{
						e.AnimateAngryRight();
						e.setYVelocity(0.2f);
						e.setXVelocity(0.2f);
					}
				}
			}
			
			if(livesRemaining == 0)
			{
				// Remove enemies that have died
				Iterator<Enemy> removeEnemy = bbg.enemy.iterator();
				
				while (removeEnemy.hasNext())
				{
					Enemy e = removeEnemy.next();
					removeEnemy.remove();
				}
				
				bbg.bub.removeAnimation();
	//			bbg.bub.removeAnimation(standing);
				bbg.bub.removeAnimation(walking);
				
				bbg.bub.AnimateBubDying();
				bbg.bub.setBubDead(true);
	//			bbg.bub.removeAnimation();
				
				livesRemaining = -1;
				
				score = 0;
				level = 1;
				
				bubDied = true;
				deadTimer = 120;
			}
			else
			{
				if(input.isKeyPressed(Input.KEY_P))
				{
					if(pause)
						pause = false;
					else
						pause = true;
				}
				
				if(pause)
					delta = 0;
		
				Attacking(bbg);
				
				if(!pause)
				{
					Controls(bbg, container); // Check what controls are happening by the player
					PlayerMovements(bbg); // Checks how the player should be setup based on controls
					EnemyMovements(bbg); // Checks how the enemies should move
				}	
				
				for (Enemy e : bbg.enemy)
				{
					e.update(delta);
				}
				
				for (Bubble b : bbg.bubble)
				{
					b.update(delta);
				}
				
				bbg.bub.update(delta); // Update the game state
			}
		}
		
	}
	 
	@Override
	public int getID() {
		return BubbleBobbleGame.PLAYINGSTATE;
	}
}
