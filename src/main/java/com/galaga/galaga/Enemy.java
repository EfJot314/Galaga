package com.galaga.galaga;

import galaga.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Enemy {

    private GameEngine engine;
    private galaga.Vector2d position;
    private galaga.Vector2d velocity;
    private Behavior behavior;

    private Circle circle;



    public Enemy(Vector2d position, galaga.Vector2d velocity, GameEngine engine, Behavior behavior){
        this.engine = engine;

        this.behavior = behavior;

        this.position = position;

        this.velocity = new Vector2d(0, 5);

        this.circle = new Circle(this.position.x, this.position.y, 10, Color.RED);
        this.engine.getPane().getChildren().addAll(this.circle);
    }

    public void move(){

    }




}
