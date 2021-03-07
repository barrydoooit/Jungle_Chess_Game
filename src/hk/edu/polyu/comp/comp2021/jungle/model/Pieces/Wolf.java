package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Wolf piece for Player X or Y
 */
public class Wolf extends Piece{

    /**
     *
     * @param position  the Wolf piece's tile id
     * @param pieceFaction the Wolf piece's faction (Player X or Y)
     */
    public Wolf(int position, Faction pieceFaction) {
        super(4, position, pieceFaction);
        this.setPieceType(PieceType.WOLF);
    }

    @Override
    public Piece movePiece(Step step) {
        return new Wolf(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wolf){
            Wolf anotherWolf=(Wolf) obj;
            if(anotherWolf.getPiecePosition()==this.getPiecePosition() && anotherWolf.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
