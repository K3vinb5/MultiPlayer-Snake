package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Obstacle extends GameElement {

	private static final int NUM_MOVES = 3;
	private static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves = NUM_MOVES;

	private Board board;
	private BoardPosition boardPosition;
	private Cell cell;

	public Obstacle(Board board) {
		super();
		this.board = board;
	}
	
	public int getRemainingMoves() {
		return remainingMoves;
	}

	public void decreaseRemainingMoves(){
		if (remainingMoves > 0){
			remainingMoves--;
		}
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return cell;
	}

	public BoardPosition getBoardPosition() {
		return boardPosition;
	}

	public void setBoardPosition(BoardPosition boardPosition) {
		this.boardPosition = boardPosition;
	}
}
