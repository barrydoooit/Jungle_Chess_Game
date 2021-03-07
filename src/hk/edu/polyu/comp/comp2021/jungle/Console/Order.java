package hk.edu.polyu.comp.comp2021.jungle.Console;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameUtils;
import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Storage;
import hk.edu.polyu.comp.comp2021.jungle.model.Pieces.Piece;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * process the order input of the cmd game
 */
class Order{
    private final static String[] ORDER_LIST={"save","open","move"};
    private GameBoard gameBoard;
    /**
     * the order
     */
    protected String order;

    /**
     *
     * @param gameBoard the current gameBoard
     * @param order the order entered by user
     */
    public Order(GameBoard gameBoard,String order){
        this.gameBoard = gameBoard;
        this.order = order;
    }

    /**
     * execute the order
     */
    public void runOrder(){
        if(order.length()<=4){
            System.out.println("Invalid order!");
        }else {
            String method = order.substring(0, 4).toLowerCase();
            if (method.equals(ORDER_LIST[0])) {
                try {
                    String filePath = order.substring((5));
                    Storage.store(gameBoard, filePath);
                }catch(Exception ex){
                    System.out.println("Save Failed!");
                }
            } else if (method.equals(ORDER_LIST[1])) {
                try {
                    String filePath = order.substring(5);
                    gameBoard = Storage.load(filePath, gameBoard);
                }catch (FileNotFoundException ex) {
                    System.out.println("File does not exist!");
                }catch (IOException ex) {
                    System.out.println("Failed to open file!");
                }catch(RuntimeException ex){
                    System.out.println("Failed to load file with illegal format!");
                }
            } else if (method.equals(ORDER_LIST[2])) {
                makeMove(order);
            } else {
                System.out.println("Invalid order!");

            }
        }
    }

    /**
     *
     * @return get the current gameBoard
     */
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    /**
     *
     * @param order the order entered by user
     */
    protected void makeMove(String order){
        try {
            int startTileID = GameUtils.stringToTileID(order.substring(5, 7));

            if (!gameBoard.isTileOccupied(startTileID)) {
                System.out.println("\nInvalid move: Chosen tile is empty. Make another call.\n");
            }
            int destinationTileID = GameUtils.stringToTileID(order.substring(8, 10));
            for (Piece pieceOnBoard : gameBoard.getPiecesDistribution()) {
                if (pieceOnBoard.getPiecePosition() == startTileID) {
                    try {
                        gameBoard = Step.createStep(gameBoard, pieceOnBoard, destinationTileID).operateStep();
                    } catch(NullPointerException ex){
                        System.out.println("That piece should not be move in this round!");
                    } catch(RuntimeException ex) {
                        System.out.println(ex);
                    }
                    break;
                }
            }
        }catch (Exception ex){
            System.out.println("Failed to make the movement!");
        }
    }
}
