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
import java.util.List;


public class Bullet implements GameObject{

    private galaga.Vector2d v;
    private galaga.Vector2d position;

    private GameEngine engine;

    private Collider collider;

    private ImageView sprite;

    private float width;
    private float height;
    private float rotation;
    private int allience;

    public Bullet(float x, float y, float velocity, int allience, GameEngine engine) throws IOException {
        this.position = new Vector2d(x, y);
        this.rotation = 0;
        this.allience = allience;
        this.v = new Vector2d(0, -velocity);

        Image img = new Image(App.class.getResource("playerBulletSprite.png").openStream());
        this.sprite = new ImageView(img);

        this.sprite.setPreserveRatio(true);

        this.sprite.setX(this.position.x-this.width/2);
        this.sprite.setY(this.position.y);

        this.sprite.setFitWidth(4);

        this.width = (float)this.sprite.getFitWidth();
        this.height = (float)this.sprite.getFitHeight();

        this.engine = engine;

        this.collider = new Collider(this.position,
                this.position.add(new Vector2d(this.width/2, this.height)),
                this.position.add(new Vector2d(this.width,0)), this.rotation);

        this.engine.addBullet(this);
        this.engine.getPane().getChildren().addAll(this.sprite);
    }

    public void move(){

        this.position = this.position.add(this.v);

        this.sprite.setX(this.position.x-this.width/2);
        this.sprite.setY(this.position.y);

        //update collidera
        this.collider.update(this.position, this.rotation);

    }


    public boolean checkHit(List<GameObject> objects) {
        for(GameObject object : objects){
            if(this != object && this.collider.isCollision(object.getCollider())){
                //jesli jest to pocisk od sojusznika, to nie jest szkodliwy
                if(this.allience != object.getAlliance()){
                    this.engine.getPane().getChildren().remove(this.sprite);
                    return true;
                }
            }
        }
        return false;
    }

    public Collider getCollider(){
        return this.collider;
    }

    public int getAlliance(){
        return this.allience;
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
