package environment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

public abstract class Board extends Observable {
	//constants
	public static final long PLAYER_PLAY_INTERVAL = 100;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_ROWS = 30;
	//attributes
	protected Cell[][] cells;
	private Lock boardCellsLock = new ReentrantLock();
	private BoardPosition goalPosition;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	private Lock boardSnakesLock = new ReentrantLock();
	private Lock boardSnakesSharedLock = new ReentrantLock();
	private LinkedList<Obstacle> obstacles= new LinkedList<Obstacle>();
	private Lock boardObstaclesLock = new ReentrantLock();
	protected boolean isFinished;
	private Lock boardLock = new ReentrantLock();

	public Board() {
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}
	}
	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}
	public Lock getBoardLock() {
		return boardLock;
	}
	public Lock getBoardCellsLock() {
		return boardCellsLock;
	}
	public Lock getBoardObstaclesLock() {
		return boardObstaclesLock;
	}
	public Lock getBoardSnakesSharedLock() {
		return boardSnakesSharedLock;
	}
	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_ROWS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}

	public boolean isObstacleFree(Cell cell){
		boardObstaclesLock.lock();
		for (Obstacle obstacle : obstacles){
			if (obstacle.getBoardPosition().equals(cell.getPosition())){
				boardObstaclesLock.unlock();
				return false;
			}
		}
		boardObstaclesLock.unlock();
		return true;
	}

	public boolean isSnakeFree(Cell cell){
		boardSnakesLock.lock();
		for (Snake snake : snakes){
			if (snake.getPath().contains(cell.getPosition())){
				boardSnakesLock.unlock();
				return false;
			}
		}
		boardSnakesLock.unlock();
		return true;
	}
	
	public void addGameElement(GameElement gameElement) throws InterruptedException{
		boolean placed = false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				getCell(pos).setGameElement(gameElement);
				if(gameElement instanceof Goal) {
					setGoalPosition(pos);
					System.out.println("Goal placed at: " + pos);
				}
				if(gameElement instanceof Obstacle){
					((Obstacle) gameElement).setBoardPosition(pos);
				}
				placed=true;
			}
		}
	}
	public void changeGoalPosition(Goal goal){
		boolean placed = false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				try{
					getCell(pos).setGameElement(goal);
					setGoalPosition(pos);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
				System.out.println("Goal changed to: " + pos);
				placed = true;
			}
		}
	}

	public Snake getSnakeAt(BoardPosition pos){
		boardSnakesLock.lock();
		for (Snake snake : snakes){
			if (snake.getSnakeHead().equals(pos)){
				boardSnakesLock.unlock();
				return snake;
			}
		}
		boardSnakesLock.unlock();
		return null;
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}

	public List<BoardPosition> getNeighboringPositionsExcludingOwnSnake(Snake snake){
		Cell snakeHead = snake.getCells().getFirst();
		List<BoardPosition> out = getNeighboringPositions(snakeHead);
		for (BoardPosition position : out){
			if (snake.getPath().contains(position)){
				//equals is implemented in BoardPosition class...
				out.remove(position);
			}
		}
		return out;
	}

	

	protected Goal addGoal() {
		Goal goal=new Goal(this);
		try{
			addGameElement( goal);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		// clear obstacle list , necessary when resetting obstacles.
		getObstacles().clear();
		while(numberObstacles>0) {
			Obstacle obs=new Obstacle(this);
			try{
				addGameElement( obs);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			getObstacles().add(obs);
			numberObstacles--;
		}
	}
	
	public LinkedList<Snake> getSnakes() {
		return snakes;
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	
	public abstract void init(); 
	
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();
	
	
	

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}


}