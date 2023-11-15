package game;

import environment.BoardPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Direction {
    NORTH(0,1), SOUTH(0,-1), EAST (1,0), WEST(-1,0);

    public final int x, y;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static List<Direction> calculateBestDirections(BoardPosition positionA, BoardPosition positionB){
        List<Direction> out = new ArrayList<>();
        int xDifference = positionA.x - positionB.x;
        int yDifference = positionA.y - positionB.y;
        if (Math.abs(xDifference) > Math.abs(yDifference)){
            //Prefere to move in the x-Direction (East and West)
            if (xDifference < 0){
                out.add(WEST);
                if(yDifference < 0){
                    out.add(SOUTH);
                    out.add(NORTH);
                }else{
                    out.add(NORTH);
                    out.add(SOUTH);
                }
                out.add(EAST);
                return out;
            }else{
                out.add(EAST);
                if(yDifference < 0){
                    out.add(SOUTH);
                    out.add(NORTH);
                }else{
                    out.add(NORTH);
                    out.add(SOUTH);
                }
                out.add(WEST);
                return out;
            }
        }else{
            //Prefere to move int the y-Direction (South and North)
            if(yDifference < 0){
                out.add(SOUTH);
                if (xDifference < 0){
                    out.add(WEST);
                    out.add(EAST);
                }else{
                    out.add(EAST);
                    out.add(WEST);
                }
                out.add(NORTH);
                return out;
            }else{
                out.add(NORTH);
                if (xDifference < 0){
                    out.add(WEST);
                    out.add(EAST);
                }else{
                    out.add(EAST);
                    out.add(WEST);
                }
                out.add(SOUTH);
                return out;
            }
        }
    }

    public static Direction getRandomDirection(){
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }
}
