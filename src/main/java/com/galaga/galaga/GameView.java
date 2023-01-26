package com.galaga.galaga;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Thread{

    private Stage stage;
    private Scene scene;

    private GameEngine engine;


    public GameView() throws IOException {

        StackPane pane = new StackPane();
        scene = new Scene(pane, 500, 700);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        engine = new GameEngine(pane, scene);



        stage = new Stage();

        stage.setTitle("Galaga");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();

    }


    public void run(){
        Thread engineThread = new Thread(engine);
        engineThread.run();
    }
}
