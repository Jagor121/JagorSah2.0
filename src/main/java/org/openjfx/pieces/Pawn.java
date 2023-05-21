package org.openjfx.pieces;

import org.openjfx.Game;

public class Pawn extends Piece {
    Boolean isFirstMove = true;

    public Pawn(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Pawn";

    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {

                int direction = (selectedPiece.getColor().equals("white")) ? -1 : 1;

                if (currentCol != desiredCol) {
                    // Check if the pawn is capturing a piece diagonally
                    if (Math.abs(currentCol - desiredCol) == 1 && desiredRow == currentRow + direction && currentPieces[desiredRow][desiredCol] != null) {
                        ((Pawn) selectedPiece).setFirstMove();
                        Game.lastPawnMoveWasTwoSquares = false;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // Check if the pawn is moving one or two squares forward
                    if (desiredRow == currentRow + direction && currentPieces[desiredRow][desiredCol] == null) {
                        ((Pawn) selectedPiece).setFirstMove();
                        Game.lastPawnMoveWasTwoSquares = false;
                        return true;
                    } else if (desiredRow == currentRow + 2 * direction && ((Pawn) selectedPiece).getFirstMove() && currentPieces[desiredRow][desiredCol] == null) {
                        // Check if the pawn is making its first move and moving two squares forward
                        ((Pawn) selectedPiece).setFirstMove();
                        Game.lastPawnMoveWasTwoSquares = true;
                        return true;
                    } 

                        return false;
                    
                }
    }

    public int enPassantChecker(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
    int desiredCol){
        int enPassantRow = (selectedPiece.getColor().equals("white")) ? 3 : 4; //odreduje red en passanta oviseci o boji

        if (Math.abs(desiredCol - currentCol) == 1 && currentRow == enPassantRow) {
            // check if the en passant capture is valid
            if (Game.lastPawnMoveWasTwoSquares && currentPieces[enPassantRow][desiredCol] instanceof Pawn && !currentPieces[enPassantRow][desiredCol].getColor().equals(selectedPiece.getColor())) {
                // play some epic sound
                return enPassantRow;
            }
        }
        return -1;
    }
    
    public Boolean Hamburger(Pawn selectedPawn, int desiredRow){ // zove se hamburger jer sam izgubio smislenost, gleda dal pijun zeli ic do kraja boarda
        Boolean isWhite = selectedPawn.getColor().equals("white");
        
        if((isWhite && 0 == desiredRow) || (!isWhite && 7 == desiredRow)){
            return true;
        }


        return false;
    }

    public void setFirstMove(){
        isFirstMove = false;
    }

    public Boolean getFirstMove(){
        return isFirstMove;
    }
}
