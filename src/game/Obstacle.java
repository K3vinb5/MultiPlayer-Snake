package game;

import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Obstacle extends GameElement {

	private static final int NUM_MOVES=3;
	private static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Board board;
	private BoardPosition boardPosition;
	public Obstacle(Board board) {
		super();
		this.board = board;
	}
	
	public int getRemainingMoves() {
		return remainingMoves;
	}

	public BoardPosition getBoardPosition() {
		return boardPosition;
	}

	public void setBoardPosition(BoardPosition boardPosition) {
		this.boardPosition = boardPosition;
	}
}
