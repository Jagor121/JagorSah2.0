package org.openjfx.pieces;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Piece {
    ImageView imageView = new ImageView();
    String color;


    public Piece(String pathToImg, String color){
        Path currentDirectory = Paths.get("").toAbsolutePath(); //relative paths do not like me so i had to do this arcane magic for it to work
        String molimte = currentDirectory + "/src/main/java/res/images/pieces/";
         
        Image image = new Image(molimte + pathToImg);
        imageView.setImage(image);
        imageView.setFitWidth(60); // Example width
        imageView.setFitHeight(60); // Example height

        this.color = color;
    }

    abstract void Move();

    public ImageView getImg(){
        return imageView;
    }

    public String getColor(){
        return color;
    }

}