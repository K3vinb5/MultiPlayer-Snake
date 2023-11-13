package game;

import environment.BoardPosition;

public enum Direction {
    NORTH(0,1), SOUTH(0,-1), EAST (1,0), WEST(-1,0);

    public final int x, y;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction calculateDirection(BoardPosition positionA, BoardPosition positionB){
        int xDifference = positionA.x - positionB.x;
        int yDifference = positionA.y - positionB.y;
        if (Math.abs(xDifference) > Math.abs(yDifference)){
            //Prefere to move in the x-Direction (East and West)
            if (xDifference < 0){
                return WEST;
            }else{
                return EAST;
            }
        }else{
            //Prefere to move int the y-Direction (South and North)
            if(yDifference < 0){
                return SOUTH;
            }else{
                return NORTH;
            }
        }
    }
}
