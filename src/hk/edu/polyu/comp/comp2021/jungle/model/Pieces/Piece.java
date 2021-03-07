package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameUtils;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * A piece of any type
 */
public abstract class Piece implements Comparable<Piece>{
    private PieceType pieceType;
    /**
     * the rank of the piece in the game
     */
    protected int rank;
    private int piecePosition;
    private final Faction pieceFaction;
    /**
     * a raw piece object
     */
    public final static Piece RAW_PIECE = new RawPiece();
    /**
     *
     * @param rank the piece's rank in the game
     * @param position the piece location(tile id)
     * @param pieceFaction the piece's faction (Player X or Y)
     */
    Piece(final int rank, final int position, final Faction pieceFaction){
        this.setPieceType(PieceType.BLANK);
        this.rank=rank;
        this.setPiecePosition(position);
        this.pieceFaction = pieceFaction;
    }

    /**
     * Possible differences of startTileId and endTileId when moving horizontally/vertically one tile
     */
    protected final static int[] POSSIBLE_MOVES = {-7, -1, 1, 7}; //tileId与这4个数进行加法运算以进行上下左右移动

    /**
     *
     * @param gameBoard the current game status
     * @return a list of Class Step that the piece can go
     */
    public List<Step> calculateLegalSteps(GameBoard gameBoard) {
        List<Step> legalMoves = new ArrayList<>();
        if(this.getPieceFaction() !=gameBoard.getCurrentPlayer()) return null;
        for(int currentCandidate : POSSIBLE_MOVES){
            //get destination position
            int destinationPosition = findPosition(gameBoard, currentCandidate);
            if(destinationPosition==-1) continue;
            //find the piece on the destination position
            Piece destinationPiece = gameBoard.getPieceOnTile(destinationPosition);

            //if the destination is null, and is not opponent's den
            if(destinationPiece==RAW_PIECE && destinationPosition!= getPieceFaction().getDenPosition()){
                legalMoves.add(new Step(gameBoard,this,destinationPosition));
            }
            //if the attack piece is attackable(is opponent's piece)
            else if (destinationPiece!=RAW_PIECE && getPieceFaction() != destinationPiece.getPieceFaction()) {
                if(this.compareTo(destinationPiece)>0){
                    legalMoves.add(new Step(gameBoard,this,destinationPosition));
                }
            }
        }
        return legalMoves;
    }

    /**
     *
     * @param gameBoard the current game status
     * @param candidate a possible different between startTileId and endTileId
     * @return -1 if the piece's move is not valid, endTileId if valid
     */
    public int findPosition(GameBoard gameBoard, int candidate) {
        for(int i=0;i<9;i++){    //the pieces on left handside border cannot move towards left
            int id=i*7;
            if(getPiecePosition()==id && candidate==-1) return -1;
        }

        for(int i=0;i<9;i++){   //the pieces on right handside border cannot move towards right
            int id=6+i*7;
            if(getPiecePosition()==id && candidate==1) return -1;
        }

        int destinationPosition = getPiecePosition() +candidate;
        if(destinationPosition<0 || destinationPosition> GameUtils.GAMEBOARD_TILE_NUMBER-1) return -1;
        if(GameUtils.isRiver(destinationPosition)) return -1;
        return destinationPosition;
    }

    /**
     *
     * @param step indicating which piece moves to which tile
     * @return create a new piece of same type (moving is actually creating new pieces)
     */
    public abstract Piece movePiece(Step step);

    @Override
    public int compareTo(Piece other){return this.getRank()>=other.getRank()?1:0;}

    public String toString(){
        return getPieceFaction()==Faction.X?this.getPieceType().toString():this.getPieceType().toString().toLowerCase();
    }

    /**
     *
     * @param piecePosition set the piece's tile id
     */
    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
    }

    /**
     *
     * @param pieceType set the piece's type
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    /**
     * A collection of possible piece types
     */
    public enum PieceType {
        /**
         * rat
         */
        RAT("R"),
        /**
         * cat
         */
        CAT("C"),
        /**
         * dog
         */
        DOG("D"),
        /**
         * wolf
         */
        WOLF("W"),
        /**
         * leopard
         */
        LEOPARD("L"),
        /**
         * tiger
         */
        TIGER("T"),
        /**
         * lion
         */
        LION("I"),
        /**
         * elephant
         */
        ELEPHANT("E"),
        /**
         * raw piece
         */
        BLANK("B");
        private String pieceName;
        PieceType(String pieceName){this.pieceName=pieceName;}

        @Override
        public String toString(){return pieceName;}
    }

    /**
     *
     * @return type of the piece
     */
    public PieceType getPieceType(){return pieceType;}

    /**
     *
     * @return rank of the piece(return 0 if the piece is in the trap)
     */
    public int getRank(){
        for(int i: Faction.X.getTrapPositions()){
            if(i==getPiecePosition()) return 0;
        }
        for(int i: Faction.Y.getTrapPositions()){
            if(i==getPiecePosition()) return 0;
        }
        return rank;
    }

    /**
     *
     * @return the piece's tile id
     */
    public int getPiecePosition(){return this.piecePosition;}

    /**
     *
     * @return the piece's faction (Player X or Y)
     */
    public Faction getPieceFaction(){return this.pieceFaction;}

    /**
     * use a raw piece to replace a "null" piece
     */
    public static final class RawPiece extends Piece{

        /**
         * a blank piece
         */
        public RawPiece() {
            super(-1, -1, null);
            setPieceType(PieceType.BLANK);
        }
        @Override
        public Piece movePiece(Step step) {
            return this;
        }
    }
}
