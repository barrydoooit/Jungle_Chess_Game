package hk.edu.polyu.comp.comp2021.jungle.Console;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Storage;

/**
 * process the order input of the online cmd game
 */
public class OnlineOrder extends Order {

    /**
     * @param gameBoard current gameBoard
     * @param order order entered by user
     */
    public OnlineOrder(GameBoard gameBoard, String order){
        super(gameBoard,order);
    }
    @Override
    public void runOrder(){
        if(order.length()<=4){
            System.out.println("Invalid order!");
        }else {
            String method = order.substring(0, 4).toLowerCase();
            if (method.equals("save")) {
                String filePath = order.substring((5));
                try {
                    Storage.store(getGameBoard(), filePath);
                }catch(Exception ex){
                    System.out.println("Save failed!");
                }
            } else if (method.equals("move")) {
                makeMove(order);
            } else {
                System.out.println("Invalid order!");
            }
        }
    }
}
