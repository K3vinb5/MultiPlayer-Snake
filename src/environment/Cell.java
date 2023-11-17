package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

public class Cell {
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement = null;
	private Lock cellLock = new ReentrantLock();
	private Condition occupied = cellLock.newCondition();

	public GameElement getGameElement() {
		return gameElement;
	}

	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public Lock getCellLock() {
		return cellLock;
	}

	public synchronized BoardPosition getPosition() {
		return position;
	}

	public void request(Snake snake) throws InterruptedException {
		cellLock.lock();
		System.out.println("Snake " + snake.getIdentification() + " Requested Cell: " + this.getPosition());
		while (ocuppyingSnake!=null || gameElement != null){
			System.out.println("Waiting for: " + ocuppyingSnake + " or " + gameElement);
			occupied.await();
		}
		ocuppyingSnake = snake;
		occupied.signalAll();
		cellLock.unlock();
		System.out.println("Snake's " + snake.getIdentification() + " Request: " + this.getPosition() + " ended");
	}

	public void release() throws InterruptedException{
		cellLock.lock();
		while(ocuppyingSnake == null)
			occupied.await();
		ocuppyingSnake = null;
		occupied.signalAll();
		cellLock.unlock();
	}

	public void setGameElement(GameElement element) throws InterruptedException{
		cellLock.lock();
		while(gameElement!=null)
			occupied.await();
		gameElement = element;
		occupied.signalAll();
		cellLock.unlock();
	}

	public void removeObstacle() throws InterruptedException{
		cellLock.lock();
		while(gameElement == null)
			occupied.await();
		gameElement = null; //empties gameElement
		occupied.signalAll();
		cellLock.unlock();
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}

	public Goal removeGoal() throws InterruptedException {
		System.out.println(gameElement);
		Goal out = null;
		//Tries to remove goal if there is one on the cell
		if (gameElement instanceof Goal){
			out = (Goal)gameElement;
			this.removeObstacle();
		}
		return out;
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	

}
