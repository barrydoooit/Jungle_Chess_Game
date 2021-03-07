package hk.edu.polyu.comp.comp2021.jungle.model.GameBoard;

import hk.edu.polyu.comp.comp2021.jungle.model.Pieces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Frame model of a game board
 */
public final class GameBoard {
    private static final int ORIG_TILE_Y_C=12,ORIG_TILE_Y_D=8,ORIG_TILE_Y_E=20,ORIG_TILE_Y_L=16,
            ORIG_TILE_Y_I=0, ORIG_TILE_Y_R=14,ORIG_TILE_Y_T=6,ORIG_TILE_Y_W=18;
    private static final int ORIG_TILE_X_C=50,ORIG_TILE_X_D=54,ORIG_TILE_X_E=42,ORIG_TILE_X_L=46,
            ORIG_TILE_X_I=62,ORIG_TILE_X_R=48,ORIG_TILE_X_T=56,ORIG_TILE_X_W=44;

    private final List<Piece> piecesDistribution; // a list that contains all the pieces
    private final Faction currentPlayer;
    private final List<Integer> occupiedTileID; // a list that contains all the occupied tileID
    //private int[] PiecesInitialLocation={0,6,8,12,14,16,18,20,62,56,54,50,48,46,44,42}

    private GameBoard(GameBoardBuilder gameBoardBuilder){
        this.piecesDistribution=gameBoardBuilder.getExpectedPiecesDistribution();
        this.currentPlayer=gameBoardBuilder.getCurrentPlayer();
        this.occupiedTileID=gameBoardBuilder.getOccupiedTileID();
    }

    /**
     *
     * @return an original gameBoard
     */
    public static GameBoard setOriginalGameBoard(){
        final GameBoardBuilder builder = new GameBoardBuilder();
        builder.setPiece(new Tiger(ORIG_TILE_Y_T, Faction.Y));
        builder.setPiece(new Lion(ORIG_TILE_Y_I, Faction.Y));
        builder.setPiece(new Cat(ORIG_TILE_Y_C, Faction.Y));
        builder.setPiece(new Dog(ORIG_TILE_Y_D, Faction.Y));
        builder.setPiece(new Elephant(ORIG_TILE_Y_E, Faction.Y));
        builder.setPiece(new Wolf(ORIG_TILE_Y_W, Faction.Y));
        builder.setPiece(new Leopard(ORIG_TILE_Y_L, Faction.Y));
        builder.setPiece(new Rat(ORIG_TILE_Y_R, Faction.Y));

        builder.setPiece(new Tiger(ORIG_TILE_X_T, Faction.X));
        builder.setPiece(new Lion(ORIG_TILE_X_I, Faction.X));
        builder.setPiece(new Cat(ORIG_TILE_X_C, Faction.X));
        builder.setPiece(new Dog(ORIG_TILE_X_D, Faction.X));
        builder.setPiece(new Elephant(ORIG_TILE_X_E, Faction.X));
        builder.setPiece(new Wolf(ORIG_TILE_X_W, Faction.X));
        builder.setPiece(new Leopard(ORIG_TILE_X_L, Faction.X));
        builder.setPiece(new Rat(ORIG_TILE_X_R, Faction.X));

        builder.setMoveMaker(Faction.X);

        return builder.build();
    }

