package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

public class Cell implements Serializable {
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
		while (isOcupied()){
			System.out.println("Waiting for: " + ocuppyingSnake + " or " + gameElement);
			occupied.await();
		}
		ocuppyingSnake = snake;
		occupied.signalAll();
		cellLock.unlock();
	}

	public void release() throws InterruptedException{
		cellLock.lock();
		if(ocuppyingSnake != null)
			occupied.signalAll();
		ocuppyingSnake = null;
		cellLock.unlock();
	}
	public boolean setGameElement(GameElement element) throws InterruptedException{
		cellLock.lock();
		if(gameElement != null)
			return false;
		if (element instanceof Obstacle){
			((Obstacle)element).setCell(this);
		}
		gameElement = element;
		cellLock.unlock();
		return true;
	}

	public void removeObstacle() throws InterruptedException{
		cellLock.lock();
		while (gameElement == null){
			System.out.println("Stuck waiting " + gameElement); // :(
			occupied.await();
		}
		if (gameElement instanceof Obstacle){
			((Obstacle)gameElement).setCell(null);
		}
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
