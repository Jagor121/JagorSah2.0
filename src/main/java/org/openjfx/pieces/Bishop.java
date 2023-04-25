package org.openjfx.pieces;

public class Bishop extends Piece{
    public Bishop(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Bishop";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currenPieces, int currentRow, int currentColumn, int desiredRow,
            int desiredColumn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Move'");
    }
    
}
