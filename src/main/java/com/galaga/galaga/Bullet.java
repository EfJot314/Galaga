package com.galaga.galaga;

import galaga.Vector2d;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Bullet {

    private galaga.Vector2d v;
    private galaga.Vector2d position;

    private GameEngine engine;

    private ImageView sprite;

    private float width;
    private float height;

    public Bullet(float x, float y, float velocity, GameEngine engine) throws IOException {
        this.position = new Vector2d(x, y);
        this.v = new Vector2d(0, -velocity);
        Image img = new Image(App.class.getResource("playerBulletSprite.png").openStream());
        this.sprite = new ImageView(img);
        this.sprite.setPreserveRatio(true);
        this.sprite.setFitWidth(4);
        this.width = (float)this.sprite.getFitWidth();
        this.height = (float)this.sprite.getFitHeight();
        this.engine = engine;
        this.engine.addBullet(this);
        this.engine.getPane().getChildren().addAll(this.sprite);
    }

    public void move(){

        this.position = this.position.add(this.v);

        this.sprite.setX(this.position.x-this.width/2);
        this.sprite.setY(this.position.y);

    }

    public boolean isHit(){
        //jesli wyszedl za daleko w gore/dol
        if(this.position.y < -100 || this.position.y > this.engine.getScene().getHeight()+100){
            return true;
        }
        //jesli wyszedl za daleko w prawo/lewo
        if(this.position.x < -100 || this.position.x > this.engine.getScene().getWidth()+100){
            return true;
        }

        //w przeciwnym przypadku
        return false;

    }


}
