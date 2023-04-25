package org.openjfx.pieces;

public class Queen extends Piece {
    public Queen(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Queen";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currenPieces, int currentRow, int currentColumn, int desiredRow,
            int desiredColumn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Move'");
    }

    
    
}
