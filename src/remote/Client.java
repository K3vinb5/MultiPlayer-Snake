package remote;

import environment.Board;
import game.Server;
import gui.SnakeGui;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;


public class Client implements Serializable {

	private ObjectInputStream in; //reader
	private PrintWriter out; //sender
	private Socket socket; //conexao
	private boolean firstRun = true;

	private SnakeGui gameGui;
	private RemoteBoard board;


	public static void main(String[] args) {
		Client client = new Client("localhost");
	}

	public Client(String address) {
		runClient(Server.PORT, address);
		while (board == null){
			//System.out.println("Board received is null");
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
		gameGui = new SnakeGui(board, 600 , 600);
		gameGui.init();
	}

	// Thread to listen every message sent by the server
	private class Listener extends Thread {

		@Override
		public void run() {
			while(true) {
				try {
					if (firstRun){
						board = RemoteBoard.createRemoteBoardFromBoard(receiveBoard(), out);
						firstRun = false;
					}else {
						updateBoard(board, receiveBoard());
					}
					board.setChanged();
					if (board == null){
						System.err.println("Error in board");
					}
				} catch (Exception e) {
					break;
				}
			}
			System.exit(0);
		}
	}

	// Initial run the client side
	public void runClient(int port,String address) {
		try {
			this.connectToServer(port, address);
			new Listener().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Connects to the server provided in Client args
	private void connectToServer(int port,String address) throws IOException {
		InetAddress inetAddress = InetAddress.getByName(address);
		this.socket = new Socket(inetAddress, port);
		this.in = new ObjectInputStream(this.socket.getInputStream());
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
	}

	private Board receiveBoard() {
		Board board = null;
		try {
			board = (Board) in.readObject();
		} catch (Exception ignore) {

		}
		if (board == null) {
			//System.err.println("Erro, board recebido é nulo");
		}
		else {
			//System.out.println("Board Recebido com sucesso");
		}
		return board;
	}

	public void updateBoard(Board board1, Board board2){
		board1.setCells(board2.getCells());
		board1.setSnakes(board2.getSnakes());
		board1.setObstacles(board2.getObstacles());
		board1.setGoalPosition(board2.getGoalPosition());
		board1.setFinished(board2.isFinished());
	}

}
