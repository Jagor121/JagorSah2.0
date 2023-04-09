package org.openjfx;

public class Tile extends javafx.scene.shape.Rectangle {
    private int row;
    private int col;

    public Tile(double width, double height) {
        super(width, height);
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
}

