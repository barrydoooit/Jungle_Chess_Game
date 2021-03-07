package hk.edu.polyu.comp.comp2021.jungle.Console;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;

import java.util.Scanner;

/**
 * process of an offline game
 */
public class SingleGame implements Runnable{
    private GameBoard gameBoard;
    private String XName;
    private String YName;

    /**
     *
     * @param gameBoard the starting gameBoard
     */
    public SingleGame(final GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    @Override
    public void run(){
        Scanner in = new Scanner(System.in);
        System.out.println(gameBoard);
        System.out.println("Enter player X's (with upper-case pieces) name: ");
        XName = in.nextLine();
        System.out.println("Enter player Y's (with lower-case pieces) name: ");
        YName = in.nextLine();
        System.out.println("Game start!");
        while (gameBoard.evaluateWinner()==null){
            System.out.println(gameBoard);
            System.out.println((gameBoard.getCurrentPlayer()== Faction.X?XName:YName) + "\'s turn.");
            String command;
            System.out.println("Order format: save/open [filename](e.g. save/open game1), move [fromPosition] [toPosition](e.g.move C7 C3)");
            System.out.println("Enter order(save/open/move): ");
            command = in.nextLine();
            Order order = new Order(gameBoard, command);
            order.runOrder();
            gameBoard = order.getGameBoard();
        }
        System.out.println(gameBoard);
        System.out.println(gameBoard.evaluateWinner()== Faction.X?XName:YName + " won the game!");
    }
}
