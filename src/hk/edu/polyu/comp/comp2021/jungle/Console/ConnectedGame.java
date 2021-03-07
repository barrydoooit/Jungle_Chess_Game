package hk.edu.polyu.comp.comp2021.jungle.Console;

import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Client;
import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.User;
import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Server;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;

import java.io.IOException;
import java.util.Scanner;

/**
 * process of connected game
 */
public class ConnectedGame implements Runnable {
    private GameBoard gameBoard;
    private User user;
    private String opponentName;
    /**
     *
     * @param gameBoard the starting gameBoard
     */
    public ConnectedGame(final GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void run() {
        try {
            System.out.println("Please enter your nickName: ");
            Scanner scanner = new Scanner(System.in);
            String playerName = scanner.nextLine();
            System.out.println("Choose:\n\t1.Create Game\n\t2.Join Game");
            int game = scanner.nextInt();
            if (game == 1) {
                user = new Server(0);
                user.setPlayerName(playerName);
                user.sendMessage(gameBoard.toMessage());
            } else {
                String ip = "";
                int port = -1;
                while (port == -1) {
                    try {
                        port = 0;
                        System.out.println("Enter server IP: ");
                        scanner.nextLine();
                        ip = scanner.nextLine();
                        System.out.println("Enter Port num: ");
                        port = scanner.nextInt();
                        user = new Client(ip, port);
                        user.setPlayerName(playerName);
                        gameBoard = GameBoard.buildGameBoardFromString(user.readMessage());
                        System.out.println("Connected!");
                    } catch (Exception ex) {
                        System.out.println("Cannot establish client with given IP and port!");
                        port = -1;
                    }
                }
            }
            user.sendMessage(user.getPlayerName());
            opponentName = user.readMessage();
            System.out.println(gameBoard);
            if (user.getFaction() == Faction.Y) {
                System.out.println("\"You are the player with lower-case pieces.\nWaiting for opponent " + opponentName + "...");
                gameBoard = GameBoard.buildGameBoardFromString(user.readMessage());
                System.out.println(gameBoard);
            } else{
                System.out.println("You are the player with upper-case pieces.");
            }
            do {
                while (true) {
                    String command;
                    System.out.println("It's "+user.getPlayerName()+"'s turn.");
                    System.out.println("Order format: save/open [filename](e.g. save/open game1), move [fromPosition] [toPosition](e.g.move C7 C3)");
                    System.out.println("Enter order(save/open/move): ");
                    Scanner in = new Scanner(System.in);
                    command = in.nextLine();
                    Order order = new OnlineOrder(gameBoard, command);
                    order.runOrder();
                    if (!gameBoard.toString().equals(order.getGameBoard().toString())) {
                        gameBoard = order.getGameBoard();
                        break;
                    }
                }
                System.out.println(gameBoard);
                user.sendMessage(gameBoard.toMessage());
                if (gameBoard.evaluateWinner() != null) {
                    System.out.println("Congratulations! Side " + user.getFaction() + ", " + user.getPlayerName() + " won the game!");
                    user.disconnect();
                    break;
                }
                System.out.println("Waiting for opponent " + opponentName + "...");

                String newBoard = user.readMessage();
                gameBoard = GameBoard.buildGameBoardFromString(newBoard);

                System.out.println(gameBoard);
                if (gameBoard.evaluateWinner() != null) {
                    System.out.println("What a pity! Opponent Side, " + opponentName + " won the game!");
                    user.disconnect();
                    break;
                }
            } while (true);
        }catch(IOException ex){
            System.out.println("Connection Failed!");
        }
    }

}
