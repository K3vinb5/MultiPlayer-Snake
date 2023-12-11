package game;

import environment.Board;
import environment.LocalBoard;
import gui.SnakeGui;
import remote.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Serializable {

    public static final int PORT = 9517;

    private Board board;
    private SnakeGui gui;
    private int players = 0;

    public Server(Board board) {
        this.board = board;
        gui = new SnakeGui(board, 600, 600);
        gui.init();
        try {
            initServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler extends Thread {
        private BufferedReader reader;
        private HumanSnake snake;
        private ObjectOutputStream outputStream;

        public ClientHandler(Socket socket) throws IOException {
            connect(socket);
            snake = new HumanSnake(LocalBoard.NUM_SNAKES + players, board);
            board.addSnake(snake);
            snake.start();
            board.setChanged();
            players++;
        }

        public void run() {
            try {
                serverConnection();
            } catch (IOException ignored) {

            }
        }

        void connect(Socket socket) throws IOException {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        private void serverConnection() throws IOException {
            System.out.println("Started Listener and Sender");
            outputStream.writeObject(board);
            new Listener().start();
            new Sender().start();
        }

        private class Listener extends Thread {

            @Override
            public void run() {
                try {
                    while(true) {
                        int keyCode = Integer.parseInt(reader.readLine());
                        System.out.println("Keycode received " + keyCode);
                        snake.updateCurrentDirection(keyCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private class Sender extends Thread {

            @Override
            public void run() {
                while (true) {

                    try {
                        outputStream.reset();
                        outputStream.writeObject(board);
                        sleep(Board.REMOTE_REFRESH_INTERVAL);

                    }catch(Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    private void initServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        try {
            while(true) {
                Socket socket = serverSocket.accept();
                if(!board.isFinished()){
                    new ClientHandler(socket).start();
                    System.out.println("Client Handler started");
                }
            }

        } finally {
            serverSocket.close();
        }
    }



}
