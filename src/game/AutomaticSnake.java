package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {

	private boolean isRunning = true;

	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}

	@Override
	public void run() {

		doInitialPositioning();
		/*try {
			cells.getLast().request(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//Snake Automatic Movement (Ask about the while)
		while(this.isRunning){
			Board board = this.getBoard();
			BoardPosition goalPosition = board.getGoalPosition();
			List<Direction> moveDirections = Direction.calculateBestDirections(goalPosition, this.getSnakeHead());
			List<BoardPosition> possibleNewPositions = new ArrayList<>();
			for(Direction direction : moveDirections){
				BoardPosition positionToAdd = new BoardPosition(this.getSnakeHead().x + direction.x, this.getSnakeHead().y + direction.y);
				if (positionToAdd.x >= 0 && positionToAdd.y >= 0 && positionToAdd.x < Board.NUM_ROWS && positionToAdd.y < Board.NUM_COLUMNS){
					possibleNewPositions.add(positionToAdd);
				}
			}
			if(getPath().size() > 1){
				board.getBoardSnakesSharedLock().lock();
				System.out.println("Possible new Positions for snake " + this.getIdentification() + " + possibleNewPositions");
				possibleNewPositions.removeIf(position -> this.getPath().get(1).equals(position) || this.getPath().get(0).equals(position));
				board.getBoardSnakesSharedLock().unlock();
			}
			try {
				//tries to move to the best position it can move
				move(board.getCell(possibleNewPositions.get(0)));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			//Try to sleep
			try {
				sleep(200);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}
}
