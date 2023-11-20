package game;

import environment.Board;
import environment.LocalBoard;

public class Goal extends GameElement  {
	private int value = 1;
	private Board board;
	public static final int MAX_VALUE=10;

	public Goal( Board board) {
		this.board = board;
	}
	
	public int getValue() {
		return value;
	}
	//Não estou a usar
	public void incrementValue() throws InterruptedException {
		value +=1 ;
	}

	public int captureGoal() {
		board.changeGoalPosition(this);
		value++;
		return value - 1;
	}
}
