package org.openjfx;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.openjfx.pieces.*;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Chessboard extends Application {

     public void pieceSetup(int size, GridPane grid, boolean isWhite, Group chessPieceLayer){
         Path currentDirectory = Paths.get("").toAbsolutePath(); //relative paths do not like me so i had to do this arcane magic for it to work
         String molimte = currentDirectory + "/src/main/java/res/images/pieces/blackrook.png";
         for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                Piece piece = new Rook(molimte, "black");  // pieceSetupParser(row,col,isWhite);
                chessPieceLayer.getChildren().add(piece.getImg());
                GridPane.setConstraints(piece.getImg(), col, row);
                grid.getChildren().add(piece.getImg());
                
                // GridPane.setHalignment(piece.getImg(), javafx.geometry.HPos.CENTER);
                // GridPane.setValignment(piece.getImg(), javafx.geometry.VPos.CENTER);
                // chessPieceLayer.getChildren().add(piece.getImg());
                // chessPieceLayer.add(piece.getImg(), col, row);

            }
        }
     }

    //  public Piece pieceSetupParser(int row, int col, boolean isWhite){
    //       Piece piece = new Piece(row, col, isWhite);

    //      return piece;
    //  }

    public void tileSetup(GridPane grid, int size, boolean tileColor){
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile rect = new Tile(60, 60); // Each cell is 60x60 pixels
                rect.setFill(tileColor ? Color.web("#f0d9b5") : Color.web("#b58863"));
                grid.add(rect, col, row);

                // pieceSetup(row, col, grid, rect, isWhite);
                

                tileColor = !tileColor; // Toggle the flag for the next cell
            }
            tileColor = !tileColor; // Toggle the flag for the next row
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        int size = 8; // Size of the chessboard
        boolean isWhite = true; // determines if you're playing as white or black/orientation of the board
        boolean tileColor = isWhite; //determines colors of the tiles
        Group chessPiecesLayer = new Group();
        
        tileSetup(grid, size, tileColor); // Create rectangles for each cell on the chessboard
        grid.getChildren().add(chessPiecesLayer); // creates a new "layer for the pieces"... i think??... makes each grid tile part of the group chessPiecesLayer?
        pieceSetup(size, grid,isWhite, chessPiecesLayer);
        


        Scene scene = new Scene(grid, 480, 480); // Set the chessboard scene size
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Chessboard");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
