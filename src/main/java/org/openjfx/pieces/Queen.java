package org.openjfx.pieces;

public class Queen extends Piece {
    public Queen(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Queen";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {

         if(new Rook().Move(selectedPiece, currentPieces, currentRow, currentCol, desiredRow, desiredCol)){
            return true;
         }

         if(new Bishop().Move(selectedPiece, currentPieces, currentRow, currentCol, desiredRow, desiredCol)){
            return true;
         }

        return false;
    }

    
    
}
