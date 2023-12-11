package game;

import environment.Board;
import environment.Cell;
import gui.SnakeGui;

import java.io.Serializable;



public class HumanSnake extends Snake implements Serializable {

	public String currentDirection = "null";

	public HumanSnake(int id,Board board) {
		super(id,board);
	}

	@Override
	public void run() {
		doInitialPositioning();
		while(!getBoard().isFinished()){
			try{
				this.move(getCellToMove());
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				sleep(100);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
    }

	@Override
	protected void move(Cell cell) throws InterruptedException {
		if(cell == null){
			return;
		}
		if (!cell.isOcupied()){
			cell.request(this);
			if(cell.getGameElement() instanceof Goal){
				System.out.println("Goal!");
				Goal goal = cell.getGoal();
				if (goal.getValue() == Board.NUM_GOALS_TO_WIN){
					getBoard().setFinished(true);
					SnakeGui.finishGameScreen();
					return;
				}
				size += goal.captureGoal();
				cell.removeGoal();
			}
			cells.addFirst(cell);
			removeTail();
		}
		getBoard().setChanged();
	}

	public Cell getCellToMove(){
		Cell cellToMove = null;
		switch (currentDirection){
			case "up":
				cellToMove = getBoard().getCell(getSnakeHead().getCellAbove());
				if (!getBoard().isWithinBounds(cellToMove.getPosition())){
					cellToMove = null;
				}
				break;
			case "down":
				cellToMove = getBoard().getCell(getSnakeHead().getCellBelow());
				if (!getBoard().isWithinBounds(cellToMove.getPosition())){
					cellToMove = null;
				}
				break;
			case "left":
				cellToMove = getBoard().getCell(getSnakeHead().getCellLeft());
				if (!getBoard().isWithinBounds(cellToMove.getPosition())){
					cellToMove = null;
				}
				break;
			case "right":
				cellToMove = getBoard().getCell(getSnakeHead().getCellRight());
				if (!getBoard().isWithinBounds(cellToMove.getPosition())){
					cellToMove = null;
				}
				break;
			default:
				cellToMove = null;
		}

		return cellToMove;
	}
	//null -1
	//esquerda 37
	//cima 38
	//direita 39
	//baixo 40
	public void updateCurrentDirection(int keyCode){
		switch (keyCode){
			case 38:
				currentDirection = "up";
				break;
			case 40:
				currentDirection = "down";
				break;
			case 37:
				currentDirection = "left";
				break;
			case 39:
				currentDirection = "right";
				break;
			default:
				//I don't think it will ever be this since the gui filters all other keys (just at the bginning)
				currentDirection = "null";
		}
	}
}
