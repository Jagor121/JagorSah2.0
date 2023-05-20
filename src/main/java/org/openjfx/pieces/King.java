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

    public int castleChecker(King selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow, int desiredCol){
        //napravi rosado logic checker i stavi da returna mozda nesto slicno kao en passant da ne bude boolean nego int kao neki red ili specificni piece, skuzi kasnije
        
        if(currentRow != desiredRow || Math.abs(desiredCol-currentCol) !=2){ // gleda dal je move dva squarea i isti row
            return -1;
        }

        int leftRightCastle = (desiredCol - currentCol > 0) ? 7 : 0;
        int pieceColor = (selectedPiece.getColor().equals("white")) ? 7 : 0;
        Boolean samecolor = currentPieces[desiredRow][leftRightCastle].getColor().equals(selectedPiece.getColor());

        if(selectedPiece.gethasMoved() || !samecolor || !(currentPieces[desiredRow][leftRightCastle] instanceof Rook) || ((Rook)currentPieces[desiredRow][leftRightCastle]).getHasMoved()){
            return -1;  // gleda prvo dal se kralj pomako, onda dal su kralj i piece ista boja, onda dal je piece Rook, i na kraju ako se taj rook pomaknuo
        }

 
        int start = Math.min(currentCol, desiredCol);
        int end = Math.max(currentCol, desiredCol);
        for (int i = start +1 ; i < end; i++) {
            if(currentPieces[pieceColor][i] != null){
                return -1; // gleda ako ima pieceova izmedau kralja i rooka
            }
        }        

        return leftRightCastle;
    }

    public Boolean gethasMoved(){
        return hasMoved;
    }

    public void setHasMoved(){
        hasMoved = true;
    }
    
}
