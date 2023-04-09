package org.openjfx.pieces;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Piece {
    ImageView imageView = new ImageView();
    String color;


    public Piece(String pathToImg, String color){
        Image image = new Image(pathToImg);
        imageView.setImage(image);
        imageView.setFitWidth(60); // Example width
        imageView.setFitHeight(60); // Example height

        this.color = color;
    }

    abstract void Move();

    public ImageView getImg(){
        return imageView;
    }

}