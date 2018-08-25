
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class Tetriminos {
	//Each block will have an array of vectors which outlines the general location of the each square in the block
	//with respect to the its pivot
	double[][] Block;
	private Color color;
	private boolean degree;
	/*
	 * The I Block and O Block are special since their pivot does not lie on a square, instead
	 * they lie along the grid lines, as a result it is important to differentiate the blocks whose
	 * grids are based in twos and those grids based in threes(i.e. blocks with pivots in squares and blocks
	 * with pivots in grids).
	 */

	public Tetriminos(int i) {
		// TBlock
		if (i == 0) {
			Block = new double[][] { { -1, 0 }, { 0, 0 }, { 0, 1 }, { 1, 0 } };
			color = Color.MAGENTA;
			degree = true;
		} // ZBlock
		else if (i == 1) {
			Block = new double[][] { { -1, 1 }, { 0, 1 }, { 0, 0 }, { 1, 0 } };
			color = Color.RED;
			degree = true;
			
		} // SBlock
		else if (i == 2) {
			Block = new double[][] { { -1, 0 }, { 0, 0 }, { 0, 1 }, { 1, 1 } };
			color = Color.green;
			degree = true;
		} // OBlock
		else if (i == 3) {
			Block = new double[][] { { -0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, -0.5 }, { -0.5, -0.5 } };
			color = Color.YELLOW;
			degree = false;
		} // LBlock
		else if (i == 4) {
			Block = new double[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 1, 1 } };
			color = Color.orange;
			degree = true;
		} // JBlock
		else if (i == 5) {
			Block = new double[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { -1, 1 } };
			color = Color.BLUE;
			degree = true;
		} // IBlock
		else if (i == 6) {
			Block = new double[][] { { .5, -1.5}, { .5, -.5}, { .5, .5}, { .5, 1.5} };
			color = Color.CYAN;
			degree = false;
		}

	}
	/**
	 * 
	 * @return the 2d block array
	 */
	public double[][] getBlock() {
		return Block;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean degree() {
		return degree;
	}


	/**
	 * Multiplies the current block matrix by a rotation matrix that rotates all the
	 * vectors 90 degrees clockwise, thereby rotating all of the matrices
	 */
	public void Clockwise() {
		double[][] rotationMatrix = { { 0, 1 }, { -1, 0 } };

		for (int i = 0; i < Block.length; i++) {
			Block[i] = matrixMultiply(rotationMatrix, Block[i]);
		}

	}
	/**
	 * Multiplies the current block matrix by a rotation matrix that rotates all the
	 * vectors 90 degrees clockwise, thereby rotating all of the matrices
	 */
	public void counterClockwise() {
		double[][] rotationMatrix = { { 0, -1 }, { 1, 0 } };

		for (int i = 0; i < Block.length; i++) {
			Block[i] = matrixMultiply(rotationMatrix, Block[i]);
		}
	}

	/**
	 * 
	 * @param Matrix
	 *            A
	 * @param vector
	 *            x
	 * @return the vector b for which Ax = b
	 */
	public static double[] matrixMultiply(double[][] A, double[] x) {
		double temp = 0;
		double[] vector = new double[A[0].length];
		for (int i = 0; i < A[0].length; i++) {
			for (int j = 0; j < x.length; j++) {
				temp += A[i][j] * x[j];
			}
			vector[i] = temp;
			temp = 0;
		}
		return vector;
	}

}
