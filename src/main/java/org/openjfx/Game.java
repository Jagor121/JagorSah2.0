package org.openjfx;


import java.io.IOException;

import org.openjfx.pieces.King;
import org.openjfx.pieces.Pawn;
import org.openjfx.pieces.Piece;
import org.openjfx.pieces.Queen;
import org.openjfx.pieces.Rook;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;



public class Game{
    public static int justMovedRow = -1, justMovedCol = -1;
    public static Boolean lastPawnMoveWasTwoSquares = false;
    public static Boolean currentlyChecking = false;
    StringProperty result = new SimpleStringProperty(null);

    Media moveSound = new Media(getClass().getResource("/res/sounds/moveself.mp3").toExternalForm());
    Media captureSound = new Media(getClass().getResource("/res/sounds/capture.mp3").toExternalForm());
    Media checkSound = new Media(getClass().getResource("/res/sounds/movecheck.mp3").toExternalForm());
    Media castleSound = new Media(getClass().getResource("/res/sounds/castle.mp3").toExternalForm());
    Media promoteSound = new Media(getClass().getResource("/res/sounds/promote.mp3").toExternalForm());
    Media enpassantSound = new Media(getClass().getResource("/res/sounds/enpassant.mp3").toExternalForm());
    Media checkmateSound = new Media(getClass().getResource("/res/sounds/checkmate.mp3").toExternalForm());

    MediaPlayer movePlayer = new MediaPlayer(moveSound);
    MediaPlayer capturePlayer = new MediaPlayer(captureSound);
    MediaPlayer checkPlayer = new MediaPlayer(checkSound);
    MediaPlayer castlePlayer = new MediaPlayer(castleSound);
    MediaPlayer promotePlayer = new MediaPlayer(promoteSound);
    MediaPlayer enpassantPlayer = new MediaPlayer(enpassantSound);
    MediaPlayer checkmatePlayer = new MediaPlayer(checkmateSound);
    
    public static void setJustMoved(int row, int col){
        justMovedRow = row;
        justMovedCol = col;
    }

    public static int getJRow(){
        return justMovedRow;
    }

    public static int getJCol(){
        return justMovedCol;
    }

