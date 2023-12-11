package game;

import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;

public abstract class Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private Board board;

	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getLength() {
		return cells.size();
	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

	protected void move(Cell cell) throws InterruptedException {
		//System.out.println("Attempt to move");
		cell.request(this);
		if(cell.getGameElement() instanceof Goal){
			System.out.println("Goal!");
			Goal goal = cell.getGoal();
			if (goal.getValue() == Board.NUM_GOALS_TO_WIN){
				board.setFinished(true);
				SnakeGui.finishGameScreen();
				return;
			}
			size += goal.captureGoal();
			cell.removeGoal();
		}
		cells.addFirst(cell);
		removeTail();
		board.setChanged();
	}

	protected void removeTail(){
		while(cells.size() > size){
			try {
				cells.getLast().release();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			cells.removeLast();
		}
	}
	//temp
	public LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}
		return coordinates;
	}

	public BoardPosition getSnakeHead(){
		return cells.getFirst().getPosition();
	}

	protected void doInitialPositioning(){
		// Random position on the first column.
		// At startup, snake occupies a single cell
		int posX = 0;
		int posY = (int) (Math.random() * Board.NUM_ROWS);
		BoardPosition at = new BoardPosition(posX, posY);

		try{
			board.getCell(at).request(this);
		}catch (InterruptedException e){
			doInitialPositioning();
		}

		cells.add(board.getCell(at));
		System.err.println("Snake " + this.getIdentification() + " starting at:" + getCells().getLast());
	}

	public Board getBoard() {
		return board;
	}


}
