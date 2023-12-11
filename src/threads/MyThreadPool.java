package threads;

import environment.Board;
import environment.LocalBoard;
import game.Obstacle;
import game.ObstacleMover;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool extends Thread implements Serializable {

    private final int maxAllowedRunningTask;
    private int currentlyRunningTasks = 0;
    private boolean movementsRemaining = true;
    private LocalBoard board;
    private List<Obstacle> obstacleList;

    private Lock threadPoolLock = new ReentrantLock();
    private Condition condition = threadPoolLock.newCondition();

    public MyThreadPool(List<Obstacle> obstacleList, int maxAllowedRunningTask, LocalBoard board){
        this.obstacleList = obstacleList;
        this.maxAllowedRunningTask = maxAllowedRunningTask;
        this.board = board;
    }

    private Thread getRandomValidObstacleMover(){
        ObstacleMover mover = null;
        synchronized (obstacleList) {
            if (!obstacleList.isEmpty()){
                Obstacle currentObstacle = obstacleList.get(new Random().nextInt(obstacleList.size()));
                mover = new ObstacleMover(currentObstacle, board);
                obstacleList.remove(currentObstacle);
            }
        }
        return mover;
    }

    public void addObstacle(Obstacle obstacle){
        synchronized (obstacleList){
            obstacleList.add(obstacle);
        }
    }

    public synchronized void updateCurrentlyRunningTasks(int n){
        currentlyRunningTasks += n;
        //System.out.println("Currently Running tasks: " + currentlyRunningTasks);
        //System.out.println("Remaining valid tasks " + obstacleList.size());
    }

    public void notifySelf(){
        threadPoolLock.lock();
        try {
            condition.signalAll(); // Notify waiting threads
        } finally {
            threadPoolLock.unlock();
        }
    }

    private class Runner extends Thread{
        @Override
        public void run() {
            Thread task = getRandomValidObstacleMover();
            if (task == null){
                movementsRemaining = false;
                return;
            }
            Obstacle currentObstacle = ((ObstacleMover)task).getObstacle();
            task.start();
            //System.out.println("Started Task");
            try {
                task.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            addObstacle(currentObstacle);

            updateCurrentlyRunningTasks(-1);
            notifySelf();
        }
    }

    @Override
    public void run() {
        while(movementsRemaining && !board.isFinished()){
            threadPoolLock.lock();
            try {
                while (currentlyRunningTasks == maxAllowedRunningTask) {
                    try {
                        condition.await(); // Passive waiting
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                new Runner().start();
                updateCurrentlyRunningTasks(1);
            }finally {
                threadPoolLock.unlock();
            }
        }

    }



}
