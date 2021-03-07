package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Cat piece for Player X or Y
 */
public class Cat extends Piece{//other than Tiger,Lion,Elephant,Rat,the rest are the same with Cat

    /**
     *
     * @param position  the Cat piece's tile id
     * @param pieceFaction the Cat piece's faction (Player X or Y)
     */
    public Cat(final int position, final Faction pieceFaction){
        super(2,position,pieceFaction);
        setPieceType(PieceType.CAT);
    }


    @Override
    public Piece movePiece(Step step) {
        return new Cat(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cat){
            Cat anotherCat=(Cat) obj;
            if(anotherCat.getPiecePosition()==this.getPiecePosition() && anotherCat.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
