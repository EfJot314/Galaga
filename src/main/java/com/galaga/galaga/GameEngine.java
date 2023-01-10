package com.galaga.galaga;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GameEngine extends Thread{
    private Pane pane;
    private Scene scene;

    private Player player;


    public GameEngine(Pane p, Scene s) throws IOException {
        this.pane = p;
        this.scene = s;
        this.player = new Player(this.pane, this.scene);
    }

    public void run(){
        while(!player.isDead()){

            player.move();

            try {
                sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
