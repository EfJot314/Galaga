package com.galaga.galaga;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Thread{

    private Stage stage;
    private Scene scene;

    private GameEngine engine;


    public GameView() throws IOException {

        Pane pane = new Pane();
        scene = new Scene(pane, 500, 700);

        engine = new GameEngine(pane, scene);



        stage = new Stage();

        stage.setTitle("Galaga");
        stage.setScene(scene);
        stage.show();

    }


    public void run(){
        Thread engineThread = new Thread(engine);
        engineThread.run();
    }
}
