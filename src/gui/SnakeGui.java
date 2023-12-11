package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import environment.Board;
import environment.LocalBoard;
import game.Snake;

/**
 *  Class to create and configure GUI.
 *  Only the listener to the button should be edited, see
 * 
 * @author luismota
 *
 */
public class SnakeGui implements Observer, Serializable {
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 800;
	public static final int NUM_COLUMNS = 40;
	public static final int NUM_ROWS = 30;
	private static JFrame frame; //static because there is always just one SnakeGui objecto for now
	private BoardComponent boardGui;
	private Board board;

	public SnakeGui(Board board, int x,int y) {
		super();
		this.board=board;
		frame= new JFrame("The Snake Game: "+(board instanceof LocalBoard?"Local":"Remote"));
		frame.setLocation(x, y);
		frame.setResizable(false);
		buildGui();
	}

	private void buildGui() {
		frame.setLayout(new BorderLayout());
		
		boardGui = new BoardComponent(board);
		boardGui.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		frame.add(boardGui,BorderLayout.CENTER);

		JButton resetObstaclesButton=new JButton("Reset snakes' directions");
		resetObstaclesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Snake snake: board.getSnakes()){
					snake.interrupt();
				}
			}
				
		});
		frame.add(resetObstaclesButton,BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
		board.addObserver(this);
		board.init();
	}

	public static void finishGameScreen(){
		JOptionPane.showMessageDialog(null, "The game has finished", "End", JOptionPane.INFORMATION_MESSAGE);
	}
	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}
}
