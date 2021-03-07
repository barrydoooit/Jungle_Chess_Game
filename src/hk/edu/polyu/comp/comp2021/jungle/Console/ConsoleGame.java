package hk.edu.polyu.comp.comp2021.jungle.Console;

import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Storage;
import hk.edu.polyu.comp.comp2021.jungle.Console.ConnectedGame;
import hk.edu.polyu.comp.comp2021.jungle.Console.SingleGame;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;

import java.util.Scanner;

/**
 * main class to run the console game
 */
public class ConsoleGame {
    /**
     * run game on Console
     * @param args not used
     */
    public static void main(String[] args){
        runConsoleGame();
    }

    /**
     * run game on Console
     */
    public static void runConsoleGame() {
        boolean restart = true;
        while(restart) {
            System.out.println("Please choose:\n\t1.New Game\n\t2.Continue\n");
            Scanner menu = new Scanner(System.in);
            int game = menu.nextInt();

            GameBoard gameBoard;
            try {
                if(game == 1) {
                    gameBoard = GameBoard.setOriginalGameBoard();
                }else {
                    menu.nextLine();
                    System.out.println("Enter the file path of the saved game, ending in .properties: ");
                    String temp = menu.nextLine();
                    String filePath = temp.equals("") ? Storage.DEFAULT_STORAGE_FILE : temp;
                    gameBoard = Storage.load(filePath, null);
                    System.out.println("Save loaded!");
                }
            }catch(Exception ex){
                gameBoard = GameBoard.setOriginalGameBoard();
                System.out.println("Load failed. New game starts.");
            }
            System.out.println("Please choose player:\n\t1.SINGLE PLAYER\n\t2.MULTI-PLAYER");
            int playerMode = menu.nextInt();
            if (playerMode == 1)
                new SingleGame(gameBoard).run();
            else {
                new ConnectedGame(gameBoard).run();
            }
            System.out.println("continue? 0 to exit.\n");
            restart = menu.nextInt() != 0;
        }
    }
 }
