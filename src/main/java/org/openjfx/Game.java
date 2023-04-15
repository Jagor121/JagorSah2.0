package org.openjfx;

import org.openjfx.pieces.Piece;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Game{

    public Boolean thisIsReallyStupid(Piece[][] currentPieces){ // this is so unimaginably stupid i hope to god nobody sees this
        int selected = 0;
        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                try {
                    if (piece.getSelected()) {
                        selected++;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        if(selected == 0){
            return true;
        }
        return false;  // ova metoda gleda svaki piece i "omogucuje" da bude samo jedan selektiran piece odjednom
    }

    public void movementHandler(Piece[][] currentPieces, Boolean isWhite){
        
        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                try {
                    piece.getImg().setOnMouseClicked(event -> {
                        if (!piece.getSelected() && thisIsReallyStupid(currentPieces)) { // Check if a chess piece has not been selected yet
                            ImageView selectedPieceIMG = (ImageView) event.getSource(); // Get the clicked chess piece
                            
                            // Store the selected chess piece in a variable for later use
                            // ...
                            System.out.println("test");
                            piece.setSelected(true); // Set the selected flag to true
                        }
                    });
                } catch (Exception e) {
                    continue;
                }
            }
        }



    }

    public void Logic(GridPane grid, Group chessPiecesLayer, Piece[][] currentPieces, Stage primaryStage, Scene scene, Boolean isWhite) {

        movementHandler(currentPieces, isWhite); 

    }
}
