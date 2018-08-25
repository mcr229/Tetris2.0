import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class BlockPanels extends Canvas {
	private int size;
	Tetriminos block;
	private int blockSize = 25;

	public BlockPanels(int s, Tetriminos b) {
		block = b;
		size = s;
		Canvas panel = new Canvas();

	}

	public void paint(Graphics g) {
		if (block != null) {
			drawBlock(g, block);
		}
	}
	
	public void drawBlock(Graphics g, Tetriminos block) {
		g.setColor(block.getColor());
		double[] midpoint = {(int)(size/blockSize)/2, (int)(size/blockSize)/2};
		double[][] blocksMap = block.getBlock();
		
		for (int i = 0; i < blocksMap.length; i++) {
			//adds each vector in tetrimino to the location to get each point a block is at.
			double[] point = addVectors(blocksMap[i], midpoint);
			
			//fills in the box but first maps each point to the window coordinates
			fillSquare(g, point);
		}
	}
	
	public void fillSquare(Graphics g, double[] vector) {
		int x = (int)vector[0] * blockSize;
		int y = (int)(size - vector[1] * blockSize);
		g.fillRect(x, y, blockSize, blockSize);
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
}
