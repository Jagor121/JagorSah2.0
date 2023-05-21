package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Button newGameButton = new Button("New Game");
        
        newGameButton.setOnAction(event -> {
            Chessboard chessboard = new Chessboard();
            try {
                chessboard.start(stage);
            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("bravo glupane");
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(newGameButton, 0, 0);

        Scene scene = new Scene(grid, 200, 200); // Set the main menu scene size
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();
        stage.setResizable(false); // Prevent resizing

        
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
