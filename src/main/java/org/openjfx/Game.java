package org.openjfx;

import org.openjfx.pieces.Piece;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
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

    public void addPiece(Group chessPieceLayer, Piece selectedPiece, int row, int col, GridPane grid){
        chessPieceLayer.getChildren().add(selectedPiece.getImg());
        GridPane.setConstraints(selectedPiece.getImg(), col, row);
        grid.getChildren().add(selectedPiece.getImg());
    }

    public void removePiece(){

    }

    public void moveAction(Piece[][] currentPieces, GridPane grid, Node currentNode, Group chessPieceLayer){
        int currentRow = 0, currentCol = 0;
        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces[i].length; j++) {
                try {
                    if(currentPieces[i][j].getSelected()){
                        currentRow = i;
                        currentCol = j;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        
        int desiredRow = GridPane.getRowIndex(currentNode);
        int desiredCol = GridPane.getColumnIndex(currentNode);
        Piece selectedPiece = currentPieces[currentRow][currentCol];

        addPiece(chessPieceLayer, selectedPiece, desiredRow, desiredCol, grid);
        selectedPiece.setSelected(false);
        Chessboard.isWhite = !Chessboard.isWhite;
        System.out.println(Chessboard.isWhite);
    }
    


    public void movementInitializer(Piece[][] currentPieces, Boolean isWhite, GridPane grid, Group chessPieceLayer){
        for (Node node : grid.getChildren()) {
            node.setOnMouseClicked(event -> {
                if(!thisIsReallyStupid(currentPieces)){ // gleda dal ima selektirani piece
                    Node currentNode = (Node) event.getSource();
                    moveAction(currentPieces, grid, currentNode, chessPieceLayer);

                }

            });
        }


        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                try {
                    piece.getImg().setOnMouseClicked(event -> {
                        Boolean correctColor = (Chessboard.isWhite && piece.getColor().equals("white")) || (!Chessboard.isWhite && !piece.getColor().equals("white"));
                        if (!piece.getSelected() && thisIsReallyStupid(currentPieces) && correctColor) { // Check if a chess piece has not been selected yet
                            ImageView selectedPieceIMG = (ImageView) event.getSource(); // Get the clicked chess piece
                            
                            // Store the selected chess piece in a variable for later use
      
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

    public void Logic(GridPane grid, Group chessPiecesLayer, Piece[][] currentPieces, Stage primaryStage, Scene scene) {

        movementInitializer(currentPieces, Chessboard.isWhite, grid, chessPiecesLayer); // initializes piece movement/selection capabilites

    }
}
