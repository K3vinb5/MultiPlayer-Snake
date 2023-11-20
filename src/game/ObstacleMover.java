package game;

import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static environment.LocalBoard.NUM_OBSTACLES;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private LocalBoard board;
	private boolean cellSecured = false;

	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		//While loop runs until a free cell is secured and "locked"
		//System.out.println("Mover - Job Started " + obstacle.getBoardPosition());
		//System.out.println("Looking for Position " + obstacle.getBoardPosition());
		BoardPosition randomBoardPosition = board.getRandomPosition();
		Cell cell = board.getCell(randomBoardPosition);
		if (cell.getGameElement() == null && cell.getOcuppyingSnake() == null && !cell.getPosition().equals(obstacle.getBoardPosition())){
			//Cell randomly chosen if is snake and obstacle free
			//System.out.println("Position Found " + obstacle.getBoardPosition() + " -> " + cell.getPosition());
			try {
				obstacle.getCell().removeObstacle();
				cell.setGameElement(obstacle);
				obstacle.decreaseRemainingMoves();
				board.setChanged();
			} catch (InterruptedException e) {
				//TODO Auto-Generated Catch Exception
				throw new RuntimeException(e);
			}
		}
		//Finished Job (Just wait a bit now)
		//TODO perguntar
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		//System.out.println("Finished Task");
	}

	public Obstacle getObstacle() {
		return obstacle;
	}
}
