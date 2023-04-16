package org.openjfx;


import org.openjfx.pieces.Piece;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game{

    public Boolean thisIsReallyStupid(Piece[][] currentPieces){ // this is so unimaginably stupid i hope to god nobody sees this
        int selected = 0;  // ova metoda gleda svaki piece i "omogucuje" da bude samo jedan selektiran piece odjednom
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
        return false; 
    }

    public void thisIsAlsoReallyStupid(Piece[][] currentPieces){ // ova metoda gleda kroz svaki piece i kada nade selektirani promijeni selected varijablu u false
        for (Piece[] pieces : currentPieces) {              // vrlo optimiziran kod, ja sam siguran da 5% cpu usagea je jako malo za sah sigurno aha
            for (Piece piece : pieces) {
                try {
                    if(piece.getSelected()){
                        piece.setSelected(false);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    
    public void addPiece(Group chessPieceLayer, Piece selectedPiece, int row, int col, GridPane grid){
        chessPieceLayer.getChildren().add(selectedPiece.getImg());
        GridPane.setConstraints(selectedPiece.getImg(), col, row);
        grid.getChildren().add(selectedPiece.getImg());
    }

    public void removePiece(Group chessPieceLayer, Piece potentialDead, int row, int col, GridPane grid){
        chessPieceLayer.getChildren().remove(potentialDead.getImg());
        grid.getChildren().remove(potentialDead.getImg());
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
        try {
            Piece potentialDeadPiece = currentPieces[desiredRow][desiredCol];
            currentPieces[desiredRow][desiredCol] = null;
            removePiece(chessPieceLayer, potentialDeadPiece, desiredRow, desiredCol, grid); // jedenje protivnickih pieceova
            
        } catch (Exception e) {
            System.out.println("nisi pojeo nista");
        }
        
        addPiece(chessPieceLayer, selectedPiece, desiredRow, desiredCol, grid);
        currentPieces[desiredRow][desiredCol] = selectedPiece;
        currentPieces[currentRow][currentCol] = null;

        recentMoveShowReset(grid);
        Tile tile = currentPieceTileLocator(grid, currentPieces);
        Color color = (Color) tile.getFill();
        Boolean colorType = color.equals(Color.web(Chessboard.tileColor1));
        colorChanger(colorType, tile);

        selectedPiece.setSelected(false);
        Chessboard.isWhite = !Chessboard.isWhite;
    }

    public void colorChanger(Boolean colorType, Tile tile){
        try {
            if(colorType){
                tile.setFill(Color.web(Chessboard.tileColor1Ac));
            }else{
                tile.setFill(Color.web(Chessboard.tileColor2Ac));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void colorRevert(Boolean colorType, Tile tile){
        try {
            if(colorType){
                tile.setFill(Color.web(Chessboard.tileColor1));
            }else{
                tile.setFill(Color.web(Chessboard.tileColor2));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

   
    public Tile currentPieceTileLocator(GridPane grid, Piece[][] currentPieces){ // jako inefficient ali sta se moze
        int col = 0;
        int row = 0;
        Tile tile = new Tile();
        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces[i].length; j++) {
                try {
                    if (currentPieces[i][j].getSelected()) {
                        row = i;
                        col = j;
                    }
                } catch (Exception e) {

                    continue;
                }
            }
        }
        for (Node node : grid.getChildren()) { // uzasno odvratno trosi processing time
            if (node instanceof Tile && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                tile = (Tile) node;
            }
        }
 
      return tile;
    }

    public void recentMoveShowReset(GridPane grid){
        int count = 0;
        Boolean tileColor = Chessboard.tileColor;
        for (Node Node : grid.getChildren()) { // ovaj loop cleara cijeli board od recent move indikatora
            if (Node instanceof Tile){
                Tile tile1 = (Tile) Node;
                if (tileColor) {
                    tile1.setFill(Color.web(Chessboard.tileColor1));
                } else if (!tileColor){
                    tile1.setFill(Color.web(Chessboard.tileColor2)); 
                }
                count++;
                tileColor = !tileColor; // Toggle the flag for the next cell 
                if(count % 8 == 0){
                    tileColor = !tileColor;
                }
            }
        }
    }

    public void recentMove(Tile tile, GridPane grid){
        Color color = (Color) tile.getFill();
        Boolean colorType = color.equals(Color.web(Chessboard.tileColor1));
        recentMoveShowReset(grid);
        

        colorChanger(colorType, tile); // metoda za mijenjanje individualnog tilea
    }

    public void pause(int millis){
        PauseTransition pause = new PauseTransition(Duration.millis(millis));
                        pause.setOnFinished(e -> {
                        System.out.println("Action after delay");
                        });
                        pause.play();

    }

    public void movementHandler(Piece[][] currentPieces, Boolean isWhite, GridPane grid, Group chessPieceLayer){
        for (Node node : grid.getChildren()) {
            node.setOnMouseClicked(event2 -> {
                if(!thisIsReallyStupid(currentPieces)){ // gleda dal ima selektirani piece
                    Node currentNode = (Node) event2.getSource();
                    Tile tile = (Tile) event2.getSource();
                    
                    recentMove(tile, grid);
                    
                    moveAction(currentPieces, grid, currentNode, chessPieceLayer);
                    pause(16);
                        
                }

            });
        }


        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                try {
                    piece.getImg().setOnMouseClicked(event1 -> {
                        Boolean correctColor = (Chessboard.isWhite && piece.getColor().equals("white")) || (!Chessboard.isWhite && !piece.getColor().equals("white"));
                        if (!piece.getSelected() && thisIsReallyStupid(currentPieces) && correctColor) { // Check if a chess piece has not been selected yet
                            ImageView selectedPieceIMG = (ImageView) event1.getSource(); // Get the clicked chess piece
               
      
                            piece.setSelected(true); // Set the selected flag to true
                            Tile tile = currentPieceTileLocator(grid, currentPieces);
                            Color color = (Color) tile.getFill();
                            Boolean colorType = color.equals(Color.web(Chessboard.tileColor1));
                            colorChanger(colorType, tile);
                            pause(16);

                        } else if(!thisIsReallyStupid(currentPieces) && correctColor && !piece.getSelected()) { // selektiranje drugog piecea
                            
                            Tile tile = currentPieceTileLocator(grid, currentPieces);
                            Color color = (Color) tile.getFill();
                            Boolean colorType = color.equals(Color.web(Chessboard.tileColor1Ac));
                            colorRevert(colorType, tile);

                            thisIsAlsoReallyStupid(currentPieces);
                            piece.setSelected(true);
                            tile = currentPieceTileLocator(grid, currentPieces);
                            color = (Color) tile.getFill();
                            colorType = color.equals(Color.web(Chessboard.tileColor1));
                            colorChanger(colorType, tile);
                            pause(16);
                        }

                        if (!thisIsReallyStupid(currentPieces) && !correctColor) { // jedenje
                            Node node = (Node) event1.getSource();

                           moveAction(currentPieces, grid, node, chessPieceLayer); 
                           pause(16);
                         }
                    });

                    piece.getImg().setOnMouseEntered(event -> {
                        Boolean correctColor = (Chessboard.isWhite && piece.getColor().equals("white")) || (!Chessboard.isWhite && !piece.getColor().equals("white"));
                        // Change cursor to hand cursor when mouse is over the ImageView
                        if(correctColor){
                            piece.getImg().setCursor(javafx.scene.Cursor.HAND);
                        }
                    });
            
                    piece.getImg().setOnMouseExited(event -> {
                        Boolean correctColor = (Chessboard.isWhite && piece.getColor().equals("white")) || (!Chessboard.isWhite && !piece.getColor().equals("white"));
                        // Reset cursor to default when mouse exits the ImageView
                        if (correctColor) {
                            piece.getImg().setCursor(javafx.scene.Cursor.DEFAULT);
                        }
  
                    });

                } catch (Exception e) {
                    continue;
                }
            }
        }
        
        

    }

    public void Logic(GridPane grid, Group chessPiecesLayer, Piece[][] currentPieces, Stage primaryStage, Scene scene) {

        movementHandler(currentPieces, Chessboard.isWhite, grid, chessPiecesLayer); // initializes piece movement/selection capabilites

    }
}
