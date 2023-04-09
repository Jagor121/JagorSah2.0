package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Chessboard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        int size = 8; // Size of the chessboard
        boolean isWhite = true; // Flag to determine if the cell should be white or black

        // Create rectangles for each cell on the chessboard
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(60, 60); // Each cell is 60x60 pixels
                rect.setFill(isWhite ? Color.web("#b58863") : Color.web("#f0d9b5"));
                grid.add(rect, col, row);
                isWhite = !isWhite; // Toggle the flag for the next cell
            }
            isWhite = !isWhite; // Toggle the flag for the next row
        }

        Scene scene = new Scene(grid, 480, 480); // Set the chessboard scene size
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chessboard");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
