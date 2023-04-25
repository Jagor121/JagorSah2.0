package org.openjfx.pieces;

public class King extends Piece{
    public King(String pathToImg, String color) {
        super(pathToImg, color);
        type = "King";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currenPieces, int currentRow, int currentColumn, int desiredRow,
            int desiredColumn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Move'");
    }

   
    
}
