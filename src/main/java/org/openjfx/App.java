package org.openjfx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Text title = new Text("Jagor sah 2.0");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 48));
        title.setFill(Color.WHITE);
        
        
        Button newGameButton = new Button("New Game");
        Button rulesButton = new Button("Rules");
        Button exitButton = new Button("Exit");
        newGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        rulesButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        exitButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        double buttonWidth = 200;
        double buttonHeight = 50;
        newGameButton.setPrefWidth(buttonWidth);
        newGameButton.setPrefHeight(buttonHeight);
        rulesButton.setPrefWidth(buttonWidth);
        rulesButton.setPrefHeight(buttonHeight);
        exitButton.setPrefWidth(buttonWidth);
        exitButton.setPrefHeight(buttonHeight);

        newGameButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        rulesButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        exitButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));

        
        newGameButton.setCursor(javafx.scene.Cursor.HAND);
        rulesButton.setCursor(javafx.scene.Cursor.HAND);
        exitButton.setCursor(javafx.scene.Cursor.HAND);

        newGameButton.setOnAction(event -> {
            Chessboard chessboard = new Chessboard();
            try {
                chessboard.start(primaryStage);
            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("bravo glupane");
            }
        });

        rulesButton.setOnAction(event -> {
            HostServices hostServices = getHostServices();
            hostServices.showDocument("https://en.wikipedia.org/wiki/Rules_of_chess"); // Replace with your link 
        });


        exitButton.setOnAction(event -> primaryStage.close());

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        vbox.getChildren().addAll(title, newGameButton, rulesButton, exitButton);

       
        Scene scene = new Scene(vbox, 400, 400); // Set the main menu scene size

        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
        primaryStage.setResizable(false); // Prevent resizing

        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
