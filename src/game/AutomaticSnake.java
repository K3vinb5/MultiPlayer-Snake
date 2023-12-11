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
	private boolean changeDirection = false;

	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}

	@Override
	public void run() {

		doInitialPositioning();

		while(!getBoard().isFinished()){
			if (changeDirection){
				//Andar numa posicao random valida
				moveToRandomPosition();
			}else{
				//meter o resto do codigo no else
			}
			//tries to move to the best position it can move
			try {
				move(getBestCellToMoveFromHead());
			} catch (InterruptedException e) {
				System.out.println("Button Pressed");
				changeDirection = true;
				continue;
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
	private void moveToRandomPosition(){
		List<BoardPosition> randomPositions = getBoard().getNeighboringPositions(getBoard().getCell(getSnakeHead()));
		List<BoardPosition> snakePositions = getPath();
		randomPositions.removeIf(snakePositions::contains);
		Cell randomNeighbouringCell = getBoard().getCell(randomPositions.get(new Random().nextInt(randomPositions.size())));
		try{
			move(randomNeighbouringCell);
		}catch (InterruptedException exception){
			changeDirection = true;
		}
		changeDirection = false;
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
