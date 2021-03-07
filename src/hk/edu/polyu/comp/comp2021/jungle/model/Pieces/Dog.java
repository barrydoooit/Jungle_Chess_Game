package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Dog piece for Player X or Y
 */
public class Dog extends Piece{

    /**
     *
     * @param position  the Dog piece's tile id
     * @param pieceFaction the Dog piece's faction (Player X or Y)
     */
    public Dog(final int position, final Faction pieceFaction){
        super(3,position,pieceFaction);
        setPieceType(PieceType.DOG);
    }


    @Override
    public Piece movePiece(Step step) {
        return new Dog(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dog){
            Dog anotherDog=(Dog) obj;
            if(anotherDog.getPiecePosition()==this.getPiecePosition() && anotherDog.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
