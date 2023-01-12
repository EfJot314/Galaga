package com.galaga.galaga;

import galaga.Vector2d;
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
import java.util.Random;

public class GameEngine extends Thread{
    private Pane pane;
    private Scene scene;
    private Player player;

    private List<Bullet> bulletTab;
    private List<Bullet> toRemove;

    private List<Enemy> enemyTab;
    private List<Enemy> deadEnemies;

    private int enemiesWidth = 4;
    private int enemiesHeight = 4;
    private galaga.Vector2d[][] positionsTab;
    private boolean[][] occupiedTab;

    private Timeline tl;


    public GameEngine(Pane p, Scene s) throws IOException {
        this.pane = p;
        this.scene = s;
        this.bulletTab = new ArrayList<>();
        this.player = new Player(this);

        //wielkosc miejsca dla enemy
        float w = 40;
        this.enemyTab = new ArrayList<>();
        this.positionsTab = new Vector2d[this.enemiesWidth][this.enemiesHeight];
        this.occupiedTab = new boolean[this.enemiesWidth][this.enemiesHeight];
        float dx = (float)(this.scene.getWidth()/2-w*this.enemiesWidth/2);
        float dy = 100;
        for(int x=0;x<this.enemiesWidth;x++){
            for(int y=0;y<this.enemiesHeight;y++){
                this.positionsTab[x][y] = new Vector2d(dx+w*x+w/2, dy+y*w+w/2);
                this.occupiedTab[x][y] = false;
            }
        }

        //testowe enemy
        for(int i=0;i<3;i++){
            this.createEnemy();

        }

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


    private void createEnemy() throws IOException {
        //szukanie miejsca
        int x=0;
        int y =0;
        boolean notFound = true;
        while(notFound && this.enemyTab.size() < this.enemiesHeight*this.enemiesWidth){
            x = this.randomInt(0, this.enemiesWidth-1);
            y = this.randomInt(0, this.enemiesHeight-1);
            notFound = this.occupiedTab[x][y];
        }

        //tworzenie zachowania
        Behavior behavior = new Behavior(null);

        //tworzenie wroga
        Enemy newEnemy = new Enemy(new Vector2d(0,0), this.positionsTab[x][y], new Vector2d(0,0), this, behavior);
        this.occupiedTab[x][y] = true;
        this.enemyTab.add(newEnemy);
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

        //przeciwnicy sie ruszaja
        for(Enemy enemy : enemyTab){
            enemy.move();
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

    //funkcja zwraca pseudolosowa liczbe calkowita z przedzialu <min, max>
    public int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


}
