package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.HumanSnake;
import game.Obstacle;
import game.Snake;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board implements Serializable {

	private PrintWriter clientOut;

	public RemoteBoard(PrintWriter clientOut){
		this.clientOut = clientOut;
	}

	public static RemoteBoard createRemoteBoardFromBoard(Board board2, PrintWriter clientOut){
		RemoteBoard board1 = new RemoteBoard(clientOut);
		board1.setCells(board2.getCells());
		board1.setSnakes(board2.getSnakes());
		board1.setObstacles(board2.getObstacles());
		board1.setGoalPosition(board2.getGoalPosition());
		board1.setFinished(board2.isFinished());
		return board1;
	}

	@Override
	public void handleKeyPress(int keyCode) {
		clientOut.println(keyCode);
	}


	@Override
	public void handleKeyRelease() {
	}

	@Override
	public void init() {
	}

}
