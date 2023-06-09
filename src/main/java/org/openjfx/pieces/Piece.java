package org.openjfx.pieces;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece {
    ImageView imageView = new ImageView();
    String color;
    Boolean selected;
    String type;

    public Piece(String pathToImg, String color){
        Path currentDirectory = Paths.get("").toAbsolutePath(); //relative paths do not like me so i had to do this arcane magic for it to work
        String molimte = currentDirectory + "/src/main/java/res/images/pieces/";
         
        Image image = new Image(molimte + pathToImg);
        imageView.setImage(image);
        imageView.setFitWidth(60); 
        imageView.setFitHeight(60); 
        imageView.setPickOnBounds(true);
        
        this.color = color;
        selected = false;
    }

    public Piece(){
        selected = false;
    }

    public Boolean Move(Piece selectedPiece, Piece[][] currentPieces, int currentRow, int currentColumn, int desiredRow, int desiredColumn){
        return false;
    }

    public ImageView getImg(){
        return imageView;
    }

    public String getColor(){
        return color;
    }

    public Boolean getSelected(){
        return selected;
    }

    public void setSelected(Boolean selected){
        this.selected = selected;
    }

    public String getType(){
        return type;
    }
}