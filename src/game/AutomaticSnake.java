package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import environment.Cell;
import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake implements Serializable {

	private boolean isRunning = true;
	private long timeToSleep;

	public AutomaticSnake(int id, LocalBoard board, long timeToSleep) {
		super(id,board);
		this.timeToSleep = timeToSleep;
	}

	@Override
	public void run() {
		System.out.println("Automatic Snake Awoke");
		doInitialPositioning();
		try {
			Thread.sleep(timeToSleep);
		}catch (Exception e){
			e.printStackTrace();
		}
		while(!getBoard().isFinished()){
			//tries to move to the best position it can move
			try {
				move(getBestCellToMoveFromHead());
			} catch (InterruptedException e) {
				System.out.println("Button Pressed");
				moveToRandomPosition();
			}
			//Try to sleep
			try {
				sleep(100);
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
	//When Interrupted
	private void moveToRandomPosition() {
		try {
			List<BoardPosition> neighbours = getBoard().getNeighboringPositions(getBoard().getCell(this.getSnakeHead()));
			neighbours.removeIf(position -> getPath().contains(position) || getBoard().getCell(position).getGameElement() != null);
			if (neighbours.isEmpty()) {
				move(getBoard().getCell(getBoard().getNeighboringPositions(getBoard().getCell(this.getSnakeHead())).get(0)));
			}else {
				move(getBoard().getCell(neighbours.get(0)));
			}
		}catch (Exception e) {
			moveToRandomPosition();
		}
	}
	private Cell getBestCellToMoveFromHead(){
		List<BoardPosition> positions = getBoard().getNeighboringPositions(getBoard().getCell(getSnakeHead()));
		positions.removeIf(position -> getPath().contains(position));
		BoardPosition goalPosition = getBoard().getGoalPosition();
		BoardPosition best = null;
		Double bestDistance = Double.POSITIVE_INFINITY;
		for (BoardPosition position : positions){
			if (position.distanceTo(goalPosition) < bestDistance){
				best = position;
				bestDistance = position.distanceTo(goalPosition);
			}
		}
		return getBoard().getCell(best);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}
}
