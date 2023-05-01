package org.openjfx.pieces;

public class Knight extends Piece{

    public Knight(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Knight";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currenPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {
                int rowDiff = Math.abs(currentRow - desiredRow);
                int colDiff = Math.abs(currentCol - desiredCol);
            

                if (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2) {
                    return true;
                } 
                 return false;
                
    }

}
