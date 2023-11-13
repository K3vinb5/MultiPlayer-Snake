package game;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}


	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size: " + cells.size());
		try {
			cells.getLast().request(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Snake Automatic Movement (Ask about the while)
		while(true){
			Board board = this.getBoard();
			BoardPosition goalPosition = board.getGoalPosition();
			Direction moveDirection = Direction.calculateDirection(goalPosition, this.getSnakeHead());
			System.out.println("Move Direction is " + moveDirection + " : " + this.getIdentification());
			//TODO Verificar que não passa da borda do tabuleiro
			if (moveDirection.x != 0 || moveDirection.y != 0){
				BoardPosition newPosition = new BoardPosition(this.getSnakeHead().x + moveDirection.x, this.getSnakeHead().y + moveDirection.y);
				try {
					move(new Cell(newPosition));
					System.out.println("Snake: " + this.getIdentification() + " moved to " + newPosition);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			//Try to sleep
			try {
				//TODO I'm using a non standart time because I think the original is too fast, change later
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}
	

	
}
