import java.awt.Canvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class Game extends Canvas{

	private int[][] tetrisBoard = new int[10][21];
	// dimensions are written {Width, Height}
	private int[] dimensions = { 400, 800 };
	private int blockSize = dimensions[0] / 10;
	public static Queue<Tetriminos> nextBlocks = new LinkedList<Tetriminos>();
	private Tetriminos currentBlock;
	private Color[][] setBlocks = new Color[22][10];
	private Tetriminos holdBlock;
	private boolean isHeld = false;
	private boolean updateQueue = false;
	private int score;

	// Location is based of blocks and is the distance from left, distance from the
	// bottom
	private double[] location;

	public Game() {
		score = 0;
		setSize(dimensions[0], dimensions[1]);
		setBackground(Color.DARK_GRAY);
		
		for (int i = 0; i < 5; i++) {
			addToQueue();
		}
		
		currentBlock = nextBlocks.poll();
		setLocation();
	}
	
	public void setLocation() {
		if(currentBlock.degree()) {
			location = new double[] {5, 19};
		} else
			location = new double[] {4.5, 19.5};
	}
	
	public void addToQueue() {
		Tetriminos temp = new Tetriminos((int)(Math.random() * 7));
		nextBlocks.add(temp);
	}
	
	public boolean updateQueue() {
		return updateQueue;
	}
	
	public int getScore() {
		return score;
	}

	public void paint(Graphics g) {
		ghostBlock(g);
		drawBlock(g, currentBlock, location, currentBlock.getColor());
		drawSetBlocks(g);
		paintBoard(g);
			
	}
	
	public void ghostBlock(Graphics g) {
		double[] ghostLocation = new double[2];
		for(int i = 0; i < location.length;i++) {
			ghostLocation[i] = location[i];
		}
		while(!collides(ghostLocation)) 
			ghostLocation[1] -= 1;
		ghostLocation[1] +=1;
		drawBlock(g, currentBlock, ghostLocation, Color.LIGHT_GRAY);
		
	}
	
	public void paintBoard(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, dimensions[0], dimensions[1]);
		for (int i = 1; i < 10; i++) {
			g.drawLine(i * dimensions[0] / 10, 0, i * dimensions[0] / 10, dimensions[1]);
		}
		for (int i = 1; i < 20; i++) {
			g.drawLine(0, i * dimensions[1] / 20, dimensions[0], i * dimensions[1] / 20);
		}
	}
	
	
	public void drawSetBlocks(Graphics g) {
		for(int height = 0; height < setBlocks.length; height++) {
			for(int width = 0; width < setBlocks[height].length; width++) {
				if(setBlocks[height][width] != null) {
					g.setColor(setBlocks[height][width]);
					fillSquare(g, new double[]{width, height});
				}
			}
		}
	}
	

	public void drawBlock(Graphics g, Tetriminos block, double[] location, Color color) {
		g.setColor(color);
		double[][] blocksMap = block.getBlock();
		
		for (int i = 0; i < blocksMap.length; i++) {
			//adds each vector in tetrimino to the location to get each point a block is at.
			double[] point = addVectors(blocksMap[i], location);
			
			//fills in the box but first maps each point to the window coordinates
			fillSquare(g, point);
		}
	}
	
	public void fillSquare(Graphics g, double[] vector) {
		g.fillRect((int)vector[0] * blockSize, (int)(dimensions[1] - vector[1] * blockSize), blockSize, blockSize);
	}


	public boolean collides(double[] location) {
		double[][] blockMap = currentBlock.getBlock();
		for(int i = 0 ; i < blockMap.length; i++) {
			double[] blockPoint = addVectors(blockMap[i], new double[]{location[0], location[1]});
			if(blockPoint[1] < 1)
				return true;
			if(blockPoint[0] < 0 || blockPoint[0] > 9)
				return true;
			if(setBlocks[(int)blockPoint[1]][(int)blockPoint[0]] != null)
				return true;
		}
		return false;
	}
	
	/**
	 * returns andouble - or +, detecting which side of the block is currently in collision
	 * - tells the left side of the block is collided, and + tells the right side of the block has collided
	 * returns 400 if there is no collision, and returns 1 if 
	 */
	public double blockSideCollision(double[] location) {
		double[][] blockMap = currentBlock.getBlock();
		for(int i = 0 ; i < blockMap.length; i++) {
			double[] blockPoint = addVectors(blockMap[i], new double[]{location[0], location[1]});
			if(blockPoint[0] < 0 || blockPoint[0] > 9)
				return blockMap[i][0];
			if(setBlocks[(int)blockPoint[1]][(int)blockPoint[0]] != null)
				return blockMap[i][0];
			
		}
		return 0;
	}
	

	
