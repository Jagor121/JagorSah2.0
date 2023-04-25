package org.openjfx.pieces;

public class Knight extends Piece{

    public Knight(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Knight";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currenPieces, int currentRow, int currentColumn, int desiredRow,
            int desiredColumn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Move'");
    }

}
