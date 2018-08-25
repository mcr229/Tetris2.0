 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.util.*;

public class TetrisWindow extends JFrame implements KeyListener, Runnable{

	private int BlockBox = 150;
	private int width = 710;
	private int height = 801;
	private Game tetrisGame = new Game();
	Thread runner;
	private JPanel LeftInfoPanel;
	private JPanel queuePanel;
	private JPanel scorePanel;
	
	
	public TetrisWindow() {
		this.setTitle("Tetris");
		setLayout(new BorderLayout());
		getContentPane().setPreferredSize((new Dimension(width, height)));
		
		LeftInfoPanel = getleftInfoPanel();
		add(LeftInfoPanel, BorderLayout.WEST);
		
		add(tetrisGame, BorderLayout.CENTER);
		addKeyListener(this);
		runner = new Thread(this);
		runner.start();
			
		queuePanel = setUpQueue();
		add(queuePanel, BorderLayout.EAST);
		
		setVisible(true);
		pack();
	}
	
	private JPanel getleftInfoPanel() {
		JPanel leftSide = new JPanel();
		leftSide.setBackground(Color.LIGHT_GRAY);
		Box b1 = new Box(BoxLayout.Y_AXIS);
		JPanel scoreLabel = setUpScoreLabel();
		JPanel holdBox = setUpHold();
		b1.add(scoreLabel);
		b1.add(holdBox);
		
		leftSide.add(b1);
		return leftSide;
	}
	
	private JPanel setUpHold() {
		// TODO Auto-generated method stub
		JPanel holdBox = new JPanel();
		holdBox.setLayout(new BorderLayout());
		holdBox.setBackground(Color.LIGHT_GRAY);
		holdBox.setPreferredSize(new Dimension(BlockBox, 400));
		
		//adding the label
		JLabel holdLabel = new JLabel("Hold", JLabel.CENTER);
		holdLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		holdLabel.setForeground(Color.DARK_GRAY);
		holdBox.add(holdLabel, BorderLayout.NORTH);
		
		//adding the block
		BlockPanels hold = new BlockPanels(BlockBox, tetrisGame.getHoldBlock());
		holdBox.add(hold, BorderLayout.CENTER);

		
		return holdBox;
		
	}


	/**
	 * Sets up the label for the score and returns the label.
	 * @return
	 */
	private JPanel setUpScoreLabel() {
		//Some how gets the Score...
		JLabel pointLabel = new JLabel("<html>" + tetrisGame.getScore() + "</html>", JLabel.CENTER);
		pointLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		pointLabel.setForeground(Color.DARK_GRAY);

		JLabel score = new JLabel("Score", JLabel.CENTER);
		score.setFont(new Font("Times New Roman", Font.BOLD, 28));
		score.setForeground(Color.DARK_GRAY);
		
		JPanel scoreLabel = new JPanel();
		scoreLabel.setLayout(new BorderLayout());
		scoreLabel.add(score, BorderLayout.NORTH);
		scoreLabel.add(pointLabel, BorderLayout.CENTER);
		
		//Changing colors and style
		scoreLabel.setPreferredSize(new Dimension(BlockBox, 100));
		scoreLabel.setBackground(Color.LIGHT_GRAY);
		scoreLabel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		
		return scoreLabel;
	}
	private JPanel setUpQueue() {
		JPanel queue = new JPanel();
		queue.setLayout(new BorderLayout());
		queue.setPreferredSize(new Dimension(BlockBox, 500));
		queue.setBackground(Color.LIGHT_GRAY);
		
		Box b1 = new Box(BoxLayout.Y_AXIS);
		JLabel next = new JLabel("Next", JLabel.CENTER);
		next.setFont(new Font("Times New Roman", Font.BOLD, 28));
		next.setForeground(Color.DARK_GRAY);

		Iterator<Tetriminos> blockIterator = tetrisGame.nextBlocks.iterator();
		while(blockIterator.hasNext()) {
			BlockPanels temp = new BlockPanels(BlockBox, blockIterator.next());
			b1.add(temp);
		}
		queue.add(next,BorderLayout.NORTH);
		queue.add(b1, BorderLayout.CENTER);
		return queue;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) {
			tetrisGame.moveLeft();
		}
		if(key == KeyEvent.VK_RIGHT) {
			tetrisGame.moveRight();
		}
		if(key == KeyEvent.VK_DOWN) {
			tetrisGame.moveDown();
		}
		if(key == KeyEvent.VK_UP) {
			tetrisGame.clockwiseTurn();
		}
		if(key == KeyEvent.VK_Z) {
			tetrisGame.counterClockwiseTurn();
		}
		if(key == KeyEvent.VK_SPACE) {
			tetrisGame.setBlock();
			updateQueue();
			updateScore();
			
		}
		if(key == KeyEvent.VK_SHIFT) {
			if(tetrisGame.canHold()) {
			tetrisGame.holdBlock();
			updateHold();}
		}
		
	}
	
	private void updateScore() {
		this.remove(LeftInfoPanel);
		LeftInfoPanel = getleftInfoPanel();
		add(LeftInfoPanel, BorderLayout.WEST);
		revalidate();
	}
	
	private void updateQueue() {
		this.remove(queuePanel);
		queuePanel = setUpQueue();
		add(queuePanel, BorderLayout.EAST);
		revalidate();
	}
	
	private void updateHold() {
		this.remove(LeftInfoPanel);
		LeftInfoPanel = getleftInfoPanel();
		add(LeftInfoPanel, BorderLayout.WEST);
		revalidate();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		e.consume();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
	}


	public static void main(String[] args) {
		TetrisWindow start = new TetrisWindow();
		start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void run() {
		queuePanel = setUpQueue();
		while(true) {
			tetrisGame.moveDown();
			tetrisGame.repaint();
			updateQueue();
			try {Thread.sleep(1200);}
			catch(InterruptedException e) {}
		}
	}
	

}
