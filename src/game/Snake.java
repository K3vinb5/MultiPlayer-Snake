package game;

import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 *
 */
public abstract class Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 1;
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
		System.out.println("Attempt to move");
		boolean inBounds = board.getNeighboringPositions(this.getCells().getFirst()).contains(cell.getPosition());
		if(cell.getGameElement() instanceof Goal){
			Goal retrievedGoal = cell.removeGoal();
			size += retrievedGoal.captureGoal();
		}
		if (cells.size() < size){
			cell.request(this);
			cells.addFirst(cell);
		}else{
			cell.request(this);
			cells.addFirst(cell);
			Cell cellToRemove = cells.getLast();
			cells.removeLast();
			cellToRemove.release();
		}
		board.setChanged();
	}

	public synchronized LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}
		return coordinates;
	}

	public BoardPosition getSnakeHead(){
		return cells.getFirst().getPosition();
	}

	protected void doInitialPositioning() {
		// Random position on the first column.
		// At startup, snake occupies a single cell
		int posX = 0;
		int posY = (int) (Math.random() * Board.NUM_ROWS);
		BoardPosition at = new BoardPosition(posX, posY);

		try {
			board.getCell(at).request(this);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cells.add(board.getCell(at));
		System.err.println("Snake " + this.getIdentification() + " starting at:" + getCells().getLast());
	}

	public Board getBoard() {
		return board;
	}


}
