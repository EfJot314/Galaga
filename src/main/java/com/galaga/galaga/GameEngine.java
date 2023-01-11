package com.galaga.galaga;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameEngine extends Thread{
    private Pane pane;
    private Scene scene;
    private Player player;

    private List<Bullet> bulletTab;
    private List<Bullet> toRemove;

    private Timeline tl;


    public GameEngine(Pane p, Scene s) throws IOException {
        this.pane = p;
        this.scene = s;
        this.bulletTab = new ArrayList<>();
        this.player = new Player(this);

    }

    public Scene getScene(){
        return this.scene;
    }

    public Pane getPane(){
        return this.pane;
    }

    public void addBullet(Bullet bullet){
        this.bulletTab.add(bullet);

    }

    public void removeBullet(Bullet bullet){
        this.bulletTab.remove(bullet);
    }

    private void mainLoop() throws IOException {

        //pociski sie ruszaja
        this.toRemove = new ArrayList<>();
        for(Bullet b : bulletTab){
            b.move();
            if(b.isHit()){
                this.toRemove.add(b);
            }
        }
        //usuwam niepotrzebne
        for(Bullet b : toRemove){
            this.bulletTab.remove(b);
        }

        //gracz sie rusza
        player.move();

    }

    public void run(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.015), ev -> {
            try {
                this.mainLoop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


}
