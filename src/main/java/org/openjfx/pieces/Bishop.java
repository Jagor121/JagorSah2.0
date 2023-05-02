package org.openjfx.pieces;

public class Bishop extends Piece{
    public Bishop(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Bishop";
        //TODO Auto-generated constructor stub
    }

    public Bishop(){
        super();
        type = "Bishop";
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {

                //gleda dal je pomak sa strane jednak pomaku gore dolje tj. dal je dijagonalan move (i ce bit jedank j ako je dijagonalan, taj princip)
                if (Math.abs(currentRow - desiredRow) != Math.abs(currentCol - desiredCol)) {
                    return false;
                }
            
                // check that there are no pieces in the way
                // crna magija kopirana s interneta koju sam skuzio kak radi eventualno... jako je pametno i ja se ne bi ovog sjetio u milijun godina
                int rowDirection = (desiredRow - currentRow) > 0 ? 1 : -1;
                int colDirection = (desiredCol - currentCol) > 0 ? 1 : -1;
                int checkRow = currentRow + rowDirection;
                int checkCol = currentCol + colDirection;
                while (checkRow != desiredRow && checkCol != desiredCol) {
                    if (currentPieces[checkRow][checkCol] != null) {
                        return false;
                    }
                    checkRow += rowDirection;
                    checkCol += colDirection;
                }
     
                
                return true;
    }
    
}
