package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameUtils;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Lion piece for Player X or Y
 */
public class Lion extends Piece{
    /**
     *
     * @param position  the Lion piece's tile id
     * @param pieceFaction the Lion piece's faction (Player X or Y)
     */
    public Lion(final int position, final Faction pieceFaction){
        super(7,position,pieceFaction);
        setPieceType(PieceType.LION);
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
        if(GameUtils.isRiver(destinationPosition)){
            if(candidate==1 || candidate==-1) candidate *= 3;
            if(candidate==7 || candidate==-7) candidate *= 4;
            destinationPosition = getPiecePosition() +candidate;
            for(Piece findPiece : gameBoard.getPiecesDistribution()) {
                if(GameUtils.isRiver(findPiece.getPiecePosition())){
                    if (Math.abs(findPiece.getPiecePosition() - getPiecePosition())%7==0) {
                        return -1;
                    } else if(Math.abs(findPiece.getPiecePosition() - getPiecePosition())<=2 && Math.abs(findPiece.getPiecePosition() -destinationPosition)<=2){
                        return -1;
                    }
                }
            }
        }
        return destinationPosition;
    }

    @Override
    public Piece movePiece(Step step) {
        return new Lion(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lion){
            Lion anotherLion=(Lion) obj;
            if(anotherLion.getPiecePosition()==this.getPiecePosition() && anotherLion.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }

}
