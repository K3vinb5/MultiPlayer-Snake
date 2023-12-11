package game;

import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static environment.LocalBoard.NUM_OBSTACLES;

public class ObstacleMover extends Thread implements Serializable {
	private Obstacle obstacle;
	private LocalBoard board;
	private int numberMovement = 0;
	private boolean movementDone = false;

	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		//While loop runs until a free cell is secured and "locked"
		while(!movementDone){
			BoardPosition randomBoardPosition = board.getRandomPosition();
			Cell cell = board.getCell(randomBoardPosition);
			if (cell.getGameElement() == null && cell.getOcuppyingSnake() == null && !cell.getPosition().equals(obstacle.getBoardPosition())){
				//Cell randomly chosen if is snake and obstacle free
				numberMovement ++;
				//System.out.println("Position Found " + obstacle.getBoardPosition() + " -> " + cell.getPosition());
				try {
					obstacle.getCell().removeObstacle();
					boolean trySetGameElement;
					trySetGameElement = cell.setGameElement(obstacle);
					while (!trySetGameElement){
						trySetGameElement = cell.setGameElement(obstacle);
					}
					obstacle.decreaseRemainingMoves();
					board.setChanged();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				try {
					sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				if (numberMovement == Obstacle.NUM_MOVES){
					movementDone = true;
				}
			}
		}
	}

	public Obstacle getObstacle() {
		return obstacle;
	}
}