    /**
     *
     * @return a string representing the gameBoard, used for server-client data transfer
     */
    public String toMessage(){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<9;i++)
            for(int j = 0;j<7;j++)
                sb.append("-");
        for (Piece piece:piecesDistribution){
            int index = piece.getPiecePosition();
            String pieceAbbre = piece.getPieceFaction().isX()?piece.toString():piece.toString().toLowerCase();
            sb.replace(index,index+1, pieceAbbre);
        }
        sb.append(this.currentPlayer.toString());
        return sb.toString();
    }

    /**
     *
     * @param description the String that represents a gameBoard
     * @return the gameBoard built according to the description
     */
    public static GameBoard buildGameBoardFromString(String description){
        final GameBoardBuilder builder = new GameBoardBuilder();
        for(int i =0;i<GameUtils.GAMEBOARD_TILE_NUMBER;++i){
            if(description.charAt(i)!='-'){
                switch(description.charAt(i)){
                    case 'i':builder.setPiece(new Lion(i, Faction.Y));break;
                    case 't':builder.setPiece(new Tiger(i, Faction.Y));break;
                    case 'd':builder.setPiece(new Dog(i, Faction.Y));break;
                    case 'c':builder.setPiece(new Cat(i, Faction.Y));break;
                    case 'r':builder.setPiece(new Rat(i, Faction.Y));break;
                    case 'l':builder.setPiece(new Leopard(i, Faction.Y));break;
                    case 'w':builder.setPiece(new Wolf(i, Faction.Y));break;
                    case 'e':builder.setPiece(new Elephant(i, Faction.Y));break;

                    case 'E':builder.setPiece(new Elephant(i, Faction.X));break;
                    case 'W':builder.setPiece(new Wolf(i, Faction.X));break;
                    case 'L':builder.setPiece(new Leopard(i, Faction.X));break;
                    case 'R':builder.setPiece(new Rat(i, Faction.X));break;
                    case 'C':builder.setPiece(new Cat(i, Faction.X));break;
                    case 'D':builder.setPiece(new Dog(i, Faction.X));break;
                    case 'T':builder.setPiece(new Tiger(i, Faction.X));break;
                    case 'I':builder.setPiece(new Lion(i, Faction.X));
                }
            }
        }
        builder.setMoveMaker(description.charAt(GameUtils.GAMEBOARD_TILE_NUMBER)=='X'?Faction.X:Faction.Y);
        return builder.build();
    }

    /**
     *
     * @return faction of current player
     */
    public Faction getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     *
     * @return pieces' info on current gameBoard
     */
    public List<Piece> getPiecesDistribution(){return this.piecesDistribution;}

    /**
     *
     * @param tileID the ID of a tile
     * @return whether the tile with offered ID is occupied by a piece
     */
    public boolean isTileOccupied(int tileID){return occupiedTileID.contains(tileID);}

    @Override
    public String toString(){
        // transfer the gameboard to a string for the console to print out
        // for example, the original gameboard is:
        // Faction.X is denoted with upperCase letters, while Y is denoted with lowerCase
        //    T  -  -  -  -  -  I
        //    -  C  -  -  -  D  -
        //    E  -  W  -  L  -  R
        //    -  -  -  -  -  -  -
        //    -  -  -  -  -  -  -
        //    -  -  -  -  -  -  -
        //    e  -  t  -  l  -  r
        //    -  c  -  -  -  d  -
        //    t  -  -  -  -  -  i

        StringBuilder sb=new StringBuilder();
        int index=0;
        for (int i=0;i<9;i++){
            for(int j = 0;j<7;j++)
                sb.append(" - ");
            sb.append("\n");
        }
        for (Piece piece:piecesDistribution){
            int p=piece.getPiecePosition();
            int rowNumber=p/GameUtils.GAMEBOARD_COLUMN_NUMBER;
            int columnNumber=p%GameUtils.GAMEBOARD_COLUMN_NUMBER;
            index=(3*7+1)*rowNumber+3*columnNumber+1;
            String pieceType=piece.getPieceFaction().isX()?piece.toString():piece.toString().toLowerCase();
            sb.replace(index,index+1,pieceType);
        }
        return sb.toString();
    }

    /**
     *
     * @param TileID the ID of a tile
     * @return the piece on that tile
     */
    public Piece getPieceOnTile(int TileID){
        for(Piece pieceOnBoard : this.piecesDistribution) {
            if (pieceOnBoard.getPiecePosition() == TileID)
                return pieceOnBoard;
        }
        return Piece.RAW_PIECE;
    }

    /**
     *
     * @return judge that whether a winner has been generated
     */
    public Faction evaluateWinner(){
        int countCurrentPlayer=0,countOpponentPlayer=0;
        for (Piece piece:piecesDistribution){
            if (piece.getPieceFaction()==currentPlayer) countCurrentPlayer++;
            else countOpponentPlayer++;

            //if the piece is in its opponent's den, then this piece.Faction is the winner
            if (piece.getPiecePosition()==piece.getPieceFaction().getOpponent().getDenPosition()) return piece.getPieceFaction();
        }
        if (countCurrentPlayer>0 && countOpponentPlayer==0) return currentPlayer;
        //a method called getOpponentPlayer() in Faction should be added to getOpponentPlayer
        if (countCurrentPlayer==0 && countOpponentPlayer>0) return currentPlayer.getOpponent();

        //if winning condition not reached, return null
        return null;
    }

    /**
     * static builder class of GameBoard
     */
    public static class GameBoardBuilder{
        private List<Piece> expectedPiecesDistribution= new ArrayList(); // a list containing all the pieces
        private Faction currentPlayer;
        private List<Integer> occupiedTileID=new ArrayList();

        /**
         *
         * @return the piece distribution on the expected gameBoard
         */
        public List<Piece> getExpectedPiecesDistribution(){
            return expectedPiecesDistribution;
        }

        /**
         *
         * @return the faction of current player of the expected gameBoard
         */
        public Faction getCurrentPlayer(){
            return  currentPlayer;
        }

        /**
         *
         * @return the ID of the occupied tiles in the expected gameBoard
         */
        public List<Integer> getOccupiedTileID(){
            return occupiedTileID;
        }

        /**
         *
         * @param piece a piece to be set on the expected gameBoard
         * @return the builder
         */
        public GameBoardBuilder setPiece(Piece piece){
            expectedPiecesDistribution.add(piece);
            occupiedTileID.add(piece.getPiecePosition());
            return this;
        }

        /**
         *
         * @param player the faction of the player on turn in the expected gameBoard
         * @return the builder
         */
        public GameBoardBuilder setMoveMaker(Faction player){
            currentPlayer=player;
            return this;
        }

        /**
         * construct a gameBoard
         * @return a gameBoard
         */
        public GameBoard build(){
            return new GameBoard(this);
        }
    }
}
