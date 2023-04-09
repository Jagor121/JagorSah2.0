package org.openjfx;

public class Tile extends javafx.scene.shape.Rectangle {
    private int row;
    private int col;
    private Boolean hasPiece;

    public Tile(double width, double height) {
        super(width, height);
        hasPiece = false;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setHasPiece(Boolean hasPiece){
        this.hasPiece = hasPiece;
    }
}

