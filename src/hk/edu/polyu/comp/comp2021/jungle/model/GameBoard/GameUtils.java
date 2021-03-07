package hk.edu.polyu.comp.comp2021.jungle.model.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Miscellaneous methods of the GameBoard
 */
public final class GameUtils {

    /**
     * @throws RuntimeException the constructor should not be called
     */
    private GameUtils() {
        throw new RuntimeException(("Cannot be instantiated!"));
    }
    /**number of columns in the game board*/
    public static final int GAMEBOARD_COLUMN_NUMBER = 7;
    /**number of rows in the game board*/
    public static final int GAMEBOARD_ROW_NUMBER = 9;
    /**number of tiles in the game board*/
    public static final int GAMEBOARD_TILE_NUMBER = 63;
    /**all possible tile ids in the river*/
    public static final int[] RIVER={22,23,25,26,29,30,32,33,36,37,39,40};
    private static final String[] column={"A","B","C","D","E","F","G"};
    /**all possible routes across the river(represented as an integer combination of startingTileID and destinationTileID)*/
    public static final List<Integer> crossRiverRoutes = new ArrayList<>(Arrays.asList(1543,1644,1846,1947,2124,2421,2427,2724,2831,3128,3134,3431,3835,3538,3841,4138,4315,4416,4618,4719));

    /**
     *
     * @param startingTileID  TileID where the route starts
     * @param destinationTileID TileID where the route ends
     * @return boolean which determines whether the move is to cross the river by the startingTileID and destinationTileID
     */
    public static Boolean isJumpingCrossRiver(int startingTileID, int destinationTileID){
        Integer i=startingTileID*100+destinationTileID;
        return crossRiverRoutes.contains(i);
    }

    /**
     * @param TileID int the input tileID
     * @return whether this tile is river
     */
    public static Boolean isRiver(int TileID){
        for (int i=0;i<RIVER.length;i++){
            if (TileID==RIVER[i]) return true;
        }
        return false;
    }

    /**
     * @param tileID int the input tileID
     * @return String that represents the tile index
     */
    public static String tileIDToString(final int tileID){
        if (tileID>=0 && tileID<GAMEBOARD_TILE_NUMBER){
            int rowNumber=GAMEBOARD_ROW_NUMBER-tileID/GAMEBOARD_COLUMN_NUMBER;
            int columnNumber=tileID%GAMEBOARD_COLUMN_NUMBER;
            return column[columnNumber]+""+rowNumber;
        }
        return null;
    }

    /**
     * @param position String the input tile index
     * @return int that represents the tileID
     */
    public static int stringToTileID(final String position){
        String[] str=position.split("");
        int rowNumber,columnNumber=0;
        for (int i=0;i<column.length;i++){
            if (column[i].equals(str[0])){
                columnNumber=i;
            }
        }
        rowNumber=GAMEBOARD_ROW_NUMBER-Integer.parseInt(str[1]);
        return rowNumber*GAMEBOARD_COLUMN_NUMBER+ columnNumber;
    }

    /**
     * @param tileAID tileID of certain tile
     * @param tileBID tileID of the other tile
     * @return whether the two input tile is in the same column
     */
    private static boolean isInSameColumn(int tileAID, int tileBID){
        return tileIDToString(tileAID).charAt(0)==tileIDToString(tileBID).charAt(0);
    }

    /**
     * @param tileAID tileID of certain tile
     * @param tileBID tileID of the other tile
     * @return whether the two input tile is in the same row
     */
    private static boolean isInSameRow(int tileAID, int tileBID){
        return tileIDToString(tileAID).charAt(1)==tileIDToString(tileBID).charAt(1);
    }

    /**
     * @param tileAID tileID of certain tile
     * @param tileBID tileID of the other tile
     * @return whether the two input tile is adjacent
     */
    public static boolean isAdjacent(int tileAID, int tileBID){
        if (tileAID==tileBID) return false;
        if (isInSameColumn(tileAID,tileBID)) return Math.abs(tileAID-tileBID)==7;
        if (isInSameRow(tileAID,tileBID)) return Math.abs(tileAID-tileBID)==1;
        return false;
    }
}