    public void playSoundEffect(MediaPlayer mediaPlayer) {
        mediaPlayer.play();
        
        // Wait for the sound effect to finish playing
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
        });
    }
    
    public void stupidForTheLastTime(Piece piece){
        try {
            if(piece instanceof King){
                ((King) piece).setHasMoved();
            }
            
            if(piece instanceof Rook){
                ((Rook) piece).setHasMoved();
            }

            if(piece instanceof Pawn){
                ((Pawn) piece).setFirstMove();
            }
        } catch (Exception e) {
            
        }
    }

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
        for (Piece[] pieces : currentPieces) {              // vrlo optimiziran kod, ja sam siguran da cu bit bas dobar programer u buducnosti
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

    public void enPassantHandler(Piece[][] currentPieces, GridPane grid, Piece selectedPiece,Group chessPieceLayer, int currentRow, int currentCol, int desiredRow, int desiredCol, int enPassantRow){
        removePiece(chessPieceLayer, currentPieces[enPassantRow][desiredCol], enPassantRow, desiredCol, grid); // removea jadnog pijuna koji je bio en passantan
        currentPieces[enPassantRow][desiredCol] = null;

        addPiece(chessPieceLayer, selectedPiece, desiredRow, desiredCol, grid);
        currentPieces[desiredRow][desiredCol] = selectedPiece;
        currentPieces[currentRow][currentCol] = null;
        selectedPiece.setSelected(false);
        Chessboard.isWhite.set(!Chessboard.isWhite.get()); 
        lastPawnMoveWasTwoSquares = false;

        System.out.println("isuse kriste en passant!!!!!!!!!");
    }

    public void castleHandler(Piece[][] currentPieces, GridPane grid, Piece selectedPiece, Group chessPieceLayer, int Row, int currentCol, int desiredCol, int rookCol){
        removePiece(chessPieceLayer, selectedPiece, Row, currentCol, grid); // remove king image

        removePiece(chessPieceLayer, currentPieces[Row][rookCol], Row, rookCol, grid); // remove rook image 

        int destinationRookCol = (rookCol == 0) ? 1 : -1;

        currentPieces[Row][desiredCol] = selectedPiece; // stavlja kralj na zeljeno mjesto
        currentPieces[Row][currentCol] = null; // mice og kralja
        addPiece(chessPieceLayer,selectedPiece,Row,desiredCol,grid);

        currentPieces[Row][desiredCol + destinationRookCol] = currentPieces[Row][rookCol]; // stavlja rook pored kralja
        currentPieces[Row][rookCol] = null; // mice og rook  
        addPiece(chessPieceLayer, currentPieces[Row][desiredCol + destinationRookCol], Row, desiredCol + destinationRookCol, grid);

        selectedPiece.setSelected(false);
        Chessboard.isWhite.set(!Chessboard.isWhite.get()); 
        lastPawnMoveWasTwoSquares = false;
        ((King)selectedPiece).setHasMoved();

        System.out.println("CASTLELELELELELELELELLEL");
    }

    public void promotionHandler(Piece[][] currentPieces, Piece selectedPiece, GridPane grid, Group chessPieceLayer, int currentRow, int currentCol, int desiredRow, int desiredCol){
        removePiece(chessPieceLayer, selectedPiece, currentRow, currentCol, grid);
        currentPieces[currentRow][currentCol] = null; // mice pijuna
        
        try {  // ovo radi funkcionalno istu stvar kao jedenje
            removePiece(chessPieceLayer, currentPieces[desiredRow][desiredCol], desiredRow, desiredCol, grid);
            currentPieces[desiredRow][desiredCol] = null;
            recentMoveShowReset(grid);
            somethingToDoWithColor(grid, desiredRow, desiredCol); // ovo radi stvari kada selectas piece posto nema node varijable
        } catch (Exception e) {
 
        }

        String isWhite = (selectedPiece.getColor().equals("white")) ? "white" : "black";  // odreduje vrstu kraljice
        Queen queen = new Queen(isWhite + "queen.png", isWhite);
        addPiece(chessPieceLayer, queen, desiredRow, desiredCol, grid);
        currentPieces[desiredRow][desiredCol] = queen; 
        pieceEventListenerLogic(currentPieces[desiredRow][desiredCol], currentPieces, grid, chessPieceLayer);
        
        
        selectedPiece.setSelected(false);
        Chessboard.isWhite.set(!Chessboard.isWhite.get()); 
        lastPawnMoveWasTwoSquares = false;
        System.out.println("PROMOCCIAJIAJIAJJAIIJI");
    }

    public Boolean checkSoundLogic(Piece[][] currentPieces){
        Boolean opponentChecked = isKingInCheck(currentPieces, currentKingLocator(currentPieces));
        if(opponentChecked){
            return true;
        }
        return false;
    }

    public King currentKingLocator(Piece[][] currentPieces){
        String playerColor = (Chessboard.isWhite.get()) ? "white" : "black";

        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                if(piece instanceof King && piece.getColor().equals(playerColor)){
                    return ((King)piece);
                }
            }
        }

        return null;
    }

    public Boolean isKingInCheck(Piece[][] currentPieces, King currentKing){
      
        int currentRow = 0, currentCol = 0;

        for (int i = 0; i < currentPieces.length; i++) {  // trazi row i col kralja, nije li java toliko zabavna>??????
            for (int j = 0; j < currentPieces[i].length; j++) {
                try {
                    if(currentPieces[i][j] == currentKing){
                        currentRow = i;
                        currentCol = j;
                    } 
                } catch (Exception e) {
                   continue;
                }
            }
        }

        if(currentKing.checkChecker(currentPieces, currentRow, currentCol)){
            return true;
        }
        return false;
    }

    public String broYouHaveBeenCheckmated (Piece[][] currentPieces){
        String playerColor = (Chessboard.isWhite.get()) ? "white" : "black";
        String winnerColor = (Chessboard.isWhite.get()) ? "black" : "white";
        int legalMovesCounter= 0;
        int stupidRookColumn = -1;
        currentlyChecking = true;

        Piece[][] futurePieces = new Piece[currentPieces.length][currentPieces.length];
        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces.length; j++) {
                futurePieces[i][j] = currentPieces[i][j];
            }
        }
        King currentKing = currentKingLocator(futurePieces);

        for (int currentRow = 0; currentRow < futurePieces.length; currentRow++) {  // ovaj spaget od koda gleda svaki piece i svaki moguci move svakog piecea (trenutnog playera)
            for (int currentCol = 0; currentCol < futurePieces.length; currentCol++) { // te broji koliko ima legalnih poteza
                try {
                    Piece selectedPiece = futurePieces[currentRow][currentCol];
                    
                    if(selectedPiece.getColor().equals(playerColor)){
                        for (int desiredRow = 0; desiredRow < futurePieces.length; desiredRow++) {
                            for (int desiredCol = 0; desiredCol < futurePieces.length; desiredCol++) {
                            Boolean validMove = selectedPiece.Move(selectedPiece, futurePieces, currentRow, currentCol, desiredRow, desiredCol);

                            if(selectedPiece instanceof King){
                                stupidRookColumn = ((King)selectedPiece).castleChecker((King)selectedPiece, futurePieces, currentRow, currentCol, currentRow, currentCol);
                            }
                            
                            if(legalMovesChecker(futurePieces, selectedPiece, currentKing, validMove, currentRow, currentCol, desiredRow, desiredCol, stupidRookColumn)){
                                legalMovesCounter++;
                                // System.out.println(selectedPiece.getType() + " " + selectedPiece.getColor());
                                // System.out.println(desiredRow + " " + desiredCol + "\n");
                            }

                            }   
                        }
                    }

                } catch (Exception e) {
                    continue;
                }
            }
        }

        if(legalMovesCounter == 0 && isKingInCheck(currentPieces, currentKing)){
            return winnerColor + " has won by checkmate!";
        } else if(legalMovesCounter == 0 && !isKingInCheck(currentPieces, currentKing)){
            return "stalemate :(";
        }
        //System.out.println(legalMovesCounter);

        return null;
    }

    public Boolean legalMovesChecker(Piece[][] currentPieces, Piece selectedPiece, King currentKing, Boolean validMove, int currentRow, int currentCol, int desiredRow, int desiredCol, int stupidRookColumn){
        Boolean isKingInCheck = isKingInCheck(currentPieces, currentKing);
        Boolean futureKingLegality = false;
        Boolean occupiedSpace = false;

        Piece[][] futurePieces = new Piece[currentPieces.length][currentPieces.length];
        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces.length; j++) {
                futurePieces[i][j] = currentPieces[i][j];
            }
        }

        if(stupidRookColumn == -1){ //castling clause,, YAYYYYY
            futurePieces[desiredRow][desiredCol] = selectedPiece;
            futurePieces[currentRow][currentCol] = null;
            futureKingLegality = (isKingInCheck && !(currentKing.checkChecker(futurePieces, desiredRow, desiredCol))) || (!isKingInCheck && !(currentKing.checkChecker(futurePieces, desiredRow, desiredCol)));
        } else{
            int Row = currentRow;
            int destinationRookCol = (stupidRookColumn == 0) ? 1 : -1;
            int step = desiredCol + destinationRookCol;
            futurePieces[Row][step] = selectedPiece;
            futurePieces[Row][currentCol] = null; // mice og kralja

            Boolean castlestep = (isKingInCheck && !(currentKing.checkChecker(futurePieces, Row, step))) || (!isKingInCheck && !(currentKing.checkChecker(futurePieces, Row, step)));

            futurePieces[Row][desiredCol] = selectedPiece; // stavlja kralj na zeljeno mjesto
            
            futurePieces[Row][step] = futurePieces[Row][stupidRookColumn]; // stavlja rook pored kralja
            futurePieces[Row][stupidRookColumn] = null; // mice og rook  
            
            futureKingLegality = castlestep && ((isKingInCheck && !(currentKing.checkChecker(futurePieces, Row, step))) || (!isKingInCheck && !(currentKing.checkChecker(futurePieces, Row, step))));
            

        }
        

        Boolean legalKingMove = validMove && futureKingLegality && selectedPiece == currentKing;
        // the line above determines if a king move would be a valid way to get out of check

        int kingRow = 0, kingCol = 0;
        for (int i = 0; i < currentPieces.length; i++) {  // trazi row i col kralja, nije li java toliko zabavna>??????
            for (int j = 0; j < currentPieces[i].length; j++) {
                try {
                    if(currentPieces[i][j] == currentKing){
                        kingRow = i;
                        kingCol = j;
                    } 
                } catch (Exception e) {
                   continue;
                }
            }
        }

        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces.length; j++) {
                futurePieces[i][j] = currentPieces[i][j];
            }
        }

        
            futurePieces[desiredRow][desiredCol] = selectedPiece;
            futurePieces[currentRow][currentCol] = null;
        

        futureKingLegality = (isKingInCheck && !(currentKing.checkChecker(futurePieces, kingRow, kingCol))) || (!isKingInCheck && !(currentKing.checkChecker(futurePieces, kingRow, kingCol)));
        Boolean legalOtherMove = validMove && futureKingLegality && selectedPiece != currentKing;
        // the above line checks if a non king move is a valid way to get out of check

        try {
            String playerColor = (Chessboard.isWhite.get()) ? "white" : "black";
            occupiedSpace = (currentPieces[desiredRow][desiredCol].getColor().equals(playerColor)) ? true : false;
        } catch (Exception e) {
            occupiedSpace = false;
        }
        

        if(validMove && (legalKingMove || legalOtherMove) && !occupiedSpace){
            //System.out.println("\nKralj: " + legalKingMove + "\nDrugi: " + legalOtherMove + "\nSah? " + isKingInCheck);
            return true;
        }

        //System.out.println("\nKralj: " + legalKingMove + "\nDrugi: " + legalOtherMove + "\nSah? " + isKingInCheck);
        return false;
    }

    public Boolean moveAction(Piece[][] currentPieces, GridPane grid, Node currentNode, Group chessPieceLayer){
                
        int desiredRow = GridPane.getRowIndex(currentNode);
        int desiredCol = GridPane.getColumnIndex(currentNode);
        Boolean validMove = false;
        Boolean legalMove = false;
        int currentRow = 0, currentCol = 0;
        King currentKing = currentKingLocator(currentPieces);

        
        
        for (int i = 0; i < currentPieces.length; i++) {
            for (int j = 0; j < currentPieces[i].length; j++) {
                try {
                    if(currentPieces[i][j].getSelected()){
                        currentRow = i;
                        currentCol = j;

                        validMove = currentPieces[currentRow][currentCol].Move(currentPieces[currentRow][currentCol], currentPieces, currentRow, currentCol, desiredRow, desiredCol);
                        // je li mozete vjerovati da je ova validMove linija gore prije bila 30 linija jer sam ja glupan??
                        
                        if(currentPieces[i][j] instanceof King){ // rosado (castling) code
                            int rookCol = currentKing.castleChecker(currentKing, currentPieces, currentRow, currentCol, desiredRow, desiredCol);
                            if(rookCol != -1){
                                validMove = true;
                                legalMove = legalMovesChecker(currentPieces, currentKing, currentKing, validMove, currentRow, currentCol, desiredRow, desiredCol, rookCol);

                                if(legalMove){
                                    stupidForTheLastTime(currentKing);
                                    castleHandler(currentPieces, grid, currentKing, chessPieceLayer, currentRow, currentCol, desiredCol, rookCol);
                                    setJustMoved(desiredRow, desiredCol);
                                    
                                    if(!checkSoundLogic(currentPieces)){
                                        playSoundEffect(castlePlayer);
                                    } else {
                                        playSoundEffect(checkPlayer);
                                    }
                                    
                                    return true;
                                }
                                validMove = false;
                            }
                        } 
                    
                         if (currentPieces[i][j] instanceof Pawn) {   // boze pomozi pawn stvarima
                             Pawn selectedPiece = (Pawn) currentPieces[currentRow][currentCol];
                             int enPssntRow = selectedPiece.enPassantChecker(selectedPiece,  currentPieces, currentRow, currentCol,desiredRow, desiredCol);

                             if(selectedPiece.Hamburger(selectedPiece, desiredRow) && validMove){ // promocija pijuna
                                String isWhite = (selectedPiece.getColor().equals("white")) ? "white" : "black";  // odreduje vrstu kraljice
                                Queen queen = new Queen(isWhite + "queen.png", isWhite);

                                validMove = true;
                                currentlyChecking = true;
                                legalMove = legalMovesChecker(currentPieces, queen, currentKing, validMove, currentRow, currentCol, desiredRow, desiredCol, -1);
                                currentlyChecking = false;
   
                                if(legalMove){
                                    promotionHandler(currentPieces, selectedPiece, grid,  chessPieceLayer, currentRow, currentCol, desiredRow, desiredCol);
                                    setJustMoved(desiredRow, desiredCol);

                                    if(!checkSoundLogic(currentPieces)){
                                        playSoundEffect(promotePlayer);
                                    } else{
                                        playSoundEffect(checkPlayer);
                                    }
                                    
                                    return true;
                                }

                             }

                             if(enPssntRow != -1){ //en passant
                                validMove = true;
                                legalMove = legalMovesChecker(currentPieces, currentPieces[currentRow][currentCol], currentKing, validMove, currentRow, currentCol, desiredRow, desiredCol, -1);

                                if(legalMove){
                                 enPassantHandler(currentPieces, grid, (Piece) selectedPiece, chessPieceLayer, currentRow, currentCol, desiredRow, desiredCol, enPssntRow);
                                 playSoundEffect(enpassantPlayer);
                                 setJustMoved(desiredRow, desiredCol);
                                 return true;
                                } 
                             }
                        } 
                       
                        
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        
        Piece selectedPiece = currentPieces[currentRow][currentCol];
        currentlyChecking = true;
        legalMove = legalMovesChecker(currentPieces, selectedPiece, currentKing, validMove, currentRow, currentCol, desiredRow, desiredCol, -1);
        currentlyChecking = false;
        try {
            System.out.println(lastPawnMoveWasTwoSquares);
            if(legalMove){
                try {
                    Piece potentialDeadPiece = currentPieces[desiredRow][desiredCol];
                    currentPieces[desiredRow][desiredCol] = null;
                    removePiece(chessPieceLayer, potentialDeadPiece, desiredRow, desiredCol, grid); // jedenje protivnickih pieceova
                    
                    recentMoveShowReset(grid);
                    somethingToDoWithColor(grid, desiredRow, desiredCol); // ovo radi stvari kada selectas piece posto nema node varijable

                } catch (Exception e) {
                    // System.out.println("nisi pojeo nista");
                }
                
                addPiece(chessPieceLayer, selectedPiece, desiredRow, desiredCol, grid);
                currentPieces[desiredRow][desiredCol] = selectedPiece;
                currentPieces[currentRow][currentCol] = null;
        
                selectedPiece.setSelected(false);
                Chessboard.isWhite.set(!Chessboard.isWhite.get()); 
                stupidForTheLastTime(selectedPiece);

                if(!(selectedPiece instanceof Pawn)){
                    lastPawnMoveWasTwoSquares = false;
                }
               
                setJustMoved(desiredRow, desiredCol);
                
                
                return true;
            }

        } catch (Exception e) {
            
        }
        return false;
    }

    public void somethingToDoWithColor(GridPane grid, int desiredRow, int desiredCol){ // does color things when eating a piece (aka when there is no node variable available)
        for (Node node : grid.getChildren()) {     // i just didn't like seeing this in the main game logic code and decided to give it it's own function
            int row = GridPane.getRowIndex(node); // ne znam zas pola puta pisem na engleskom, pola na hrvatskom.... bok zavrsni rad gledatelji/citatelji :)
            int col = GridPane.getColumnIndex(node);
            if(row == desiredRow && col == desiredCol){
                Tile tile = (Tile) node;
                Color color = (Color) tile.getFill();
                Boolean colorType = color.equals(Color.web(Chessboard.tileColor1));
                colorChanger(colorType, tile);
            }
        }
    }

    public void colorChanger(Boolean colorType, Tile tile){
        try {
            if(colorType){
                tile.setFill(Color.web(Chessboard.tileColor1Ac));
            }else{
                tile.setFill(Color.web(Chessboard.tileColor2Ac));
            }
        } catch (Exception e) {

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

    public void Deselector(GridPane grid, Piece[][] currentPieces){
        Tile currentPieceTile = currentPieceTileLocator(grid, currentPieces);
        Color color = (Color) currentPieceTile.getFill();
        Boolean colorType = color.equals(Color.web(Chessboard.tileColor1Ac));
        colorRevert(colorType, currentPieceTile);
        thisIsAlsoReallyStupid(currentPieces);
    }

    public void pause(int millis){
        PauseTransition pause = new PauseTransition(Duration.millis(millis));
                        pause.setOnFinished(e -> {
                        });
                        pause.play();

    }

    public void movementHandler(Piece[][] currentPieces, BooleanProperty isWhite, GridPane grid, Group chessPieceLayer){
        for (Node node : grid.getChildren()) {
            node.setOnMouseClicked(event2 -> {
                if(!thisIsReallyStupid(currentPieces)){ // gleda dal ima selektirani piece
                    Node currentNode = (Node) event2.getSource();
                    Tile tile = (Tile) event2.getSource();
                    currentlyChecking = false;
                    
                    if(moveAction(currentPieces, grid, currentNode, chessPieceLayer)){

                        recentMove(tile, grid);

                        if(!checkSoundLogic(currentPieces)){
                            playSoundEffect(movePlayer);
                        }else{
                            playSoundEffect(checkPlayer);
                        }

                        result.set(broYouHaveBeenCheckmated(currentPieces));
     
                    }else{
                        // stavi da makne selected svugdje i makne boju

                        Deselector(grid, currentPieces);
                    }

                    
                    pause(16);
                        
                }

            });
        }


        for (Piece[] pieces : currentPieces) {
            for (Piece piece : pieces) {
                pieceEventListenerLogic(piece, currentPieces, grid, chessPieceLayer);
            }
        }
        
        

    }

    public void pieceEventListenerLogic(Piece piece, Piece[][] currentPieces, GridPane grid, Group chessPieceLayer) {
        try {
            piece.getImg().setOnMouseClicked(event1 -> {
                Boolean correctColor = (Chessboard.isWhite.get() && piece.getColor().equals("white")) || (!Chessboard.isWhite.get() && !piece.getColor().equals("white"));
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
                    currentlyChecking = false;

                   if(!moveAction(currentPieces, grid, node, chessPieceLayer)){
                    Deselector(grid, currentPieces);
                   } else {

                    if(!checkSoundLogic(currentPieces)){
                        playSoundEffect(capturePlayer);
                    }else{
                        playSoundEffect(checkPlayer);
                    }
                    
                    result.set(broYouHaveBeenCheckmated(currentPieces)); 
      
                   }
                   
                   pause(16);
                 }
            });

            piece.getImg().setOnMouseEntered(event -> {
                Boolean correctColor = (Chessboard.isWhite.get() && piece.getColor().equals("white")) || (!Chessboard.isWhite.get() && !piece.getColor().equals("white"));
                // Change cursor to hand cursor when mouse is over the ImageView
                if(correctColor){
                    piece.getImg().setCursor(javafx.scene.Cursor.HAND);
                }
            });
    
            piece.getImg().setOnMouseExited(event -> {
                Boolean correctColor = (Chessboard.isWhite.get() && piece.getColor().equals("white")) || (!Chessboard.isWhite.get() && !piece.getColor().equals("white"));
                // Reset cursor to default when mouse exits the ImageView
                if (correctColor) {
                    piece.getImg().setCursor(javafx.scene.Cursor.DEFAULT);
                }

            });

        } catch (Exception e) {
            
        }
    }

    public void Logic(GridPane grid, Group chessPiecesLayer, Piece[][] currentPieces, Stage primaryStage, Scene scene) {
        
        movementHandler(currentPieces, Chessboard.isWhite, grid, chessPiecesLayer); // initializes piece movement/selection capabilites



         // Create an InvalidationListener
    InvalidationListener listener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            Text title = new Text(result.get());
            title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
            title.setFill(Color.BLACK);
            
            Button playAgainButton = new Button("Play Again");// b u t t o n
            Button mainMenuButton = new Button("Main Menu"); // b u t t o n
            playAgainButton.setStyle("-fx-background-color: #e6d7ff; -fx-text-fill: black;");// b u t t o n
            mainMenuButton.setStyle("-fx-background-color: #ffe599; -fx-text-fill: black;");// b u t t o n
            playAgainButton.setPrefWidth(300); // b u t t o n
            playAgainButton.setPrefHeight(50);// b u t t o n
            mainMenuButton.setPrefWidth(300);// b u t t o n
            mainMenuButton.setPrefHeight(50);// b u t t o n
            playAgainButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));// b u t t o n
            mainMenuButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));// b u t t o n
            playAgainButton.setCursor(javafx.scene.Cursor.HAND);// b u t t o n
            mainMenuButton.setCursor(javafx.scene.Cursor.HAND);// b u t t o n

            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.getChildren().addAll(title, playAgainButton, mainMenuButton);
    
            Scene scene1 = new Scene(root, 400, 300);
            Stage secondaryStage = new Stage();

            playSoundEffect(checkmatePlayer);
            secondaryStage.setTitle("Game Over");
            secondaryStage.setScene(scene1);
            secondaryStage.show();
            
            playAgainButton.setOnAction(event -> {
               
                secondaryStage.close();

                Chessboard chessboard = new Chessboard();
                Chessboard.isWhite.set(true);
                try {
                    chessboard.start(primaryStage);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            });
    
            mainMenuButton.setOnAction(event -> {
                
                secondaryStage.close();
                App app = new App();
                try {
                    Chessboard.isWhite.set(true);
                    app.start(primaryStage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });

        }
    };

        result.addListener(listener);

                         // ovaj while true je ovdje jer sam htio imat eventlistener za 2d array ali to je propalo u vodu, duga prica ovo treba biti tu da mogu
        //while (true) {  // promijeniti scenu kada je checkmate
            
        //}

    }
}
