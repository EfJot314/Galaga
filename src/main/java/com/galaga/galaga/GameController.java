package com.galaga.galaga;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class GameController {

    private Circle player;
    private Pane pane;

    public GameController(){
        pane = new Pane();
        player = new Circle(10,10,10, Color.RED);
        pane.getChildren().addAll(player);
    }

    public void movePlayer(galaga.Vector2d pos){
        player.setCenterX(pos.x);
        player.setCenterY(pos.y);
    }
}