package org.openjfx.pieces;

public class King extends Piece{
    Boolean hasMoved = false;
    public King(String pathToImg, String color) {
        super(pathToImg, color);
        type = "King";
        //TODO Auto-generated constructor stub
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {
        
                if(Math.abs(currentCol - desiredCol) <= 1 && Math.abs(currentRow - desiredRow) <= 1){
                    setHasMoved();
                    return true;
                }
                return false;
    }

    public Boolean castleChecker(King selectedPiece, int currentRow, int currentCol, int desiredRow, int desiredCol){
        //napravi rosado logic checker i stavi da returna mozda nesto slicno kao en passant da ne bude boolean nego int kao neki red ili specificni piece, skuzi kasnije
        return true;
    }

    public Boolean gethasMoved(){
        return hasMoved;
    }

    public void setHasMoved(){
        hasMoved = true;
    }
    
}
