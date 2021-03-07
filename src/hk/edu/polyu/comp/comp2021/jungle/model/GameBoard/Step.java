package hk.edu.polyu.comp.comp2021.jungle.model.GameBoard;

import static hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard.GameBoardBuilder;
import hk.edu.polyu.comp.comp2021.jungle.model.Pieces.Piece;

/**
 * model for a piece's movement
 */
public class Step {
    private final GameBoard gameBoard;
    private final Piece movedPiece;
    private final int destinationTileID;
    private final int cachedHashCode;
    private static final int HASH_NUM=31;
    /**
     * cached NullStep object used for reacting to invalid movement
     */
    public static final Step NULL_STEP = new NullStep();


    /**
     *
     * @return current gameBoard
     */
    public GameBoard getGameBoard(){return this.gameBoard;}

    /**
     *
     * @return the moving piece
     */
    public Piece getMovedPiece(){return this.movedPiece;}

    /**
     *
     * @return the destination of the moving piece
     */
    public int getDestinationTileID(){return this.destinationTileID;}

    /**
     *
     * @param gameBoard current gameBoard when making the movement
     * @param movedPiece the piece to be moved
     * @param destinationTileID the destination of the moving piece
     */
    public Step(final GameBoard gameBoard,
         final Piece movedPiece,
         final int destinationTileID){
        this.gameBoard=gameBoard;
        this.movedPiece=movedPiece;
        this.destinationTileID=destinationTileID;
        this.cachedHashCode=computeHashCode();
    }

    /**
     * execute the movement and construct a new gameBoard accordingly
     * @return a new gameBoard
     */
    public GameBoard operateStep(){
        final GameBoardBuilder builder = new GameBoardBuilder();

        int movedPiecePosition = this.movedPiece.getPiecePosition();

        for(final Piece piece : this.gameBoard.getPiecesDistribution()){
            if(piece.getPiecePosition()!=movedPiecePosition && piece.getPiecePosition()!=this.destinationTileID)
                builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));

        builder.setMoveMaker(this.gameBoard.getCurrentPlayer().getOpponent());

        return builder.build();
    }


    private int computeHashCode(){
        if(gameBoard==null)
            return -1;
        int result=0;
        result+=this.movedPiece.getPiecePosition();
        result=result*HASH_NUM+this.destinationTileID;
        return result;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }


    @Override
    public boolean equals(final Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Step))
            return false;
        final Step otherStep=(Step)other;
        return this.hashCode()==otherStep.hashCode();
    }


    /**
     * static factory method
     * @param gameBoard the current gameBoard when making the movement
     * @param pieceToMove the piece to be moved
     * @param destinationTileID the destination of the moving piece
     * @return a Step that follows the game rule
     */
    public static Step createStep(final GameBoard gameBoard, final Piece pieceToMove, final int destinationTileID){
        Step attemptStep = new Step(gameBoard, pieceToMove, destinationTileID);
        System.out.println("piece "+pieceToMove+" tried to move from "+pieceToMove.getPiecePosition()+" to "+ destinationTileID);
        for(Step possibleSteps : pieceToMove.calculateLegalSteps(gameBoard)) {
            if(attemptStep.equals(possibleSteps))
                return attemptStep;
        }
        return NULL_STEP;
    }

    /**
     * representation of an invalid movement
     */
    public static final class NullStep extends Step{
        /**
         * constructor
         */
        public NullStep(){
            super(null, null, -1);
        }
        @Override
        public GameBoard operateStep(){
            throw new RuntimeException("Illegal Move! Choose another step!");
        }
    }

}
