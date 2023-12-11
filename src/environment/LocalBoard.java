package environment;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.*;
import threads.MyThreadPool;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board implements Serializable {
	
	public static final int NUM_SNAKES = 2;
	public static final int NUM_OBSTACLES = 25;
	public static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3; //maybe not

	//private ExecutorService threadPool = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES); //maybe not
	private transient MyThreadPool threadPool = new MyThreadPool(getObstacles(), NUM_SIMULTANEOUS_MOVING_OBSTACLES, this);

	public LocalBoard() {
		for (int i = 0; i < NUM_SNAKES; i++) {
			//AutomaticSnake snake = new AutomaticSnake(i, this);
			//snakes.add(snake);
		}
		addObstacles( NUM_OBSTACLES);
		Goal goal = addGoal();
	}

	public void init() {
		System.out.println("Snakes Started");
		for(Snake s : snakes){
			s.start();
		}
		threadPool.start();
		setChanged();
	}
	
	public MyThreadPool getThreadPool() {
		return threadPool;
	}

	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}





}
