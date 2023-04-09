package org.openjfx;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        Path currentDirectory = Paths.get("").toAbsolutePath();
        

        // Create rectangles for each cell on the chessboard
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile rect = new Tile(60, 60); // Each cell is 60x60 pixels
                rect.setFill(isWhite ? Color.web("#b58863") : Color.web("#f0d9b5"));
                grid.add(rect, col, row);

                ImageView chessPiece = new ImageView(new Image(currentDirectory + "/src/main/java/res/images/testpawn.png"));
                GridPane.setHalignment(chessPiece, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(chessPiece, javafx.geometry.VPos.CENTER);
                grid.add(chessPiece, col, row);

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
