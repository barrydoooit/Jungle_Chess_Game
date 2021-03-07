package hk.edu.polyu.comp.comp2021.jungle.model.Pieces;


import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameUtils;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Step;

/**
 * A Tiger piece for Player X or Y
 */
public class Tiger extends Piece{

    /**
     *
     * @param position the Tiger piece's tile id
     * @param pieceFaction the Tiger piece's faction (Player X or Y)
     */
    public Tiger(int position, Faction pieceFaction){
        super(6,position,pieceFaction);
        this.setPieceType(PieceType.TIGER);
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
        return new Tiger(step.getDestinationTileID(), step.getMovedPiece().getPieceFaction());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tiger){
            Tiger anotherTiger=(Tiger) obj;
            if(anotherTiger.getPiecePosition()==this.getPiecePosition() && anotherTiger.getPieceFaction()==this.getPieceFaction())
                return true;
        }
        return false;
    }
}
