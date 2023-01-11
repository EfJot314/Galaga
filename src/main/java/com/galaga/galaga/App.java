package com.galaga.galaga;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 540);
        stage.setTitle("Galaga");
        stage.setScene(scene);
        stage.show();
    }

    public static void startGame() throws IOException {
        GameView newGame = new GameView();
        Thread gameThreat = new Thread(newGame);
        gameThreat.start();
    }


    public static void main(String[] args) {
        launch();
    }
}