package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Leopard piece for Player X or Y
 */
public class Leopard extends Piece{

    /**
     *
     * @param position  the Leopard piece's tile id
     * @param pieceFaction the Leopard piece's faction (Player X or Y)
     */
    public Leopard(final int position, final Faction pieceFaction){
        super(5,position,pieceFaction);
        setPieceType(PieceType.LEOPARD);
    }


    @Override
    public Piece movePiece(Step step) {
        return new Leopard(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Leopard){
            Leopard anotherLeo=(Leopard) obj;
            if(anotherLeo.getPiecePosition()==this.getPiecePosition() && anotherLeo.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