//############################################################## ACTION METHODS  ##########################################################################
	
	public boolean pushOff(double[] location) {
		int count = 0;
		while(collides(location)) {
			if(count > 4)
				return false;
			if(blockSideCollision(location) < 0)
				location[0] +=1;
			if(blockSideCollision(location) > 0)
				location[0] -=1;
			if(count > 3)
				location[1] -= 1;
			count++;
		}
		return true;
	}
	
	/**
	 * Adds to vectors of equal length Precondition: the vectors a and b must be of
	 * equal length in order for the vectors to be added
	 */
	public static double[] addVectors(double[] a, double[] b) {
		double[] temp = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			temp[i] = a[i] + b[i];
		}
		return temp;
	}
	
	
	public void moveDown() {
		location[1] -= 1;
		if(collides(location)) {
			setBlock();
			updateQueue = true;
		}
		repaint();
	}
	public void moveRight() {
		location[0] = location[0] + 1;
		if(collides(location))
			location[0] = location[0] - 1;
		repaint();
	}
	public void moveLeft() {
		location[0] = location[0] - 1;
		if(collides(location))
			location[0] = location[0] + 1;
		repaint();
	}
	
	public void clockwiseTurn() {
		double[] tempLocation = new double[2];
		for(int i = 0; i < location.length;i++) {
			tempLocation[i] = location[i];
		}
		currentBlock.Clockwise();
		if(!pushOff(tempLocation))
			currentBlock.counterClockwise();
		else
			location = tempLocation;
		repaint();
	}
	
	public void counterClockwiseTurn() {
		double[] tempLocation = new double[2];
		for(int i = 0; i < location.length;i++) {
			tempLocation[i] = location[i];
		}
		currentBlock.counterClockwise();
		if(!pushOff(tempLocation))
			currentBlock.Clockwise();
		else
			location = tempLocation;
		repaint();
		
	}
	public Tetriminos getHoldBlock() {
		return holdBlock;
	}
	
	public void holdBlock() {
		if(holdBlock == null) {
			holdBlock = currentBlock;
			currentBlock = nextBlocks.poll();
			addToQueue();
		} else {
			Tetriminos temp = holdBlock;
			holdBlock = currentBlock;
			currentBlock = temp;
		}
		isHeld = true;
		setLocation();
		repaint();
	}
	
	public boolean canHold() {
		return !isHeld;
	}
	
	public void setBlock() {
		while(!collides(location)) 
			location[1] -= 1;
		location[1] +=1;
		
		double[][] blockMap = currentBlock.getBlock();
		
		//adds the blocks to the set block array
		for(int i = 0; i < blockMap.length; i++) {
			double[] blockPoint = addVectors(blockMap[i], location);
			setBlocks[(int)(blockPoint[1])][(int)blockPoint[0]] = currentBlock.getColor();
		}
		isHeld = false;
		currentBlock=nextBlocks.poll();
		addToQueue();
		setLocation();
		clearBoard();
		repaint();
	}
	
	public void clearBoard() {
		for(int y = 0; y < setBlocks.length; y++) {
			if(fullLine(y)) {
				clearLine(y);
				y = y-1;
			}
		}
	}
	
	public boolean fullLine(int y) {
		for(int x = 0; x < setBlocks[y].length; x++) {
			if(setBlocks[y][x] == null)
				return false;
		}
		score += 10;
		return true;
	}
	
	public void clearLine(int y) {
		for(int i = 0; i <10; i++) {
			setBlocks[y][i] = null;
		}
		for(int i = y + 1; i < setBlocks.length; i++) {
			for(int x = 0; x < setBlocks[i].length; x++) {
				setBlocks[i - 1][x] = setBlocks[i][x];
				setBlocks[i][x] = null;
			}
		}
		repaint();
	}
}
