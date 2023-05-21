package org.openjfx.pieces;

public class Rook extends Piece {
    Boolean hasMoved = false;

    public Rook(String pathToImg, String color) {
        super(pathToImg, color);
        type = "Rook";
        //TODO Auto-generated constructor stub
    }

    public Rook(){
        super();
        type = "Rook";
    }

    @Override
    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentCol, int desiredRow,
            int desiredCol) {
            
            if (currentRow == desiredRow || currentCol == desiredCol) {
                // Check if there are any pieces blocking the rook's path
                int start, end;
                if (currentRow == desiredRow) {
                    // Moving along a row
                    start = Math.min(currentCol, desiredCol);
                    end = Math.max(currentCol, desiredCol);
                    for (int i = start + 1; i < end; i++) {
                        if (currentPieces[currentRow][i] != null) {
                            return false;
                        }
                    }
                } else {
                    // Moving along a column
                    start = Math.min(currentRow, desiredRow);
                    end = Math.max(currentRow, desiredRow);
                    for (int i = start + 1; i < end; i++) {
                        if (currentPieces[i][currentCol] != null) {
                            return false;
                        }
                    }
                }
             } else{
                return false;
             }


            return true;
            }

            public Boolean getHasMoved(){
                return hasMoved;
            }

            public void setHasMoved(){
                hasMoved = true;
            }
    
}
