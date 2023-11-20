package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import environment.Cell;
import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {

	private boolean isRunning = true;
	private boolean changeDirection = false;

	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}

	@Override
	public void run() {
		try{
			doInitialPositioning();
		}catch (InterruptedException e){
			moveToRandomPosition();
		}
		while(!getBoard().isFinished()){
			if (changeDirection){
				//Andar numa posicao random valida
				moveToRandomPosition();
			}
			Board board = this.getBoard();
			BoardPosition goalPosition = board.getGoalPosition();
			List<Direction> moveDirections = Direction.calculateBestDirections(goalPosition, this.getSnakeHead());
			List<BoardPosition> possibleNewPositions = new ArrayList<>();
			moveDirections.forEach(direction -> {
				BoardPosition positionToAdd = new BoardPosition(this.getSnakeHead().x + direction.x, this.getSnakeHead().y + direction.y);
				if (positionToAdd.x >= 0 && positionToAdd.y >= 0 && positionToAdd.x < Board.NUM_ROWS && positionToAdd.y < Board.NUM_COLUMNS)
					possibleNewPositions.add(positionToAdd);
			});
			//TODO Daqui pra frente manter, modificar atrás
			if(getPath().size() > 1){
				//System.out.println("Possible new Positions for snake " + this.getIdentification() + " + possibleNewPositions");
				possibleNewPositions.removeIf(position -> (this.getPath().contains(position) && possibleNewPositions.size() > 1));
			}
			try {
				//tries to move to the best position it can move
				move(board.getCell(possibleNewPositions.get(0)));
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

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}
}
