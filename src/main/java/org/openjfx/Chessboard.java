package org.openjfx;

import org.openjfx.pieces.*;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chessboard extends Application {
    static BooleanProperty isWhite = new SimpleBooleanProperty(true);// determines if you're playing as white or black/orientation of the board
    // static boolean isWhite = true; 
    static boolean tileColor = isWhite.get(); //determines colors of the tiles
    static int size = 8;
    static String tileColor1 = "#f0d9b5";
    static String tileColor2 = "#b58863";
    static String tileColor1Ac = "#f7ec59";
    static String tileColor2Ac = "#dac331";

     public void pieceSetup(int size, GridPane grid,  Group chessPieceLayer, Piece[][] currentPieces){
        pieceSetupParser(currentPieces);
    
         for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                
                try {
                    chessPieceLayer.getChildren().add(currentPieces[row][col].getImg());
                    GridPane.setConstraints(currentPieces[row][col].getImg(), col, row);
                    grid.getChildren().add(currentPieces[row][col].getImg());
                } catch (Exception e) {
                    continue;
                }    
    
            }
        }
     }

     public static void rotate180(Piece[][] matrix) { // stavi u game.java kasnije molim te tako da rotira board i pieces
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Reverse rows
        for (int i = 0; i < rows; i++) {
            int left = 0;
            int right = cols - 1;
            while (left < right) {
                Piece temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }

        // Reverse columns
        int top = 0;
        int bottom = rows - 1;
        while (top < bottom) {
            for (int j = 0; j < cols; j++) {
                Piece temp = matrix[top][j];
                matrix[top][j] = matrix[bottom][j];
                matrix[bottom][j] = temp;
            }
            top++;
            bottom--;
        }
    }

      public void pieceSetupParser(Piece[][] currentPieces){
        // originalno sam imao cijelu kul ideju raditi ovo sa json-om ali to je apsolutno propalo i unistilo moje mentalno stanje pa evo nas sad ovdje
            currentPieces[0][0] = new Rook("blackrook.png","black");
            currentPieces[0][1] = new Knight("blackknight.png","black");
            currentPieces[0][2] = new Bishop("blackbishop.png","black");
            currentPieces[0][3] = new Queen("blackqueen.png","black");
            currentPieces[0][4] = new King("blackking.png","black");
            currentPieces[0][5] = new Bishop("blackbishop.png","black");
            currentPieces[0][6] = new Knight("blackknight.png","black");
            currentPieces[0][7] = new Rook("blackrook.png","black");

            for (int i = 0; i < 8; i++) {
                currentPieces[1][i] = new Pawn("blackpawn.png","black");
            }

            currentPieces[7][0] = new Rook("whiterook.png","white");
            currentPieces[7][1] = new Knight("whiteknight.png","white");
            currentPieces[7][2] = new Bishop("whitebishop.png","white");
            currentPieces[7][3] = new Queen("whitequeen.png","white");
            currentPieces[7][4] = new King("whiteking.png","white");
            currentPieces[7][5] = new Bishop("whitebishop.png","white");
            currentPieces[7][6] = new Knight("whiteknight.png","white");
            currentPieces[7][7] = new Rook("whiterook.png","white");

            for (int i = 0; i < 8; i++) {
             currentPieces[6][i] = new Pawn("whitepawn.png","white");
            }

           if(!isWhite.get()){
            rotate180(currentPieces);
           }

      }

     
      

    public void tileSetup(GridPane grid, int size, boolean tileColor){
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile rect = new Tile(60, 60); // Each cell is 60x60 pixels
                rect.setFill(tileColor ? Color.web(tileColor1) : Color.web(tileColor2));
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
        
        
        Group chessPiecesLayer = new Group();
        Piece[][] currentPieces = new Piece[8][8]; // piece array logika

        tileSetup(grid, size, tileColor); // Create rectangles for each cell on the chessboard
        grid.getChildren().add(chessPiecesLayer); // creates a new "layer for the pieces"... i think??... makes each grid tile part of the group chessPiecesLayer?
        pieceSetup(size, grid,chessPiecesLayer, currentPieces);
        


        Scene scene = new Scene(grid, 480, 480); // Set the chessboard scene size
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("Chessboard");
        primaryStage.show();
        
        Game game = new Game();
        game.Logic(grid, chessPiecesLayer, currentPieces, primaryStage, scene); // bog zna zasto ovo ovako radim ali tako sam odlucio

        // isWhite.addListener((observable, oldValue, newValue) -> {
            
        //      chessPiecesLayer.getChildren().clear();
        //      grid.getChildren().clear();
        //      tileSetup(grid, size, tileColor); // Create rectangles for each cell on the chessboard
        //      pieceSetup(size, grid,chessPiecesLayer, currentPieces);

        //      game.movementHandler(currentPieces, isWhite, grid, chessPiecesLayer);
        // });


    
        // for (int i = 0; i < currentPieces.length; i++) {
        //     for (int j = 0; j < currentPieces.length; j++) {
        //         System.out.print(currentPieces[i][j]);
        //     }
        //     System.out.println();
        // }

    }

    public static Boolean getPlayer(){
        return isWhite.get();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
