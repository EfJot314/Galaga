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
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Player implements GameObject{

    private int hp = 3;

    private float v = 3;
    private Vector2d position;
    private ImageView sprite;

    private boolean isA = false;
    private boolean isD = false;
    private boolean fire = false;
    private float fireDuration = 300;

    private float width;
    private float height;

    private float sceneWidth;
    private float sceneHeight;

    private GameEngine engine;

    private Collider collider;

    private Instant start;
    private Instant end;


    public Player(GameEngine engine) throws IOException {

        this.start = Instant.now();

        this.engine = engine;

        Scene scene = this.engine.getScene();
        Pane pane = this.engine.getPane();


        //dane ekranu
        this.sceneWidth = (float)scene.getWidth();
        this.sceneHeight = (float)scene.getHeight();

        //zmienne gracza
        this.position = new Vector2d(this.sceneWidth/2, this.sceneHeight-60);
        Image img = new Image(App.class.getResource("playerSprite.png").openStream());
        this.sprite = new ImageView(img);
        this.sprite.setPreserveRatio(true);
        this.sprite.setFitWidth(30);
        this.sprite.setFitHeight(30);

        this.width = (float)this.sprite.getFitWidth();
        this.height = (float)this.sprite.getFitHeight();


        //dodaje gracza na ekran
        pane.getChildren().addAll(this.sprite);

        //dodaje collider
        this.collider = new Collider(this.position,
                (new Vector2d(this.width/2, this.height)),
                (new Vector2d(this.width,0)), 0);


        //obsluga klawiatury
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.A){
                isA = true;
            } else if (key.getCode()==KeyCode.D) {
                isD = true;
            } else if (key.getCode()==KeyCode.SPACE) {
                fire = true;
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            if(key.getCode() == KeyCode.A){
                isA = false;
            } else if (key.getCode()==KeyCode.D) {
                isD = false;
            } else if (key.getCode()==KeyCode.SPACE) {
                fire = false;
            }
        });

    }


    public boolean isDead(){
        return (this.hp <= 0);
    }



    public void move(){
        Vector2d dpos = new Vector2d(0,0);

        if(isA && this.position.x-this.width/2 > 0){
            dpos = new Vector2d(-1,0);
        }
        if(isD && this.position.x+this.width/2< this.sceneWidth){
            dpos = new Vector2d(1,0);
        }

        position = position.add(dpos.multiply(v));

        float x = position.x - this.width/2;
        float y = position.y - this.height/2;


        this.sprite.setX(x);
        this.sprite.setY(y);

        //strzelam
        if(this.fire){
            this.end = Instant.now();
            if(Duration.between(this.start, this.end).toMillis() > this.fireDuration){
                this.start = Instant.now();
                try {
                    Bullet bullet = new Bullet(this.position.x, this.position.y, new Vector2d(0,-10), 0, this.engine);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
        }

        //update collidera
        this.collider.update(this.position, 0);


    }


    public boolean checkHit(List<GameObject> objects) {
        for(GameObject object : objects){
            if(this != object && object.getAlliance() != 0 && this.collider.isCollision(object.getCollider())){
                this.hp -= 1;
                return isDead();
            }
        }
        return false;
    }

    public Collider getCollider(){
        return this.collider;
    }

    public int getAlliance(){
        return 0;
    }

    public String getType(){
        return "Player";
    }


}
