package com.galaga.galaga;

import galaga.Vector2d;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine extends Thread{
    private Pane pane;

    private StackPane textPane;
    private Scene scene;
    private Player player;

    private List<GameObject> allObjects;
    private List<GameObject> toAdd;
    private List<GameObject> toRemove;

    public final int enemiesWidth = 4;
    public final int enemiesHeight = 4;
    private int nOfEnemies = 0;
    public galaga.Vector2d[][] positionsTab;
    public boolean[][] occupiedTab;

    private ImageView[] hearts;
    private Label scoreLabel;

    private Timeline tl;

    public boolean gameOver = false;

    private int score = 0;


    public GameEngine(StackPane p, Scene s) throws IOException {
        this.gameOver = false;
        this.score = 0;

        this.textPane = p;
        this.pane = new Pane();
        this.scene = s;

        this.textPane.getChildren().add(this.pane);

        this.scoreLabel = new Label();
        this.scoreLabel.setFont(new Font(25));
        this.scoreLabel.setTextFill(Color.WHITE);
        VBox scoreBox = new VBox(this.scoreLabel);
        scoreBox.setAlignment(Pos.TOP_RIGHT);
        this.textPane.getChildren().add(scoreBox);
        this.updateScore(0);

        this.allObjects = new ArrayList<>();
        this.toAdd = new ArrayList<>();
        this.toRemove = new ArrayList<>();


        //zdrowie
        this.hearts = new ImageView[3];

        this.player = new Player(this);
        this.allObjects.add(player);

        //wielkosc miejsca dla enemy
        float w = 40;
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



    }

    public Scene getScene(){
        return this.scene;
    }

    public Pane getPane(){
        return this.pane;
    }

    public void addBullet(Bullet bullet){
        this.toAdd.add(bullet);
    }


    private void updateScore(int dS){
        //dodaje punkty do calosciowego score-a
        this.score += dS;
        String actText = "Score: ";
        actText += this.score;
        //update-uje napis na ekranie
        this.scoreLabel.setText(actText);
    }

    private void enemyWaive(int n) throws IOException {
        for(int i=0;i<n;i++){
            this.createEnemy();
        }
    }


    private void createEnemy() throws IOException {
        //szukanie miejsca
        int x=0;
        int y =0;
        boolean notFound = true;
        while(notFound && this.nOfEnemies < this.enemiesHeight*this.enemiesWidth){
            x = this.randomInt(0, this.enemiesWidth-1);
            y = this.randomInt(0, this.enemiesHeight-1);
            notFound = this.occupiedTab[x][y];
        }

        //tworzenie zachowania
        Behavior behavior = new Behavior(null);

        //tworzenie wroga
        GameObject newEnemy = null;
        if(this.randomInt(1,10) > 2) {
            newEnemy = new Enemy2(new Vector2d((float)(this.scene.getWidth()/2),0), this.positionsTab[x][y], new Vector2d(0,0), this, behavior);
        }
        else{
            newEnemy = new Enemy1(new Vector2d((float)(this.scene.getWidth()/2),0), this.positionsTab[x][y], new Vector2d(0,0), this, behavior);
        }
        this.occupiedTab[x][y] = true;
        this.toAdd.add(newEnemy);
        nOfEnemies += 1;
    }

    public void updateHealth(int n) throws IOException {
        //usuwanie serduszek
        for(int i=0;i<3;i++){
            this.pane.getChildren().remove(this.hearts[i]);
            this.hearts[i] = null;
        }
        //sprawdzanie czy jeszcze jest zycie
        if(n > 0){
            //tworzenie nowych
            for(int i=0;i<n;i++){
                Image img = new Image(App.class.getResource("heart.png").openStream());
                this.hearts[i] = new ImageView(img);
                this.hearts[i].setX(i*33+15);
                this.hearts[i].setY(15);
                this.hearts[i].setFitWidth(30);
                this.hearts[i].setFitHeight(30);
                this.pane.getChildren().add(this.hearts[i]);
            }
        }
        //jesli ma 0 zyc to przegral
        else{
            this.gameOver = true;
            //napis
            String goText = "Game Over";
            Label gameOverLabel = new Label(goText);
            gameOverLabel.setFont(new Font(60));
            gameOverLabel.setTextFill(Color.RED);
            gameOverLabel.setAlignment(Pos.CENTER);
            VBox gameOverBox = new VBox(gameOverLabel);
            gameOverBox.getChildren().add(this.scoreLabel);
            gameOverBox.setAlignment(Pos.CENTER);
            this.textPane.getChildren().add(gameOverBox);
        }

    }


    private void mainLoop() throws IOException {
        if(!this.gameOver){

            //fale przeciwnikow
            if(nOfEnemies <= 1){
                this.enemyWaive(10);
            }

            //wszystko sie ruszaja
            for(GameObject object : allObjects){
                object.move();
            }
            //uruchamianie colliderow
            for(GameObject object : allObjects){
                if(object.checkHit(allObjects)){
                    //jesli to nie jest gracz to daje do usuniecia
                    if(!object.getType().equals("Player")){
                        this.toRemove.add(object);
                        //update-uje score
                        this.updateScore(object.getScorePoints());
                    }
                }
            }
            //dodawanie nowych
            for(GameObject object : toAdd){
                this.allObjects.add(object);
            }
            //usuwanie zmarlych
            for(GameObject object : toRemove){
                this.allObjects.remove(object);
                if(object.getType().equals("Enemy")){
                    nOfEnemies -= 1;
                }
            }



            this.toAdd = new ArrayList<>();
            this.toRemove = new ArrayList<>();



        }

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
