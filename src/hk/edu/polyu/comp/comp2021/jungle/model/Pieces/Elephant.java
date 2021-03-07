package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;


/**
 * A Elephant piece for Player X or Y
 */
public class Elephant extends Piece{

    /**
     *
     * @param position  the Elephant piece's tile id
     * @param pieceFaction the Elephant piece's faction (Player X or Y)
     */
    public Elephant(final int position, final Faction pieceFaction){
        super(8,position,pieceFaction);
        setPieceType(PieceType.ELEPHANT);
    }

    @Override
    public Piece movePiece(Step step) {
        return new Elephant(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public int compareTo(Piece other){
        int otherRank=other.getRank();
        return (this.getRank()>=otherRank && otherRank!=1)?1:0;
    } //consider the situation to compare with Rat

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Elephant){
            Elephant anotherEle=(Elephant) obj;
            if(anotherEle.getPiecePosition()==this.getPiecePosition() && anotherEle.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
