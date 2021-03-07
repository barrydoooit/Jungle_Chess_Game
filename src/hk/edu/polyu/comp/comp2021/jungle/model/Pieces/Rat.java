package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;


import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameUtils;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Rat piece for Player X or Y
 */
public class Rat extends Piece{

    private boolean isInRiver;

    /**
     *
     * @param position the Rat piece's tile id
     * @param pieceFaction the Rat piece's faction (Player X or Y)
     */
    public Rat(int position, Faction pieceFaction){
        super(1,position,pieceFaction);
        this.setPieceType(PieceType.RAT);
        this.setInRiver(GameUtils.isRiver(position));
    }


    @Override
    public int findPosition(GameBoard gameBoard, int candidate){
        for(int i=0;i<9;i++){    //the pieces on left handside border cannot move towards left
            int id=i*7;
            if(getPiecePosition()==id && candidate==-1) return -1;
        }

        for(int i=0;i<9;i++){   //the pieces on right handside border cannot move towards right
            int id=6+i*7;
            if(getPiecePosition()==id && candidate==1) return -1;
        }
        int destinationPosition = getPiecePosition() +candidate;
        if(destinationPosition<0 || destinationPosition>GameUtils.GAMEBOARD_TILE_NUMBER-1) return -1;
        return destinationPosition;
    }

    @Override
    public Piece movePiece(Step step) {
        return new Rat(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public int compareTo(Piece other) {
        if(other instanceof Rat) {
            if(this.isInRiver() == ((Rat) other).isInRiver()) {
                return 1;
            }else {
                return 0;
            }
        }else if(other.getPieceType() ==PieceType.ELEPHANT && !this.isInRiver()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rat){
            Rat anotherRat=(Rat) obj;
            if(anotherRat.getPiecePosition()==this.getPiecePosition() && anotherRat.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
    /**
     *
     * @return whether the Rat is in the river or not
     */
    public boolean isInRiver() {
        return isInRiver;
    }

    /**
     *
     * @param inRiver set the Rat status(tile id in river or not)
     */
    public void setInRiver(boolean inRiver) {
        isInRiver = inRiver;
    }
}
